package com.example.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.entity.Table;
import com.example.repository.StatisticsRepository;
import com.example.repository.TableRepository;
import com.example.service.TableService;
import com.example.service.impl.TableServiceImpl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@SpringBootTest
@RequiredArgsConstructor
class RestaurantApplicationTests {
	@Test
	void contextLoads() {
	}
	@Test
	void test() {
		
//		List<Object[]> get = this.st.getAvgBill();
//		
//		Object[] ob = get.get(0);
//		
//		float total= (float) ob[0];
//		float avgTime= (float) ob[1];
//		float avgPeople= (float) ob[2];
//		float avgFoodNum= (float) ob[3];
//		System.out.println(total);
//		System.out.println(avgFoodNum);
//		System.out.println(avgPeople);
//		System.out.println(avgTime);
	}
}
