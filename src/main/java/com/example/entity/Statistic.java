package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@jakarta.persistence.Table(name = "tblstatistics")
public class Statistic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="stat_id")
	Integer id;
	@Column
	public String statStartDate;
	@Column
	public String statEndDate;
	@Column
	public int statCustomer;
	@Column 
	public int statSales;
	@Column
	public float statRevenue;
	@Column
	public String statNotes;
	@Column
	public LocalDateTime statCreatedTime;
	@Column
	public LocalDateTime statModifiedTime;
}
