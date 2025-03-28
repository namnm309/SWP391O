package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.ProductResponse;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.enums.AgeGroup;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "[Product]", description = "")
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

    @Operation(summary = "API hiển thị danh sách sản phẩm product(vaccine)(all)")
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        List<ProductResponse> products = productService.getAllProducts().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API thêm product(vaccine)(staff)",
            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh")
    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> addProduct(
            @RequestParam String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam double price,
            @RequestParam Integer quantity,
            @RequestParam String description,
            @RequestParam int discount,
            @RequestParam double discountPrice,
            @RequestParam boolean isActive,
            @RequestParam String manufacturer,
            @RequestParam AgeGroup targetGroup,
            @RequestParam String schedule,
            @RequestParam String sideEffects,
            @RequestParam boolean available,
            @RequestParam boolean isPriority,
            @RequestParam Integer minAgeMonths,
            @RequestParam Integer maxAgeMonths,
            @RequestParam Integer numberOfDoses,
            @RequestParam Integer minDaysBetweenDoses,
            @RequestParam(required = false) List<MultipartFile> images) throws IOException {

        Product product = new Product();
        product.setTitle(title);

        Category category;
        if (categoryId == null) {
            category = categoryRepository.findByName("Chưa phân loại")
                    .orElseThrow(() -> new RuntimeException("KO thấy danh mục mặc định "));
        } else {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        }
        product.setCategory(category);

        product.setPrice(price);
        product.setQuantity(quantity);
        product.setQuantity(quantity); // Gán stock = quantity ban đầu
        product.setDescription(description);
        product.setDiscount(discount);
        product.setDiscountPrice(discountPrice);
        product.setIsActive(isActive);
        product.setManufacturer(manufacturer);
        product.setTargetGroup(targetGroup);
        product.setSchedule(schedule);
        product.setSideEffects(sideEffects);
        product.setAvailable(available);
        product.setIsPriority(isPriority);
        product.setMinAgeMonths(minAgeMonths);
        product.setMaxAgeMonths(maxAgeMonths);
        product.setNumberOfDoses(numberOfDoses);
        product.setMinDaysBetweenDoses(minDaysBetweenDoses);

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

        Product saved = productService.addProduct(product, images);
        return ResponseEntity.ok(new ProductResponse(saved));
    }

    @Operation(summary = "API tìm kiếm sản phẩm theo tên(all) ")
    @GetMapping("/searchProduct")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProduct(@RequestParam String title) {
        List<ProductResponse> searchProducts = productService.getProductByTitle(title).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "Search results", searchProducts));
    }

    @GetMapping("/product/{id}")
    @Operation(summary = "Lấy sản phẩm theo ID(all)", description = "Trả về thông tin sản phẩm với ID tương ứng.")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "ID của sản phẩm cần tìm") @PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", new ProductResponse(product)));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API cập nhật sản phẩm (PATCH - multipart)")
    @PatchMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer discount,
            @RequestParam(required = false) Double discountPrice,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) AgeGroup targetGroup,
            @RequestParam(required = false) String schedule,
            @RequestParam(required = false) String sideEffects,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Boolean isPriority,
            @RequestParam(required = false) Integer minAgeMonths,
            @RequestParam(required = false) Integer maxAgeMonths,
            @RequestParam(required = false) Integer numberOfDoses,
            @RequestParam(required = false) Integer minDaysBetweenDoses,
            @RequestParam(value = "file", required = false) List<MultipartFile> image) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (title != null) existingProduct.setTitle(title);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
            existingProduct.setCategory(category);
        }
        if (price != null) existingProduct.setPrice(price);
        if (quantity != null) existingProduct.setQuantity(quantity);
        if (description != null) existingProduct.setDescription(description);
        if (discount != null) existingProduct.setDiscount(discount);
        if (discountPrice != null) existingProduct.setDiscountPrice(discountPrice);
        if (isActive != null) existingProduct.setIsActive(isActive);
        if (manufacturer != null) existingProduct.setManufacturer(manufacturer);
        if (targetGroup != null) existingProduct.setTargetGroup(targetGroup);
        if (schedule != null) existingProduct.setSchedule(schedule);
        if (sideEffects != null) existingProduct.setSideEffects(sideEffects);
        if (available != null) existingProduct.setAvailable(available);
        if (isPriority != null) existingProduct.setIsPriority(isPriority);
        if (minAgeMonths != null) existingProduct.setMinAgeMonths(minAgeMonths);
        if (maxAgeMonths != null) existingProduct.setMaxAgeMonths(maxAgeMonths);
        if (numberOfDoses != null) existingProduct.setNumberOfDoses(numberOfDoses);
        if (minDaysBetweenDoses != null) existingProduct.setMinDaysBetweenDoses(minDaysBetweenDoses);

        Product updatedProduct = productService.updateProduct(existingProduct, image);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Product updated successfully", new ProductResponse(updatedProduct)));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API xóa sản phẩm = id sản phẩm ")
    @DeleteMapping("/deleteProduct/{productId}")
    public String deleteUser(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return "Đã delete product ";
    }

    @GetMapping("/byCategory")
    @Operation(summary = "Lấy danh sách sản phẩm theo danh mục",
            description = "Truy vấn danh sách sản phẩm dựa trên ID hoặc tên danh mục.")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
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
            return ResponseEntity.ok(new ApiResponse<>(404, "Không có sản phẩm nào thuộc danh mục này", List.of()));
        }

        List<ProductResponse> responseList = products.stream().map(ProductResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(1000, "Danh sách sản phẩm", responseList));
    }
}
