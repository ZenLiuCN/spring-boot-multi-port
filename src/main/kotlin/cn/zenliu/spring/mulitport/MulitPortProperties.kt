package cn.zenliu.spring.mulitport

import org.springframework.boot.context.properties.*

@ConfigurationProperties("server")
class MulitPortProperties {
	/**
	 * Secondary Http Port (default null)
	 */
	var secondaryHttp:Int?=null
}
