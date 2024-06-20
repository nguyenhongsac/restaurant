package com.example.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.dto.OrderDetailDTO;
import com.example.entity.Bill;
import com.example.entity.CategoryEntity;
import com.example.entity.FoodEntity;
import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.Table;
import com.example.entity.User;
import com.example.service.BillService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.UserService;
import com.example.service.impl.CategoryServiceImpl;
import com.example.service.impl.FoodServiceImpl;
import com.example.service.impl.TableServiceImpl;
import com.example.service.impl.UserServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


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
	@Autowired
	private TableServiceImpl tableService;

	@Autowired
	private UserServiceImpl userService;

	List<OrderDetailDTO> OrderDetailDTOs = new ArrayList<>();

	@GetMapping("/{tableId}")
	public String OrderView(Model model, @PathVariable Integer tableId) {
		Order order = orderService.getLatestOrderByTableId(tableId);
		if (order == null) {
			// Lấy User theo id
			User user = userService.findUserById(1);

			// Tạo bill mới theo user_id
			Bill bill = new Bill();

			// cài đặt các thuộc tính cần thiết
			bill.setUser(user);
			bill.setBill_created_time(new Timestamp(System.currentTimeMillis()));
			bill.setBill_modified_time(new Timestamp(System.currentTimeMillis()));
			bill.setBill_status("null");

			billService.saveBill(bill);

			// Tạo order theo bill vừa tạo
			order = new Order();

			// Thêm các thuộc tính cần thiết
			order.setBill(bill);
			order.setOrder_created_time(new Timestamp(System.currentTimeMillis()));
			order.setTable_id(tableId);
			order.setOrder_status("done");
			orderService.addOrder(order);
		}
		OrderDetailDTOs.clear();

		// Category
		List<CategoryEntity> items = categoryService.getAll();

		List<FoodEntity> foods = foodService.getAll();

		// Bill
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(order.getOrder_id());
		for (OrderDetail item : orderDetails) {
			OrderDetailDTOs.add(
					new OrderDetailDTO(item.getOrder_detail_id(), item.getFood().getFoodId(), item.getFood_number(),
							item.getFood().getFoodName(), item.getFood().getFoodPrice(), item.getOrder_note()));
		}

		// Table
		Table thisTable = tableService.getById(tableId);
		thisTable.setStatus("busy");
		tableService.update(tableId, thisTable);
		// Bàn hiện tại
		List<Table> tables = new ArrayList<>();
		tableService.getByStatus("available").forEach(item -> { // Lấy danh sách bàn trống
			if (item.getId() != tableId) {
				tables.add(item);
			}
		});

		model.addAttribute("thisTable", thisTable);
		model.addAttribute("tableId", tableId);
		model.addAttribute("OrderDetailDTOs", OrderDetailDTOs);
		model.addAttribute("foods", foods);
		model.addAttribute("categories", items);
		model.addAttribute("tables", tables);
		return "order";
	}

	@PostMapping("/{tableId}/save")
	public String saveOrder(@PathVariable Integer tableId, @RequestBody List<OrderDetailDTO> orders) {
		Order order = orderService.getLatestOrderByTableId(tableId);
		Integer orderId = order.getOrder_id();
		// Cập nhật danh sách order detail theo orderId
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
		boolean exist;
		
		//Chọn vòng lặp phù hợp theo kích thước của 2 list 
		if (orderDetails.size() >= orders.size()) {
			for (OrderDetail item : orderDetails) {
				exist = false;
				for (OrderDetailDTO dto : orders) {
					//Tìm các cặp có cùng id 2 mảng để sửa dữ liệu, nếu số lượng giống nhau thì bỏ qua
					if (item.getOrder_detail_id().equals(dto.getId()) && dto.getQuantity() != item.getFood_number()) {
						
						//Thay đổi số lượng theo dữ liệu mới nhận được
						item.setFood_number(dto.getQuantity());
						if (item.getFood_number() == 0) {
							//Nếu số lượng bằng 0 thì xóa đối tượng
							orderDetailService.deleteOrderDetail(item);
						} else {
							//Số lượng khác 0 thì lưu thay đổi, lưu thời gian thay đổi
							Timestamp timestamp = new Timestamp(System.currentTimeMillis());
							item.setOrder_detail_modified_time(timestamp);
							orderDetailService.updateOrderDetail(item);
						}
						exist = true;
						break;
					} 
				}
			}
		} else {
			for (OrderDetailDTO dto : orders) {
				exist = false;
				for (OrderDetail item : orderDetails) {
					if (item.getOrder_detail_id().equals(dto.getId()) && dto.getQuantity() != item.getFood_number()) {
						item.setFood_number(dto.getQuantity());
						if (item.getFood_number() == 0) {
							orderDetailService.deleteOrderDetail(item);
						} else {
							Timestamp timestamp = new Timestamp(System.currentTimeMillis());
							item.setOrder_detail_modified_time(timestamp);
							orderDetailService.updateOrderDetail(item);
						}
						exist = true;
						break;
					}
				}
				if (!exist) {
					OrderDetail newOrderDetail = new OrderDetail();
					Order orderEntity = orderService.getOrderByOrder(orderId);
					FoodEntity foodEntity = foodService.findById(dto.getFoodId());
					newOrderDetail.setFood(foodEntity);
					newOrderDetail.setOrder(orderEntity);
					newOrderDetail.setFood_number(dto.getQuantity());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					newOrderDetail.setOrder_detail_created_time(timestamp);
					newOrderDetail.setOrder_note(dto.getNote());
					// Lưu OrderDetail vào cơ sở dữ liệu
					orderDetailService.saveOrderDetail(newOrderDetail);
				}
			}
		}
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if(timestamp.compareTo(order.getOrder_created_time()) > 0) {
			order.setOrder_created_time(timestamp);
		}
		
		List<OrderDetail> ODL = orderDetailService.getOrderDetailsByOrder(orderId);
		System.out.println(ODL.size());
		if (ODL.size() == 0) {
			Bill bill = billService.getBillById(order.getBill().getBill_id());
			orderService.deleteOrder(orderId);
			billService.deleteBill(bill.getBill_id());
			Table table = tableService.getById(tableId);
			table.setStatus("available");
			tableService.update(tableId, table);
		}
		return "redirect:/order/" + tableId;
	}

	@PostMapping("/{OldTableId}/changeTable/{tableId}")
	public String addOrder(@PathVariable Integer OldTableId, @PathVariable Integer tableId) {
		// Tìm đối tượng order theo orderId
		Order orderEntity = orderService.getLatestOrderByTableId(OldTableId);

		// Đổi tableId của order sang tableId mới
		orderEntity.setTable_id(tableId);

		// Đổi trạng thái bàn gốc sang available
		Table OldTable = tableService.getById(OldTableId);
		OldTable.setStatus("available");
		tableService.update(OldTableId, OldTable);

		// Đổi trạng thái bàn chuyển sang busy
		Table NewTable = tableService.getById(tableId);
		NewTable.setStatus("busy");
		tableService.update(tableId, NewTable);

		orderService.updateOrder(orderEntity);
		return "redirect:/order/" + tableId;
	}

	@PostMapping("{tableId}/print-order")
	public ResponseEntity<byte[]> printOrder(@PathVariable Integer tableId) {
		Order orderEntity = orderService.getLatestOrderByTableId(tableId);
		Integer orderId = orderEntity.getOrder_id();
		Table tableEntity = tableService.getById(tableId);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// Thiết lập khổ giấy
		com.itextpdf.text.Rectangle pageSize = new com.itextpdf.text.Rectangle(200f, 1000f); // 80mm x 200mm
		Document document = new Document(pageSize, 5f, 5f, 5f, 5f); // margins: left, right, top, bottom
		try {
			PdfWriter.getInstance(document, out);
			document.open();

			// Load font hỗ trợ tiếng Việt
			String fontPath = "times.ttf"; // Đường dẫn tương đối đến file font
			BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font font = new Font(bf, 12);

			// Tạo header
			Paragraph header = new Paragraph("Phiếu báo bếp", font);

			header.setAlignment(Element.ALIGN_CENTER);

			document.add(header);

			Paragraph tableLine = new Paragraph("Bàn: " + tableEntity.getName(), font);
			Paragraph orderLine = new Paragraph("Mã order: " + orderId, font);

			orderLine.setAlignment(Element.ALIGN_CENTER);
			tableLine.setAlignment(Element.ALIGN_CENTER);

			document.add(tableLine);
			document.add(orderLine);

			// Tạo bảng
			PdfPTable table = new PdfPTable(3); // 3 cột: STT, Tên món, Số lượng
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			// Thiet lap kich thuoc cột
			float[] columnWidths = { 1f, 4f, 1f }; // Tỷ lệ: STT - 1 phần, Tên món - 4 phần, Số lượng - 1 phần
			table.setWidths(columnWidths);

			// Thêm tiêu đề cột
			addTableHeader(table, font);

			// Thêm dữ liệu vào bảng
			int i = 1;
			for (OrderDetailDTO item : OrderDetailDTOs) {
				addRows(i, table, item, font);
				i++;

			}
			document.add(table);

			document.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

		byte[] pdfBytes = out.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);

		// Thay đổi phần headers.setContentDispositionFormData
		headers.setContentDisposition(ContentDisposition.builder("attachment")
				.filename("order " + orderId + ".pdf", StandardCharsets.UTF_8).build());

		return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}

	private void addTableHeader(PdfPTable table, Font font) {
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("STT", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Tên món", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("SL", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
	}

	private void addRows(int stt, PdfPTable table, OrderDetailDTO item, Font font) {
		String fontPath = "timesi.ttf"; // Đường dẫn tương đối đến file font
		BaseFont bfi = null;
		try {
			bfi = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Font fonti = new Font(bfi, 11);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase(String.valueOf(stt), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		if (item.getNote() != null) {
			cell = new PdfPCell();
			cell.addElement(new Phrase(item.getItemName(), font));
			cell.addElement(new Phrase(item.getNote(), fonti));
			cell.setVerticalAlignment(Element.ALIGN_TOP);
		} else {
			cell = new PdfPCell(new Phrase(item.getItemName(), font));
		}
		// cell.setFixedHeight(20f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// cell.setFixedHeight(20f);
		table.addCell(cell);
	}

}
