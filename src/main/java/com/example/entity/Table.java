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
@Entity(name = "tbltable")
public class Table {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="table_id")
	Integer id;
	@Column(name="table_name")
	String name;
	@Column(name="table_fullname")
	String fullName;
	@Column(name="table_status")
	String status;
	@Column(name="table_seat")
	Integer seat;
	@Column(name="table_note")
	String note;
	@Column(name="table_created_time")
	String createdTime;
	@Column(name="table_modified_time")
	String modifiedTime;
}
