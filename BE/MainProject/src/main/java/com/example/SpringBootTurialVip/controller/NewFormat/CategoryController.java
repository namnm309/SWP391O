package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.CategoryResponse;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.serviceimpl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name="[Category]",description = "")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService;

    //API hiển thị tất cả các danh mục kể cả ẩn ( chỉ dành cho admin)
    //API lấy tất cả category
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Api hiển thị tất cả danh mục kể cả ẩn (admin,staff) ")
    @GetMapping("/showCategory")
    public ResponseEntity<ApiResponse<List<Category>>> loadAddProduct() {
        List<Category> categories = categoryService.getAllCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }

    //API hiển thị danh mục hoạt động
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN','TEST')")
    @Operation(summary = "API hiển thị tất cả các danh mục đang hoạt động (all) ")
    @GetMapping("/showActiveCategory")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> showActiveCategory() {
        List<CategoryResponse> categories = categoryService.getAllActiveCategory().stream()
                .map(category -> new CategoryResponse(category.getName(), category.getImageName()))
                .collect(Collectors.toList());

        ApiResponse<List<CategoryResponse>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }

    //API tạo danh mục hoạt động
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(
            summary = "API tạo danh mục (cho staff)",
            description = "Tạo danh mục mới với thông tin và hình ảnh"
    )
    @PostMapping(value = "/createCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCategory(
            @RequestParam("name") String name,
            @RequestParam("active") boolean isActive,
            @RequestParam(value = "file", required = false) MultipartFile image) throws IOException {

        try {
            // Tạo đối tượng Category từ request params
            Category category = new Category();
            category.setName(name);
            category.setIsActive(isActive);

            // Xử lý tên ảnh (nếu không có, dùng mặc định)
//            String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
//            category.setImageName(imageName);
            // Nếu có file ảnh, upload lên Cloudinary và lấy URL
            if (image != null && !image.isEmpty()) {
                try {
                    System.out.println("DEBUG: Uploading file to Cloudinary...");

                    // Lấy tên file từ MultipartFile
                    String imageName = image.getOriginalFilename();

                    // Upload file lên Cloudinary và lấy URL
                    String avatarUrl = fileStorageService.uploadFile(image);
                    System.out.println("DEBUG: File uploaded successfully. URL: " + avatarUrl);

                    // Gán URL ảnh vào sản phẩm
                    category.setImageName(avatarUrl);


                } catch (IOException e) {
                    throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
                }
            } else {
                // Nếu không có file ảnh, đặt ảnh mặc định
                System.out.println("DEBUG: No image uploaded, using default image.");
                category.setImageName("noimalge"); // Ảnh mặc định trên Cloudinary

            }


            // Kiểm tra nếu danh mục đã tồn tại
            if (categoryService.existCategory(name)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Category Name already exists"));
            }

            // Lưu category vào DB
            Category savedCategory = categoryService.saveCategory(category);

//            if (!ObjectUtils.isEmpty(savedCategory)) {
//                // Lưu file ảnh nếu có
//                if (file != null && !file.isEmpty()) {
//                    File saveFile = new ClassPathResource("static/img/").getFile();
//                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
//                            + file.getOriginalFilename());
//                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                }
//                return ResponseEntity.ok(Collections.singletonMap("message", "Category saved successfully"));
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Collections.singletonMap("error", "Not saved! Internal server error"));
//            }
            return ResponseEntity.ok(Collections.singletonMap("message", "Category saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }

    }


    //API Update(Edit) Category = ID
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(
            summary = "API edit danh mục(cho staff)",
            description = "Chỉnh sửa thông tin danh mục dựa trên ID."
    )
    @PutMapping(value = "/updateCategory/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("isActive") Boolean isActive,
            @RequestParam(value = "file", required = false) MultipartFile image) {

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

//            // Xử lý ảnh nếu có file mới
//            if (file != null && !file.isEmpty()) {
//                String imageName = file.getOriginalFilename();
//                oldCategory.setImageName(imageName);
//
//                // Lưu file ảnh
//                File saveFile = new ClassPathResource("static/img").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath(), "category_img", imageName);
//                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//            }
            // Nếu có file ảnh, upload lên Cloudinary và lấy URL
            if (image != null && !image.isEmpty()) {
                try {
                    System.out.println("DEBUG: Uploading file to Cloudinary...");

                    // Lấy tên file từ MultipartFile
                    String imageName = image.getOriginalFilename();

                    // Upload file lên Cloudinary và lấy URL
                    String avatarUrl = fileStorageService.uploadFile(image);
                    System.out.println("DEBUG: File uploaded successfully. URL: " + avatarUrl);

                    // Gán URL ảnh vào sản phẩm
                    oldCategory.setImageName(avatarUrl);


                } catch (IOException e) {
                    throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
                }
            } else {
                // Nếu không có file ảnh, đặt ảnh mặc định
                System.out.println("DEBUG: No image uploaded, using default image.");
                oldCategory.setImageName("noimalge"); // Ảnh mặc định trên Cloudinary

            }

            // Lưu danh mục sau khi chỉnh sửa
            Category updatedCategory = categoryService.saveCategory(oldCategory);

            return ResponseEntity.ok(new ApiResponse<>(1000, "Category updated successfully", updatedCategory));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1002, "Invalid input: " + e.getMessage(), null));
        }
    }

    //API Xóa Category = ID
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "API xóa danh mục = id ( staff) ")
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
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "[Ko cần xài] API tìm kiếm danh mục ")
    @GetMapping("/searchCategory")
    public ResponseEntity<ApiResponse<List<Category>>> searchCategory(@RequestParam String name) {
        List<Category> categories = categoryService.findByNameContaining(name);

        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "No categories found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Categories found", categories));
    }




}
