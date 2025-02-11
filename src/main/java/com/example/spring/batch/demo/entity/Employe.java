package com.example.spring.batch.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Employe {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;




    private String name;

    private String email;

    private String phone;



}
