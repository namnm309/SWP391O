package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.CategoryResponse;
import com.example.SpringBootTurialVip.dto.response.CategoryTreeResponse;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.enums.CategoryType;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.CategoryRepository;
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
@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CategoryRepository categoryRepository;

    //API hiển thị tất cả các danh mục kể cả ẩn ( chỉ dành cho admin)
    //API lấy tất cả category
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Api hiển thị tất cả danh mục kể cả ẩn (dạng cây)")
    @GetMapping("/showCategory")
    public ResponseEntity<ApiResponse<List<CategoryTreeResponse>>> loadAddProduct() {
        List<CategoryTreeResponse> tree = categoryService.getCategoryTreeByType(null); // lấy tất cả type
        return ResponseEntity.ok(new ApiResponse<>(1000, "Fetched categories successfully", tree));
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
            @RequestParam("type") CategoryType type,
            @RequestParam(value = "postparentid",required = false) Long parentid,
            @RequestParam(value = "file", required = false) MultipartFile image) throws IOException {

        try {
            // Tạo đối tượng Category từ request params
            Category category = new Category();
            category.setName(name);
            category.setIsActive(isActive);
            category.setType(type != null ? type : CategoryType.ELSE);

            if (parentid != null) {
                Category parent = categoryRepository.findById(parentid.intValue())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha"));
                category.setParent(parent);
            }
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
            summary = "API edit danh mục (cho staff)",
            description = "Chỉnh sửa thông tin danh mục dựa trên ID."
    )
    @PutMapping(value = "/updateCategory/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "type", required = false) CategoryType type,
            @RequestParam(value = "postparentid", required = false) Long parentId,
            @RequestParam(value = "file", required = false) MultipartFile image
    ) {
        try {
            // 1. Tìm danh mục theo ID
            Category oldCategory = categoryService.getCategoryById(id);
            if (oldCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(1004, "Không tìm thấy danh mục", null));
            }

            // 2. Cập nhật thông tin "name" (tránh set null)
            if (name != null && !name.trim().isEmpty()) {
                oldCategory.setName(name);
            }
            // 3. Cập nhật isActive nếu được truyền
            if (isActive != null) {
                oldCategory.setIsActive(isActive);
            }
            // 4. Cập nhật type nếu có, ngược lại giữ nguyên
            if (type != null) {
                oldCategory.setType(type);
            }
            // 5. Xử lý danh mục cha
            if (parentId != null) {
                Category parent = categoryRepository.findById(parentId.intValue())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha với ID: " + parentId));
                oldCategory.setParent(parent);
            }

            // 6. Xử lý ảnh
            // - Nếu có upload file -> upload lên Cloudinary (hoặc nơi khác)
            // - Nếu không, vẫn giữ ảnh cũ (hoặc set default)
            if (image != null && !image.isEmpty()) {
                try {
                    // Lấy tên file
                    String imageName = image.getOriginalFilename();

                    // Upload ảnh lên Cloudinary, nhận về URL
                    String avatarUrl = fileStorageService.uploadFile(image);

                    // Gán URL ảnh vào danh mục
                    oldCategory.setImageName(avatarUrl);

                } catch (IOException e) {
                    // Bắt lỗi và trả về mã lỗi tùy ý
                    throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
                }
            } else {
                // Nếu client không upload file mới:
                //  - Hoặc giữ nguyên ảnh cũ (nếu đã có)
                //  - Hoặc gán ảnh mặc định (nếu hiện đang null / empty)
                if (oldCategory.getImageName() == null || oldCategory.getImageName().trim().isEmpty()) {
                    // Ảnh đang null, ta đặt ảnh mặc định
                    oldCategory.setImageName("noimalge");
                }
            }

            // 7. Lưu danh mục
            Category updatedCategory = categoryService.saveCategory(oldCategory);

            // 8. Trả về kết quả
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

//    //API tìm Category theo tên
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "[Ko cần xài] API tìm kiếm danh mục ")
//    @GetMapping("/searchCategory")
//    public ResponseEntity<ApiResponse<List<Category>>> searchCategory(@RequestParam String name) {
//        List<Category> categories = categoryService.findByNameContaining(name);
//
//        if (categories.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(1004, "No categories found", null));
//        }
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Categories found", categories));
//    }




}
