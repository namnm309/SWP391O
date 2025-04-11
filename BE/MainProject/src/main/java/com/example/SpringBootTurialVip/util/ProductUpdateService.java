//package com.example.SpringBootTurialVip.util;
//
//import com.example.SpringBootTurialVip.entity.Product;
//import com.example.SpringBootTurialVip.entity.ProductAgeGroup;
//import com.example.SpringBootTurialVip.entity.ProductDetails;
//import com.example.SpringBootTurialVip.enums.AgeGroup;
//import com.example.SpringBootTurialVip.repository.ProductAgeGroupRepository;
//import com.example.SpringBootTurialVip.repository.ProductRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//@Service
//public class ProductUpdateService {
//
//    private final ProductRepository productRepository;
//    private final ProductAgeGroupRepository productAgeGroupRepository;
//
//    public ProductUpdateService(ProductRepository productRepository, ProductAgeGroupRepository productAgeGroupRepository) {
//        this.productRepository = productRepository;
//        this.productAgeGroupRepository = productAgeGroupRepository;
//    }
//
//    // Phương thức này sẽ được gọi liên tục sau mỗi 10 giây
////    @Scheduled(fixedRate = 10000) // Chạy mỗi 10 giây
////    @Transactional
////    public void updateProductAgeGroups() {
////        // Lấy tất cả các sản phẩm
////        List<Product> products = productRepository.findAll();
////
////        // Duyệt qua các sản phẩm và cập nhật targetGroup
////        for (Product product : products) {
////            // Tính toán lại targetGroup cho mỗi sản phẩm
////            updateProductAgeGroupForProduct(product);
////        }
////    }
//
////    @Transactional
////    private void updateProductAgeGroupForProduct(Product product) {
////        // Xóa các ProductAgeGroup cũ liên kết với sản phẩm
////        productAgeGroupRepository.deleteByProductId(product.getId()); // Xóa tất cả ProductAgeGroup có liên kết với product_id
////
////        // Tính toán lại các ProductAgeGroup mới
////        List<ProductAgeGroup> newAgeGroups = determineAgeGroups(product.getMinAgeMonths(), product.getMaxAgeMonths(), product);
////
////        // Nếu không có nhóm tuổi nào phù hợp, có thể thêm nhóm AGE_ALL vào
////        if (newAgeGroups.isEmpty()) {
////            newAgeGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_ALL)); // Thêm nhóm tuổi phù hợp với mọi độ tuổi
////        }
////
////        // Lưu các ProductAgeGroup mới
////        productAgeGroupRepository.saveAll(newAgeGroups); // Lưu các ProductAgeGroup mới vào DB
////    }
//
//
////    // Phương thức tính toán các nhóm tuổi phù hợp với sản phẩm
////    private List<ProductAgeGroup> determineAgeGroups(int minAgeMonths, int maxAgeMonths, Product product) {
////        List<ProductAgeGroup> ageGroups = new ArrayList<>();
////
////        // Kiểm tra và thêm các nhóm tuổi vào danh sách nếu phạm vi của sản phẩm giao với nhóm tuổi
////        if (minAgeMonths <= 3 && maxAgeMonths >= 0) {
////            ageGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_0_3));
////        }
////        if (minAgeMonths <= 6 && maxAgeMonths >= 4) {
////            ageGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_4_6));
////        }
////        if (minAgeMonths <= 12 && maxAgeMonths >= 7) {
////            ageGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_7_12));
////        }
////        if (minAgeMonths <= 24 && maxAgeMonths >= 13) {
////            ageGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_13_24));
////        }
////        if (minAgeMonths >= 25) {
////            ageGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_25_PLUS));
////        }
////
////        // Trả về nhóm tuổi AGE_ALL nếu không có nhóm tuổi nào phù hợp
////        if (ageGroups.isEmpty()) {
////            ageGroups.add(new ProductAgeGroup(product, AgeGroup.AGE_ALL));
////        }
////
////        return ageGroups;
////    }
//
//
//    // Phương thức cron job để chạy mỗi 10 giây (hoặc theo tần suất bạn muốn)
//    @Scheduled(fixedRate = 10000) // Mỗi 10 giây
//    @Transactional
//    public void updateProductQuantities() {
//        // Lấy tất cả các sản phẩm
//        List<Product> products = productRepository.findAll();
//
//        // Duyệt qua các sản phẩm và gọi phương thức updateQuantities
//        for (Product product : products) {
//            // Gọi phương thức updateQuantities để tính toán lại quantity và reservedQuantity
//            product.updateQuantities();
//            // Lưu lại sản phẩm với thông tin quantity và reservedQuantity đã được cập nhật
//            productRepository.save(product);
//        }
//    }
//
//}
//
