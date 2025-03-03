package com.example.SpringBootTurialVip.controller.Format;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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

    //API show ra vaccine khi chưa log in
    @Operation(summary = "API hiển thị danh sách sản phẩm vaccine(all)")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.getAllProducts().stream()
                .peek(product -> product.setCategory(product.getCategory())) // Đảm bảo category hiển thị tên
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "API thêm vaccine(staff)",
            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh"
    )
    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestParam String title,
            @RequestParam String category,
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

        Product product = new Product();
        product.setTitle(title);
        product.setCategory(category);
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

    //API update productId
    @Operation(summary = "API updateprofile(staff)")
    @PutMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String category,
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

        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setCategory(category);
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

        return ResponseEntity.ok(new ApiResponse<>(1000, "Product updated successfully", productService.updateProduct(product, image)));
    }

    //API Xóa sản phẩm
    @Operation(summary = "API xóa sản phẩm = id sản phẩm ")
    @DeleteMapping("/deleteProduct/{productId}")
    String deleteUser(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return "Đã delete product ";
    }


}
