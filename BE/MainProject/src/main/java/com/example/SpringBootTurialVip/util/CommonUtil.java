package com.example.SpringBootTurialVip.util;


import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.OrderDetailRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	public Boolean sendMail(String code, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {


		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("namnm309@gmail.com", "Shooping Cart");
		helper.setTo(reciepentEmail);

		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password.</p>"
				+ "<p>Your security code to change password is: <strong>" + code + "</strong></p>";

		helper.setSubject("Password Reset");
		helper.setText(content, true);
		mailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {

		// http://localhost:8080/forgot-password
		String siteUrl = request.getRequestURL().toString();

		return siteUrl.replace(request.getServletPath(), "");
	}

	String msg = null;
	;

	//	public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception
//	{
//
//		msg="<p>Hello [[name]],</p>"
//				+ "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
//				+ "<p><b>Product Details:</b></p>"
//				+ "<p>Name : [[productName]]</p>"
//				+ "<p>Category : [[category]]</p>"
//				+ "<p>Quantity : [[quantity]]</p>"
//				+ "<p>Price : [[price]]</p>"
//				+ "<p>Payment Type : [[paymentType]]</p>";
//
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//
//		helper.setFrom("namnm309@gmail.com", "Vaccine Cart");
//		helper.setTo(order.getOrderDetail().getEmail());
//
//
//		// ✅ Cập nhật cách lấy category name
//		String categoryName = order.getOrderDetail().getProduct().getCategory() != null ? order.getOrderDetail().getProduct().getCategory().getName() : "Unknown";
//
//		msg=msg.replace("[[name]]",order.getOrderDetail().getFirstName());
//		msg=msg.replace("[[orderStatus]]",status);
//		msg=msg.replace("[[productName]]", order.getOrderDetail().getProduct().getTitle());
//		msg = msg.replace("[[category]]", categoryName);  // ✅ Lấy `name` của `Category`
//		msg=msg.replace("[[quantity]]", order.getOrderDetail().getQuantity().toString());
//		msg=msg.replace("[[price]]", order.getTotalPrice().toString());
//		msg=msg.replace("[[paymentType]]", order.getPaymentType());
//
//		helper.setSubject("Product Order Status");
//		helper.setText(msg, true);
//		mailSender.send(message);
//		return true;
//	}
	public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {
		StringBuilder msg = new StringBuilder();

		msg.append("<p>Hello [[name]],</p>")
				.append("<p>Thank you for your order. Your order status is: <b>[[orderStatus]]</b>.</p>")
				.append("<p><b>Product Details:</b></p>");

		for (OrderDetail orderDetail : orderDetailRepository.findByOrderId(order.getOrderId())) {
			String categoryName = orderDetail.getProduct().getCategory() != null
					? orderDetail.getProduct().getCategory().getName()
					: "Unknown";

			msg.append("<hr>")
					.append("<p>Name : [[productName]]</p>")
					.append("<p>Category : [[category]]</p>")
					.append("<p>Quantity : [[quantity]]</p>")
					.append("<p>Price : [[price]]</p>");

			// Thay thế giá trị cho từng sản phẩm
			msg = new StringBuilder(msg.toString()
					.replace("[[productName]]", orderDetail.getProduct().getTitle())
					.replace("[[category]]", categoryName)
					.replace("[[quantity]]", orderDetail.getQuantity().toString())
					.replace("[[price]]", orderDetail.getProduct().getDiscountPrice().toString())
			);
		}

		msg.append("<p><b>Payment Type:</b> [[paymentType]]</p>");

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("namnm309@gmail.com", "Vaccine Cart");
		helper.setTo(orderDetailRepository.findByOrderId(order.getOrderId()).get(0).getEmail());

		msg = new StringBuilder(msg.toString()
				.replace("[[name]]", orderDetailRepository.findByOrderId(order.getOrderId()).get(0).getFirstName())
				.replace("[[orderStatus]]", status)
				.replace("[[paymentType]]", order.getPaymentType())
		);

		helper.setSubject("Product Order Status");
		helper.setText(msg.toString(), true);
		mailSender.send(message);

		return true;
	}


	public User getLoggedInUserDetails() {

		var context = SecurityContextHolder.getContext();
		//Tên user đang log in
		String name = context.getAuthentication().getName();

		User user = userRepository.findByUsername(name).orElseThrow(
				() -> new AppException((ErrorCode.USER_NOT_EXISTED)));
		return user;
	}

	//Chức năng kiểm tra điều kiện y tế

	/**
	 * Hàm kiểm tra điều kiện y tế trước khi đặt vaccine cho bé
	 * Gồm: kiểm tra tuổi, số mũi đã tiêm, thời gian giữa các mũi
	 */
	public void validateProductForUser(User child, Product product, int dosesAlreadyTaken) {
		// Tính số tháng tuổi hiện tại của bé
		int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12 +
				Period.between(child.getBod(), LocalDate.now()).getMonths();

		// Kiểm tra giới hạn độ tuổi tiêm
		if (product.getMinAgeMonths() != null && ageInMonths < product.getMinAgeMonths()) {
			throw new IllegalArgumentException("Bé còn quá nhỏ để tiêm sản phẩm: " + product.getTitle());
		}
		if (product.getMaxAgeMonths() != null && ageInMonths > product.getMaxAgeMonths()) {
			throw new IllegalArgumentException("Bé đã quá lớn để tiêm sản phẩm: " + product.getTitle());
		}

		// Đếm số mũi đã đặt trong hệ thống
		int systemDoses = orderDetailRepository.countDosesTaken(product.getId(), child.getId());

		// Tổng mũi đã/đang tiêm = mũi đã tiêm ở nơi khác + mũi đã đặt trong hệ thống
		int totalDoses = dosesAlreadyTaken + systemDoses;

		if (totalDoses >= product.getNumberOfDoses()) {
			throw new IllegalArgumentException("Bé đã tiêm đủ số mũi cho sản phẩm này.");
		}

		// Lấy ngày đặt gần nhất (nếu có)
		Optional<LocalDate> lastBookingDate = orderDetailRepository.findLastBookingDate(product.getId(), child.getId());
		System.out.println(">> [DEBUG] lastBookingDate = " + lastBookingDate.orElse(null));

		// Kiểm tra khoảng cách giữa lần đặt trước và hiện tại
		if (lastBookingDate.isPresent()) {
			LocalDate lastDate = lastBookingDate.get();
			long days = ChronoUnit.DAYS.between(lastDate, LocalDate.now());
			if (days < product.getMinDaysBetweenDoses()) {
				long wait = product.getMinDaysBetweenDoses() - days;
				throw new IllegalArgumentException("Bạn vừa đặt mũi này cách đây " + days + " ngày. Vui lòng quay lại sau " + wait + " ngày.");
			}
		}
	}

	//Auto check lịch quá hạn vaccine
	@Scheduled(cron = "0 0 0 * * ?") // Chạy vào 0h mỗi ngày
	public void updateOverdueVaccinations() {
		List<OrderDetail> overdueOrders = orderDetailRepository.findOverdueVaccines(LocalDateTime.now());

		for (OrderDetail orderDetail : overdueOrders) {
			orderDetail.setStatus(OrderDetailStatus.QUA_HAN);
			orderDetailRepository.save(orderDetail);
		}
	}



}
