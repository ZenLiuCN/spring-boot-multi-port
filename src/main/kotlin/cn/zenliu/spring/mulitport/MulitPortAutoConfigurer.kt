package cn.zenliu.spring.mulitport

import io.undertow.*
import org.apache.catalina.connector.Connector
import org.apache.catalina.startup.*
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.*
import org.springframework.boot.autoconfigure.condition.*
import org.springframework.boot.autoconfigure.web.embedded.*
import org.springframework.boot.context.properties.*
import org.springframework.boot.web.embedded.jetty.*
import org.springframework.boot.web.embedded.netty.*
import org.springframework.boot.web.embedded.tomcat.*
import org.springframework.boot.web.embedded.undertow.*
import org.springframework.context.annotation.*
import reactor.netty.http.server.*

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(MulitPortProperties::class)
class MulitPortAutoConfigurer {
	@Bean
	@ConditionalOnMissingBean(JettyServerCustomizer::class)
	fun jettyServerCustomizer() = JettyServerCustomizer {}

	@Bean
	@ConditionalOnClass(Jetty::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	fun JettyServletWebServerFactory(
		prop: MulitPortProperties,
		jettyServerCustomizer: JettyServerCustomizer
	) = JettyServletWebServerFactory().apply {
		this.serverCustomizers.apply {
			add(jettyServerCustomizer)
			prop.secondaryHttp?.let { http ->
				add(JettyServerCustomizer {
					it.connectors.plus(ServerConnector(it).apply {
						port = http
					})
				})
			}
		}
	}

	@Bean
	@ConditionalOnClass(Jetty::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	fun jettyReactiveWebServerFactory(
		prop: MulitPortProperties,
		jettyServerCustomizer: JettyServerCustomizer
	) = JettyReactiveWebServerFactory().apply {
		this.serverCustomizers.apply {
			add(jettyServerCustomizer)
			prop.secondaryHttp?.let { http ->
				add(JettyServerCustomizer {
					it.connectors.plus(ServerConnector(it).apply {
						port = http
					})
				})
			}
		}
	}

	@Bean
	@ConditionalOnClass(Tomcat::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	fun tomcatServletWebServerFactory(
		prop: MulitPortProperties
	) = TomcatServletWebServerFactory().apply {
		prop.secondaryHttp?.let { http ->
			this.additionalTomcatConnectors.add(Connector().apply {
				port = http
			})
		}

	}

	@Bean
	@ConditionalOnClass(Tomcat::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	fun tomcatReactiveWebServerFactory(
		prop: MulitPortProperties
	) = TomcatReactiveWebServerFactory().apply {
		prop.secondaryHttp?.let { http ->
			/*this.addConnectorCustomizers(TomcatConnectorCustomizer {
				it.
			}).add(Connector().apply {
				port=http
			})*/
			//not support yet
			NotImplementedError("spring not offical implemented yet")
		}

	}

	@Bean
	@ConditionalOnClass(Undertow::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	fun undertowServletWebServerFactory(
		prop: MulitPortProperties
	) = UndertowServletWebServerFactory().apply {
		prop.secondaryHttp?.let { http ->
			this.builderCustomizers.add(UndertowBuilderCustomizer {
				it.addHttpListener(http, "0.0.0.0")
			})
		}

	}


	@Bean
	@ConditionalOnClass(Undertow::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	fun undertowReactiveWebServerFactory(
		prop: MulitPortProperties
	) = UndertowReactiveWebServerFactory().apply {
		prop.secondaryHttp?.let { http ->
			this.builderCustomizers.add(UndertowBuilderCustomizer {
				it.addHttpListener(http, "0.0.0.0")
			})
		}

	}
	@Bean
	@ConditionalOnClass(HttpServer::class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	fun nettyReactiveWebServerFactory(
		prop: MulitPortProperties
	) = NettyReactiveWebServerFactory().apply {
		prop.secondaryHttp?.let { http ->
			NotImplementedError("spring not offical implemented yet")
		}

	}
}

