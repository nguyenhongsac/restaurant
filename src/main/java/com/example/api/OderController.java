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
import com.example.entity.Table;
import com.example.service.BillService;
import com.example.service.CategoryService;
import com.example.service.FoodService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.TableService;
import com.example.service.impl.CategoryServiceImpl;
import com.example.service.impl.FoodServiceImpl;
import com.example.service.impl.TableServiceImpl;
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
	@Autowired
	private TableServiceImpl tableService;

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

		// Table
		Table thisTable = new Table();
		Order order = orderService.getOrderByOrder(orderId);
		List<Table> tables = new ArrayList<>();
		tableService.getByStatus("available").forEach(item -> {
			if (item.getId() != order.getTable_id()) {
				tables.add(item);
			} else if (item.getId() == order.getTable_id()) {
				thisTable.setName(item.getName());
				thisTable.setId(item.getId());
			}
		});

		model.addAttribute("thisTable", thisTable);

		List<FoodEntity> foods = foodService.getAll();
		model.addAttribute("orderId", orderId);
		model.addAttribute("OrderDetailDTOs", OrderDetailDTOs);
		model.addAttribute("foods", foods);
		model.addAttribute("categories", items);
		model.addAttribute("tables", tables);
		return "order";
	}

	@PostMapping("/{orderId}/save")
	public String saveOrder(@PathVariable Integer orderId, @RequestBody List<OrderDetailDTO> orders) {

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
		return "redirect:/order/" + orderId;
	}

	@PostMapping("/{orderId}/changeTable/{tableId}")
	public String addOrder(@PathVariable Integer orderId, @PathVariable Integer tableId) {
		Order orderEntity = orderService.getOrderByOrder(orderId);
		orderEntity.setTable_id(tableId);
		orderService.updateOrder(orderEntity);
		return "redirect:/order/" + orderId;
	}

//	@PostMapping("/{orderId}/print-order")
//	public ResponseEntity<byte[]> printOrder(@PathVariable Integer orderId) {
//		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);
//		OrderDetailDTOs.clear();
//		int i = 0;
//		for (OrderDetail item : orderDetails) {
//			OrderDetailDTOs.add(new OrderDetailDTO(i, item.getFood().getFoodId(), item.getFood_number(),
//					item.getFood().getFoodName(), item.getFood().getFoodPrice(), item.getOrder_note()));
//			i++;
//		}
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		
//		// Load font hỗ trợ tiếng Việt
//        BaseFont bf = null;
//		try {
//			bf = BaseFont.createFont("times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf, 12);
//
//        Document document = new Document();
//        try {
//            PdfWriter.getInstance(document, out);
//            document.open();
//            document.add(new Paragraph("Hóa đơn của bạn", font));
//            document.add(new Paragraph(" "));
//
//            for (OrderDetailDTO item : OrderDetailDTOs) {
//                document.add(new Paragraph(item.getItemName() + " - " + item.getQuantity(), font));
//            }
//            document.close();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//
//        byte[] pdfBytes = out.toByteArray();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "order.pdf");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(pdfBytes);
//	}

	@PostMapping("{orderId}/print-order")
	public ResponseEntity<byte[]> printOrder(@PathVariable Integer orderId) {
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

			// Thiết lập alignment cho header
			header.setAlignment(Element.ALIGN_CENTER);

			// Thêm header vào tài liệu
			document.add(header);
			
			Paragraph order = new Paragraph("Mã order: " + orderId, font);

			order.setAlignment(Element.ALIGN_CENTER);

			document.add(order);

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
		// headers.setContentDispositionFormData("attachment", "order" + orderId +
		// ".pdf");

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
		PdfPCell cell;
		cell = new PdfPCell(new Phrase(String.valueOf(stt), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(20f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(item.getItemName(), font));
		cell.setFixedHeight(20f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(20f);
		table.addCell(cell);
	}

}
