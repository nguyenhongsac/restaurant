package com.example.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tblcategory")
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cat_id")
	public Integer catId;
	
	@Column(name="cat_name")
	public String catName;	
	@Column(name="cat_description")
	public String catDescription;
	@Column
	public Date catCreateTime;
	@Column
	public Date catModifieTime;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<FoodEntity> list = new ArrayList<>();
	public Integer getCatId() {
		return catId;
	}
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getCatDescription() {
		return catDescription;
	}
	public void setCatDescription(String catDescription) {
		this.catDescription = catDescription;
	}
	public Date getCatCreateTime() {
		return catCreateTime;
	}
	public void setCatCreateTime(Date catCreateTime) {
		this.catCreateTime = catCreateTime;
	}
	public Date getCatModifieTime() {
		return catModifieTime;
	}
	public void setCatModifieTime(Date catModifieTime) {
		this.catModifieTime = catModifieTime;
	}
	public List<FoodEntity> getList() {
		return list;
	}
	public void setList(List<FoodEntity> list) {
		this.list = list;
	}
}
