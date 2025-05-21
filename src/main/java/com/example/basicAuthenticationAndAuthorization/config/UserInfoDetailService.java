package com.example.basicAuthenticationAndAuthorization.config;

import com.example.basicAuthenticationAndAuthorization.entity.UserInfo;
import com.example.basicAuthenticationAndAuthorization.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoDetailService implements UserDetailsService {

    @Autowired
    AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = authRepository.findByUsername(username);
        return userInfo.map(UserInfoDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}