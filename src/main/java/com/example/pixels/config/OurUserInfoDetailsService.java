package com.example.pixels.config;

import com.example.pixels.entity.User;
import com.example.pixels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Optional<User> user = userRepository.findUserByUserEmail(username);
        if(user.isPresent()) {
            return buildUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public void refreshUserAuthentication(String username) {
        User user = userRepository.findUserByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        UserDetails userDetails = buildUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails buildUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getUserRole().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getPassword()) // You would normally not include the password in this method
                .authorities(authorities)
                .build();
    }


//    private String[] getRoles(User user){
//        if(user.getUserRole() == null){
//            return new String[]{"User"};
//        }
//        return user.getUserRole().split(",");
//    }

}
