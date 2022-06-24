package org.apache.spark.deploy.history


import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.internal.Logging
import org.apache.spark.internal.config.History
import org.apache.spark.internal.config.UI.ACLS_ENABLE
import org.apache.spark.util.{ShutdownHookManager, Utils}
import org.apache.spark.{SecurityManager, SparkConf}

object HistoryServer2 extends Logging {
  private val conf = new SparkConf
  conf.set("spark.history.fs.logDirectory", "/tmp/spark-events")
  val UI_PATH_PREFIX = "/history"

  def main(argStrings: Array[String]): Unit = {
    Utils.initDaemon(log)
    new HistoryServerArguments(conf, argStrings)
    initSecurity()
    val securityManager = createSecurityManager(conf)

    val providerName = conf.get(History.PROVIDER)
      .getOrElse(classOf[FsHistoryProvider].getName())
    val provider = Utils.classForName[ApplicationHistoryProvider](providerName)
      .getConstructor(classOf[SparkConf])
      .newInstance(conf)

    val port = conf.get(History.HISTORY_SERVER_UI_PORT)

    val server = new HistoryServer(conf, provider, securityManager, port)
    server.bind()
    provider.start()

    ShutdownHookManager.addShutdownHook { () => server.stop() }

    // Wait until the end of the world... or if the HistoryServer process is manually stopped
    while (true) {
      Thread.sleep(Int.MaxValue)
    }
  }

  /**
   * Create a security manager.
   * This turns off security in the SecurityManager, so that the History Server can start
   * in a Spark cluster where security is enabled.
   *
   * @param config configuration for the SecurityManager constructor
   * @return the security manager for use in constructing the History Server.
   */
  private[history] def createSecurityManager(config: SparkConf): SecurityManager = {
    if (config.getBoolean(SecurityManager.SPARK_AUTH_CONF, false)) {
      logDebug(s"Clearing ${SecurityManager.SPARK_AUTH_CONF}")
      config.set(SecurityManager.SPARK_AUTH_CONF, "false")
    }

    if (config.get(ACLS_ENABLE)) {
      logInfo(s"${ACLS_ENABLE.key} is configured, " +
        s"clearing it and only using ${History.HISTORY_SERVER_UI_ACLS_ENABLE.key}")
      config.set(ACLS_ENABLE, false)
    }

    new SecurityManager(config)
  }

  def initSecurity(): Unit = {
    // If we are accessing HDFS and it has security enabled (Kerberos), we have to login
    // from a keytab file so that we can access HDFS beyond the kerberos ticket expiration.
    // As long as it is using Hadoop rpc (hdfs://), a relogin will automatically
    // occur from the keytab.
    if (conf.get(History.KERBEROS_ENABLED)) {
      // if you have enabled kerberos the following 2 params must be set
      val principalName = conf.get(History.KERBEROS_PRINCIPAL)
        .getOrElse(throw new NoSuchElementException(History.KERBEROS_PRINCIPAL.key))
      val keytabFilename = conf.get(History.KERBEROS_KEYTAB)
        .getOrElse(throw new NoSuchElementException(History.KERBEROS_KEYTAB.key))
      SparkHadoopUtil.get.loginUserFromKeytab(principalName, keytabFilename)
    }
  }

  private[history] def getAttemptURI(appId: String, attemptId: Option[String]): String = {
    val attemptSuffix = attemptId.map { id => s"/$id" }.getOrElse("")
    s"${HistoryServer.UI_PATH_PREFIX}/${appId}${attemptSuffix}"
  }

}
