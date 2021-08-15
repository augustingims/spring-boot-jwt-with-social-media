package cm.skysoft.app.config;

import cm.skysoft.app.utils.ApplicationDefaultsUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Properties specific to LocalExternCRM.
 * <p>
 * Properties are configured in the {@code application-*.yml} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Async async = new Async();
    private final Http http = new Http();
    private final Cache cache = new Cache();
    private final Mail mail = new Mail();
    private final Security security = new Security();
    private final Swagger swagger = new Swagger();
    private final Metrics metrics = new Metrics();
    private final Logging logging = new Logging();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final ClientApp clientApp = new ClientApp();
    private final AuditEvents auditEvents = new AuditEvents();
    private final Social social = new Social();

    public ApplicationProperties() {
    }

    public Async getAsync() {
        return this.async;
    }

    public Http getHttp() {
        return this.http;
    }

    public Cache getCache() {
        return this.cache;
    }

    public Mail getMail() {
        return this.mail;
    }

    public Security getSecurity() {
        return this.security;
    }

    public Swagger getSwagger() {
        return this.swagger;
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    public Logging getLogging() {
        return this.logging;
    }

    public CorsConfiguration getCors() {
        return this.cors;
    }

    public Social getSocial() {
        return social;
    }

    public ClientApp getClientApp() {
        return this.clientApp;
    }

    public AuditEvents getAuditEvents() {
        return this.auditEvents;
    }

    public static class AuditEvents {
        private int retentionPeriod = 30;

        public AuditEvents() {
        }

        public int getRetentionPeriod() {
            return this.retentionPeriod;
        }

        public void setRetentionPeriod(int retentionPeriod) {
            this.retentionPeriod = retentionPeriod;
        }
    }

    public static class ClientApp {
        private String name = "springBootJwtWithSocialMedia";

        public ClientApp() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Social {
        private final Social.Google google = new Social.Google();
        private String secretPwd;

        public Social() {
        }

        public Social.Google getGoogle() {
            return this.google;
        }

        public String getSecretPwd() {
            return secretPwd;
        }

        public void setSecretPwd(String secretPwd) {
            this.secretPwd = secretPwd;
        }

        public static class Google {
            private String clientId;

            public Google() {
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }
        }
    }

    public static class Logging {
        private boolean useJsonFormat = false;
        private final Logging.Logstash logstash = new Logging.Logstash();

        public Logging() {
        }

        public boolean isUseJsonFormat() {
            return this.useJsonFormat;
        }

        public void setUseJsonFormat(boolean useJsonFormat) {
            this.useJsonFormat = useJsonFormat;
        }

        public Logging.Logstash getLogstash() {
            return this.logstash;
        }

        public static class Logstash {
            private boolean enabled = false;
            private String host = "localhost";
            private int port = 5000;
            private int queueSize = 512;

            public Logstash() {
            }

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return this.host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return this.port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return this.queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }
    }

    public static class Metrics {
        private final Metrics.Logs logs = new Metrics.Logs();

        public Metrics() {
        }

        public Metrics.Logs getLogs() {
            return this.logs;
        }

        public static class Logs {
            private boolean enabled = false;
            private long reportFrequency = 60L;

            public Logs() {
            }

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getReportFrequency() {
                return this.reportFrequency;
            }

            public void setReportFrequency(long reportFrequency) {
                this.reportFrequency = reportFrequency;
            }
        }
    }

    public static class Swagger {
        private String title = "Application API";
        private String description = "API documentation";
        private String version = "0.0.1";
        private String termsOfServiceUrl;
        private String contactName;
        private String contactUrl;
        private String contactEmail;
        private String license;
        private String licenseUrl;
        private String defaultIncludePattern;
        private String host;
        private String[] protocols;
        private boolean useDefaultResponseMessages;

        public Swagger() {
            this.termsOfServiceUrl = ApplicationDefaultsUtils.Swagger.termsOfServiceUrl;
            this.contactName = ApplicationDefaultsUtils.Swagger.contactName;
            this.contactUrl = ApplicationDefaultsUtils.Swagger.contactUrl;
            this.contactEmail = ApplicationDefaultsUtils.Swagger.contactEmail;
            this.license = ApplicationDefaultsUtils.Swagger.license;
            this.licenseUrl = ApplicationDefaultsUtils.Swagger.licenseUrl;
            this.defaultIncludePattern = "/api/.*";
            this.host = ApplicationDefaultsUtils.Swagger.host;
            this.protocols = ApplicationDefaultsUtils.Swagger.protocols;
            this.useDefaultResponseMessages = true;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return this.termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return this.contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return this.contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return this.contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return this.license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return this.licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getDefaultIncludePattern() {
            return this.defaultIncludePattern;
        }

        public void setDefaultIncludePattern(String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String[] getProtocols() {
            return this.protocols;
        }

        public void setProtocols(String[] protocols) {
            this.protocols = protocols;
        }

        public boolean isUseDefaultResponseMessages() {
            return this.useDefaultResponseMessages;
        }

        public void setUseDefaultResponseMessages(boolean useDefaultResponseMessages) {
            this.useDefaultResponseMessages = useDefaultResponseMessages;
        }
    }

    public static class Security {
        private final Security.ClientAuthorization clientAuthorization = new Security.ClientAuthorization();
        private final Security.Authentication authentication = new Security.Authentication();
        private final Security.RememberMe rememberMe = new Security.RememberMe();
        private final Security.OAuth2 oauth2 = new Security.OAuth2();

        public Security() {
        }

        public Security.ClientAuthorization getClientAuthorization() {
            return this.clientAuthorization;
        }

        public Security.Authentication getAuthentication() {
            return this.authentication;
        }

        public Security.RememberMe getRememberMe() {
            return this.rememberMe;
        }

        public Security.OAuth2 getOauth2() {
            return this.oauth2;
        }

        public static class OAuth2 {
            private List<String> audience = new ArrayList();

            public OAuth2() {
            }

            public List<String> getAudience() {
                return Collections.unmodifiableList(this.audience);
            }

            public void setAudience(@NotNull List<String> audience) {
                this.audience.addAll(audience);
            }
        }

        public static class RememberMe {
            @NotNull
            private String key;

            public RememberMe() {
                this.key = ApplicationDefaultsUtils.Security.RememberMe.key;
            }

            public String getKey() {
                return this.key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        public static class Authentication {
            private final Security.Authentication.Jwt jwt = new Security.Authentication.Jwt();

            public Authentication() {
            }

            public Security.Authentication.Jwt getJwt() {
                return this.jwt;
            }

            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;
                private long tokenValidityInSecondsForRememberMe;

                public Jwt() {
                    this.secret = ApplicationDefaultsUtils.Security.Authentication.Jwt.secret;
                    this.base64Secret = ApplicationDefaultsUtils.Security.Authentication.Jwt.base64Secret;
                    this.tokenValidityInSeconds = 1800L;
                    this.tokenValidityInSecondsForRememberMe = 2592000L;
                }

                public String getSecret() {
                    return this.secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return this.base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return this.tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return this.tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class ClientAuthorization {
            private String accessTokenUri;
            private String tokenServiceId;
            private String clientId;
            private String clientSecret;

            public ClientAuthorization() {
                this.accessTokenUri = ApplicationDefaultsUtils.Security.ClientAuthorization.accessTokenUri;
                this.tokenServiceId = ApplicationDefaultsUtils.Security.ClientAuthorization.tokenServiceId;
                this.clientId = ApplicationDefaultsUtils.Security.ClientAuthorization.clientId;
                this.clientSecret = ApplicationDefaultsUtils.Security.ClientAuthorization.clientSecret;
            }

            public String getAccessTokenUri() {
                return this.accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return this.tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return this.clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return this.clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }
    }

    public static class Mail {
        private boolean enabled = false;
        private String from = "";
        private String baseUrl = "";

        public Mail() {
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getBaseUrl() {
            return this.baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public static class Cache {
        private final Cache.Ehcache ehcache = new Cache.Ehcache();

        public Cache() {
        }

        public Cache.Ehcache getEhcache() {
            return this.ehcache;
        }

        public static class Ehcache {
            private int timeToLiveSeconds = 3600;
            private long maxEntries = 100L;

            public Ehcache() {
            }

            public int getTimeToLiveSeconds() {
                return this.timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public long getMaxEntries() {
                return this.maxEntries;
            }

            public void setMaxEntries(long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }

    public static class Http {
        private final Http.Cache cache = new Http.Cache();

        public Http() {
        }

        public Http.Cache getCache() {
            return this.cache;
        }

        public static class Cache {
            private int timeToLiveInDays = 1461;

            public Cache() {
            }

            public int getTimeToLiveInDays() {
                return this.timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }

    public static class Async {
        private int corePoolSize = 2;
        private int maxPoolSize = 50;
        private int queueCapacity = 10000;

        public Async() {
        }

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }
}
