package cn.zenliu.spring.multiport

import org.springframework.boot.context.properties.*

@ConfigurationProperties("server")
class MultiPortProperties {
	/**
	 * Secondary Http Port (default null)
	 */
	var secondaryHttp:Int?=null
}
