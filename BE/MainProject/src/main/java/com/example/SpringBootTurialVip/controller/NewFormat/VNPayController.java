package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.config.VNPayConfig;
import com.example.SpringBootTurialVip.dto.request.VNPayResponse;
import com.example.SpringBootTurialVip.entity.Cart;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.CartRepository;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.VNPayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.example.SpringBootTurialVip.config.VNPayConfig.vnp_Command;
import static com.example.SpringBootTurialVip.config.VNPayConfig.vnp_Version;

@RestController
@Transactional
@RequestMapping("/payment")
@Tag(name="[VNPAY]",description = "")
public class VNPayController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductOrderRepository productOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private CartRepository cartRepository;

    /**
     * API tạo giao dịch thanh toán VNPay (Frontend gọi API này để nhận URL thanh toán)
     */
    @PostMapping("create-payment")
    public ResponseEntity<?> createPayment(
            @RequestParam("userId") Long userId,
            @RequestParam("productId") Long productId,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
        // 🛑 Kiểm tra người dùng và sản phẩm
        User user = userRepository.findById(userId).orElse(null);
        Product product = productService.getProductById(productId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Người dùng không tồn tại!"));
        }
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Sản phẩm không tồn tại!"));
        }

        // 💰 Lấy giá sản phẩm
        long amount = (long) (product.getPrice() * 100);
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        // 🔥 Tạo tham số gửi VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

        String vnp_OrderInfo = "Thanh toan don hang:" + vnp_TxnRef + "|userId=" + userId + "|productId=" + productId;
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // 🕒 Tạo thời gian giao dịch
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // 🔑 Mã hóa dữ liệu
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        // 🔥 Trả về link thanh toán
        VNPayResponse vnPayResponse = new VNPayResponse();
        vnPayResponse.setStatus("OK");
        vnPayResponse.setMessage("Successfully");
        vnPayResponse.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);
    }



    //    /**
//     * API xử lý thanh toán và lưu vào database
//     */
//    @GetMapping("/payment-info")
//    public ResponseEntity<?> transaction() {
//        // Lấy giao dịch gần nhất từ database (hoặc theo logic của bạn)
//        Optional<ProductOrder> productOrderOpt = productOrderRepository.findTopByOrderByOrderDateDesc();
//
//        if (productOrderOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Không tìm thấy giao dịch\"}");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Successful\"}");
//    @GetMapping("/payment-info")
//    public ResponseEntity<?> transaction(HttpServletRequest request) {
//        // 🛑 Lấy thông tin từ query parameters do VNPay gửi về
//        String vnp_Amount = request.getParameter("vnp_Amount");
//        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
//        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
//        String vnp_PaymentType = "VNPay";
//        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
//
//        // 📝 Ghi log để kiểm tra phản hồi từ VNPay
//        System.out.println("VNPay Response Code: " + vnp_ResponseCode);
//
//        // 📝 Ghi log kiểm tra `vnp_OrderInfo`
//        System.out.println("🔥 VNPay Redirect Received!");
//        System.out.println("📝 Received vnp_OrderInfo: " + vnp_OrderInfo);
//        if (vnp_OrderInfo == null || vnp_OrderInfo.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Không có dữ liệu vnp_OrderInfo từ VNPay!"));
//        }
//
//        // 🔥 Kiểm tra giao dịch thành công
//        if (!"00".equals(vnp_ResponseCode)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Giao dịch thất bại!"));
//        }
//
//        // 🛑 Sử dụng VNPayService để lấy userId và productId từ vnp_OrderInfo
//        Long userId = vnPayService.extractUserIdFromOrderInfo(vnp_OrderInfo);
//        Long productId = vnPayService.extractProductIdFromOrderInfo(vnp_OrderInfo);
//
//        if (userId == null || productId == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Thiếu thông tin người dùng hoặc sản phẩm!"));
//        }
//
//        // 🛑 Kiểm tra người dùng và sản phẩm
//        User user = userRepository.findById(userId).orElse(null);
//        Product product = productService.getProductById(productId);
//
//        if (user == null || product == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Người dùng hoặc sản phẩm không tồn tại!"));
//        }
//
////    // 💰 Lưu thông tin thanh toán vào bảng ProductOrder
////    ProductOrder newOrder = new ProductOrder();
////    newOrder.setOrderId(vnp_TxnRef);
////    newOrder.setOrderDate(LocalDate.now());
////    newOrder.setPaymentType(vnp_PaymentType);
////    newOrder.setPrice(Double.parseDouble(vnp_Amount) / 100); // Chuyển từ VNĐ
////    newOrder.setQuantity(1);
////    newOrder.setStatus("PAID");
////    newOrder.setUser(user);
////    newOrder.setProduct(product);
////
////    productOrderRepository.save(newOrder);
////
////    return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Thanh toán thành công!", "orderId", vnp_TxnRef));
////}
////}
//        // 💰 Lưu thông tin thanh toán vào bảng ProductOrder (cả giao dịch thành công & thất bại)
//        ProductOrder newOrder = new ProductOrder();
//        newOrder.setOrderId(vnp_TxnRef);
//        newOrder.setOrderDate(LocalDate.now());
//        newOrder.setPaymentType(vnp_PaymentType);
//        newOrder.setPrice(Double.parseDouble(vnp_Amount) / 100);
//        newOrder.setQuantity(1);
//
//
//        // 🔥 Nếu giao dịch thành công (vnp_ResponseCode == "00"), trạng thái = "PAID"
//        // ❌ Nếu thất bại, đặt trạng thái là "FAILED"
//        if ("00".equals(vnp_ResponseCode)) {
//            newOrder.setStatus("PAID");
//        } else {
//            newOrder.setStatus("FAILED");
//        }
//
//        newOrder.setUser(user);
//        newOrder.setProduct(product);
//
//        productOrderRepository.save(newOrder); // Lưu vào database
//
//        // 📌 Trả về phản hồi phù hợp với trạng thái giao dịch
//        if ("00".equals(vnp_ResponseCode)) {
//            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Thanh toán thành công!", "orderId", vnp_TxnRef));
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Giao dịch thất bại!", "orderId", vnp_TxnRef, "status", "FAILED"));
//        }
//    }
//

