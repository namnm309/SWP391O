package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.CategoryRepository;
import com.example.SpringBootTurialVip.repository.ProductRepository;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name="[Product]",description = "")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

    //API show ra vaccine khi chưa log in
    @Operation(summary = "API hiển thị danh sách sản phẩm product(vaccine)(all)")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.getAllProducts().stream()
                .peek(product -> product.setCategory(product.getCategory())) // Đảm bảo category hiển thị tên
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "API thêm product(vaccine)(staff)",
            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh"
    )
    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<Product> addProduct(
            @RequestParam String title,
            @RequestParam(required = false) Long categoryId,  // Chuyển thành categoryId
            @RequestParam double price,
            @RequestParam int stock,
            @RequestParam String description,
            @RequestParam int discount,
            @RequestParam double discountPrice,
            @RequestParam boolean isActive,
            @RequestParam String manufacturer,
            @RequestParam String targetGroup,
            @RequestParam String schedule,
            @RequestParam String sideEffects,
            @RequestParam boolean available,
            @RequestParam(required = false) MultipartFile image) throws IOException {



        // Tạo sản phẩm với category đã lấy được
        Product product = new Product();
        product.setTitle(title);
        // Nếu categoryId không được nhập, không gán category
        Category category;
        if (categoryId == null) {
            category = categoryRepository.findByName("Chưa phân loại")  // Lấy danh mục mặc định
                    .orElseThrow(() -> new RuntimeException("KO thấy danh mục mặc định "));
        } else {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        }
        product.setCategory(category);

        // Gán Category thay vì String
        product.setPrice(price);
        product.setStock(stock);
        product.setDescription(description);
        product.setDiscount(discount);
        product.setDiscountPrice(discountPrice);
        product.setIsActive(isActive);
        product.setManufacturer(manufacturer);
        product.setTargetGroup(targetGroup);
        product.setSchedule(schedule);
        product.setSideEffects(sideEffects);
        product.setAvailable(available);
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

        return ResponseEntity.ok(productService.addProduct(product));
    }

    //API lấy thông tin sản phẩm theo tên
    @Operation(summary = "API tìm kiếm sản phẩm theo tên(all) ")
    @GetMapping("/searchProduct")
    public ResponseEntity<ApiResponse<List<Product>>> searchProduct(@RequestParam String title) {
        List<Product> searchProducts = productService.getProductByTitle(title);

        List<Category> categories = categoryService.getAllActiveCategory();

        ApiResponse<List<Product>> response = new ApiResponse<>(1000, "Search results", searchProducts);
        return ResponseEntity.ok(response);
    }

    //API kiếm product = id
    @GetMapping("/product/{id}")
    @Operation(summary = "Lấy sản phẩm theo ID(all)", description = "Trả về thông tin sản phẩm với ID tương ứng.")
    public ResponseEntity<ApiResponse<Product>> getProductById(
            @Parameter(description = "ID của sản phẩm cần tìm") @PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", productService.getProductById(id)));
    }

    //API update productId
    @Operation(summary = "API updateprofile(staff)")
    @PutMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam(required = false) Long categoryId,  // Category là tùy chọn
            @RequestParam double price,
            @RequestParam int stock,
            @RequestParam String description,
            @RequestParam int discount,
            @RequestParam double discountPrice,
            @RequestParam boolean isActive,
            @RequestParam String manufacturer,
            @RequestParam String targetGroup,
            @RequestParam String schedule,
            @RequestParam String sideEffects,
            @RequestParam boolean available,
            @RequestParam(value = "file", required = false) MultipartFile image) {

        // Lấy sản phẩm từ DB
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        // Xác định danh mục
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        } else {
            category = categoryRepository.findByName("Chưa phân loại")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục mặc định"));
        }

        // Cập nhật thông tin sản phẩm
        existingProduct.setTitle(title);
        existingProduct.setCategory(category);
        existingProduct.setPrice(price);
        existingProduct.setStock(stock);
        existingProduct.setDescription(description);
        existingProduct.setDiscount(discount);
        existingProduct.setDiscountPrice(discountPrice);
        existingProduct.setIsActive(isActive);
        existingProduct.setManufacturer(manufacturer);
        existingProduct.setTargetGroup(targetGroup);
        existingProduct.setSchedule(schedule);
        existingProduct.setSideEffects(sideEffects);
        existingProduct.setAvailable(available);

        // Gọi service để cập nhật sản phẩm
        Product updatedProduct = productService.updateProduct(existingProduct, image);

        return ResponseEntity.ok(new ApiResponse<>(1000, "Product updated successfully", updatedProduct));
    }


    //API Xóa sản phẩm
    @Operation(summary = "API xóa sản phẩm = id sản phẩm ")
    @DeleteMapping("/deleteProduct/{productId}")
    String deleteUser(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return "Đã delete product ";
    }

    // Tìm sản phẩm theo ID danh mục
    @GetMapping("/byCategory")
    @Operation(summary = "Lấy danh sách sản phẩm theo danh mục",
            description = "Truy vấn danh sách sản phẩm dựa trên ID hoặc tên danh mục.")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName) {

        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (categoryName != null) {
            products = productRepository.findByCategory_Name(categoryName);
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Category ID hoặc Category Name là bắt buộc", null));
        }

        if (products.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(404, "Không có sản phẩm nào thuộc danh mục này", products));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Danh sách sản phẩm", products));
    }




}
