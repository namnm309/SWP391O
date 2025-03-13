package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.Product;

import com.example.SpringBootTurialVip.repository.VaccineOrderStats;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    //Thêm sản phẩm
    public Product addProduct(Product product,List<MultipartFile> images) throws IOException;

    //Lấy danh sách sản phẩm
    public List<Product> getAllProducts();

    //Xóa sản phẩm = id product
    public Boolean deleteProduct(Long id);

    //Kiếm sản phẩm bằng id,dùng để update product
    public Product getProductById(Long id);

    //Kiếm sản phẩm = tên
    public List<Product> getProductByTitle(String title);

    //Cập nhật sản phẩm = id , cả hình
    public Product updateProduct(Product product, List<MultipartFile> images);

    //Check productId có tồn tại ko
    public List<Long> findInvalidProductIds(List<Long> productIds);

    //Check quantity yêu cầu có lớn hơn hàng có sẵn trong kho ko
    public List<Long> findOutOfStockProducts(List<Long> productIds, List<Integer> quantities);


}