//=========================================================================================================================
@GetMapping("/payment-info")
public ResponseEntity<?> processPayment(
        @RequestParam(value = "userId", required = false) Long userId,
        @RequestParam("vnp_TxnRef") String txnRef, // ✅ Cập nhật đúng tên tham số từ VNPay
        @RequestParam(value = "productIds", required = false) String productIds,
        @RequestParam("vnp_Amount") Long amount,
        @RequestParam("vnp_ResponseCode") String responseCode,
        @RequestParam("vnp_OrderInfo") String vnpOrderInfo
) {
    System.out.println("🔥 VNPay Redirect Received!");
    System.out.println("📝 Debug Received txnRef: " + txnRef);
    System.out.println("📝 Debug Received userId: " + userId);
    System.out.println("📝 Debug Received productIds: " + productIds);
    System.out.println("📝 Debug vnp_Amount: " + amount);
    System.out.println("📝 Debug vnp_ResponseCode: " + responseCode);
    System.out.println("📝 Debug vnp_OrderInfo: " + vnpOrderInfo);

    if (!"00".equals(responseCode)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Giao dịch thất bại!"));
    }

    // Nếu userId không được truyền trực tiếp, trích xuất từ vnp_OrderInfo
    if (userId == null) {
        userId = vnPayService.extractUserIdFromOrderInfo(vnpOrderInfo);
    }

    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Người dùng không tồn tại!"));
    }

    // Nếu productIds không được truyền trực tiếp, trích xuất từ vnp_OrderInfo
    if (productIds == null || productIds.isEmpty()) {
        productIds = vnPayService.extractProductIdsFromOrderInfo(vnpOrderInfo);
    }
   // 🔥 Nếu `productIds` vẫn rỗng, thử lấy `productId` duy nhất từ `vnp_OrderInfo`
    if (productIds == null || productIds.isEmpty()) {
        Long singleProductId = vnPayService.extractSingleProductIdFromOrderInfo(vnpOrderInfo);
        if (singleProductId != null) {
            productIds = String.valueOf(singleProductId);
        }
    }

    if (productIds == null || productIds.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Thiếu thông tin sản phẩm!"));
    }

