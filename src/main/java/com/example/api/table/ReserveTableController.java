package com.example.api.table;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.ReserveDTO;
import com.example.dto.TableDTO;
import com.example.entity.*;
import com.example.service.BillService;
import com.example.service.OrderService;
import com.example.service.TableService;
import com.example.service.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Controller
@AllArgsConstructor
public class ReserveTableController {
	
	private TableService tableService;
	private UserService userService;
	private BillService billService;
	private OrderService orderService;

	@GetMapping("/reserve")
	public String reserveHome(Model model) {
		model.addAttribute("reserveDTO", new ReserveDTO());
		return "book-table";
	}
	
	@PostMapping("/reserve/guest")
	public String reserveGuest(@ModelAttribute ReserveDTO reserveTable, Model model) {
		
		// Update status of a reserve table
		Table table = tableService.getAvailable();
		table.setNote("");
		table.setStatus("reserve");
		table.setNote(reserveTable.getNote());
		if (tableService.update(table)) {
			// Set user, bill data for reserve
			User user = new User();
			user.setFullname(reserveTable.getUserName());
			user.setEmail(reserveTable.getUserEmail());
			user.setPhone(reserveTable.getUserPhone());
			user = userService.addUser(user); // add and update current variable
			
			Bill bill = new Bill();
			bill.setUser(user);
			bill.setBill_created_time(new Timestamp(System.currentTimeMillis()));
			bill.setBill_modified_time(new Timestamp(System.currentTimeMillis()));
			bill.setBill_start_time(reserveTable.getDate() + " " +reserveTable.getTime());
			bill.setBill_people(reserveTable.getPeople());
			bill.setBill_status("null");
			bill = billService.saveBill(bill);
			
			Order order = new Order();
			order.setBill(bill);
			order.setTable_id(table.getId());
			
			order = orderService.add(order);
			
			if (order != null) {
				model.addAttribute("responseMessage", "Your booking request was sent. We will call back or send an Email to confirm your reservation. Thank you!");
			} else {
				model.addAttribute("responseMessage", "Cannot booking right now, please try again later!");
			}
		} else {
			model.addAttribute("responseMessage", "Cannot booking right now, please try again later!");
		};
		
		return "redirect:/reserve";
	}
	
	@PostMapping("/reserve/admin")
	public ResponseEntity<String> reserveAdmin(@RequestBody ReserveDTO reserveTable) {
		
		// Update status of a reserve table
		Table table = tableService.getById(reserveTable.getTableId());
		table.setStatus("reserve");
		table.setNote("");
		table.setNote(reserveTable.getNote());
		if (tableService.update(table)) {
			// Set user, bill data for reserve
			User user = new User();
			user.setFullname(reserveTable.getUserName());
			user.setPhone(reserveTable.getUserPhone());
			user = userService.addUser(user); // add and update current variable
			
			Bill bill = new Bill();
			bill.setUser(user);
			bill.setBill_created_time(new Timestamp(System.currentTimeMillis()));
			bill.setBill_modified_time(new Timestamp(System.currentTimeMillis()));
			bill.setBill_start_time(reserveTable.getDate() + " " +reserveTable.getTime()+":00");
			bill.setBill_people(reserveTable.getPeople());
			bill.setBill_status("null");
			bill = billService.saveBill(bill);
			
			Order order = new Order();
			order.setBill(bill);
			order.setTable_id(table.getId());
			order = orderService.add(order);
			
			if (order != null) {
				System.out.println("Reserve table "+table.getId());
			} else {
				return ResponseEntity.ok("Reserve table "+table.getName()+" failed!");
			}
		} else {
			return ResponseEntity.ok("Reserve table "+table.getName()+" failed!");
		};
		
		return ResponseEntity.ok("Reserve table "+table.getName()+" successfully!");
	}
	
	@PostMapping("/reserve/cancel")
	public String cancelReserve(@RequestParam int id, Model model) {
		Table table = tableService.getById(id);
		table.setStatus("available");
		tableService.update(table);

		return "redirect:/home";
	}
}
