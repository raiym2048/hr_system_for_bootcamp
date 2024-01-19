package com.example.hr_system.entities;


import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "position")
    private List<Vacancy> vacancies;


    @OneToMany(mappedBy = "position")
    private List<Profession> profession;

}
