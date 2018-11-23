package com.exhibition.reserve.model;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name="member")
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String userId;

    @Column(nullable = false, length = 200)
    private String pw;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}
