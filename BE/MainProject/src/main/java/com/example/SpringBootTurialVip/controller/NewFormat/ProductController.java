package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.ProductDetailCreateRequest;
import com.example.SpringBootTurialVip.dto.request.ProductDetailRequest;
import com.example.SpringBootTurialVip.dto.response.ProductDetailResponse;
import com.example.SpringBootTurialVip.dto.response.ProductResponse;
import com.example.SpringBootTurialVip.dto.response.SKUResponse;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.ProductDetails;
import com.example.SpringBootTurialVip.enums.AgeGroup;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.CategoryRepository;
import com.example.SpringBootTurialVip.repository.ProductRepository;

import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

//    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
//    @Operation(summary = "API thêm product(vaccine)(staff)",
//            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh")
//    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ProductResponse> addProduct(
//            @RequestParam String title,
//            @RequestParam(required = false) Long categoryId,
//            @RequestParam double price,
//            @RequestParam Integer quantity,
//            @RequestParam String description,
//            @RequestParam int discount,
//            @RequestParam double discountPrice,
//            @RequestParam boolean isActive,
//            @RequestParam String manufacturer,
//            @RequestParam AgeGroup targetGroup,
//            @RequestParam String schedule,
//            @RequestParam String sideEffects,
//            @RequestParam boolean available,
//            @RequestParam boolean isPriority,
//            @RequestParam Integer minAgeMonths,
//            @RequestParam Integer maxAgeMonths,
//            @RequestParam Integer numberOfDoses,
//            @RequestParam Integer minDaysBetweenDoses,
//            @RequestParam(required = false) List<MultipartFile> images) throws IOException {
//
//        Product product = new Product();
//        product.setTitle(title);
//
//        Category category;
//        if (categoryId == null) {
//            category = categoryRepository.findByName("Chưa phân loại")
//                    .orElseThrow(() -> new RuntimeException("KO thấy danh mục mặc định "));
//        } else {
//            category = categoryRepository.findById(categoryId)
//                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
//        }
//        product.setCategory(category);
//
//        product.setPrice(price);
//        product.setQuantity(quantity);
//        product.setQuantity(quantity); // Gán stock = quantity ban đầu
//        product.setDescription(description);
//        product.setDiscount(discount);
//        product.setDiscountPrice(discountPrice);
//        product.setIsActive(isActive);
//        product.setManufacturer(manufacturer);
//        product.setTargetGroup(targetGroup);
//        product.setSchedule(schedule);
//        product.setSideEffects(sideEffects);
//        product.setAvailable(available);
//        product.setIsPriority(isPriority);
//        product.setMinAgeMonths(minAgeMonths);
//        product.setMaxAgeMonths(maxAgeMonths);
//        product.setNumberOfDoses(numberOfDoses);
//        product.setMinDaysBetweenDoses(minDaysBetweenDoses);
//
//        if (images != null && !images.isEmpty()) {
//            try {
//                List<String> imageUrls = images.stream()
//                        .map(image -> {
//                            try {
//                                return fileStorageService.uploadFile(image);
//                            } catch (IOException e) {
//                                throw new RuntimeException("Lỗi khi upload ảnh");
//                            }
//                        })
//                        .collect(Collectors.toList());
//
//                product.setImageList(imageUrls);
//            } catch (Exception e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
//
//        Product saved = productService.addProduct(product, images);
//        return ResponseEntity.ok(new ProductResponse(saved));
//    }


