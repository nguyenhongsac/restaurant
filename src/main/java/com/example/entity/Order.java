package com.example.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tblorder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

//    private Integer kin_id;

    private Integer table_id;

	@Column(name = "order_status")
    private String order_status;
	@Column(name = "order_number")
    private String order_number;
    @Column(name = "order_created_time")
    private Timestamp order_created_time;
    @Column(name = "order_modified_time")
    private Timestamp order_modified_time;

}

