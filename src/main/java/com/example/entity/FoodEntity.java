package com.example.entity;

import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name = "tblfood")
public class FoodEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="food_id")
	public Integer foodId;
	@Column
	public String foodImg;
	@Column
	public String foodName;
	@Column
	public float foodPrice;
	@Column
	public String foodNote;
	@Column
	public boolean foodAvailable;
	@Column
	public String foodAllergenInfo;
	@Column
	public String foodIngredients;
	@Column
	public LocalDateTime foodCreatedTime;
	@Column
	public LocalDateTime foodModifiedTime;
	@ManyToOne
    @JoinColumn(name="cat_id")
	public CategoryEntity category;
}
