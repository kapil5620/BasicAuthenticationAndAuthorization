package com.example.basicAuthenticationAndAuthorization.repository;

import com.example.basicAuthenticationAndAuthorization.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByUsername(String username);

    @Query("SELECT u FROM UserInfo u WHERE u.roles = 'USER'")
    List<UserInfo> findAllUsers();

    @Query("SELECT u FROM UserInfo u WHERE u.roles = 'ADMIN'")
    List<UserInfo> findAllAdmins();

    @Query("SELECT u FROM UserInfo u WHERE u.username = :username AND (u.roles = 'ADMIN' OR u.roles = 'ADMIN, USER')")
    UserInfo findAdminByUsername(@Param("username") String name);

    @Query("SELECT u FROM UserInfo u WHERE u.username = :username AND u.roles = 'USER'")
    UserInfo findUserByUsername(@Param("username") String name);
}