package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.TableDTO;
import com.example.entity.Table;
import com.example.repository.TableRepository;
import com.example.service.TableService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableServiceImpl implements TableService{
	
	TableRepository tr;

	@Override
	public List<Table> getAll() {
		
		return tr.findAll();
	}

	@Override
	public List<Table> getByStatus(String status) {
		
		return tr.findByStatus(status);
	}

	@Override
	public boolean add(Table t) {
		if (tr.existsByName(t.getName())) {
			return false;
		}
		tr.save(t);
		return true;
	}

	@Override
	public boolean update(int id, Table table) {
		// Get table for update
		Optional<Table> opTable = tr.findById(id);
		
		if (!opTable.isPresent()) {
			return false;
		}
		
		Table t = opTable.get();
		tr.save(t);
		
		// Check update
		Optional<Table> upTable = tr.findById(id);
		if (upTable.isPresent()) {
			Table ut = upTable.get();
			return t.equals(ut);
		}
		
		return false;
	}

	@Override
	public boolean delete(int id) {
		if (tr.existsById(id)) {
			tr.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public int countOccupiedTable1() {
		return tr.countOccupiedFloor1();
	}

	@Override
	public int countOccupiedTable2() {
		return tr.countOccupiedFloor2();
	}

	@Override
	public int countFloor1() {
		return tr.countFloor1();
	}

	@Override
	public int countFloor2() {
		return tr.countFloor2();
	}

	@Override
	public List<TableDTO> mapToTableDTO() {
		return null;
	}

	@Override
	public List<Table> getFloor(String floor) {
		List<Table> list = tr.findAll();
		List<Table> rlist = new ArrayList<Table>();
		
		list.forEach(item -> {
			if (item.getName().contains(floor)) {
				rlist.add(item);
			}
		});
		
		return rlist;
	}

	@Override
	public Table getById(int id) {
		// TODO Auto-generated method stub
		return tr.findById(id).get();
	}
}
