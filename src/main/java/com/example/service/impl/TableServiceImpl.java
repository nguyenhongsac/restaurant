package com.example.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.TableDTO;
import com.example.entity.Table;
import com.example.repository.TableRepository;
import com.example.service.TableService;
import com.example.util.TimeManage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableServiceImpl implements TableService{

	TableRepository tr;
	TimeManage time;
	
	@Override
	public Table getById(int id) {
		return tr.findById(id).get();
	}
	
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
		t.setCreatedTime(time.getCurrentDateTime());
		t.setModifiedTime(time.getCurrentDateTime());
		tr.save(t);
		return true;
	}

	@Override
	public boolean update(Table table) {
		// Get table for update
		Optional<Table> opTable = tr.findById(table.getId());

		if (!opTable.isPresent()) {
			return false;
		}

		Table t = opTable.get();
		t.setModifiedTime(time.getCurrentDateTime());
		tr.save(t);

		// Check update
		Optional<Table> upTable = tr.findById(table.getId());
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
	public List<Table> getFloor(String floor) {
		List<Table> list = tr.findAll();
		List<Table> rlist = new ArrayList<>();

		list.forEach(item -> {
			if (item.getName().contains(floor)) {
				rlist.add(item);
			}
		});

		return rlist;
	}

	@Override
	public List<TableDTO> getBasicInfo() {
		List<Table> rawList = tr.findAll();
		List<TableDTO> showList = new ArrayList<>();

		// Map raw info
		try {
		rawList.forEach(item -> {
			TableDTO t = new TableDTO();
			// For table reserve or occupied
			if ("reserve".equals(item.getStatus()) || "occupied".equals(item.getStatus())) {
				List<Object[]> raw = tr.getInfo(item.getId());
				if (!raw.isEmpty()) {
					Object[] info = raw.get(0);
					try {
					t = new TableDTO((Integer) info[0], (String) info[1], (String) info[2], (Integer) info[3], (String) info[4], (String) info[5], (BigDecimal) info[6]);

					// Caculate time
					String timestamp = (String) info[2];
					DateTimeFormatter formatter;
					if (timestamp.length() < 19) { // CHECK IF HAVE SECONDS ATTRIBUTES
						formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm");
					} else {
						formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
					}
			        LocalDateTime givenTime = LocalDateTime.parse(timestamp, formatter);

			        LocalDateTime now = LocalDateTime.now();// Current time
			        Duration duration = Duration.between(givenTime, now);// Calculate the difference

			        // Get the difference in minutes
			        long minutesDifference = duration.toMinutes();
			        t.setStartTime(""+minutesDifference+"");

					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			// For tables waiting or available
			} else {
				t.setId(item.getId());
				t.setName(item.getName());
				t.setNote(item.getNote());
				t.setStatus(item.getStatus());
				t.setStartTime("0");
				t.setPeople(0);
			}
			showList.add(t);
		});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return showList;
	}

	@Override
	public List<TableDTO> divideFloor(String floor) {
		List<TableDTO> rawList = this.getBasicInfo();
		List<TableDTO> list = new ArrayList<>();

		rawList.forEach(item -> {
			if (item.getName().contains(floor)) {
				list.add(item);
			}
		});

		return list;
	}
	
	@Override
	public Table getAvailable() {
		return tr.findAvailable();
	}

}
