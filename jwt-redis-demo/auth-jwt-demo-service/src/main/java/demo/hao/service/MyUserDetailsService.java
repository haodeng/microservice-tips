package demo.hao.service;

import demo.hao.dao.UserRepository;
import demo.hao.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);
        if (user != null) {
            MyUserDetails myUserDetails = new MyUserDetails();
            BeanUtils.copyProperties(user, myUserDetails);

            // TODO: add authorities to myUserDetails

            return myUserDetails;
        }

        return null;
    }

    public void save(User user) {
        userRepository.save(user);
    }

}