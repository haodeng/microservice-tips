package demo.hao;

import demo.hao.service.MyUserDetails;
import demo.hao.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        MyUserDetails myUserDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
        if (myUserDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!new BCryptPasswordEncoder().matches(password, myUserDetails.getPassword())) {
            throw new BadCredentialsException("User/Pass wrong");
        }

        return new UsernamePasswordAuthenticationToken(myUserDetails, password, myUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
