package com.example.spring.batch.demo.repo;

import com.example.spring.batch.demo.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeRepo extends JpaRepository<Employe,Integer> {
}
