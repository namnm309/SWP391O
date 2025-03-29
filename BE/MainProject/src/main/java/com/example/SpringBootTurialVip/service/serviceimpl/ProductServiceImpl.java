package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.VaccineOrderStats;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Product addProduct(Product product,List<MultipartFile> images) throws IOException {
        if (productRepository.existsByTitle(product.getTitle())) {
            throw new RuntimeException("Product already exists");
        }

        product.setTitle(product.getTitle());
        product.setCategory(product.getCategory());
        product.setPrice(product.getPrice());
        product.setQuantity(product.getQuantity());
        product.setDescription(product.getDescription());
        product.setDiscount(product.getDiscount());
        product.setDiscountPrice(product.getDiscountPrice());
        product.setIsActive(product.getIsActive());
        product.setManufacturer(product.getManufacturer());
        product.setTargetGroup(product.getTargetGroup());
        product.setSchedule(product.getSchedule());
        product.setSideEffects(product.getSideEffects());
        product.setAvailable(product.isAvailable());
        product.setIsPriority(product.getIsPriority());
        product.setNumberOfDoses(product.getNumberOfDoses());
        product.setMinAgeMonths(product.getMinAgeMonths());
        product.setMaxAgeMonths(product.getMaxAgeMonths());
        product.setMinDaysBetweenDoses(product.getMinDaysBetweenDoses());
        product.updateTargetGroupFromAge();

//        if (product.getImage() != null && !product.getImage().isEmpty()) {
//            product.setImage(product.getImage()); // Lưu URL ảnh vào User
//            log.info(String.valueOf(product.getImage()));
//        }
        // Lưu danh sách ảnh nếu có
        if (images != null && !images.isEmpty()) {
            try {
                List<String> imageUrls = images.stream()
                        .map(image -> {
                            try {
                                return fileStorageService.uploadFile(image);
                            } catch (IOException e) {
                                throw new RuntimeException("Lỗi khi upload ảnh");
                            }
                        })
                        .collect(Collectors.toList());

                product.setImageList(imageUrls);
            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(product)) {
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public List<Product> getProductByTitle(String title) {
        return productRepository.findByTitle(title);
    }

    @Override
    public Product updateProduct(Product product,List<MultipartFile> images) {
        // Lấy sản phẩm từ DB
        Product dbProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + product.getId()));

        // Cập nhật thông tin sản phẩm
        dbProduct.setTitle(product.getTitle());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setQuantity(product.getQuantity());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setDiscount(product.getDiscount());
        dbProduct.setDiscountPrice(product.getDiscountPrice());
        dbProduct.setIsActive(product.getIsActive());
        dbProduct.setManufacturer(product.getManufacturer());
        dbProduct.setTargetGroup(product.getTargetGroup());
        dbProduct.setSchedule(product.getSchedule());
        dbProduct.setSideEffects(product.getSideEffects());
        dbProduct.setAvailable(product.isAvailable());
        dbProduct.setUpdatedAt(LocalDateTime.now());
        dbProduct.setIsPriority(product.getIsPriority());
        dbProduct.setNumberOfDoses(product.getNumberOfDoses());
        dbProduct.setMinAgeMonths(product.getMinAgeMonths());
        dbProduct.setMaxAgeMonths(product.getMaxAgeMonths());
        dbProduct.setMinDaysBetweenDoses(product.getMinDaysBetweenDoses());
        dbProduct.updateTargetGroupFromAge();
        // Kiểm tra giảm giá hợp lệ
        if (dbProduct.getDiscount() < 0 || dbProduct.getDiscount() > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        // Tính toán giá sau khi giảm
        Double discountAmount = dbProduct.getPrice() * (dbProduct.getDiscount() / 100.0);
        dbProduct.setDiscountPrice(dbProduct.getPrice() - discountAmount);

//
        // Cập nhật danh sách ảnh nếu có ảnh mới
        if (images != null && !images.isEmpty()) {
            try {
                List<String> imageUrls = images.stream()
                        .map(image -> {
                            try {
                                return fileStorageService.uploadFile(image);
                            } catch (IOException e) {
                                throw new RuntimeException("Lỗi khi upload ảnh");
                            }
                        })
                        .collect(Collectors.toList());

                dbProduct.setImageList(imageUrls);
            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }


        return productRepository.save(dbProduct);
    }

    @Override
    public List<Long> findInvalidProductIds(List<Long> productIds) {
        List<Long> existingProductIds = productRepository.findAllById(productIds)
                .stream().map(Product::getId).toList();

        return productIds.stream()
                .filter(id -> !existingProductIds.contains(id))
                .collect(Collectors.toList());
    }

    @Override
    // Kiểm tra xem có sản phẩm nào bị thiếu hàng không
    public List<Long> findOutOfStockProducts(List<Long> productIds, List<Integer> quantities) {
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo map chứa productId -> stock từ database
        Map<Long, Integer> stockMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getQuantity));

        // Lọc ra danh sách productId nào có stock < quantity yêu cầu
        return productIds.stream()
                .filter(id -> stockMap.getOrDefault(id, 0) < quantities.get(productIds.indexOf(id)))
                .collect(Collectors.toList());
    }




}
