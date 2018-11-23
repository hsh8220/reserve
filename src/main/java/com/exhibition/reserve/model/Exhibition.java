package com.exhibition.reserve.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "exhibition")
public class Exhibition {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private String time;

    @Column(name = "guide")
    private String guide;

}
