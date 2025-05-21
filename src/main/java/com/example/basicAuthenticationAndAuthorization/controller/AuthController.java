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
    public String addNewSuperAdmin(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return authService.addNewUser(userInfo);
    }

    @PostMapping("/addNewUser")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String addNewUser(@RequestBody UserInfo userInfo, Authentication auth) {
        // Prevent normal users from creating admins
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
                userInfo.getRoles().contains("ADMIN")) {
            return "Access Denied: USER role cannot create ADMIN users.";
        }

        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return authService.addNewUser(userInfo);
    }

    @PostMapping("/addNewAdmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addNewAdmin(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return authService.addNewUser(userInfo);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public List<UserInfo> getAllUsers() {
        return authService.getAllUserDetails();
    }

    @GetMapping("/getAllAdmins")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserInfo>> getAllAdmins() {
        List<UserInfo> allAdminDetails = authService.getAllAdminDetails();
        return new ResponseEntity<>(allAdminDetails, HttpStatus.OK);
    }

    @GetMapping("/getUser/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public UserInfo getUserByName(@PathVariable String username) {
        return authService.findUserByUsername(username);
    }

    @GetMapping("/getAdmin/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAdminByName(@PathVariable String username) {
        try {
            return new ResponseEntity<>(authService.findAdminByUsername(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAdmin/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteAdminByName(@PathVariable String username, Authentication auth) {
        return authService.deleteUserByUsername(username, auth);
    }

    @DeleteMapping("/deleteUser/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public String deleteUserByName(@PathVariable String username, Authentication auth) {
        return authService.deleteUserByUsername(username, auth);
    }
}