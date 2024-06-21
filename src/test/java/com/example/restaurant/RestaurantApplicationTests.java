package com.example.restaurant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.service.TableService;

import lombok.AllArgsConstructor;
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
        
        LocalDateTime givenTime = LocalDateTime.of(2024, 06, 20, 14, 30, 00);
        LocalDateTime now = LocalDateTime.now();// Current time
        Duration duration = Duration.between(givenTime, now);
        long minutesDifference = duration.toMinutes();
        System.out.println(minutesDifference);
	}
}
