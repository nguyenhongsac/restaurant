package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.OrderDetail;
import com.example.entity.Table;

public interface TableRepository extends JpaRepository<Table, Integer>{
	public List<Table> findByStatus(String status);
	
	public boolean existsByName(String name);
	
	@Query(value = "SELECT count(*) FROM tbltable WHERE table_name LIKE '%A%' AND table_status = 'occupied'", nativeQuery = true)
	public int countOccupiedFloor1();
	@Query(value = "SELECT count(*) FROM tbltable WHERE table_name LIKE '%B%' AND table_status = 'occupied'", nativeQuery = true)
	public int countOccupiedFloor2();
	@Query(value = "SELECT count(*) FROM tbltable WHERE table_name LIKE '%A%'", nativeQuery = true)
	public int countFloor1();
	@Query(value = "SELECT count(*) FROM tbltable WHERE table_name LIKE '%B%'", nativeQuery = true)
	public int countFloor2();
}
