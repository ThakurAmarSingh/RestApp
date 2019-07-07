package conf

import com.typesafe.config.ConfigFactory

object AppConf {
  val conf = ConfigFactory.load
  val localNodeAddress   = conf.getString("node.address")
  val localNodePort = conf.getInt("node.port")
}
