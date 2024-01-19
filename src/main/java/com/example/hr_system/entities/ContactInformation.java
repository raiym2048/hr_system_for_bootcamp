package com.example.hr_system.entities;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContactInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String street_house;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "contactInformation")
    private Vacancy vacancy;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
