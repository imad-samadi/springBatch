package com.example.spring.batch.demo.config;

import com.example.spring.batch.demo.entity.Employe;
import org.springframework.batch.item.ItemProcessor;

public class EmployeProcessor implements ItemProcessor<Employe, Employe> {

    @Override
    public Employe process(Employe employe) {

        employe.setName(employe.getName().toUpperCase());


        if (!employe.getEmail().contains("@")) {
            throw new IllegalArgumentException("***********\nInvalid email: ***********\n" + employe.getEmail());
        }




        return employe;
    }
}
