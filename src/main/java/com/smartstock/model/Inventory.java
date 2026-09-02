package com.smartstock.model;

import jakarta.persistence.*;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int reservedQuantity;

    @OneToOne
    @JoinColumn(name = "product_id",nullable = false,unique = true)
    private Product product;

}
