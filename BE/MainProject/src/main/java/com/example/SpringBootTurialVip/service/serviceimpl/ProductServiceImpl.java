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
    public Product addProduct(Product product) throws IOException {
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

        if (product.getImage() != null && !product.getImage().isEmpty()) {

            product.setImage(product.getImage()); // Lưu URL ảnh vào User
            log.info(String.valueOf(product.getImage()));

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
//    public Product updateProduct(Product product, MultipartFile image) {
//        // Lấy sản phẩm từ DB
//        Product dbProduct = getProductById(product.getId());
//        if (dbProduct == null) {
//            throw new RuntimeException("Product not found with id: " + product.getId());
//        }
//
//        // Xử lý ảnh
//        String imageName = (image != null && !image.isEmpty()) ? image.getOriginalFilename() : dbProduct.getImage();
//
//        // Cập nhật thông tin sản phẩm
//        product.setTitle(product.getTitle());
//        product.setCategory(product.getCategory());
//        product.setPrice(product.getPrice());
//        product.setStock(product.getStock());
//        product.setDescription(product.getDescription());
//        product.setDiscount(product.getDiscount());
//        product.setDiscountPrice(product.getDiscountPrice());
//        product.setIsActive(product.getIsActive());
//        product.setManufacturer(product.getManufacturer());
//        product.setTargetGroup(product.getTargetGroup());
//        product.setSchedule(product.getSchedule());
//        product.setSideEffects(product.getSideEffects());
//        product.setAvailable(product.isAvailable());
//
//        // Kiểm tra giảm giá hợp lệ
//        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
//            throw new IllegalArgumentException("Invalid discount percentage");
//        }
//
//        // Tính toán giá sau khi giảm
//        Double discountAmount = product.getPrice() * (product.getDiscount() / 100.0);
//        Double discountPrice = product.getPrice() - discountAmount;
//        dbProduct.setDiscount(product.getDiscount());
//        dbProduct.setDiscountPrice(discountPrice);
//
//        // Lưu sản phẩm vào database
//        Product updatedProduct = productRepository.save(dbProduct);
//
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
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Error saving image: " + e.getMessage());
//            }
//        }
//
//        return updatedProduct;
//    }
    public Product updateProduct(Product product, MultipartFile image) {
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
        if (image != null && !image.isEmpty()) {
            try {
                byte[] avatarBytes = image.getBytes();
                String avatarUrl = fileStorageService.uploadFile(image);
                product.setImage(avatarUrl); // Lưu URL ảnh vào User
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return productRepository.save(dbProduct);
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