//    @Operation(summary = "API thêm product(vaccine)(staff)",
//            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh")
//    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ProductResponse> addProduct(
//            @RequestParam String title,
//            @RequestParam(required = false) Long categoryId,
//            @RequestParam double price,
//            @RequestParam String description,
//            @RequestParam int discount,
//            @RequestParam double discountPrice,
//            @RequestParam boolean isActive,
//            @RequestParam String manufacturer,
//            @RequestParam String schedule,
//            @RequestParam String sideEffects,
//            @RequestParam boolean available,
//            @RequestParam boolean isPriority,
//            @RequestParam Integer minAgeMonths,
//            @RequestParam Integer maxAgeMonths,
//            @RequestParam Integer numberOfDoses,
//            @RequestParam Integer minDaysBetweenDoses,
//            @RequestParam(required = false) List<MultipartFile> images,
//
//            @RequestParam List<String> batchNumbers,       // Nhận mảng batchNumbers từ FE
//            @RequestParam List<String> expirationDates,    // Nhận mảng expirationDates từ FE
//            @RequestParam List<Integer> quantities         // Nhận mảng quantities từ FE
//    ) throws IOException {
//
//        // Tạo đối tượng Product
//        Product product = new Product();
//        product.setTitle(title);
//
//        // Xử lý category
//        Category category;
//        if (categoryId == null) {
//            category = categoryRepository.findByName("Chưa phân loại")
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục mặc định"));
//        } else {
//            category = categoryRepository.findById(categoryId)
//                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
//        }
//        product.setCategory(category);
//
//        // Gán các giá trị còn lại cho sản phẩm
//        product.setPrice(price);
//        product.setDescription(description);
//        product.setDiscount(discount);
//        product.setDiscountPrice(discountPrice);
//        product.setIsActive(isActive);
//        product.setManufacturer(manufacturer);
//        product.setSchedule(schedule);
//        product.setSideEffects(sideEffects);
//        product.setAvailable(available);
//        product.setIsPriority(isPriority);
//        product.setMinAgeMonths(minAgeMonths);
//        product.setMaxAgeMonths(maxAgeMonths);
//        product.setNumberOfDoses(numberOfDoses);
//        product.setMinDaysBetweenDoses(minDaysBetweenDoses);
//
//        // Tạo SKU tự động cho sản phẩm
//        product.generateSku();  // Tạo SKU theo định dạng "SKU-{productId}-VAX" khi lưu sản phẩm
//
//        // Tạo ProductDetails từ các tham số batchNumbers, expirationDates, quantities nhận được từ FE
//        List<ProductDetails> productDetailsList = new ArrayList<>();
//        for (int i = 0; i < batchNumbers.size(); i++) {
//            ProductDetails detailItem = new ProductDetails();
//            detailItem.setSku(product.getSku());  // Gán SKU tự động cho từng ProductDetails
//            detailItem.setBatchNumber(batchNumbers.get(i));
//            detailItem.setExpirationDate(LocalDate.parse(expirationDates.get(i)));  // Chuyển đổi từ String sang LocalDate
//            detailItem.setQuantity(quantities.get(i));
//            detailItem.setProduct(product);  // Liên kết với Product
//            productDetailsList.add(detailItem);  // Thêm vào danh sách
//        }
//
//        // Gán danh sách ProductDetails cho sản phẩm
//        product.setProductDetails(productDetailsList);
//
//        // Xử lý ảnh nếu có
//        if (images != null && !images.isEmpty()) {
//            try {
//                List<String> imageUrls = images.stream()
//                        .map(image -> {
//                            try {
//                                return fileStorageService.uploadFile(image);
//                            } catch (IOException e) {
//                                throw new RuntimeException("Lỗi khi upload ảnh");
//                            }
//                        })
//                        .collect(Collectors.toList());
//
//                product.setImageList(imageUrls);
//            } catch (Exception e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
//
//        // Lưu sản phẩm vào DB
//        Product saved = productService.addProduct(product, images);
//        return ResponseEntity.ok(new ProductResponse(saved));
//    }

    //===============================================================================
    @Operation(summary = "API thêm product(vaccine)(staff)",
            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh")
    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> addProduct(
            @RequestParam String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam double price,
            @RequestParam String description,
            @RequestParam int discount,
            @RequestParam double discountPrice,
            @RequestParam boolean isActive,
            @RequestParam String manufacturer,
            @RequestParam String schedule,
            @RequestParam String sideEffects,
            @RequestParam boolean available,
            @RequestParam boolean isPriority,
            @RequestParam Integer minAgeMonths,
            @RequestParam Integer maxAgeMonths,
            @RequestParam Integer numberOfDoses,
            @RequestParam Integer minDaysBetweenDoses,
            @RequestParam(required = false) List<MultipartFile> images,

            @RequestParam String batchNumber,       // Chỉ nhận 1 batchNumber duy nhất
            @RequestParam String expirationDate,    // Chỉ nhận 1 expirationDate duy nhất
            @RequestParam Integer quantity         // Chỉ nhận 1 quantity duy nhất
    ) throws IOException {

        // Tạo đối tượng Product
        Product product = new Product();
        product.setTitle(title);

        // Xử lý category
        Category category;
        if (categoryId == null) {
            category = categoryRepository.findByName("Chưa phân loại")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục mặc định"));
        } else {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        }
        product.setCategory(category);

        // Gán các giá trị còn lại cho sản phẩm
        product.setPrice(price);
        product.setDescription(description);
        product.setDiscount(discount);
        product.setDiscountPrice(discountPrice);
        product.setIsActive(isActive);
        product.setManufacturer(manufacturer);
        product.setSchedule(schedule);
        product.setSideEffects(sideEffects);
        product.setAvailable(available);
        product.setIsPriority(isPriority);
        product.setMinAgeMonths(minAgeMonths);
        product.setMaxAgeMonths(maxAgeMonths);
        product.setNumberOfDoses(numberOfDoses);
        product.setMinDaysBetweenDoses(minDaysBetweenDoses);

        // Tạo SKU tự động cho sản phẩm
        product.generateSku();  // Tạo SKU theo định dạng "SKU-{productId}-VAX" khi lưu sản phẩm

        // Tạo ProductDetails từ các tham số batchNumber, expirationDate, quantity nhận được từ FE
        ProductDetails productDetail = new ProductDetails();
        productDetail.setSku(product.getSku());  // Gán SKU tự động cho ProductDetails
        productDetail.setBatchNumber(batchNumber);  // Gán batch number duy nhất
        productDetail.setExpirationDate(LocalDate.parse(expirationDate));  // Chuyển đổi từ String sang LocalDate
        productDetail.setQuantity(quantity);  // Gán quantity duy nhất
        productDetail.setProduct(product);  // Liên kết với Product

        // Gán ProductDetails cho sản phẩm
        product.setProductDetails(Collections.singletonList(productDetail));

        // Xử lý ảnh nếu có
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

        // Lưu sản phẩm vào DB
        Product saved = productService.addProduct(product, images);
        return ResponseEntity.ok(new ProductResponse(saved));
    }


    //================================================================================

//@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
//@Operation(summary = "API thêm product(vaccine)(staff)",
//        description = "Thêm vaccine mới với thông tin sản phẩm và ảnh")
//@PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//public ResponseEntity<ApiResponse<ProductResponse>> addProduct(
//        @RequestParam String title,
//        @RequestParam(required = false) Long categoryId,
//        @RequestParam double price,
//        @RequestParam String description,
//        @RequestParam boolean isActive,
//        @RequestParam String manufacturer,
//        @RequestParam String schedule,
//        @RequestParam String sideEffects,
//        @RequestParam boolean available,
//        @RequestParam boolean isPriority,
//        @RequestParam Integer minAgeMonths,
//        @RequestParam Integer maxAgeMonths,
//        @RequestParam Integer numberOfDoses,
//        @RequestParam Integer minDaysBetweenDoses,
//        @RequestParam(required = false) List<MultipartFile> images,
//
//        @RequestParam String batchNumber,       // Chỉ nhận 1 batchNumber duy nhất
//        @RequestParam String expirationDate,    // Chỉ nhận 1 expirationDate duy nhất
//        @RequestParam Integer quantity         // Chỉ nhận 1 quantity duy nhất
//) throws IOException {
//
//    // Validate Input
//    validateProductInputs(title, price, description, manufacturer);
//
//    // Tạo đối tượng Product
//    Product product = createProduct(title, categoryId, price, description, isActive, manufacturer, schedule, sideEffects, available, isPriority, minAgeMonths, maxAgeMonths, numberOfDoses, minDaysBetweenDoses, images);
//
//    // Tạo SKU cho sản phẩm
//    product.generateSku();
//
//    // Tạo ProductDetails từ batchNumber, expirationDate, quantity nhận được từ FE
//    List<ProductDetails> productDetailsList = createProductDetailsList(product, batchNumber, expirationDate, quantity);
//
//    // Gán danh sách ProductDetails cho sản phẩm
//    product.setProductDetails(productDetailsList);
//
//    // Xử lý ảnh nếu có
//    processProductImages(images, product);
//
//    // Lưu sản phẩm vào DB
//    Product savedProduct = productService.addProduct(product, images);
//
//    // Tạo response trả về
//    ProductResponse response = new ProductResponse(savedProduct);
//    return ResponseEntity.ok(new ApiResponse<>(1000, "Product details added successfully", response));
//}
//
//
//    // Tách phần logic tạo product
//    private Product createProduct(String title, Long categoryId, double price, String description,
//                                 // int discount, double discountPrice,
//                                  boolean isActive,
//                                  String manufacturer, String schedule, String sideEffects,
//                                  boolean available, boolean isPriority, Integer minAgeMonths,
//                                  Integer maxAgeMonths, Integer numberOfDoses, Integer minDaysBetweenDoses,
//                                  List<MultipartFile> images) {
//        Product product = new Product();
//        product.setTitle(title);
//        // Các thông tin còn lại như category, price, description, discount... sẽ được gán ở đây.
//        return product;
//    }
//
//    // Tạo danh sách ProductDetails, nhưng chỉ có một lô
//    private List<ProductDetails> createProductDetailsList(Product product, String batchNumber,
//                                                          String expirationDate, Integer quantity) {
//        List<ProductDetails> productDetailsList = new ArrayList<>();
//
//        ProductDetails detailItem = new ProductDetails();
//        detailItem.setSku(product.getSku());  // Gán SKU cho mỗi ProductDetails
//        detailItem.setBatchNumber(batchNumber);
//        detailItem.setExpirationDate(LocalDate.parse(expirationDate));
//        detailItem.setQuantity(quantity);
//        detailItem.setProduct(product);
//
//        productDetailsList.add(detailItem);
//
//        return productDetailsList;
//    }
//
//
//    // Xử lý ảnh
//    private void processProductImages(List<MultipartFile> images, Product product) throws IOException {
//        if (images != null && !images.isEmpty()) {
//            try {
//                List<String> imageUrls = images.stream()
//                        .map(image -> {
//                            try {
//                                return fileStorageService.uploadFile(image);
//                            } catch (IOException e) {
//                                throw new RuntimeException("Error uploading image");
//                            }
//                        })
//                        .collect(Collectors.toList());
//
//                product.setImageList(imageUrls);
//            } catch (Exception e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
//    }
//
//    // Kiểm tra tính hợp lệ của các input
//    private void validateProductInputs(String title, double price, String description, String manufacturer) {
//        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title is required.");
//        if (price <= 0) throw new IllegalArgumentException("Price must be greater than zero.");
//        if (description == null || description.isEmpty()) throw new IllegalArgumentException("Description is required.");
//        if (manufacturer == null || manufacturer.isEmpty()) throw new IllegalArgumentException("Manufacturer is required.");
//    }





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

//    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
//    @Operation(summary = "API cập nhật sản phẩm (PATCH - multipart)")
//    @PatchMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
//            @PathVariable Long id,
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) Long categoryId,
//            @RequestParam(required = false) Double price,
//            @RequestParam(required = false) Integer quantity,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) Integer discount,
//            @RequestParam(required = false) Double discountPrice,
//            @RequestParam(required = false) Boolean isActive,
//            @RequestParam(required = false) String manufacturer,
//            @RequestParam(required = false) AgeGroup targetGroup,
//            @RequestParam(required = false) String schedule,
//            @RequestParam(required = false) String sideEffects,
//            @RequestParam(required = false) Boolean available,
//            @RequestParam(required = false) Boolean isPriority,
//            @RequestParam(required = false) Integer minAgeMonths,
//            @RequestParam(required = false) Integer maxAgeMonths,
//            @RequestParam(required = false) Integer numberOfDoses,
//            @RequestParam(required = false) Integer minDaysBetweenDoses,
//            @RequestParam(value = "file", required = false) List<MultipartFile> image) {
//
//        Product existingProduct = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
//
//        if (title != null) existingProduct.setTitle(title);
//        if (categoryId != null) {
//            Category category = categoryRepository.findById(categoryId)
//                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
//            existingProduct.setCategory(category);
//        }
//        if (price != null) existingProduct.setPrice(price);
//        if (quantity != null) existingProduct.setQuantity(quantity);
//        if (description != null) existingProduct.setDescription(description);
//        if (discount != null) existingProduct.setDiscount(discount);
//        if (discountPrice != null) existingProduct.setDiscountPrice(discountPrice);
//        if (isActive != null) existingProduct.setIsActive(isActive);
//        if (manufacturer != null) existingProduct.setManufacturer(manufacturer);
////        if (targetGroup != null) existingProduct.setTargetGroup(targetGroup);
//        if (schedule != null) existingProduct.setSchedule(schedule);
//        if (sideEffects != null) existingProduct.setSideEffects(sideEffects);
//        if (available != null) existingProduct.setAvailable(available);
//        if (isPriority != null) existingProduct.setIsPriority(isPriority);
//        if (minAgeMonths != null) existingProduct.setMinAgeMonths(minAgeMonths);
//        if (maxAgeMonths != null) existingProduct.setMaxAgeMonths(maxAgeMonths);
//        if (numberOfDoses != null) existingProduct.setNumberOfDoses(numberOfDoses);
//        if (minDaysBetweenDoses != null) existingProduct.setMinDaysBetweenDoses(minDaysBetweenDoses);
//
//        Product updatedProduct = productService.updateProduct(existingProduct, image);
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Product updated successfully", new ProductResponse(updatedProduct)));
//    }