//    //  Tạo OrderDetail trước khi tạo ProductOrder để tránh lỗi NULL
//    OrderDetail orderDetail = new OrderDetail();
//    orderDetail.setFirstName(user.getFullname());
//    orderDetail.setEmail(user.getEmail());
//    orderDetail.setMobileNo(user.getPhone());
//    orderDetailRepository.save(orderDetail);

    String[] productIdArray = productIds.replaceAll(",$", "").split(",");
    for (String productIdStr : productIdArray) {
        if (productIdStr.trim().isEmpty()) continue;
        Long productId = Long.parseLong(productIdStr.trim());
        System.out.println("🔹 Xử lý productId: " + productId);
        Product product = productService.getProductById(productId);

        if (product != null) {
            ProductOrder order = new ProductOrder();
            order.setOrderId(txnRef);
            order.setOrderDate(LocalDate.now());
            order.setPaymentType("VNPay");
            order.setPrice((double) amount / 100);
            order.setQuantity(1);
            order.setStatus("PAID");
            order.setUser(user);
            order.setProduct(product);
            productOrderRepository.save(order);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Sản phẩm ID " + productId + " không tồn tại!"));
        }
    }

    cartRepository.deleteByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Thanh toán thành công!"));
}



//=========================================================================================================================


    //    @PostMapping("/cart/create-payment")
//    public ResponseEntity<?> createCartPayment(
//            @RequestParam("userId") Long userId,
//            HttpServletRequest request
//    ) throws UnsupportedEncodingException {
//        //  Kiểm tra người dùng
//        User user = userRepository.findById(userId).orElse(null);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Người dùng không tồn tại!"));
//        }
//
//        // 🛒 Lấy tất cả sản phẩm trong giỏ hàng của người dùng
//        List<Cart> cartItems = cartRepository.findByUserId(userId);
//        if (cartItems.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Giỏ hàng trống!"));
//        }
//
//        //  Tính tổng tiền đơn hàng
//        long totalAmount = 0;
//        StringBuilder orderInfoBuilder = new StringBuilder("Thanh toán giỏ hàng:");
//        for (Cart item : cartItems) {
//            totalAmount += (long) (item.getProduct().getPrice() * item.getQuantity() * 100);
//            orderInfoBuilder.append("|productId=").append(item.getProduct().getId())
//                    .append("_qty=").append(item.getQuantity());
//        }
//
//        // Tạo mã giao dịch
//        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
//        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
//        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
//
//        //  Tạo tham số gửi VNPay
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_BankCode", "NCB");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        String vnp_OrderInfo = "Thanh toan don hang:" + vnp_TxnRef + "|userId=" + userId + "|productId=" + productId;
//        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_OrderType", "other");
//        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        //  Tạo thời gian giao dịch
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        //  Mã hóa dữ liệu
//        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        for (String fieldName : fieldNames) {
//            String fieldValue = vnp_Params.get(fieldName);
//            if (fieldValue != null && !fieldValue.isEmpty()) {
//                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
//                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//
//        String queryUrl = query.toString();
//        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
//
//        //  Trả về link thanh toán
//        VNPayResponse vnPayResponse = new VNPayResponse();
//        vnPayResponse.setStatus("OK");
//        vnPayResponse.setMessage("Successfully");
//        vnPayResponse.setURL(paymentUrl);
//
//        return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);
//    }
//}
    @PostMapping("/cart/create-payment")
    public ResponseEntity<?> createCartPayment(
            @RequestParam("userId") Long userId,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
        //  Kiểm tra người dùng
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Người dùng không tồn tại!"));
        }

        //  Lấy tất cả sản phẩm trong giỏ hàng của người dùng
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Giỏ hàng trống!"));
        }

        //  Tính tổng tiền đơn hàng
        long totalAmount = 0;
        StringBuilder orderInfoBuilder = new StringBuilder("Thanh toán giỏ hàng:");
        StringBuilder productIdList = new StringBuilder();
        for (Cart item : cartItems) {
            totalAmount += (long) (item.getProduct().getPrice() * item.getQuantity() * 100);
            orderInfoBuilder.append("|productId=").append(item.getProduct().getId())
                    .append("_qty=").append(item.getQuantity());
            productIdList.append(item.getProduct().getId()).append(",");
        }

        //  Tạo mã giao dịch
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        // Tạo tham số gửi VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfoBuilder.toString() + "|userId=" + userId);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", "other");

        //  Chỉnh sửa ReturnUrl để truyền userId và productId vào
        String returnUrl = VNPayConfig.vnp_ReturnUrl + "?userId=" + userId + "&txnRef=" + vnp_TxnRef + "&productIds=" + productIdList.toString();
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        //  Tạo thời gian giao dịch
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        //  Mã hóa dữ liệu
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        //  Trả về link thanh toán
        VNPayResponse vnPayResponse = new VNPayResponse();
        vnPayResponse.setStatus("OK");
        vnPayResponse.setMessage("Successfully");
        vnPayResponse.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);
    }
}
