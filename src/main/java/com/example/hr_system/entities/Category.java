package com.example.hr_system.entities;


import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

  //  int counter;

    //private String image;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Vacancy> vacancies;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Position> positions;
}
