package com.example.demomaven.models;

import com.example.demomaven.interfaces.Computer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class Desktop implements Computer {

    public void compile()
    {
        System.out.println("Compile Desktop which is faster");
    }
}
