package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.config.VNPayConfig;
import com.example.SpringBootTurialVip.dto.request.TransactionResponse;
import com.example.SpringBootTurialVip.dto.request.VNPayResponse;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/payment")
public class VNPayController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductOrderRepository productOrderRepository;
    @Autowired
    private UserRepository userRepository;

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
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
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

    /**
     * API xử lý thanh toán và lưu vào database
     */
    @GetMapping("/payment-info")
    public ResponseEntity<?> transaction() {
        // Lấy giao dịch gần nhất từ database (hoặc theo logic của bạn)
        Optional<ProductOrder> productOrderOpt = productOrderRepository.findTopByOrderByOrderDateDesc();

        if (productOrderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Không tìm thấy giao dịch\"}");
        }

        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Successful\"}");
    }
}
