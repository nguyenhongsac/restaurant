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

import lombok.Getter;

@Controller
@RequestMapping("/order")
public class OderController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private BillService billService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private FoodService foodService;

	List<OrderDetailDTO> OrderDetailDTOs = new ArrayList<>();

	@GetMapping("/{orderId}")
	public String OrderView(Model model, @PathVariable Integer orderId) {
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
		// model.addAttribute("name", "order");
		OrderDetailDTOs.clear();

		// Category
		List<CategoryEntity> items = categoryService.getAllCategories();

		// Bill
		int i = 0;
		for (OrderDetail item : orderDetails) {
			OrderDetailDTOs.add(new OrderDetailDTO(i, item.getFood().getFoodId(), item.getFood_number(), item.getFood().getFoodName(),
					item.getFood().getFoodPrice(), item.getOrder_note()));
			i++;
		}

		List<FoodEntity> foods = foodService.getAllFoods();
		model.addAttribute("orderId", orderId);
		model.addAttribute("OrderDetailDTOs", OrderDetailDTOs);
		model.addAttribute("foods", foods);
		model.addAttribute("categories", items);
		return "order";
	}

	@PostMapping("/{orderId}/save")
	public ResponseEntity<String> saveOrder(@PathVariable Integer orderId, @RequestBody List<OrderDetailDTO> orders) {
	    //Cập nhật danh sách order detail theo orderId
		orderDetailService.deleteOrderDetailByOrder(orderId);
	    for (OrderDetailDTO order : orders) {
	        // Thực hiện cập nhật bill
	        Integer foodId = order.getFoodId();
	        Integer quantity = order.getQuantity();
	        if(quantity > 0) {
	        	FoodEntity foodEntity = foodService.findFoodById(foodId);
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
//	        // Tìm order detail và cập nhật
//	        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
//	        for (OrderDetail item : orderDetails) {
//	        	boolean exist = false;
//		        if (item.getFood().getFoodId() == foodId) {
//			        item.setFood_number(quantity);
//			        exist = true;
//		        }
//		        if(!exist) {
//		        	FoodEntity foodEntity = foodService.findFoodById(foodId);
//		        	Order orderEntity = orderService.getOrderByOrder(orderId);
//		        	OrderDetail newOrderDetail = new OrderDetail();
//		        	newOrderDetail.setFood(foodEntity);
//					newOrderDetail.setOrder(orderEntity);
//					newOrderDetail.setFood_number(1);
//					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//					newOrderDetail.setOrder_created_time(timestamp);
//					newOrderDetail.setOrder_modified_time(timestamp);
//		
//					// Lưu OrderDetail vào cơ sở dữ liệu
//					orderDetailService.saveOrderDetail(newOrderDetail);
//		        }
//		        break;
//	        }
	    }
	    
//	    for(OrderDetail item : orderDetailService.getOrderDetailsByOrder(orderId)) {
//	    	int i = 0;
//		    for(OrderDetailDTO order : orders) {
//			    if(item.getFood().getFoodId() == order.getFoodId()) {
//				    i++;
//			    }
//		    }
//		    if(i == 0) {
//		    	orderDetailService.deleteOrderDetail(item);
//		    }
//	    }
	    return ResponseEntity.ok("Order has been updated successfully");
	}

//	@PostMapping("/{orderId}/addToBill/{foodId}")
//	public String addToBill(Model model, @PathVariable Integer foodId, @PathVariable Integer orderId) {
//		// Tìm FoodEntity tương ứng trong cơ sở dữ liệu dựa trên foodId
//		FoodEntity foodEntity = foodService.findFoodById(foodId);
//
//		// Kiểm tra nếu foodEntity không tồn tại hoặc không hợp lệ
//		if (foodEntity == null) {
//			return "order";
//		}
//
//		// Tìm OrderDetail tương ứng trong cơ sở dữ liệu dựa trên orderId
//		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
//		
//		//Kiểm tra foodId đã tồn tại trong OrderDetail hay chưa
//		boolean exist = false;
//		for (OrderDetail item : orderDetails) {
//			//Nếu đã tồn tại thì tăng số lượng
//			if (item.getFood().getFoodId() == foodId) {
//				item.setFood_number(item.getFood_number() + 1);
//				Timestamp timestamp = new Timestamp(System.currentTimeMillis());	
//				item.setOrder_modified_time(timestamp);
//				orderDetailService.updateOrderDetail(item);
//				exist = true;
//				break;
//			}
//		}
//		
//		//Nếu chưa tồn tại thì tạo OrderDetail mới
//		if (!exist) {
//			// Tạo OrderDetail mới và truyền dữ liệu vào
//			OrderDetail orderDetail = new OrderDetail();
//			orderDetail.setFood(foodEntity);
//			orderDetail.setOrder(orderService.getOrderByOrder(orderId));
//			orderDetail.setFood_number(1);
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//			orderDetail.setOrder_created_time(timestamp);
//			orderDetail.setOrder_modified_time(timestamp);
//
//			// Lưu OrderDetail vào cơ sở dữ liệu
//			orderDetailService.saveOrderDetail(orderDetail);
//		}
//		model.addAttribute("orderId", orderId);
//		return "order";
//	}

//	@PostMapping("/{orderId}/deleteFromBill")
//	public String deleteFromBill(Model model, @PathVariable Integer orderId) {
//		// Tìm OrderDetail tương ứng trong cơ sở dữ liệu dựa trên orderId
//		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
//		for (OrderDetail item : orderDetails) {
//			orderDetailService.deleteOrderDetail(item);
//		}
//		model.addAttribute("orderId", orderId);
//		return "order";
//	}
//	
//	@PostMapping("/{orderId}/updateQuantity/{foodId}/{quantity}")
//	public String updateQuantity(Model model, @PathVariable Integer foodId, @PathVariable Integer orderId, @PathVariable Integer quantity) {
//		// Tìm OrderDetail tương ứng trong cơ sở dữ liệu dựa trên orderId
//		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
//		for (OrderDetail item : orderDetails) {
//			if (item.getFood().getFoodId() == foodId) {
//				if(quantity > 0) {
//					item.setFood_number(quantity);
//					orderDetailService.updateOrderDetail(item);
//					break;
//				} else {
//					orderDetailService.deleteOrderDetail(item);
//					break;
//				}
//			}
//		}
//		return "order";
//	}

}
