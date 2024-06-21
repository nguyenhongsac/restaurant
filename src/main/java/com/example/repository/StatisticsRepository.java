package com.example.repository;

import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dto.FoodDTO;
import com.example.entity.OrderDetail;
import com.example.entity.Statistic;
import com.example.entity.Table;

public interface StatisticsRepository extends JpaRepository<Statistic, Integer>{

	@Query(value = "SELECT count(bill_id) as total, "
			+ "avg(TIMESTAMPDIFF(MINUTE, bill_start_time, bill_end_time)) as avg_time, "
			+ "avg(bill_people) as avg_people, "
			+ "(select avg(od.food_number) from tblorderdetail od inner join "
			+ "(tblorder o inner join tblbill b on b.bill_id = o.bill_id) on od.order_id = o.order_id "
			+ "where o.bill_id = b.bill_id) as avg_foodnumber "
			+ "FROM tblbill "
			+ "WHERE bill_id > 0 and bill_end_time IS NOT NULL;", nativeQuery = true)
	public List<Object[]> getAvgBill();
	
	@Query(value = "SELECT  "
			+ "    f.food_name,  "
			+ "    SUM(od.food_number) AS total_quantity,  "
			+ "    SUM(od.food_number * f.food_price) AS total_revenue,  "
			+ "    f.food_img,  "
			+ "    f.food_price "
			+ "FROM  "
			+ "    tblorderdetail od "
			+ "JOIN  "
			+ "    tblfood f ON od.food_id = f.food_id "
			+ "JOIN  "
			+ "    tblorder o ON od.order_id = o.order_id "
			+ "JOIN  "
			+ "    tblbill b ON o.bill_id = b.bill_id "
			+ "GROUP BY  "
			+ "    f.food_id "
			+ "ORDER BY  "
			+ "    total_quantity DESC "
			+ "LIMIT 5;", nativeQuery = true)
	public List<Object[]> findTopSellingProducts();
	@Query(value = "SELECT "
			+ "    f.food_id, "
			+ "    c.cat_name, "
			+ "    f.food_name, "
			+ "    f.food_price, "
			+ "    o.order_status, "
			+ "    od.order_detail_created_time "
			+ "FROM "
			+ "    tblorderdetail od "
			+ "JOIN "
			+ "    tblfood f ON od.food_id = f.food_id "
			+ "JOIN "
			+ "    tblcategory c ON f.cat_id = c.cat_id "
			+ "JOIN "
			+ "    tblorder o ON od.order_id = o.order_id "
			+ "ORDER BY "
			+ "    od.order_detail_created_time DESC "
			+ "LIMIT 5;", nativeQuery = true)
	public List<Object[]> findRecentSale();
	
	
	
}
