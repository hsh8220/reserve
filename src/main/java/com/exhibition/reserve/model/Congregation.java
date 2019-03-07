package com.exhibition.reserve.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "congregation")
public class Congregation {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

}
