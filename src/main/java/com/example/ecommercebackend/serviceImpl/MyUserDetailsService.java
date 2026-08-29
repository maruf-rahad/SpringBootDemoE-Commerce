package com.example.ecommercebackend.serviceImpl;

import com.example.ecommercebackend.models.UserPrinciple;
import com.example.ecommercebackend.models.Users;
import com.example.ecommercebackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername");
        Users user = usersRepository.findByUsername(username);

        if(user == null) {
            System.out.println("user not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        System.out.println("loadUserByUsername1");
        return new UserPrinciple(user);
    }
}
