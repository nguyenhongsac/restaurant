package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Statistic;
import com.example.repository.StatisticsRepository;
import com.example.service.StatisticsService;
@Service
public class StatisticsServiceImpl implements StatisticsService {
	@Autowired
	private StatisticsRepository st;
	@Override
	public List<Statistic> getAll() {
		// TODO Auto-generated method stub
		return this.st.findAll();
	}
}