@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
@Operation(summary = "API cập nhật sản phẩm (PATCH - multipart)")
@PatchMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
        @PathVariable Long id,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Double price,
        //RequestParam(required = false) Integer quantity,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) Integer discount,
        @RequestParam(required = false) Double discountPrice,
        @RequestParam(required = false) Boolean isActive,
        @RequestParam(required = false) String manufacturer,
        @RequestParam(required = false) String schedule,
        @RequestParam(required = false) String sideEffects,
        @RequestParam(required = false) Boolean available,
        @RequestParam(required = false) Boolean isPriority,
        @RequestParam(required = false) Integer minAgeMonths,
        @RequestParam(required = false) Integer maxAgeMonths,
        @RequestParam(required = false) Integer numberOfDoses,
        @RequestParam(required = false) Integer minDaysBetweenDoses,
        @RequestParam(value = "file", required = false) List<MultipartFile> image) {

    // Lấy sản phẩm từ DB
    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

    // Cập nhật các thuộc tính nếu có
    if (title != null) existingProduct.setTitle(title);
    if (categoryId != null) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        existingProduct.setCategory(category);
    }
    if (price != null) existingProduct.setPrice(price);
    //if (quantity != null) existingProduct.setQuantity(quantity);
    if (description != null) existingProduct.setDescription(description);
    if (discount != null) existingProduct.setDiscount(discount);
    if (discountPrice != null) existingProduct.setDiscountPrice(discountPrice);
    if (isActive != null) existingProduct.setIsActive(isActive);
    if (manufacturer != null) existingProduct.setManufacturer(manufacturer);
    if (schedule != null) existingProduct.setSchedule(schedule);
    if (sideEffects != null) existingProduct.setSideEffects(sideEffects);
    if (available != null) existingProduct.setAvailable(available);
    if (isPriority != null) existingProduct.setIsPriority(isPriority);
    if (minAgeMonths != null) existingProduct.setMinAgeMonths(minAgeMonths);
    if (maxAgeMonths != null) existingProduct.setMaxAgeMonths(maxAgeMonths);
    if (numberOfDoses != null) existingProduct.setNumberOfDoses(numberOfDoses);
    if (minDaysBetweenDoses != null) existingProduct.setMinDaysBetweenDoses(minDaysBetweenDoses);

    // Cập nhật sản phẩm
    Product updatedProduct = productService.updateProduct(existingProduct, image);

    // Trả về phản hồi API
    return ResponseEntity.ok(new ApiResponse<>(1000, "Đã cập nhật vaccine thành công", new ProductResponse(updatedProduct)));
}

