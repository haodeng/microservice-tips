package demo.hao.security.handler;

import demo.hao.security.JWTConfig;
import demo.hao.utils.JWTTokenUtils;
import demo.hao.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) {
        String token = request.getHeader(JWTConfig.tokenHeader);
        JWTTokenUtils.addBlackList(token);

        log.info("User {} logout", JWTTokenUtils.getUserNameByToken(token));

        SecurityContextHolder.clearContext();
        ResponseUtils.responseJson(response, ResponseUtils.response(200, "Logout ok", null));
    }
}
