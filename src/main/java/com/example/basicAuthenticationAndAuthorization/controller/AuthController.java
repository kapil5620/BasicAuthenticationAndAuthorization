package com.example.basicAuthenticationAndAuthorization.controller;

import com.example.basicAuthenticationAndAuthorization.entity.UserInfo;
import com.example.basicAuthenticationAndAuthorization.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userDetails")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/addNewSuperAdmin")
    public ResponseEntity<String> addNewSuperAdmin(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        String response = authService.addNewUser(userInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addNewUser")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo, Authentication auth) {
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
                userInfo.getRoles().contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: USER role cannot create ADMIN users.");
        }

        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        String response = authService.addNewUser(userInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addNewAdmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addNewAdmin(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        String response = authService.addNewUser(userInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        try {
            List<UserInfo> users = authService.getAllUserDetails();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/getAllAdmins")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserInfo>> getAllAdmins() {
        try {
            List<UserInfo> admins = authService.getAllAdminDetails();
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/getUser/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getUserByName(@PathVariable String username) {
        try {
            UserInfo user = authService.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        }
    }

    @GetMapping("/getAdmin/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAdminByName(@PathVariable String username) {
        try {
            UserInfo admin = authService.findAdminByUsername(username);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin not found: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAdmin/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteAdminByName(@PathVariable String username, Authentication auth) {
        String response = authService.deleteUserByUsername(username, auth);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteUser/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> deleteUserByName(@PathVariable String username, Authentication auth) {
        String response = authService.deleteUserByUsername(username, auth);
        return ResponseEntity.ok(response);
    }
}