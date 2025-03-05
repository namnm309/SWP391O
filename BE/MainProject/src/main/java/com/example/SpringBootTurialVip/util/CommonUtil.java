package com.example.SpringBootTurialVip.util;


import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

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
	
	String msg=null;;
	
	public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception
	{
		
		msg="<p>Hello [[name]],</p>"
				+ "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
				+ "<p><b>Product Details:</b></p>"
				+ "<p>Name : [[productName]]</p>"
				+ "<p>Category : [[category]]</p>"
				+ "<p>Quantity : [[quantity]]</p>"
				+ "<p>Price : [[price]]</p>"
				+ "<p>Payment Type : [[paymentType]]</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("namnm309@gmail.com", "Vaccine Cart");
		helper.setTo(order.getOrderDetail().getEmail());


		// ✅ Cập nhật cách lấy category name
		String categoryName = order.getProduct().getCategory() != null ? order.getProduct().getCategory().getName() : "Unknown";

		msg=msg.replace("[[name]]",order.getOrderDetail().getFirstName());
		msg=msg.replace("[[orderStatus]]",status);
		msg=msg.replace("[[productName]]", order.getProduct().getTitle());
		msg = msg.replace("[[category]]", categoryName);  // ✅ Lấy `name` của `Category`
		msg=msg.replace("[[quantity]]", order.getQuantity().toString());
		msg=msg.replace("[[price]]", order.getPrice().toString());
		msg=msg.replace("[[paymentType]]", order.getPaymentType());
		
		helper.setSubject("Product Order Status");
		helper.setText(msg, true);
		mailSender.send(message);
		return true;
	}
	
	public User getLoggedInUserDetails() {

		var context = SecurityContextHolder.getContext();
		//Tên user đang log in
		String name=context.getAuthentication().getName();

		User user =userRepository.findByUsername(name).orElseThrow(
				() -> new AppException((ErrorCode.USER_NOT_EXISTED)));
		return user;
	}
	

}
