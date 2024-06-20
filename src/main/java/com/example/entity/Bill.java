package com.example.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private int bill_people;
    private String bill_start_time;
    private String bill_end_time;
    private int user_id;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}
