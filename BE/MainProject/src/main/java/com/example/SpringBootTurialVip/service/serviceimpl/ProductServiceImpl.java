package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ProductDetailCreateRequest;
import com.example.SpringBootTurialVip.dto.request.ProductDetailRequest;
import com.example.SpringBootTurialVip.dto.request.ProductUnderlyingConditionDTO;
import com.example.SpringBootTurialVip.dto.response.SKUResponse;
import com.example.SpringBootTurialVip.entity.ProductAgeGroup;
import com.example.SpringBootTurialVip.entity.ProductDetails;
import com.example.SpringBootTurialVip.enums.AgeGroup;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.*;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.entity.Product;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private ProductAgeGroupRepository productAgeGroupRepository;

//    @Override
//    public Product addProduct(Product product, List<MultipartFile> images) throws IOException {
//        if (productRepository.existsByTitle(product.getTitle())) {
//            throw new RuntimeException("Product already exists");
//        }
//
//        // Thiết lập các thuộc tính của product
//        product.setTitle(product.getTitle());
//        product.setCategory(product.getCategory());
//        product.setPrice(product.getPrice());
//        product.setQuantity(product.getQuantity());
//        product.setDescription(product.getDescription());
//        product.setDiscount(product.getDiscount());
//        product.setDiscountPrice(product.getDiscountPrice());
//        product.setIsActive(product.getIsActive());
//        product.setManufacturer(product.getManufacturer());
//        product.setSchedule(product.getSchedule());
//        product.setSideEffects(product.getSideEffects());
//        product.setAvailable(product.isAvailable());
//        product.setIsPriority(product.getIsPriority());
//        product.setNumberOfDoses(product.getNumberOfDoses());
//        product.setMinAgeMonths(product.getMinAgeMonths());
//        product.setMaxAgeMonths(product.getMaxAgeMonths());
//        product.setMinDaysBetweenDoses(product.getMinDaysBetweenDoses());
//
//        // Cập nhật lại targetGroup tự động từ minAgeMonths và maxAgeMonths
//        List<ProductAgeGroup> productAgeGroups = determineAgeGroups(product.getMinAgeMonths(), product.getMaxAgeMonths(), product);
//        product.setTargetGroup(productAgeGroups); // Gán ProductAgeGroup vào Product
//
//        // Lưu danh sách ảnh nếu có
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
//        return productRepository.save(product);
//    }
@Override
public Product addProduct(Product product, List<MultipartFile> images) throws IOException {
    // Kiểm tra nếu sản phẩm đã tồn tại
    if (productRepository.existsByTitle(product.getTitle())) {
        throw new RuntimeException("Tên sản phẩm đã tồn tại ");
    }

    // Tạo SKU cho sản phẩm
    product.generateSku();

    // Cập nhật targetGroup tự động từ minAgeMonths và maxAgeMonths
    updateAgeGroupFromAge(product);

    // Lưu danh sách ảnh nếu có
    if (images != null && !images.isEmpty()) {
        List<String> imageUrls = uploadImages(images); // Xử lý upload ảnh
        product.setImageList(imageUrls);  // Gán URL ảnh vào product
    }

    // Lưu sản phẩm vào cơ sở dữ liệu
    Product savedProduct = productRepository.save(product);  // Lưu Product trước

    // Cập nhật lại số lượng cho sản phẩm dựa trên các ProductDetails
    savedProduct.updateQuantities();  // Cập nhật quantity của Product

    // Lưu lại targetGroup (ProductAgeGroup) vào cơ sở dữ liệu
    saveTargetGroups(savedProduct);  // Lưu các nhóm độ tuổi vào ProductAgeGroup

    return savedProduct;
}


    // Phương thức để tự động cập nhật targetGroup từ minAgeMonths và maxAgeMonths
        private void updateAgeGroupFromAge(Product product) {
            List<ProductAgeGroup> productAgeGroups = determineAgeGroups(product.getMinAgeMonths(), product.getMaxAgeMonths(), product);
            product.setTargetGroup(productAgeGroups); // Gán ProductAgeGroup vào Product
        }

        // Phương thức để lưu targetGroup (ProductAgeGroup) vào cơ sở dữ liệu
        private void saveTargetGroups(Product product) {
            for (ProductAgeGroup ageGroup : product.getTargetGroup()) {
                ageGroup.setProduct(product);  // Liên kết lại Product với ProductAgeGroup
                productAgeGroupRepository.save(ageGroup);  // Lưu ProductAgeGroup vào cơ sở dữ liệu
            }
        }

        // Phương thức xử lý upload ảnh và trả về danh sách URL ảnh
        private List<String> uploadImages(List<MultipartFile> images) throws IOException {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                try {
                    String imageUrl = fileStorageService.uploadFile(image); // Giả sử bạn có dịch vụ uploadFile
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException("Error while uploading image: " + image.getOriginalFilename(), e);
                }
            }
            return imageUrls;
        }


    private List<ProductAgeGroup> determineAgeGroups(int minAgeMonths, int maxAgeMonths, Product product) {
        List<ProductAgeGroup> productAgeGroups = new ArrayList<>();

        // Kiểm tra nếu phạm vi độ tuổi bao trùm tất cả độ tuổi (AGE_ALL)
        if (minAgeMonths == 0 && maxAgeMonths == Integer.MAX_VALUE) {
            // Nếu phạm vi độ tuổi bao phủ tất cả, trả về AGE_ALL
            ProductAgeGroup productAgeGroup = new ProductAgeGroup(product, AgeGroup.AGE_ALL);
            productAgeGroups.add(productAgeGroup);
        } else {
            // Duyệt qua tất cả các giá trị của enum AgeGroup
            for (AgeGroup group : AgeGroup.values()) {
                // Kiểm tra sự giao nhau giữa phạm vi độ tuổi của sản phẩm và phạm vi độ tuổi của AgeGroup
                if (minAgeMonths <= group.getMaxMonth() && maxAgeMonths >= group.getMinMonth()) {
                    // Nếu có giao nhau, thêm vào danh sách các ProductAgeGroup
                    ProductAgeGroup productAgeGroup = new ProductAgeGroup(product, group);
                    productAgeGroups.add(productAgeGroup);
                }
            }
        }

        return productAgeGroups;
    }


    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            // Xóa tất cả các bản ghi liên quan trong ProductDetails và ProductAgeGroup
            // Xóa các liên kết liên quan tới sản phẩm từ bảng ProductDetails
            product.getProductDetails().forEach(productDetail -> {
                // Xóa các bản ghi trong ProductAgeGroup liên quan tới ProductDetails
                productDetail.getProductAgeGroups().forEach(productAgeGroup -> {
                    productAgeGroupRepository.delete(productAgeGroup);
                });
                // Sau đó, xóa ProductDetails
                productDetailsRepository.delete(productDetail);
            });

            // Xóa các bản ghi liên quan trong ProductAgeGroup (nếu có bất kỳ liên kết nào trực tiếp từ Product)
            product.getTargetGroup().forEach(productAgeGroup -> {
                productAgeGroupRepository.delete(productAgeGroup);
            });

            // Cuối cùng, xóa sản phẩm chính
            productRepository.delete(product);

            return true;
        }
        return false;
    }


    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public List<Product> getProductByTitle(String title) {
        return productRepository.findByTitle(title);
    }

