package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

	@Query(value = "SELECT t.table_id as id, t.table_name as name, b.bill_start_time as startTime, b.bill_people as people, t.table_status as status, t.table_note as note, b.bill_total as total " +
            "FROM tbltable t " +
            "INNER JOIN tblorder o ON t.table_id = o.table_id " +
            "INNER JOIN tblbill b ON o.bill_id = b.bill_id " +
            "WHERE t.table_id = ?1 " +
            "ORDER BY b.bill_start_time DESC " +
            "LIMIT 1", nativeQuery = true)
	public List<Object[]> getInfo(int tableId);
	
	@Query(value = "SELECT * FROM tbltable WHERE table_status = 'available' LIMIT 1", nativeQuery = true)
	public Table findAvailable();
}
