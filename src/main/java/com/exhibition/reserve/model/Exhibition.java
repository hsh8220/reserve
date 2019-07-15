package com.exhibition.reserve.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "exhibition")
public class Exhibition {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @Column
    private String time;

    @Column
    private String guide;

    @Column
    private Integer limitation;

    @Column
    private Integer timeLimit;

    @ManyToOne
    @JoinColumn(name = "congregation_id")
    private Congregation congregation;
}
