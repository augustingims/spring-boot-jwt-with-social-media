package cm.skysoft.app.swagger;

import cm.skysoft.app.config.ApplicationProperties;
import com.fasterxml.classmate.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Springfox Swagger configuration.
 *
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue. In that case, you can use a
 * specific Spring profile for this class, so that only front-end developers have access to the Swagger view.
 */
@Configuration
@ConditionalOnClass({ ApiInfo.class, BeanValidatorPluginsConfiguration.class })
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    public static final String STARTING_MESSAGE = "Starting Swagger";
    public static final String STARTED_MESSAGE = "Started Swagger in {} ms";
    public static final String MANAGEMENT_TITLE_SUFFIX = "management API";
    public static final String MANAGEMENT_GROUP_NAME = "management";
    public static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    private final ApplicationProperties.Swagger properties;

    public SwaggerConfiguration(ApplicationProperties applicationProperties) {
        this.properties = applicationProperties.getSwagger();
    }

    /**
     * Springfox configuration for the API Swagger docs.
     *
     * @return the Swagger Springfox configuration
     */
    @Bean
    public Docket swaggerSpringfoxApiDocket() {
        log.debug(STARTING_MESSAGE);
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(
                properties.getContactName(),
                properties.getContactUrl(),
                properties.getContactEmail());

        ApiInfo apiInfo = new ApiInfo(
                properties.getTitle(),
                properties.getDescription(),
                properties.getVersion(),
                properties.getTermsOfServiceUrl(),
                contact,
                properties.getLicense(),
                properties.getLicenseUrl(),
                new ArrayList<>());

        Docket docket = createDocket()
                .host(properties.getHost())
                .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(properties.getDefaultIncludePattern()))
                .build();
        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        return docket;
    }

    /**
     * Springfox configuration for the management endpoints (actuator) Swagger docs.
     *
     * @param appName               the application name
     * @param managementContextPath the path to access management endpoints
     * @param appVersion            the application version
     * @return the Swagger Springfox configuration
     */
    @Bean
    public Docket swaggerSpringfoxManagementDocket(@Value("${spring.application.name}") String appName,
                                                   @Value("${management.endpoints.web.base-path}") String managementContextPath,
                                                   @Value("${info.project.version}") String appVersion) {

        return createDocket()
                .apiInfo(new ApiInfo(appName + " " + MANAGEMENT_TITLE_SUFFIX, MANAGEMENT_DESCRIPTION,
                        appVersion, "", ApiInfo.DEFAULT_CONTACT, "", "", new ArrayList<>()))
                .groupName(MANAGEMENT_GROUP_NAME)
                .host(properties.getHost())
                .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(managementContextPath + ".*"))
                .build();
    }

    @Bean
    PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor nameExtractor,
                                                                  TypeResolver resolver) {

        return new PageableParameterBuilderPlugin(nameExtractor, resolver);
    }

    protected Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2);
    }

}
