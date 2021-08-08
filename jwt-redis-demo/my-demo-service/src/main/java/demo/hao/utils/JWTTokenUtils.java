package demo.hao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import demo.hao.security.JWTConfig;
import demo.hao.service.MyUserDetails;
import demo.hao.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class JWTTokenUtils {
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    private static JWTTokenUtils jwtTokenUtils;

    @PostConstruct
    public void init() {
        jwtTokenUtils = this;
        jwtTokenUtils.myUserDetailsService = this.myUserDetailsService;
    }

    public static String createAccessToken(MyUserDetails myUserDetails) {
        String token = Jwts.builder()
                .setId(myUserDetails.getId().toString())
                .setSubject(myUserDetails.getUsername())
                .setIssuedAt(new Date())
                .setIssuer("Hao")
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration))
                .signWith(SignatureAlgorithm.HS512, JWTConfig.secret)
                .claim("authorities", JSON.toJSONString(myUserDetails.getAuthorities()))
                .compact();
        return JWTConfig.tokenPrefix + token;
    }

    public static String refreshAccessToken(String oldToken) {
        String username = JWTTokenUtils.getUserNameByToken(oldToken);
        MyUserDetails myUserDetails = (MyUserDetails) jwtTokenUtils.myUserDetailsService
                .loadUserByUsername(username);
        return createAccessToken(myUserDetails);
    }


    public static MyUserDetails parseAccessToken(String token) {
        MyUserDetails myUserDetails = null;
        if (StringUtils.isNotEmpty(token)) {
            try {
                token = token.substring(JWTConfig.tokenPrefix.length());
                Claims claims = Jwts.parser().setSigningKey(JWTConfig.secret).parseClaimsJws(token).getBody();

                myUserDetails = new MyUserDetails();
                myUserDetails.setId(Long.parseLong(claims.getId()));
                myUserDetails.setUsername(claims.getSubject());

                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                String authority = claims.get("authorities").toString();
                if (StringUtils.isNotEmpty(authority)) {
                    List<Map<String, String>> authorityList = JSON.parseObject(authority,
                            new TypeReference<List<Map<String, String>>>() {
                            });

                    for (Map<String, String> role : authorityList) {
                        if (!role.isEmpty()) {
                            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                        }
                    }
                }

                myUserDetails.setAuthorities(authorities);

            } catch (Exception e) {
                log.error("parseAccessToken error", e);
            }
        }
        return myUserDetails;
    }

    public static void setTokenInfo(String token, String username) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());

            Integer refreshTime = JWTConfig.refreshTime;
            LocalDateTime localDateTime = LocalDateTime.now();

            RedisUtils.hset(token, "username", username, refreshTime);
            RedisUtils.hset(token, "refreshTime",
                    df.format(localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS)), refreshTime);
            RedisUtils.hset(token, "expiration", df.format(localDateTime.plus(JWTConfig.expiration, ChronoUnit.MILLIS)),
                    refreshTime);
        }
    }

    public static void addBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            RedisUtils.hset("blackList", token, df.format(LocalDateTime.now()));
        }
    }

    public static void deleteRedisToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            RedisUtils.deleteKey(token);
        }
    }


    public static boolean isExpiration(String expiration) {
        LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.compareTo(expirationTime) > 0) {
            return true;
        }
        return false;
    }


    public static boolean isValid(String refreshTime) {
        LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.compareTo(validTime) > 0) {
            return false;
        }
        return true;
    }


    public static boolean hasToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hasKey(token);
        }
        return false;
    }

    public static String getExpirationByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "expiration").toString();
        }
        return null;
    }

    public static String getRefreshTimeByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "refreshTime").toString();
        }
        return null;
    }

    public static String getUserNameByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "username").toString();
        }
        return null;
    }

}
