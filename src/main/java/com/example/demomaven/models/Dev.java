package com.example.demomaven.models;

import com.example.demomaven.interfaces.Computer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Dev {

//    @Autowired
    private final Computer comp;

    //constructor injection
    Dev(@Qualifier("desktop") Computer comp) {
        this.comp = comp;
    }

/*    // setter injection
    @Autowired
    public void setComp(@Qualifier("desktop") Computer comp) {
        this.comp = comp;
    }*/

    public void getTestString(){

        comp.compile();
        System.out.println("working with demo project");
    }
}
