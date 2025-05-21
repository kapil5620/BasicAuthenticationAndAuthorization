package com.example.basicAuthenticationAndAuthorization.service;

import com.example.basicAuthenticationAndAuthorization.entity.UserInfo;
import com.example.basicAuthenticationAndAuthorization.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl {

    @Autowired
    AuthRepository authRepository;

    public String addNewUser(UserInfo userInfo) {
        authRepository.save(userInfo);
        return "User added successfully: " + userInfo.getUsername() + " | Role: " + userInfo.getRoles();
    }

    public List<UserInfo> getAllUserDetails() {
        return authRepository.findAllUsers();
    }

    public List<UserInfo> getAllAdminDetails() {
        return authRepository.findAllAdmins();
    }

    public UserInfo findAdminByUsername(String name) {
        try {
            return authRepository.findAdminByUsername(name);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute findAdminByUsername");
        }
    }

    public UserInfo findUserByUsername(String name) {
        return authRepository.findUserByUsername(name);
    }

    public String deleteUserByUsername(String username, Authentication auth) {
        UserInfo targetUser = authRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        boolean isAdminRequester = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        boolean isTargetAdmin = targetUser.getRoles().contains("ADMIN");

        if (!isAdminRequester && isTargetAdmin) {
            return "Access Denied: USER role cannot delete ADMINs.";
        }

        authRepository.delete(targetUser);
        return "User deleted successfully: " + username;
    }
}