package com.example.entity;

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
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor 
@Entity
@Table(name = "tblbill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bill_id;

    private String bill_total;
    private String bill_voucher;
    private String bill_status;
    private int bill_tax;
    private Timestamp bill_created_time;
    private Timestamp bill_modified_time;

    //@ManyToOne
    // @JoinColumn(name = "user_id")
    //private User user;
}
