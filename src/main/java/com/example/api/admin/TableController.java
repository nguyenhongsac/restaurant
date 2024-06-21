package com.example.api.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.dto.TableDTO;
import com.example.entity.Table;
import com.example.service.TableService;

@Controller
public class TableController {
	@Autowired
	private TableService tableService;
	@GetMapping("/table")
	public String getAll(Model model) {
		List<Table> table = this.tableService.getAll();
		model.addAttribute("table", table);
		return "table-data";
	}
	@PostMapping("/add-table")
	public String add(@RequestParam String name,@RequestParam String fullname,@RequestParam Integer seat,@RequestParam String note) {
		Table table = new Table();
		table.setName(name);
		table.setFullName(fullname);
		table.setSeat(seat);
		table.setNote(note);
		table.setCreatedTime(LocalDateTime.now().toString());
		table.setModifiedTime(LocalDateTime.now().toString());
		tableService.add(table);
		return "redirect:/table";
	}
	
	@PostMapping("/update-table/{id}")
	public String update(@PathVariable Integer id,@RequestParam String name,@RequestParam String fullname,@RequestParam Integer seat,@RequestParam String note) {
		Table table = this.tableService.getById(id);
		table.setName(name);
		table.setFullName(fullname);
		table.setSeat(seat);
		table.setNote(note);
		table.setCreatedTime(LocalDateTime.now().toString());
		table.setModifiedTime(LocalDateTime.now().toString());
		tableService.update(table);
		return "redirect:/table";
	}
	
	@PostMapping("/delete-table/{id}")
	public String del(@PathVariable Integer id) {
		tableService.delete(id);
		return "redirect:/table";

	}

	
}
