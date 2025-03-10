package com.example.SpringBootTurialVip.controller.OldFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;

import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.*;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import com.example.SpringBootTurialVip.repository.CategoryRepository;
import com.example.SpringBootTurialVip.service.*;
import com.example.SpringBootTurialVip.service.serviceimpl.StaffServiceImpl;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@RestController
@RequestMapping("/staff")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
@RequiredArgsConstructor
@Tag(name="[StaffController]",description = "Cần authen")
@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
public class StaffController {

    /*
     * @ModelAttribute :Tự động ánh xạ dữ liệu từ form vào một đối tượng (Model)
     * @RequestParam	:Lấy dữ liệu từ query string hoặc form input (dạng key-value)
     * @RequestBody	:Đọc dữ liệu từ request body (thường dùng cho API với JSON/XML)
     * ObjectUtils giúp tránh các giá trị null
     */

    private final StaffServiceImpl staffServiceImpl;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PostService postService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CategoryRepository categoryRepository;


//    //API: Xem danh sách tất cả trẻ
//    @Operation(summary = "Xem danh sách tất cả trẻ em")
//    @GetMapping("/children")
//    public ResponseEntity<List<ChildResponse>> getAllChildren() {
//        return ResponseEntity.ok(staffServiceImpl.getAllChildren());
//    }
//
//    //API: Update(Edit) thông tin `Child`
//    @Operation(
//            summary = "Update thông tin trẻ dựa theo ID",
//            description = "API này cho phép cập nhật thông tin của một đứa trẻ dựa vào ID."
//    )
//    @PutMapping("/children/{childId}/update")
//    public ResponseEntity<ChildResponse> updateChildInfo(
//            @PathVariable Long childId,
//            @RequestBody ChildCreationRequest request) {
//
//        ChildResponse updatedChild = staffServiceImpl.updateChildInfo(childId, request);
//
//        if (updatedChild == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(updatedChild);
//    }

    //API: Xem danh sách tất cả customer
    @Operation(summary = "Xem danh sách tất cả khách hàng")
    @GetMapping("/parents")
    public ResponseEntity<List<UserResponse>> getAllParents() {
        return ResponseEntity.ok(staffServiceImpl.getAllParents());
    }

    //API: Tạo child cho 1 customer theo
//    @Operation(summary = "Tạo 1 child cho 1 khách hàng = cách gán parentid của trẻ đc tạo = id của khách")
//    @PostMapping("/children/create/{parentId}")
//    public ResponseEntity<ChildResponse> createChildForParent(
//            @PathVariable("parentId") Long parentId,
//            @RequestBody ChildCreationRequest request) {
//        return ResponseEntity.ok(staffServiceImpl.createChildForParent(parentId, request));
//    }


//    @Operation(
//            summary = "API thêm vaccine",
//            description = "Thêm vaccine mới với thông tin sản phẩm và ảnh"
//    )
//    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> saveProduct(
//            @RequestParam("name") String title,
//            @RequestParam("category") String category,
//            @RequestParam("price") double price,
//            @RequestParam("stock") int stock,
//            @RequestParam("description") String description,
//            @RequestParam("discountPrice") double discountPrice,
//            @RequestParam("isActive") boolean isActive,
//            @RequestParam("file") MultipartFile image) {
//
//        try {
//            // Tạo đối tượng Product từ request params
//            Product product = new Product();
//            product.setTitle(title);
//            product.setCategory(category);
//            product.setPrice(price);
//            product.setStock(stock);
//            product.setDescription(description);
//            product.setDiscountPrice(discountPrice);
//            product.setIsActive(isActive);
//
//            // Xử lý hình ảnh
//            String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
//            product.setImage(imageName);
//
//            Product savedProduct = productService.addProduct(product);
//
//            if (!ObjectUtils.isEmpty(savedProduct)) {
//                File saveFile = new ClassPathResource("/static/img/").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
//                        + image.getOriginalFilename());
//                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//                return ResponseEntity.ok(Collections.singletonMap("message", "Product saved successfully"));
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Collections.singletonMap("error", "Something went wrong on server"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Collections.singletonMap("error", e.getMessage()));
//        }
//    }
//    public ResponseEntity<Product> addProduct(
//            @RequestParam String title,
//            @RequestParam Long categoryId,  // Chuyển thành categoryId
//            @RequestParam double price,
//            @RequestParam int stock,
//            @RequestParam String description,
//            @RequestParam int discount,
//            @RequestParam double discountPrice,
//            @RequestParam boolean isActive,
//            @RequestParam String manufacturer,
//            @RequestParam String targetGroup,
//            @RequestParam String schedule,
//            @RequestParam String sideEffects,
//            @RequestParam boolean available,
//            @RequestParam(required = false) MultipartFile image) throws IOException {
//
//        // Tìm category theo ID
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
//
//        // Tạo sản phẩm với category đã lấy được
//        Product product = new Product();
//        product.setTitle(title);
//        product.setCategory(category); // Gán Category thay vì String
//        product.setPrice(price);
//        product.setStock(stock);
//        product.setDescription(description);
//        product.setDiscount(discount);
//        product.setDiscountPrice(discountPrice);
//        product.setIsActive(isActive);
//        product.setManufacturer(manufacturer);
//        product.setTargetGroup(targetGroup);
//        product.setSchedule(schedule);
//        product.setSideEffects(sideEffects);
//        product.setAvailable(available);
//
//        return ResponseEntity.ok(productService.addProduct(product));
//    }



