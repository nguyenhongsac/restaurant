package com.example.entity;

import java.io.Serializable;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
}
