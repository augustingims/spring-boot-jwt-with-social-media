package cm.skysoft.app.config;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PrefixedKeyGenerator implements KeyGenerator {
    private final String prefix;

    public PrefixedKeyGenerator(GitProperties gitProperties, BuildProperties buildProperties) {
        this.prefix = this.generatePrefix(gitProperties, buildProperties);
    }

    String getPrefix() {
        return this.prefix;
    }

    private String generatePrefix(GitProperties gitProperties, BuildProperties buildProperties) {
        String shortCommitId = null;
        if (Objects.nonNull(gitProperties)) {
            shortCommitId = gitProperties.getShortCommitId();
        }

        Instant time = null;
        String version = null;
        if (Objects.nonNull(buildProperties)) {
            time = buildProperties.getTime();
            version = buildProperties.getVersion();
        }

        Object p = ObjectUtils.firstNonNull(new Serializable[]{shortCommitId, time, version, RandomStringUtils.randomAlphanumeric(12)});
        return p instanceof Instant ? DateTimeFormatter.ISO_INSTANT.format((Instant)p) : p.toString();
    }

    public Object generate(Object target, Method method, Object... params) {
        return new PrefixedSimpleKey(this.prefix, method.getName(), params);
    }
}