    //API lấy thông tin tất cả sản phẩm
    @Operation(summary = "API hiển thị danh sách sản phẩm vaccine")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.getAllProducts().stream()
                .peek(product -> product.setCategory(product.getCategory())) // Đảm bảo category hiển thị tên
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    //API lấy thông tin sản phẩm theo tên
    @Operation(summary = "API tìm kiếm sản phẩm theo tên ")
    @GetMapping("/searchProduct")
    public ResponseEntity<ApiResponse<List<Product>>> searchProduct(@RequestParam String title) {
        List<Product> searchProducts = productService.getProductByTitle(title);

        List<Category> categories = categoryService.getAllActiveCategory();

        ApiResponse<List<Product>> response = new ApiResponse<>(1000, "Search results", searchProducts);
        return ResponseEntity.ok(response);
    }

    //API Update(Edit) sản phẩm
    @Operation(
            summary = "API cập nhật sản phẩm theo ID",
            description = "Cập nhật thông tin sản phẩm bằng ID và cho phép cập nhật hình ảnh"
                )
//    @PutMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<Product>> updateProduct(
//            @PathVariable Long id,
//            @ModelAttribute Product product,
//            @RequestParam(value = "file", required = false) MultipartFile image) {
//
//        try {
//            // Đảm bảo ID trong product trùng với ID trong URL
//            product.setId(id);
//
//            // Gọi service để cập nhật sản phẩm
//            Product updatedProduct = productService.updateProduct(product, image);
//
//            if (updatedProduct == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse<>(1004, "Product not found", null));
//            }
//
//            return ResponseEntity.ok(new ApiResponse<>(1000, "Product updated successfully", updatedProduct));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(1002, "Invalid input: " + e.getMessage(), null));
//        }
//    }
    @PutMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam Long categoryId,  // ✅ Thay đổi từ String category -> Long categoryId
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

        // ✅ Tìm category theo ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        // ✅ Tạo product và set thông tin
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setCategory(category); // ✅ Gán Category thay vì String category
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


