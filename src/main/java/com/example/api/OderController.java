package com.example.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.dto.OrderDetailDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.FoodEntity;
import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.service.BillService;
import com.example.service.CategoryService;
import com.example.service.FoodService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.impl.CategoryServiceImpl;
import com.example.service.impl.FoodServiceImpl;

import lombok.Getter;

@Controller
@RequestMapping("/order")
public class OderController {
	@Autowired
	private CategoryServiceImpl categoryService;
	@Autowired
	private BillService billService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private FoodServiceImpl foodService;

	List<OrderDetailDTO> OrderDetailDTOs = new ArrayList<>();

	@GetMapping("/{orderId}")
	public String OrderView(Model model, @PathVariable Integer orderId) {
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
		// model.addAttribute("name", "order");
		OrderDetailDTOs.clear();

		// Category
		List<CategoryEntity> items = categoryService.getAll();

		// Bill
		int i = 0;
		for (OrderDetail item : orderDetails) {
			OrderDetailDTOs.add(new OrderDetailDTO(i, item.getFood().getFoodId(), item.getFood_number(),
					item.getFood().getFoodName(), item.getFood().getFoodPrice(), item.getOrder_note()));
			i++;
		}

		List<FoodEntity> foods = foodService.getAll();
		model.addAttribute("orderId", orderId);
		model.addAttribute("OrderDetailDTOs", OrderDetailDTOs);
		model.addAttribute("foods", foods);
		model.addAttribute("categories", items);
		return "order";
	}

	@PostMapping("/{orderId}/save")
	public ResponseEntity<String> saveOrder(@PathVariable Integer orderId, @RequestBody List<OrderDetailDTO> orders) {
		
		// Cập nhật danh sách order detail theo orderId
		orderDetailService.deleteOrderDetailByOrder(orderId);
		for (OrderDetailDTO order : orders) {
			
			// Thực hiện cập nhật bill
			Integer foodId = order.getFoodId();
			Integer quantity = order.getQuantity();
			if (quantity > 0) {
				FoodEntity foodEntity = foodService.findById(foodId);
				Order orderEntity = orderService.getOrderByOrder(orderId);
				OrderDetail newOrderDetail = new OrderDetail();
				newOrderDetail.setFood(foodEntity);
				newOrderDetail.setOrder(orderEntity);
				newOrderDetail.setFood_number(quantity);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				newOrderDetail.setOrder_created_time(timestamp);
				newOrderDetail.setOrder_modified_time(timestamp);
				newOrderDetail.setOrder_note(order.getNote());

				// Lưu OrderDetail vào cơ sở dữ liệu
				orderDetailService.saveOrderDetail(newOrderDetail);
			}
		}
		return ResponseEntity.ok("Order has been updated successfully");
	}

}
