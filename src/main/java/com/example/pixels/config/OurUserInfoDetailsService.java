package com.example.pixels.config;

import com.example.pixels.entity.User;
import com.example.pixels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class OurUserInfoDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByUserEmail(username));
        if(user.isPresent()) {
            User user1 = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user1.getUserEmail())
                    .password(user1.getPassword())
                    .roles(getRoles(user1))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(User user){
        if(user.getUserRole() == null){
            return new String[]{"User"};
        }
        return user.getUserRole().split(",");
    }

}
