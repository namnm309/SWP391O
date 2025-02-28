package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    //Thêm sản phẩm
    public Product addProduct(Product product);

    //Lấy danh sách sản phẩm
    public List<Product> getAllProducts();

    //Xóa sản phẩm = id product
    public Boolean deleteProduct(Long id);

    //Kiếm sản phẩm bằng id,dùng để update product
    public Product getProductById(Long id);

    //Kiếm sản phẩm = tên
    public List<Product> getProductByTitle(String title);

    //Cập nhật sản phẩm = id , cả hình
    public Product updateProduct(Product product, MultipartFile file);

    //Search profuct by tên ##
    public List<Product> searchProduct(String name);

    //Lấy tất cả sản phẩm đang hoạt động theo phân trang hiện tại
    public Page<Product> getAllActiveProductPagination(Integer pageNo,
                                                       Integer pageSize,
                                                       String category);

    //Tìm kiếm tất cả sản phẩm theoo phân trang hiện tại
    public Page<Product> searchProductPagination(Integer pageNo,
                                                 Integer pageSize,
                                                 String ch);

    //Lấy tất cả sản phẩm theo phân trang hiện tại
    public Page<Product> getAllProductsPagination(Integer pageNo,
                                                  Integer pageSize);

    //Tìm kiếm tất cả sản phẩm hoạt động theo phân trang hiện tạu
    public Page<Product> searchActiveProductPagination(Integer pageNo,
                                                       Integer pageSize,
                                                       String category,
                                                       String ch);

}