//    @Override
//    public Product updateProduct(Product product, List<MultipartFile> images) {
//        // Lấy sản phẩm từ DB
//        Product dbProduct = productRepository.findById(product.getId())
//                .orElseThrow(() -> new RuntimeException("Product not found with id: " + product.getId()));
//
//        // Cập nhật thông tin sản phẩm
//        dbProduct.setTitle(product.getTitle());
//        dbProduct.setCategory(product.getCategory());
//        dbProduct.setPrice(product.getPrice());
//        dbProduct.setQuantity(product.getQuantity());
//        dbProduct.setDescription(product.getDescription());
//        dbProduct.setDiscount(product.getDiscount());
//        dbProduct.setDiscountPrice(product.getDiscountPrice());
//        dbProduct.setIsActive(product.getIsActive());
//        dbProduct.setManufacturer(product.getManufacturer());
//        dbProduct.setTargetGroup(product.getTargetGroup());
//        dbProduct.setSchedule(product.getSchedule());
//        dbProduct.setSideEffects(product.getSideEffects());
//        dbProduct.setAvailable(product.isAvailable());
//        dbProduct.setUpdatedAt(LocalDateTime.now());
//        dbProduct.setIsPriority(product.getIsPriority());
//        dbProduct.setNumberOfDoses(product.getNumberOfDoses());
//        dbProduct.setMinAgeMonths(product.getMinAgeMonths());
//        dbProduct.setMaxAgeMonths(product.getMaxAgeMonths());
//        dbProduct.setMinDaysBetweenDoses(product.getMinDaysBetweenDoses());
//
//        // Cập nhật lại targetGroup tự động từ minAgeMonths và maxAgeMonths
//        List<ProductAgeGroup> productAgeGroups = determineAgeGroups(dbProduct.getMinAgeMonths(), dbProduct.getMaxAgeMonths(), dbProduct);
//        dbProduct.setTargetGroup(productAgeGroups); // Gán ProductAgeGroup vào Product
//
//        // Kiểm tra giảm giá hợp lệ
//        if (dbProduct.getDiscount() < 0 || dbProduct.getDiscount() > 100) {
//            throw new IllegalArgumentException("Invalid discount percentage");
//        }
//
//        // Tính toán giá sau khi giảm
//        Double discountAmount = dbProduct.getPrice() * (dbProduct.getDiscount() / 100.0);
//        dbProduct.setDiscountPrice(dbProduct.getPrice() - discountAmount);
//
//        // Cập nhật danh sách ảnh nếu có ảnh mới
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
//                dbProduct.setImageList(imageUrls);
//            } catch (Exception e) {
//                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
//            }
//        }
//
//        // Lưu sản phẩm đã được cập nhật
//        return productRepository.save(dbProduct);
//    }

    @Override
    @Transactional
    public Product updateProduct(Product product, List<MultipartFile> images) {
        // Lấy sản phẩm từ DB
        Product dbProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + product.getId()));

        // Cập nhật thông tin sản phẩm
        if (product.getTitle() != null) dbProduct.setTitle(product.getTitle());
        if (product.getCategory() != null) dbProduct.setCategory(product.getCategory());
        if (product.getPrice() != null) dbProduct.setPrice(product.getPrice());
        if (product.getQuantity() != null) dbProduct.setQuantity(product.getQuantity());
        if (product.getDescription() != null) dbProduct.setDescription(product.getDescription());
        if (product.getDiscount() != null) dbProduct.setDiscount(product.getDiscount());
        if (product.getDiscountPrice() != null) dbProduct.setDiscountPrice(product.getDiscountPrice());
        if (product.getIsActive() != null) dbProduct.setIsActive(product.getIsActive());
        if (product.getManufacturer() != null) dbProduct.setManufacturer(product.getManufacturer());
        if (product.getSchedule() != null) dbProduct.setSchedule(product.getSchedule());
        if (product.getSideEffects() != null) dbProduct.setSideEffects(product.getSideEffects());
        if (product.getIsActive() != null) dbProduct.setAvailable(product.isAvailable());
        if (product.getIsPriority() != null) dbProduct.setIsPriority(product.getIsPriority());
        if (product.getNumberOfDoses() != null) dbProduct.setNumberOfDoses(product.getNumberOfDoses());
        if (product.getMinAgeMonths() != null) dbProduct.setMinAgeMonths(product.getMinAgeMonths());
        if (product.getMaxAgeMonths() != null) dbProduct.setMaxAgeMonths(product.getMaxAgeMonths());
        if (product.getMinDaysBetweenDoses() != null) dbProduct.setMinDaysBetweenDoses(product.getMinDaysBetweenDoses());

        // Cập nhật lại targetGroup tự động từ minAgeMonths và maxAgeMonths
        updateAgeGroupFromAge(dbProduct);  // Cập nhật lại targetGroup từ minAge và maxAge

        // Kiểm tra giảm giá hợp lệ
        if (dbProduct.getDiscount() < 0 || dbProduct.getDiscount() > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        // Tính toán giá sau khi giảm
        Double discountAmount = dbProduct.getPrice() * (dbProduct.getDiscount() / 100.0);
        dbProduct.setDiscountPrice(dbProduct.getPrice() - discountAmount);

        // Cập nhật danh sách ảnh nếu có ảnh mới
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

                dbProduct.setImageList(imageUrls);
            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        // Xóa các ProductAgeGroup cũ liên kết với sản phẩm
        productAgeGroupRepository.deleteByProductId(dbProduct.getId()); // Xóa tất cả ProductAgeGroup có liên kết với product_id

        // Lưu lại các ProductAgeGroup mới
        List<ProductAgeGroup> newAgeGroups = determineAgeGroups(dbProduct.getMinAgeMonths(), dbProduct.getMaxAgeMonths(), dbProduct);
        dbProduct.setTargetGroup(newAgeGroups); // Gán các ProductAgeGroup mới vào targetGroup
        productAgeGroupRepository.saveAll(newAgeGroups); // Lưu các ProductAgeGroup mới vào DB

        // Lưu lại Product đã cập nhật
        return productRepository.save(dbProduct);
    }





    @Override
    public List<Long> findInvalidProductIds(List<Long> productIds) {
        List<Long> existingProductIds = productRepository.findAllById(productIds)
                .stream().map(Product::getId).toList();

        return productIds.stream()
                .filter(id -> !existingProductIds.contains(id))
                .collect(Collectors.toList());
    }

    @Override
    // Kiểm tra xem có sản phẩm nào bị thiếu hàng không
    public List<Long> findOutOfStockProducts(List<Long> productIds, List<Integer> quantities) {
        List<Product> products = productRepository.findAllById(productIds);

        // Tạo map chứa productId -> stock từ database
        Map<Long, Integer> stockMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getQuantity));

        // Lọc ra danh sách productId nào có stock < quantity yêu cầu
        return productIds.stream()
                .filter(id -> stockMap.getOrDefault(id, 0) < quantities.get(productIds.indexOf(id)))
                .collect(Collectors.toList());
    }

    //============================================================Lô hàng ========================================


    private void updateProductQuantity(Product product) {
        // Tính tổng số lượng từ tất cả các ProductDetails liên kết với Product
        int totalQuantity = product.getProductDetails().stream()
                .filter(ProductDetails::getIsActive)  // Chỉ tính số lượng các lô hàng đang hoạt động
                .mapToInt(ProductDetails::getQuantity)
                .sum();

        // Cập nhật lại số lượng trong Product
        product.setQuantity(totalQuantity);

        // Lưu lại Product với số lượng mới
        productRepository.save(product);
    }


