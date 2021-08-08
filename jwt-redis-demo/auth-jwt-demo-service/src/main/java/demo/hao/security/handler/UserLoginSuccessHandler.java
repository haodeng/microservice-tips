package demo.hao.security.handler;

import demo.hao.service.MyUserDetails;
import demo.hao.utils.JWTTokenUtils;
import demo.hao.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        // add token to Redis
        String token = JWTTokenUtils.createAccessToken(myUserDetails);
        JWTTokenUtils.setTokenInfo(token, myUserDetails.getUsername());

        log.info("User {} login ok", myUserDetails.getUsername());

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        ResponseUtils.responseJson(response, ResponseUtils.response(200, "Login ok", tokenMap));
    }
}
