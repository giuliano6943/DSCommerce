package com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant moment;
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id") //Adicionando uma coluna com a chave estrangeira de clientes
    private User client;

}
