package com.example.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.dto.OrderDetailDTO;
import com.example.entity.Bill;
import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.service.BillService;
import com.example.service.CategoryService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.impl.TableServiceImpl;
import com.example.entity.Table;	
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
import com.example.entity.Table;
import com.example.util.TimeManage;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	TableServiceImpl tableService;

	@Autowired
	OrderService orderService;

	@Autowired
	BillService billService;
	List<OrderDetailDTO> OrderDetailDTOs = new ArrayList<>();

	@GetMapping("/{tableId}")
	public String getPayment(@PathVariable Integer tableId, Model model) {
		Order order = orderService.getLatestOrderByTableId(tableId);
		if (order == null) {
			return "Đơn hàng không tồn tại";
		}
		Integer orderId = order.getOrder_id();
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);

		OrderDetailDTOs.clear();
		Table thisTable = tableService.getById(tableId);

		for (OrderDetail item : orderDetails) {
			OrderDetailDTOs.add(
					new OrderDetailDTO(item.getOrder_detail_id(), item.getFood().getFoodId(), item.getFood_number(),
							item.getFood().getFoodName(), item.getFood().getFoodPrice(), item.getOrder_foodnotes()));
		}

		System.out.println(OrderDetailDTOs.size());

		model.addAttribute("OrderDetails", OrderDetailDTOs);
		model.addAttribute("orderId", orderId);
		model.addAttribute("tableId", tableId);
		return "payment";
	}

	@PostMapping("{tableId}/print")
	public ResponseEntity<byte[]> printOrder(@PathVariable Integer tableId) {
		Order orderEntity = orderService.getLatestOrderByTableId(tableId);
		if (orderEntity == null) {
			return ResponseEntity.ok(("Không tìm thấy đơn hàng").getBytes(StandardCharsets.UTF_8));
		}
		Integer orderId = orderEntity.getOrder_id();
		Table tableEntity = tableService.getById(tableId);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(orderId);

		OrderDetailDTOs.clear();

		Table thisTable = tableService.getById(tableId);

		for (OrderDetail item : orderDetails) {
			boolean exist = false;
			for (OrderDetailDTO dto : OrderDetailDTOs) {
				if (dto.getFoodId().equals(item.getFood().getFoodId())) {
					dto.setQuantity(dto.getQuantity() + item.getFood_number());
					exist = true;
					break;
				}
			}
			if (!exist) {
				OrderDetailDTOs.add(new OrderDetailDTO(item.getOrder_detail_id(), item.getFood().getFoodId(),
						item.getFood_number(), item.getFood().getFoodName(), item.getFood().getFoodPrice(),
						item.getOrder_foodnotes()));
			}
		}
		System.out.println(OrderDetailDTOs.size());

		// Thiết lập khổ giấy
		com.itextpdf.text.Rectangle pageSize = new com.itextpdf.text.Rectangle(200f, 1000f); // 80mm x 200mm
		Document document = new Document(pageSize, 5f, 5f, 5f, 5f); // margins: left, right, top, bottom
		byte[] pdfBytes;
		Float total = 0f;
		try {
			PdfWriter.getInstance(document, out);
			document.open();

			// Load font hỗ trợ tiếng Việt
			String fontPath = "times.ttf"; // Đường dẫn tương đối đến file font
			BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font font = new Font(bf, 10, Font.NORMAL);

			// Tạo header
			Paragraph header = new Paragraph("Hóa đơn", new Font(bf, 16, Font.BOLD));

			header.setAlignment(Element.ALIGN_CENTER);

			document.add(header);

			Paragraph tableLine = new Paragraph("Bàn: " + tableEntity.getName(), font);
			Paragraph orderLine = new Paragraph("Mã order: " + orderId, font);
			TimeManage time = new TimeManage();
			Paragraph TimeLine = new Paragraph("Thời gian: " + time.getCurrentDateTime(), font);

			orderLine.setAlignment(Element.ALIGN_CENTER);
			tableLine.setAlignment(Element.ALIGN_CENTER);

			document.add(tableLine);
			document.add(orderLine);
			document.add(TimeLine);

			// Tạo bảng
			PdfPTable table = new PdfPTable(4); // 3 cột: STT, Tên món, Số lượng, Đơn giá
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			// Thiet lap kich thuoc cột
			float[] columnWidths = { 1f, 3f, 1f, 2f }; // Tỷ lệ: STT - 1 phần, Tên món - 4 phần, Số lượng - 1 phần
			table.setWidths(columnWidths);

			// Thêm tiêu đề cột
			addTableHeader(table, new Font(bf, 12, Font.BOLD));

			// Thêm dữ liệu vào bảng
			int i = 1;
			for (OrderDetailDTO item : OrderDetailDTOs) {
				total += item.getQuantity() * item.getPrice();
				addRows(i, table, item, font);
				i++;
			}
			document.add(table);

			Paragraph TotalLine = new Paragraph("Tổng cộng: " + total.toString() + " VND", font);

			TotalLine.setAlignment(Element.ALIGN_RIGHT);

			document.add(TotalLine);

			document.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		pdfBytes = out.toByteArray();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);

		// Thay đổi phần headers.setContentDispositionFormData
		headers.setContentDisposition(ContentDisposition.builder("attachment")
				.filename("Bill " + tableId + ".pdf", StandardCharsets.UTF_8).build());

		Bill bill = orderEntity.getBill();
		bill.setBill_total(total.toString());

		billService.saveBill(bill);

		return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}

	@PostMapping("{tableId}/success")
	public ResponseEntity<byte[]> payment(@PathVariable Integer tableId) {
		Order orderEntity = orderService.getLatestOrderByTableId(tableId);
		if (orderEntity == null) {
			return ResponseEntity.ok(("Không tìm thấy đơn hàng").getBytes(StandardCharsets.UTF_8));
		}

		Bill bill = orderEntity.getBill();
		bill.setBill_status("paid");
		TimeManage time = new TimeManage();
		bill.setBill_end_time(time.getCurrentDateTime().toString());
		
		Table table = tableService.getById(tableId);
		table.setStatus("available");
		tableService.update(table);

		billService.saveBill(bill);
		return ResponseEntity.ok(("Success").getBytes(StandardCharsets.UTF_8));
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

		cell = new PdfPCell(new Phrase("Đơn giá", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
	}

	private void addRows(int stt, PdfPTable table, OrderDetailDTO item, Font font) {
		PdfPCell cell;
		cell = new PdfPCell(new Phrase(String.valueOf(stt), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		cell = new PdfPCell();
		cell.addElement(new Phrase(item.getItemName(), font));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// cell.setFixedHeight(20f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// cell.setFixedHeight(20f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(String.valueOf(item.getPrice()), font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// cell.setFixedHeight(20f);
		table.addCell(cell);
	}
}
