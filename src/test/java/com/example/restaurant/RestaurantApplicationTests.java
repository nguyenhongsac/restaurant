package com.example.restaurant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.service.TableService;

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

		LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedCurrentTime = currentTime.format(formatter);
        System.out.println("Current Time: " + formattedCurrentTime);
	}
}
