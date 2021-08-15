package cm.skysoft.app.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ApplicationDefaultsUtils {

    public interface AuditEvents {
        int retentionPeriod = 30;
    }

    public interface Logging {
        boolean useJsonFormat = false;

        public interface Logstash {
            boolean enabled = false;
            String host = "localhost";
            int port = 5000;
            int queueSize = 512;
        }
    }

    public interface Metrics {
        public interface Prometheus {
            boolean enabled = false;
            String endpoint = "/prometheusMetrics";
        }

        public interface Logs {
            boolean enabled = false;
            long reportFrequency = 60L;
        }

        public interface Jmx {
            boolean enabled = false;
        }
    }

    public interface Swagger {
        String title = "Application API";
        String description = "API documentation";
        String version = "0.0.1";
        String termsOfServiceUrl = null;
        String contactName = null;
        String contactUrl = null;
        String contactEmail = null;
        String license = null;
        String licenseUrl = null;
        String defaultIncludePattern = "/api/.*";
        String host = null;
        String[] protocols = new String[0];
        boolean useDefaultResponseMessages = true;
    }

    public interface Security {
        public interface RememberMe {
            String key = null;
        }

        public interface Authentication {
            public interface Jwt {
                String secret = null;
                String base64Secret = null;
                long tokenValidityInSeconds = 1800L;
                long tokenValidityInSecondsForRememberMe = 2592000L;
            }
        }

        public interface ClientAuthorization {
            String accessTokenUri = null;
            String tokenServiceId = null;
            String clientId = null;
            String clientSecret = null;
        }
    }

    public interface Mail {
        boolean enabled = false;
        String from = "";
        String baseUrl = "";
    }

    public interface Cache {

        public interface Ehcache {
            int timeToLiveSeconds = 3600;
            long maxEntries = 100L;
        }

    }

    public interface Http {
        public interface Cache {
            int timeToLiveInDays = 1461;
        }
    }

    public interface Async {
        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }
}
