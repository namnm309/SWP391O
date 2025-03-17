package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.PostUpdateRequest;

import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Post;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.PostRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.FileUploadService;
import com.example.SpringBootTurialVip.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CategoryService categoryService;

    // Thêm bài viết có ảnh
    @Override
    public Post addPostWithImage(String title,
                                 String content,
                                 Long userId,
                                 String maincontent,
                                 Long categoryId,
                                 List<MultipartFile> image) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setMainContent(maincontent);
        if (categoryId!=null) {
            Category category = categoryService.getCategoryById(Math.toIntExact(categoryId));
            post.setCategory(category);
        } else {
            Category category = categoryService.getCategoryById(1);
            post.setCategory(category);
        }

        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

//        if (image != null && !image.isEmpty()) {
//            String imageUrl = fileUploadService.saveFile(image);
//            post.setImageUrl(imageUrl);
//        }
        // Nếu có file ảnh avatar, upload lên Cloudinary trước khi lưu user
//        if (image != null && !image.isEmpty()) {
//            try {
//                byte[] avatarBytes = image.getBytes();
//                String avatarUrl = fileStorageService.uploadFile(image);
//                post.setImageUrl(avatarUrl); // Lưu URL ảnh vào User
//            } catch (IOException e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
        // Upload ảnh lên Cloudinary (hoặc server) và lưu URL
        if (image != null && !image.isEmpty()) {
            try {
                List<String> imageUrls = image.stream()
                        .map(images -> {
                            try {
                                return fileStorageService.uploadFile(images);
                            } catch (IOException e) {
                                throw new RuntimeException("Lỗi khi upload ảnh");
                            }
                        })
                        .collect(Collectors.toList());

                post.setImageList(imageUrls); // Lưu danh sách ảnh dưới dạng chuỗi
            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return postRepository.save(post);
    }

    //Xem all bài viết
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    //Xem bài viết của 1 staff cụ thể
    @Override
    public List<Post> getPostsByStaff(Long staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return postRepository.findByAuthor(staff);
    }

    //Cập nhật bài viết
    public Post updatePost(Long id,
                           String title,
                           String content,
                           String maincontent,
                           Long categoryId,
                           List<MultipartFile> image) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with ID: " + id));

        // Cập nhật tiêu đề & nội dung
        post.setTitle(title);
        post.setContent(content);
        post.setMainContent(maincontent);
        if (categoryId!=null) {
            Category category = categoryService.getCategoryById(Math.toIntExact(categoryId));
            post.setCategory(category);
        } else {
            Category category = categoryService.getCategoryById(1);
            post.setCategory(category);
        }
        post.setUpdatedAt(LocalDateTime.now());

//        // Nếu có ảnh mới, thay thế ảnh cũ
//        if (image != null && !image.isEmpty()) {
//            String imageName = image.getOriginalFilename();
//            post.setImageUrl(imageName);
//
//            // Lưu ảnh vào thư mục
//            File saveFile = new ClassPathResource("/static/img/").getFile();
//            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "post_img" + File.separator + imageName);
//            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//        }
//        if (image != null && !image.isEmpty()) {
//            try {
//                byte[] avatarBytes = image.getBytes();
//                String avatarUrl = fileStorageService.uploadFile(image);
//                post.setImageUrl(avatarUrl); // Lưu URL ảnh vào User
//            } catch (IOException e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
        // Nếu có ảnh mới, cập nhật danh sách ảnh
        if (image != null && !image.isEmpty()) {
            try {
                List<String> imageUrls = image.stream()
                        .map(images -> {
                            try {
                                return fileStorageService.uploadFile(images);
                            } catch (IOException e) {
                                throw new RuntimeException("Lỗi khi upload ảnh");
                            }
                        })
                        .collect(Collectors.toList());

                post.setImageList(imageUrls);
            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }


        return postRepository.save(post);
    }


    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id " + id);
        }
        postRepository.deleteById(id);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public List<Post> searchByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Post> searchByCategoryId(Long categoryId) {
        return postRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Post> searchByCategoryName(String categoryName) {
        return postRepository.findByCategory_NameContainingIgnoreCase(categoryName);
    }
}
