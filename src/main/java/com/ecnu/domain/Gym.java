package com.ecnu.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/10 -10:57 下午
 */
@Data
@Entity
@Table(name = "gym")
public class Gym implements Serializable {
    @Id
    @Column(name = "gym_id")
    private String gymId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String address;
    @Column
    private Double rent;
    @Column
    private Boolean open;
    @Column
    private String photo;
    @Column
    private String type;
    @OneToMany(targetEntity = Order.class)
    @JoinColumn(name = "order_gym_id", referencedColumnName = "gym_id")
    private Set<Order> orderSet;
}
