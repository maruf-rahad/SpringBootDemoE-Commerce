package com.example.demomaven.models;

import com.example.demomaven.interfaces.Computer;
import org.springframework.stereotype.Component;

@Component
public class Laptop implements Computer {

    public void compile()
    {
        System.out.println("Compile Laptop");
    }
}

