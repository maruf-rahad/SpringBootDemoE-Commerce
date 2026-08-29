package com.example.demomaven.services;

import com.example.demomaven.models.Users;
import com.example.demomaven.models.enums.Role;
import com.example.demomaven.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }

    public Users registerUser(Users user) {
        Users existingUser = usersRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username '" + user.getUsername() + "' is already taken!");
        }

        // Default role if not provided
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_CUSTOMER);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    public String verifyUserForLogin(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        else{
            return "Fail";
        }
    }

    public Users createAdmin(Users newAdmin) {
        // Check if username is taken
        if (usersRepository.findByUsername(newAdmin.getUsername()) != null) {
            throw new RuntimeException("Username '" + newAdmin.getUsername() + "' is already taken!");
        }

        // Force role to ROLE_ADMIN
        newAdmin.setRole(Role.ROLE_ADMIN);
        newAdmin.setPassword(bCryptPasswordEncoder.encode(newAdmin.getPassword()));

        return usersRepository.save(newAdmin);
    }
}
