package cn.darkjrong.gateway.common.config;

import cn.darkjrong.gateway.common.handler.ErrorExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 错误处理程序配置
 *
 * @author Rong.Jia
 * @date 2022/02/08
 */
@Configuration
@AutoConfigureBefore(ErrorWebFluxAutoConfiguration.class)
@EnableConfigurationProperties({ServerProperties.class, WebProperties.class})
public class ErrorHandlerConfig {

	private final ServerProperties serverProperties;

	private final ApplicationContext applicationContext;

	private final WebProperties webProperties;

	private final List<ViewResolver> viewResolvers;

	private final ServerCodecConfigurer serverCodecConfigurer;

	public ErrorHandlerConfig(ServerProperties serverProperties,
							  WebProperties webProperties,
							  ObjectProvider<List<ViewResolver>> viewResolversProvider,
							  ServerCodecConfigurer serverCodecConfigurer,
							  ApplicationContext applicationContext) {
		this.serverProperties = serverProperties;
		this.applicationContext = applicationContext;
		this.webProperties = webProperties;
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
		ErrorExceptionHandler exceptionHandler = new ErrorExceptionHandler(
			errorAttributes,
			this.webProperties.getResources(),
			this.serverProperties.getError(),
			this.applicationContext);
		exceptionHandler.setViewResolvers(this.viewResolvers);
		exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
		exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
		return exceptionHandler;
	}

}
