package com.tappay.service.webservice.User;

import com.tappay.service.jparepository.User.UserRepository;
import com.tappay.service.webservice.User.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user=userRepository.findByEmail(username);
        if (user.isPresent()){
            return new MyUserDetail(user.get());
        }else{
            throw new UsernameNotFoundException("User not found");
        }
    }
}
