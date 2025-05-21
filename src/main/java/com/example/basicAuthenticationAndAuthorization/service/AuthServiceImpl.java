package com.example.basicAuthenticationAndAuthorization.service;

import com.example.basicAuthenticationAndAuthorization.entity.UserInfo;
import com.example.basicAuthenticationAndAuthorization.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public UserInfo findAdminByUsername(String username) {
        try {
            return Optional.ofNullable(authRepository.findAdminByUsername(username))
                    .orElseThrow(() -> new RuntimeException("Admin not found with username: " + username));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch admin: " + username, e);
        }
    }

    public UserInfo findUserByUsername(String username) {
        try {
            return Optional.ofNullable(authRepository.findUserByUsername(username))
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user: " + username, e);
        }
    }

    public String deleteUserByUsername(String username, Authentication auth) {
        try {
            UserInfo targetUser = authRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            boolean isAdminRequester = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));
            boolean isTargetAdmin = targetUser.getRoles().contains("ADMIN");
            if (!isAdminRequester && isTargetAdmin) {
                return "Access Denied: USER role cannot delete ADMIN.";
            }
            authRepository.delete(targetUser);
            return "User deleted successfully: " + username;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete the user: " + username, e);
        }

    }
}