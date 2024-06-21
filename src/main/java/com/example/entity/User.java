package com.example.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "tbluser")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	Integer id;
	@Column(name="user_name")
	String name;
	@Column(name="user_password")
	String password;
	@Column(name="user_fullname")
	String fullname;
	@Column(name="user_avatar")
	String avatar;
	@Column(name="user_dob")
	String dob;
	@Column(name="user_gender")
	String gender;
	@Column(name="user_phone")
	String phone;
	@Column(name="user_email")
	String email;
	@Column(name="user_address")
	String address;
	@Column(name="user_role")
	String role;
	@CreationTimestamp
	@Column(name="user_created_time")
	LocalDateTime createdTime;
	@UpdateTimestamp
	@Column(name="user_modified_time")
	LocalDateTime modifiedTime;
	@Column(name="user_last_logined")
	String lastLogined;
	@Column(name="user_permission")
	Integer permission;
	@Column(name="user_applyyear")
	Integer applyyear;
	@Column(name="user_deleted")
	Boolean deleted;

}
