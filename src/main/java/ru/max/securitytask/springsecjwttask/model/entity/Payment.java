package ru.max.securitytask.springsecjwttask.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by maxxii on 23.02.2021.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "amount",precision = 20,scale = 2)
    private Double amount;
    private LocalDateTime operationDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
