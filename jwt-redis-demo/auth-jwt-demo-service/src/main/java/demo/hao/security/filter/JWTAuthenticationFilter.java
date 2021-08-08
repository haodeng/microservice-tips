package demo.hao.security.filter;

import demo.hao.security.JWTConfig;
import demo.hao.service.MyUserDetails;
import demo.hao.utils.JWTTokenUtils;
import demo.hao.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = request.getHeader(JWTConfig.tokenHeader);

        if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
            if (JWTTokenUtils.hasToken(token)) {
                String expiration = JWTTokenUtils.getExpirationByToken(token);
                String username = JWTTokenUtils.getUserNameByToken(token);

                if (JWTTokenUtils.isExpiration(expiration)) {
                    String validTime = JWTTokenUtils.getRefreshTimeByToken(token);
                    if (JWTTokenUtils.isValid(validTime)) {
                        String newToken = JWTTokenUtils.refreshAccessToken(token);

                        JWTTokenUtils.deleteRedisToken(token);
                        JWTTokenUtils.setTokenInfo(newToken, username);
                        response.setHeader(JWTConfig.tokenHeader, newToken);

                        log.info("User {} token expired, refresh", username);

                        token = newToken;
                    } else {
                        log.info("User {} Token expired, stop the refresh", username);

                        JWTTokenUtils.deleteRedisToken(token);
                        ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token expired", "Expired refresh time"));
                        return;
                    }
                }

                MyUserDetails myUserDetails = JWTTokenUtils.parseAccessToken(token);
                if (myUserDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            myUserDetails, myUserDetails.getId(), myUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