    @Operation(
            summary = "API tạo danh mục",
            description = "Tạo danh mục mới với thông tin và hình ảnh"
    )
    @PostMapping(value = "/createCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCategory(
            @RequestParam("name") String name,
            @RequestParam("active") boolean isActive,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        try {
            // Tạo đối tượng Category từ request params
            Category category = new Category();
            category.setName(name);
            category.setIsActive(isActive);

            // Xử lý tên ảnh (nếu không có, dùng mặc định)
            String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
            category.setImageName(imageName);

            // Kiểm tra nếu danh mục đã tồn tại
            if (categoryService.existCategory(name)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Category Name already exists"));
            }

            // Lưu category vào DB
            Category savedCategory = categoryService.saveCategory(category);

            if (!ObjectUtils.isEmpty(savedCategory)) {
                // Lưu file ảnh nếu có
                if (file != null && !file.isEmpty()) {
                    File saveFile = new ClassPathResource("static/img/").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                            + file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                return ResponseEntity.ok(Collections.singletonMap("message", "Category saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Not saved! Internal server error"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    //API kiếm product = id
    @GetMapping("/product/{id}")
    @Operation(summary = "Lấy sản phẩm theo ID", description = "Trả về thông tin sản phẩm với ID tương ứng.")
    public ResponseEntity<ApiResponse<Product>> getProductById(
            @Parameter(description = "ID của sản phẩm cần tìm") @PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", productService.getProductById(id)));
    }

    //API lấy tất cả category
    @Operation(summary = "Api hiển thị tất cả danh mục ")
    @GetMapping("/showCategory")
    public ResponseEntity<ApiResponse<List<Category>>> loadAddProduct() {
        List<Category> categories = categoryService.getAllCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }



    //API lấy tất cả category đang hoạt động
    @Operation(summary = "API hiển thị tất cả các danh mục đang hoạt động")
    @GetMapping("/showActiveCategory")
    public ResponseEntity<ApiResponse<List<Category>>> showActiveCategory() {
        List<Category> categories = categoryService.getAllActiveCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }

    //API Update(Edit) Category = ID
    @Operation(
            summary = "API edit danh mục",
            description = "Chỉnh sửa thông tin danh mục dựa trên ID."
    )
    @PutMapping(value = "/updateCategory/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("isActive") Boolean isActive,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // Tìm danh mục theo ID
            Category oldCategory = categoryService.getCategoryById(id);
            if (oldCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(1004, "Category not found", null));
            }

            // Cập nhật thông tin danh mục
            if (name != null && !name.trim().isEmpty()) {
                oldCategory.setName(name);
            }
            if (isActive != null) {
                oldCategory.setIsActive(isActive);
            }

            // Xử lý ảnh nếu có file mới
            if (file != null && !file.isEmpty()) {
                String imageName = file.getOriginalFilename();
                oldCategory.setImageName(imageName);

                // Lưu file ảnh
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath(), "category_img", imageName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            // Lưu danh mục sau khi chỉnh sửa
            Category updatedCategory = categoryService.saveCategory(oldCategory);

            return ResponseEntity.ok(new ApiResponse<>(1000, "Category updated successfully", updatedCategory));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1001, "Error saving image: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1002, "Invalid input: " + e.getMessage(), null));
        }
    }

    //API Xóa Category = ID
    @Operation(summary = "API xóa danh mục = id ")
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable int id) {
        Boolean isDeleted = categoryService.deleteCategory(id);

        if (isDeleted) {
            return ResponseEntity.ok(new ApiResponse<>(1000, "Category deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1004, "Failed to delete category", null));
        }
    }

    //API tìm Category theo tên
    @Operation(summary = "[Ko cần xài] API tìm kiếm danh muc6 ")
    @GetMapping("/searchCategory")
    public ResponseEntity<ApiResponse<List<Category>>> searchCategory(@RequestParam String name) {
        List<Category> categories = categoryService.findByNameContaining(name);

        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "No categories found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Categories found", categories));
    }

    //API cập nhật  tình trạng đơn hàng
    @Operation(summary = "API cập nhật trạng thái đơn hàng = id đơn hàng",description =
            "StatusID list : (1,In Progress) \n"+
                    "(2,Order Received) \n" +
                    "(3, Out for Stock) \n" +
                    "(4,Cancelled) \n" +
                    "(5,Success) \n")
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<ProductOrder>> updateOrderStatus(
            @RequestParam Long id,//ID đơn hàng
            @RequestParam Integer statusId) {

        //Tìm `OrderStatus` nhanh hơn bằng Stream API
        Optional<OrderStatus> matchedStatus = Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getId().equals(statusId))
                .findFirst();

        if (matchedStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1001, "Invalid status ID", null));
        }

        String status = matchedStatus.get().getName();

        // Cập nhật trạng thái đơn hàng
        ProductOrder updatedOrder = orderService.updateOrderStatus(id, status);

        if (updatedOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1002, "Order not found or status not updated", null));
        }

        // Gửi email thông báo nếu cần
        try {
            commonUtil.sendMailForProductOrder(updatedOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1003, "Order updated but email failed", updatedOrder));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order status updated successfully", updatedOrder));
    }

    //API tìm kiếm user
    @Operation(summary = "APi tìm kiếm 1 user = user id ")
    @GetMapping("/{userId}")
        //Nhận 1 param id để tìm thông tin user đó
    UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    //==========================================================================================================================

     //API Thêm bài viết (có ảnh)
    @Operation(summary = "API thêm bài viết", description =
            "Cho phép staff thêm bài viết mới, có thể kèm hình ảnh.\n"
                    + "Yêu cầu: gửi dưới dạng multipart/form-data."
    )
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        return ResponseEntity.ok(postService.addPostWithImage(title, content, userId, image));
    }

        // API Lấy danh sách tất cả bài viết
