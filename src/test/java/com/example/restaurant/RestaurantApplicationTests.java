package com.example.restaurant;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.entity.Table;
import com.example.repository.TableRepository;
import com.example.service.TableService;
import com.example.service.impl.TableServiceImpl;

import lombok.RequiredArgsConstructor;


@SpringBootTest
@RequiredArgsConstructor
class RestaurantApplicationTests {
	
	@Autowired
	private TableService tableService;
	@Test
	void contextLoads() {
	}
	@Test
	void test() {
		
		ArrayList<Table> list1 = (ArrayList<Table>) tableService.getFloor("A");
		ArrayList<Table> list2 = (ArrayList<Table>) tableService.getFloor("B");
		
		System.out.println(list1.size() + " -- " + list2.size());
	}
}
