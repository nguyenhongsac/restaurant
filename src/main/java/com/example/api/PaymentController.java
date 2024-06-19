package com.example.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.dto.OrderDetailDTO;
import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.service.CategoryService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.impl.TableServiceImpl;
import com.example.entity.Table;	

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	OrderDetailService orderDetailService;
	
	@Autowired
	TableServiceImpl tableService;
	
	@Autowired
	OrderService orderService;
	
	List<OrderDetailDTO> OrderDetailDTOs = new ArrayList<>();
	
	@GetMapping("/{tableId}")
	public String getPayment(@PathVariable Integer tableId, Model model) {
		Order order = orderService.getLatestOrderByTableId(tableId);
		Integer orderId = order.getOrder_id();
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);

		OrderDetailDTOs.clear();
		Table thisTable = tableService.getById(tableId);

		// Bill
		for (OrderDetail item : orderDetails) {
			OrderDetailDTOs.add(new OrderDetailDTO(item.getOrder_detail_id(), item.getFood().getFoodId(), item.getFood_number(),
					item.getFood().getFoodName(), item.getFood().getFoodPrice(), item.getOrder_note()));
		}
		
		System.out.println(OrderDetailDTOs.size());

		model.addAttribute("OrderDetails", OrderDetailDTOs);
		model.addAttribute("orderId", orderId);
		return "payment";
	}
}
