package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.entity.Bill;

public interface ReportRepository extends JpaRepository<Bill, Integer> {
	// Bao cao theo thang lay tam du lieu cua thang 4
	@Query(value = "SELECT  "
			+ "    IFNULL(SUM(b.bill_people), 0) AS total_customers, "
			+ "    IFNULL(SUM(b.bill_total), 0) AS total_revenue, "
			+ "    IFNULL(SUM(od.food_number), 0) AS total_food_sold "
			+ "FROM  "
			+ "    tblbill b "
			+ "JOIN  "
			+ "    tblorder o ON b.bill_id = o.bill_id "
			+ "JOIN  "
			+ "    tblorderdetail od ON o.order_id = od.order_id "
			+ "WHERE  "
			+ "    YEAR(b.bill_end_time) = YEAR(CURRENT_DATE) " +
            "    AND MONTH(b.bill_end_time) = MONTH(CURRENT_DATE);", nativeQuery = true)
	public List<Object[]> findMonthlyReport();

	// bao cao theo ngay
	@Query(value="SELECT "
			+ "    IFNULL(SUM(b.bill_people), 0) AS total_customers, "
			+ "    IFNULL(SUM(b.bill_total), 0) AS total_revenue, "
			+ "    IFNULL(SUM(od.food_number), 0) AS total_food_sold "
			+ "FROM "
			+ "    tblbill b "
			+ "JOIN "
			+ "    tblorder o ON b.bill_id = o.bill_id "
			+ "JOIN "
			+ "    tblorderdetail od ON o.order_id = od.order_id "
			+ "WHERE "
			+ "    DATE(b.bill_end_time) = CURDATE() "
			+ "GROUP BY "
			+ "    DATE(b.bill_end_time);" , nativeQuery = true)
	public List<Object[]> findDailyReport();

	@Query(value="SELECT "
			
			+ "    IFNULL(SUM(b.bill_people), 0) AS total_customers, "
			+ "    IFNULL(SUM(b.bill_total), 0) AS total_revenue, "
			+ "    IFNULL(SUM(od.food_number), 0) AS total_food_sold "
			+ "FROM "
			+ "    tblbill b "
			+ "JOIN "
			+ "    tblorder o ON b.bill_id = o.bill_id "
			+ "JOIN "
			+ "    tblorderdetail od ON o.order_id = od.order_id "
			+ "WHERE "
			+ "    YEAR(b.bill_end_time) = YEAR(CURDATE())  "
			+ "GROUP BY "
			+ "    YEAR(b.bill_end_time);", nativeQuery = true)
	public List<Object[]> findYearlyReport();
}