//    @Operation(summary = "API lấy danh sách bài viết", description =
//            "Trả về danh sách tất cả bài viết trong hệ thống."
//    )
//    @GetMapping("/posts")
//    public ResponseEntity<List<Post>> getAllPosts() {
//        List<Post> post=postService.getAllPosts();
//
//        return ResponseEntity.ok(postService.getAllPosts());
//    }

    // API Lấy danh sách bài viết của 1 nhân viên cụ thể
//    @Operation(summary = "API lấy danh sách bài viết của một nhân viên", description =
//            "Trả về danh sách bài viết của nhân viên dựa trên staffId."
//    )
//    @GetMapping("/posts/staff/{staffId}")
//    public ResponseEntity<List<Post>> getPostsByStaff(@PathVariable Long staffId) {
//        return ResponseEntity.ok(postService.getPostsByStaff(staffId));
//    }

    // API Cập nhật bài viết (có ảnh mới hoặc không)
    @PutMapping(value = "/posts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "API cập nhật bài viết",
            description = "Cho phép staff cập nhật tiêu đề, nội dung bài viết và thay thế ảnh cũ nếu có."
    )
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile image) {

        try {
            // Gọi service để cập nhật bài viết
            Post updatedPost = postService.updatePost(id, title, content, image);

            return ResponseEntity.ok(Collections.singletonMap("message", "Post updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }



    // API Xóa bài viết
    @Operation(summary = "API xóa bài viết", description =
            "Xóa bài viết dựa trên ID bài viết. Hành động này không thể khôi phục."
    )
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    //==================================================================================================================================================
   //API xem đánh giá chưa phản hồi
    @Operation(
            summary = "API lấy danh sách đánh giá chưa được phản hồi",
            description = "Trả về danh sách tất cả đánh giá của khách hàng chưa được phản hồi."
    )
    @GetMapping("/feedback/unreplied")
    public ResponseEntity<List<Feedback>> getUnrepliedFeedbacks() {
        return ResponseEntity.ok(feedbackService.getUnrepliedFeedbacks());
    }

    //API reply
    @Operation(
            summary = "API phản hồi đánh giá của khách hàng",
            description = "Cho phép nhân viên phản hồi đánh giá của khách hàng.\n"
                    + "Sau khi phản hồi, đánh giá sẽ tự động được đánh dấu là đã phản hồi."
    )
    @PutMapping("/feedback/{id}/reply")
    public ResponseEntity<?> replyFeedback(
            @PathVariable("id") Long id,
            @RequestParam("reply") String reply) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = jwt.getClaim("id");
            return ResponseEntity.ok(feedbackService.replyFeedback(id, reply, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    //==================================================================================================================================================

    @Operation(summary = "API gửi thông báo đến khách hàng", description = "Staff có thể gửi thông báo đến khách hàng.")
    @PostMapping("/notifications")
    public ResponseEntity<Notification> sendNotification(
            @RequestParam Long userId,
            @RequestParam String message) {
        return ResponseEntity.ok(notificationService.sendOrderStatusNotification(userId, message));
    }
    @Operation(summary = "API lấy danh sách thông báo", description = "Trả về danh sách thông báo của khách hàng.")
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @Operation(summary = "API đánh dấu thông báo đã đọc", description = "Cho phép khách hàng đánh dấu thông báo là đã đọc.")
    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    //==================================================================================================================================================








    //API active or unactive 1 customer = id

    //API cho xem order

    //API Update trạng thái order

    //API tìm order theo id

    //API quản lý và theo dõi lịch tiêm chủng (tạo bảng tbl_appointment để lưu lịch tiêm or sử dụng bảng cũ tbl_productorder)
        //Truy vấn lịch tiêm sắp tới và gửi mail cho customer (thông báo lịch tiêm cho khách hàng qua mail )

    //API nhận thông báo từ admin

    //API phản hồi feedback của khách hàng ( tạo 1 bảng tbl_feedback ) (staff sẽ liên hệ dưới comment đánh giá của khách hàng )

    //










}