//    @Override
//    public ProductDetails createProductDetails(Long productId, ProductDetailCreateRequest productDetailRequest) {
//        // Tìm sản phẩm trong DB
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
//
//        // Tạo ProductDetails mới
//        ProductDetails productDetails = new ProductDetails();
//        productDetails.setSku(productDetailRequest.getSku());
//        productDetails.setBatchNumber(productDetailRequest.getBatchNumber());
//        productDetails.setExpirationDate(LocalDate.parse(productDetailRequest.getExpirationDate())); // Convert từ String sang LocalDate
//        productDetails.setQuantity(productDetailRequest.getQuantity());
//        productDetails.setReservedQuantity(productDetailRequest.getReservedQuantity());
//        productDetails.setIsActive(productDetailRequest.getIsActive());
//
//        // Liên kết ProductDetails với Product
//        productDetails.setProduct(product);  // Liên kết với product_id
//
//        // Lưu ProductDetails
//        ProductDetails savedProductDetails = productDetailsRepository.save(productDetails);
//
//        // Cập nhật lại số lượng của Product sau khi thêm ProductDetails
//        product.updateQuantities();  // Cập nhật lại quantity và reservedQuantity của Product
//        productRepository.save(product);  // Lưu Product với số lượng đã cập nhật
//
//        return savedProductDetails;
//    }
@Override
public ProductDetails createProductDetails(Long productId, ProductDetailCreateRequest productDetailRequest) {
    // Tìm sản phẩm trong DB
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

    // Tạo ProductDetails mới
    ProductDetails productDetails = new ProductDetails();

    // Lấy SKU từ sản phẩm (tự động lấy SKU của sản phẩm theo định dạng "SKU-{productId}-VAX")
    productDetails.setSku(product.getSku());  // Lấy SKU của sản phẩm

    // Gán thông tin batch number, expiration date, quantity cho lô hàng mới
    productDetails.setBatchNumber(productDetailRequest.getBatchNumber());
    productDetails.setExpirationDate(LocalDate.parse(productDetailRequest.getExpirationDate())); // Convert từ String sang LocalDate
    productDetails.setQuantity(productDetailRequest.getQuantity());
    productDetails.setReservedQuantity(0);
    productDetails.setIsActive(true);

    // Liên kết ProductDetails với Product
    productDetails.setProduct(product);  // Liên kết với product_id

    // Lưu ProductDetails
    ProductDetails savedProductDetails = productDetailsRepository.save(productDetails);

    // Cập nhật lại số lượng của Product sau khi thêm ProductDetails
    product.updateQuantities();  // Cập nhật lại quantity và reservedQuantity của Product
    productRepository.save(product);  // Lưu Product với số lượng đã cập nhật

    return savedProductDetails;
}




    public List<ProductDetails> getProductDetailsByProductId(Long productId) {
        return productDetailsRepository.findByProductId(productId);
    }


    @Override
    public ProductDetails updateProductDetails(Long id, ProductDetailCreateRequest productDetailRequest) {
        // Tìm ProductDetails theo ID
        ProductDetails productDetails = productDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductDetails not found with ID: " + id));

        // Cập nhật các thông tin chỉ nếu có giá trị mới
        if (productDetailRequest.getBatchNumber() != null)
            productDetails.setBatchNumber(productDetailRequest.getBatchNumber());
        // Chỉ cập nhật expirationDate nếu có giá trị mới
        if (productDetailRequest.getExpirationDate() != null)
            productDetails.setExpirationDate(LocalDate.parse(productDetailRequest.getExpirationDate()));
        // Chỉ cập nhật quantity nếu có giá trị mới
        if (productDetailRequest.getQuantity() != null)
            productDetails.setQuantity(productDetailRequest.getQuantity());

        // Nếu bạn có các trường khác, kiểm tra như sau:
        // if (productDetailRequest.getReservedQuantity() != null)
        //     productDetails.setReservedQuantity(productDetailRequest.getReservedQuantity());

        // Lưu lại ProductDetails đã được cập nhật
        ProductDetails updatedProductDetails = productDetailsRepository.save(productDetails);

        // Cập nhật lại số lượng của Product sau khi cập nhật ProductDetails
        updateProductQuantity(productDetails.getProduct());

        return updatedProductDetails;
    }

    @Override
    public boolean deleteProductDetails(Long id) {
        // Tìm ProductDetails theo ID
        ProductDetails productDetails = productDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductDetails not found with ID: " + id));

        // Xóa ProductDetails
        productDetailsRepository.delete(productDetails);

        // Cập nhật lại số lượng của Product sau khi xóa ProductDetails
        updateProductQuantity(productDetails.getProduct());

        return true;  // Trả về true khi xóa thành công
    }


    // ========================= BỆNH NỀN =========================

    // Thêm bệnh nền cho sản phẩm (request đơn giản, response đầy đủ thông tin)
    @Override
    public ProductUnderlyingConditionDTO addUnderlyingConditionToProduct(Long productId, String condition) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean exists = product.getUnderlyingConditions().stream()
                .anyMatch(c -> c.equalsIgnoreCase(condition));

        if (exists) {
            throw new RuntimeException("Sản phẩm đã có bệnh nền này rồi.");
        }

        product.getUnderlyingConditions().add(condition);
        productRepository.save(product);

        return toDto(product, condition);
    }

    // Cập nhật bệnh nền trong sản phẩm (request đơn giản, response đầy đủ thông tin)
    @Override
    public ProductUnderlyingConditionDTO updateUnderlyingConditionForProduct(Long productId,
                                                                            // String condition,
                                                                             String newCondition) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String oldCondtition=product.getUnderlyingConditions().get(0);

        int index = -1;
        for (int i = 0; i < product.getUnderlyingConditions().size(); i++) {
            if (product.getUnderlyingConditions().get(i).equalsIgnoreCase(oldCondtition)) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            product.getUnderlyingConditions().set(index, newCondition);
        } else {
            throw new RuntimeException("Không tìm thấy bệnh nền cần cập nhật");
        }



        productRepository.save(product);
        return toDto(product, newCondition);
    }

    // Xóa bệnh nền khỏi sản phẩm
    @Override
    public ProductUnderlyingConditionDTO removeUnderlyingConditionFromProduct(Long productId, String condition) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean removed = product.getUnderlyingConditions().removeIf(c -> c.equalsIgnoreCase(condition));

        if (!removed) {
            throw new RuntimeException("Không tìm thấy bệnh nền để xóa");
        }

        productRepository.save(product);
        return toDto(product, condition);
    }

    // Lấy danh sách bệnh nền của sản phẩm (giữ nguyên dạng List<String>)
    @Override
    public List<String> getConditionsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return product.getUnderlyingConditions();
    }

    @Override
    public List<SKUResponse> getAllSKUs(String sku, String batch) {
        // Nếu không có tham số sku và batch, trả về tất cả SKU
        if (sku == null && batch == null) {
            return productDetailsRepository.findAll().stream()
                    .map(productDetail -> new SKUResponse(
                            productDetail.getId(),
                            productDetail.getProduct().getTitle(),
                            productDetail.getSku(),
                            productDetail.getBatchNumber(),
                            productDetail.getExpirationDate(),
                            productDetail.getProduct().getId()
                    ))
                    .collect(Collectors.toList());
        }

        // Nếu có tham số sku và batch, tìm kiếm theo cả hai
        if (sku != null && batch != null) {
            return productDetailsRepository.findBySkuAndBatchNumber(sku, batch).stream()
                    .map(productDetail -> new SKUResponse(
                            productDetail.getId(),
                            productDetail.getProduct().getTitle(),
                            productDetail.getSku(),
                            productDetail.getBatchNumber(),
                            productDetail.getExpirationDate(),
                            productDetail.getProduct().getId()
                    ))
                    .collect(Collectors.toList());
        }

        // Nếu chỉ có sku, tìm kiếm theo sku và trả về tất cả các bản ghi phù hợp với sku (dùng findBySkuList)
        if (sku != null) {
            return productDetailsRepository.findBySku(sku).stream()
                    .map(productDetail -> new SKUResponse(
                            productDetail.getId(),
                            productDetail.getProduct().getTitle(),
                            productDetail.getSku(),
                            productDetail.getBatchNumber(),
                            productDetail.getExpirationDate(),
                            productDetail.getProduct().getId()
                    ))
                    .collect(Collectors.toList());
        }

        // Nếu chỉ có batch, tìm kiếm theo batch
        if (batch != null) {
            return productDetailsRepository.findByBatchNumber(batch).stream()
                    .map(productDetail -> new SKUResponse(
                            productDetail.getId(),
                            productDetail.getProduct().getTitle(),
                            productDetail.getSku(),
                            productDetail.getBatchNumber(),
                            productDetail.getExpirationDate(),
                            productDetail.getProduct().getId()
                    ))
                    .collect(Collectors.toList());
        }

        // Tìm kiếm theo cả sku hoặc batch
        if (sku != null || batch != null) {
            return productDetailsRepository.findBySkuOrBatch(sku, batch).stream()
                    .map(productDetail -> new SKUResponse(
                            productDetail.getId(),
                            productDetail.getProduct().getTitle(),
                            productDetail.getSku(),
                            productDetail.getBatchNumber(),
                            productDetail.getExpirationDate(),
                            productDetail.getProduct().getId()
                    ))
                    .collect(Collectors.toList());
        }

        return null;
    }


    // Hàm tiện ích để chuyển từ entity sang DTO đầy đủ
    private ProductUnderlyingConditionDTO toDto(Product product, String condition) {
        return new ProductUnderlyingConditionDTO(
                product.getId(),
                product.getTitle(),
                condition,
                "Không có mô tả"
        );
    }
    //Cron job quản lí hsd => quantity
    @Scheduled(cron = "0 0 0 * * ?")  // Chạy mỗi ngày lúc 12:00 AM
    public void updateExpiredProducts() {
        LocalDate currentDate = LocalDate.now();
        List<ProductDetails> expiredProducts = productDetailsRepository.findExpiredProducts(currentDate);

        for (ProductDetails productDetails : expiredProducts) {
            // Cập nhật trạng thái của sản phẩm hết hạn
            Product product = productDetails.getProduct();
            product.setIsActive(false);  // Đánh dấu sản phẩm là không còn hoạt động nữa

            // Giảm số lượng sản phẩm nếu cần
            int currentQuantity = product.getQuantity();
            int remainingQuantity = Math.max(0, currentQuantity - productDetails.getQuantity());  // Giảm số lượng

            product.setQuantity(remainingQuantity);
            productRepository.save(product);  // Lưu lại sản phẩm sau khi cập nhật
        }
    }




}
