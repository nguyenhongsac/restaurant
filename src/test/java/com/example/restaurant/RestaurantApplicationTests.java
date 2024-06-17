package com.example.restaurant;

import java.time.Duration;
import java.time.LocalDateTime;
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

		String timestamp = "2024-06-08 23:38:56";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
        LocalDateTime givenTime = LocalDateTime.parse(timestamp, formatter);

        // Current time
        LocalDateTime now = LocalDateTime.now();

        // Calculate the difference
        Duration duration = Duration.between(givenTime, now);

        // Get the difference in minutes
        long minutesDifference = duration.toMinutes();
        String out = ""+minutesDifference+"";
        System.out.println("Difference in minutes: " + out);
	}
}
