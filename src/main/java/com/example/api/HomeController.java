package com.example.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dto.TableDTO;
import com.example.entity.Table;
import com.example.entity.User;
import com.example.service.TableService;
import com.example.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {

	TableService tableService;
	UserService userService;

	@GetMapping("/home")
    public String home(HttpSession session, Model model) {
		// Set logged in user
		User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedUser", loggedUser);

        // Set data
        ArrayList<Table> tableList = (ArrayList<Table>) tableService.getAll();
        ArrayList<TableDTO> tableList1 = (ArrayList<TableDTO>) tableService.divideFloor("A");
        ArrayList<TableDTO> tableList2 = (ArrayList<TableDTO>) tableService.divideFloor("B");
        if (tableList == null || tableList.isEmpty()) {
        	model.addAttribute("error", "Can not get table list.");
        	return "home";
        }
        // Set empty / total
        int occupiedFloor1 = tableService.countOccupiedTable1();
        int total1 = tableService.countFloor1();
        int occupiedFloor2 = tableService.countOccupiedTable2();
        int total2 = tableService.countFloor2();
        int occupiedTotal = occupiedFloor1 + occupiedFloor2;
        int totalAll = total1 + total2;
        model.addAttribute("total", (totalAll-occupiedTotal) + "/" + totalAll);
        model.addAttribute("floor1", (total1-occupiedFloor1) + "/" + total1);
        model.addAttribute("floor2", (total2-occupiedFloor2) + "/" + total2);

        // Set table -- change to DTO later
        model.addAttribute("tableFloor1", tableList1);
        model.addAttribute("tableFloor2", tableList2);

        return "home";
    }

	@PostMapping("/home/update")
	public String update(Model model) {
		return "redirect:/login";
	}

	@GetMapping("/home/test")
	public List<TableDTO> haha() {
		List<TableDTO> list = tableService.getBasicInfo();

		return list;
	}

}
