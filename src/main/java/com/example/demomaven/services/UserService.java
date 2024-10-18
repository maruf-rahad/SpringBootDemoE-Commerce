package com.example.demomaven.services;

import com.example.demomaven.models.Users;
import com.example.demomaven.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);


    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }

    public Users registerUser(Users user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return usersRepository.save(user );
    }
}
