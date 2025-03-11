package com.example.SpringBootTurialVip.service.serviceimpl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VNPayService {

    public Long extractUserIdFromOrderInfo(String orderInfo) {
        try {
            String[] parts = orderInfo.split("\\|");
            for (String part : parts) {
                if (part.startsWith("userId=")) {
                    return Long.parseLong(part.split("=")[1]);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Long extractProductIdFromOrderInfo(String orderInfo) {
        try {
            String[] parts = orderInfo.split("\\|");
            for (String part : parts) {
                if (part.startsWith("productId=")) {
                    return Long.parseLong(part.split("=")[1]);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String extractProductIdsFromOrderInfo(String vnpOrderInfo) {
        if (vnpOrderInfo == null || vnpOrderInfo.isEmpty()) {
            return "";
        }

        // Tách thông tin đơn hàng theo ký tự "|"
        String[] parts = vnpOrderInfo.split("\\|");
        List<String> productIds = new ArrayList<>();

        for (String part : parts) {
            if (part.startsWith("productId=")) {
                // Tách productId từ chuỗi (loại bỏ phần "_qty=" phía sau)
                String productId = part.split("_")[0].replace("productId=", "").trim();
                productIds.add(productId);
            }
        }

        // Nối danh sách productId lại thành chuỗi, phân tách bằng dấu phẩy
        return String.join(",", productIds);
    }
    public Long extractSingleProductIdFromOrderInfo(String orderInfo) {
        try {
            Pattern pattern = Pattern.compile("productId=(\\d+)");
            Matcher matcher = pattern.matcher(orderInfo);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(1));
            }
        } catch (Exception e) {
            System.out.println(" Lỗi khi trích xuất productId từ vnp_OrderInfo: " + e.getMessage());
        }
        return null;
    }


}
