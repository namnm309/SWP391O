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
        product.setStock(product.getStock());
        product.setDescription(product.getDescription());
        product.setDiscount(product.getDiscount());
        product.setDiscountPrice(product.getDiscountPrice());
        product.setIsActive(product.getIsActive());
        product.setManufacturer(product.getManufacturer());
        product.setTargetGroup(product.getTargetGroup());
        product.setSchedule(product.getSchedule());
        product.setSideEffects(product.getSideEffects());
        product.setAvailable(product.isAvailable());

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
        dbProduct.setStock(product.getStock());
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

        // Kiểm tra giảm giá hợp lệ
        if (dbProduct.getDiscount() < 0 || dbProduct.getDiscount() > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        // Tính toán giá sau khi giảm
        Double discountAmount = dbProduct.getPrice() * (dbProduct.getDiscount() / 100.0);
        dbProduct.setDiscountPrice(dbProduct.getPrice() - discountAmount);

//        // Nếu có ảnh mới, lưu ảnh
//        if (image != null && !image.isEmpty()) {
//            try {
//                File saveFile = new ClassPathResource("static/img").getFile();
//                File productImgDir = new File(saveFile, "product_img");
//
//                // Tạo thư mục nếu chưa có
//                if (!productImgDir.exists()) {
//                    productImgDir.mkdirs();
//                }
//
//                Path imagePath = Paths.get(productImgDir.getAbsolutePath(), image.getOriginalFilename());
//                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
//                dbProduct.setImage(image.getOriginalFilename());
//            } catch (IOException e) {
//                throw new RuntimeException("Error saving image: " + e.getMessage());
//            }
//        }
        // Nếu có file ảnh avatar, upload lên Cloudinary trước khi lưu user
//        if (image != null && !image.isEmpty()) {
//            try {
//                byte[] avatarBytes = image.getBytes();
//                String avatarUrl = fileStorageService.uploadFile(image);
//                product.setImage(avatarUrl); // Lưu URL ảnh vào User
//            } catch (IOException e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
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
                .collect(Collectors.toMap(Product::getId, Product::getStock));

        // Lọc ra danh sách productId nào có stock < quantity yêu cầu
        return productIds.stream()
                .filter(id -> stockMap.getOrDefault(id, 0) < quantities.get(productIds.indexOf(id)))
                .collect(Collectors.toList());
    }

    //Search product = tên
//    @Override
//    public List<Product> searchProduct(String name) {
//        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(name, name);
//    }
//
//    @Override
//    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
//        return null;
//    }
//
//    @Override
//    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
//        return null;
//    }
//
//    @Override
//    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
//        return null;
//    }
//
//    @Override
//    public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch) {
//        return null;
//    }


}
