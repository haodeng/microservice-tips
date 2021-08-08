package demo.hao.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@SuppressWarnings("static-access")
public class JWTConfig {

    public static String secret;
    public static String tokenHeader;
    public static String tokenPrefix;
    public static Integer expiration;
    public static Integer refreshTime;
    public static String antMatchers;

    public void setExpiration(Integer expiration) {
        this.expiration = expiration * 1000;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime * 24 * 60 * 60 * 1000;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix + " ";
    }

    public void setAntMatchers(String antMatchers) {
        this.antMatchers = antMatchers;
    }

}
