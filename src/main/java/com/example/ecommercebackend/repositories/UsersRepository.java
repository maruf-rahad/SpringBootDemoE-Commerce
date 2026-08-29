package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

    Optional<Users> findByUsernameOptional(String username); // Use when you want .orElseThrow()

    boolean existsByUsername(String username);
}
