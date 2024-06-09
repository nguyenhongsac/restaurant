package com.example.entity;

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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tblfood")
public class FoodEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="food_id")
	private Integer foodId;
	@Column
	private String foodImg;
	@Column
	private String foodName;
	@Column
	private float foodPrice;
	@Column
	private String foodNote;
	@Column
	private byte foodAvaiable;
	@Column
	private String foodAllergenInfo;
	@Column
	private String foodIngredients;
	@Column
	private Date foodCreateTime;
	@Column
	private Date foodModifieTime;
	@ManyToOne
    @JoinColumn(name="cat_id", nullable=false)
	private CategoryEntity catId;
	
}
