package com.example.api.table;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Table;
import com.example.service.TableService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/table")
public class TableController {

	TableService tableService;

	@GetMapping("/getall")
	public List<Table> getAll() {

		return tableService.getAll();
	}

	@PostMapping("/add") // missing password encoder
	public ResponseEntity<String> add(@RequestBody Table tableDTO) {
		if (tableService.add(tableDTO)) {
			return ResponseEntity.ok().body("Add table successful!");
		}

		return ResponseEntity.ok().body("Add table failed!");
	}

	@PostMapping("/update")
	public ResponseEntity<String> update(@RequestParam int id, @RequestBody Table tableDTO) {
		if (tableService.update(tableDTO)) {
			return ResponseEntity.ok().body("Update table "+id+" successful!");
		}
		return ResponseEntity.ok().body("Update table "+id+" failed!");
	}

	@DeleteMapping("/del")
	public ResponseEntity<String> del(@RequestParam int id) {
		if (tableService.delete(id)) {
			return ResponseEntity.ok().body("Delete table "+id+" successful!");
		}
		return ResponseEntity.ok().body("Delete table "+id+" failed!");
	}
}