//====================================================================Lô hàng ==============================================================

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API thêm lô hàng cho sản phẩm (POST)")
    @PostMapping(value = "/addProductDetails/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ProductDetailResponse>> createProductDetails(
            @PathVariable Long productId,
            @RequestBody ProductDetailCreateRequest productDetailRequest) {

        // Tạo mới ProductDetails
        ProductDetails createdProductDetails = productService.createProductDetails(productId, productDetailRequest);

        // Trả về phản hồi API
        return ResponseEntity.ok(new ApiResponse<>(1000, "Product details added successfully", new ProductDetailResponse(createdProductDetails)));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API lấy thông tin lô hàng theo sản phẩm (GET)")
    @GetMapping(value = "/getProductDetails/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<ProductDetailResponse>>> getProductDetails(
            @PathVariable Long productId) {

        // Lấy thông tin ProductDetails của sản phẩm
        List<ProductDetails> productDetailsList = productService.getProductDetailsByProductId(productId);

        // Trả về phản hồi API
        return ResponseEntity.ok(new ApiResponse<>(1000, "Product details fetched successfully",
                productDetailsList.stream().map(ProductDetailResponse::new).collect(Collectors.toList())));
    }



    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API cập nhật thông tin lô hàng (PATCH) theo ID")
    @PatchMapping(value = "/updateProductDetails/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ProductDetailResponse>> updateProductDetails(
            @PathVariable Long id,  // Sử dụng Long id thay vì sku
            @RequestBody ProductDetailCreateRequest productDetailRequest) {

        // Cập nhật thông tin ProductDetails theo ID
        ProductDetails updatedProductDetails = productService.updateProductDetails(id, productDetailRequest);

        // Trả về phản hồi API
        return ResponseEntity.ok(new ApiResponse<>(1000, "Product details updated successfully", new ProductDetailResponse(updatedProductDetails)));
    }




    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API xóa lô hàng theo ID (DELETE)")
    @DeleteMapping(value = "/deleteProductDetails/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> deleteProductDetails(
            @PathVariable Long id) {  // Sử dụng Long id thay vì sku

        // Xóa thông tin ProductDetails theo ID
        boolean isDeleted = productService.deleteProductDetails(id);

        // Trả về phản hồi API
        if (isDeleted) {
            return ResponseEntity.ok(new ApiResponse<>(1000, "Product details deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1001, "Failed to delete product details", null));
        }
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

    // API lấy danh sách tất cả SKU hoặc tìm kiếm theo SKU và Batch
    @GetMapping("/all-skus")
    @Operation(summary = "Lấy danh sách tất cả SKU hoặc tìm kiếm theo SKU, Batch",
            description = "Nếu không có tham số tìm kiếm, API sẽ trả về tất cả SKU. " +
                    "Nếu có tham số, API sẽ tìm theo SKU, Batch hoặc cả hai.")
    public ResponseEntity<List<SKUResponse>> getAllSKUs(
            @RequestParam(required = false) String sku,    // SKU (không bắt buộc)
            @RequestParam(required = false) String batch   // Batch (không bắt buộc)
    ) {
        List<SKUResponse> skuResponses = productService.getAllSKUs(sku, batch);
        return ResponseEntity.ok(skuResponses);
    }
}
