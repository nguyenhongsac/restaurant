package com.example.api.admin;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.dto.FoodDTO;
import com.example.dto.OrderDetailDTO;
import com.example.entity.Bill;
import com.example.entity.OrderDetail;
import com.example.entity.Statistic;
import com.example.repository.OrderDetailRepository;
import com.example.repository.ReportRepository;
import com.example.repository.StatisticsRepository;
import com.example.service.BillService;
import com.example.service.StatisticsService;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class AdminController {
	private BillService billService;

	private StatisticsRepository st;

	private ReportRepository rp;

	private StatisticsService statisticsService;

	@GetMapping("/admin")
	public String admin(Model model) {
		// tính trung bình
		List<Object[]> getAvg = this.st.getAvgBill();

		Object[] ob = getAvg.get(0);

		Long total = (Long) ob[0];
		BigDecimal avgTime = (BigDecimal) ob[1];
		BigDecimal avgPeople = (BigDecimal) ob[2];
		BigDecimal avgFoodNum = (BigDecimal) ob[3];

		model.addAttribute("avgFoodNum", avgFoodNum);
		model.addAttribute("avgTime", avgTime);
		model.addAttribute("avgPeople", avgPeople);
		model.addAttribute("total", total);

		// Lấy doanh thu theo tháng
		List<Object[]> getMonthlyReport = this.rp.findMonthlyReport();
		Object[] monthlyReport = getMonthlyReport.get(0);
		BigDecimal totalCustomers = (BigDecimal) monthlyReport[0];
		BigDecimal totalRevenue = (BigDecimal) monthlyReport[1];
		DecimalFormat df = new DecimalFormat("#.##");
		BigDecimal totalFoodSold = (BigDecimal) monthlyReport[2];

		model.addAttribute("totalCustomers", totalCustomers);
		model.addAttribute("totalRevenue", df.format(totalRevenue));
		model.addAttribute("totalFoodSold", totalFoodSold);

		List<Bill> bill = billService.getAllBills();
		model.addAttribute("bill", bill);

		// Top Saling
		List<FoodDTO> food = new ArrayList<FoodDTO>();
		List<Object[]> f = st.findTopSellingProducts();
		for (Object[] objects : f) {
			FoodDTO f1 = new FoodDTO();
			f1.setFoodNumber((BigDecimal) objects[1]);
			f1.setFoodName((String) objects[0]);
			BigDecimal value = (BigDecimal) objects[2];
			Double value1 = value.doubleValue();
			f1.setRevenue(Math.round((Double) value1 * Math.pow(10, 2)) / Math.pow(10, 2));
			f1.setFoodImg((String) objects[3]);
			value = (BigDecimal) objects[4];
			float value2 = value.floatValue();
			f1.setFoodPrice((Float) value2);
			food.add(f1);
		}
		model.addAttribute("f", food);

		// RecentSale
		List<FoodDTO> food1 = new ArrayList<FoodDTO>();
		List<Object[]> f2 = st.findRecentSale();
		for (Object[] objects : f2) {
			FoodDTO f3 = new FoodDTO();
			f3.setId((Integer) objects[0]);
			f3.setCatName((String) objects[1]);
			f3.setFoodName((String) objects[2]);
			BigDecimal value = (BigDecimal) objects[3];
			float value2 = value.floatValue();
			f3.setFoodPrice(value2);
			f3.setStatus((String) objects[4]);
			food1.add(f3);
		}
		model.addAttribute("f1", food1);

		// Report
		List<Statistic> statistic = this.statisticsService.getAll();

		List<Integer> statCustomer = new ArrayList<Integer>();
		List<Integer> statSales = new ArrayList<Integer>();
		List<Float> statRevenue = new ArrayList<Float>();
		List<String> statMonth = new ArrayList<String>();

		for (Statistic item : statistic) {
			statCustomer.add(item.getStatCustomer());
			statSales.add(item.getStatSales());
			statRevenue.add(item.getStatRevenue());
			String dateString = item.getStatStartDate();

			try {
				// Chuyển đổi chuỗi thành đối tượng LocalDate
				LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

				// Lấy tháng từ đối tượng LocalDate
				int month = date.getMonthValue();
				statMonth.add("Tháng " +month);

			} catch (DateTimeParseException e) {
				
			}
			
		}
		Collections.reverse(statMonth);
		model.addAttribute("statCustomer", statCustomer);
		model.addAttribute("statSales", statSales);
		model.addAttribute("statRevenue", statRevenue);
		model.addAttribute("statMonth", statMonth );
		return "dashboard";
	}
}
