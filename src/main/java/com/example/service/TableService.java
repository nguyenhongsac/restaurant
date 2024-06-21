package com.example.service;

import java.util.List;

import com.example.dto.TableDTO;
import com.example.entity.Table;

public interface TableService {
	public Table getById(int id);
	public List<Table> getAll();
	public List<Table> getFloor(String floor); // like A or B
	public List<Table> getByStatus(String status);

	public boolean add(Table table);
	public boolean update(Table table);
	public boolean delete(int id);

	public int countOccupiedTable1();
	public int countOccupiedTable2();
	public int countFloor1();
	public int countFloor2();

	public List<TableDTO> getBasicInfo();
	public List<TableDTO> divideFloor(String floor);
	
	public Table getAvailable();
}
