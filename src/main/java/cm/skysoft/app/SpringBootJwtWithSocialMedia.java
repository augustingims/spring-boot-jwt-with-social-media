package cm.skysoft.app;

import cm.skysoft.app.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class SpringBootJwtWithSocialMedia {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJwtWithSocialMedia.class, args);
	}

}
