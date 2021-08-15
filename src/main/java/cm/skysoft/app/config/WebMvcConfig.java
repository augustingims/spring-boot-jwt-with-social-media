package cm.skysoft.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	private final ApplicationProperties applicationProperties;

	public WebMvcConfig(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:" + applicationProperties.getUpload().getResourcesServerStore());

		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * Allowing requests from certains origins
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
			.allowCredentials(true)
			.allowedHeaders("*")
			.exposedHeaders("Authorization", "Link", "X-Total-Count");
	}
}
