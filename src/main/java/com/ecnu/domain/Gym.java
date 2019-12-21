package com.ecnu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/10 -10:57 下午
 */
@Getter
@Setter
@ToString
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
//    @OneToMany(targetEntity = Order.class)
//    @JoinColumn(name = "order_gym_id", referencedColumnName = "gym_id")
    @OneToMany(mappedBy = "gym")
    @JsonIgnoreProperties("gym")
    private Set<Order> orderSet;
}
