package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ProductUnderlyingConditionDTO;
import com.example.SpringBootTurialVip.dto.request.ProductUnderlyingConditionRequestDTO;
import com.example.SpringBootTurialVip.dto.request.UnderlyingConditionRequestDTO;
import com.example.SpringBootTurialVip.dto.response.ChildHealthInfoDTO;
import com.example.SpringBootTurialVip.dto.response.UnderlyingConditionResponseDTO;
import com.example.SpringBootTurialVip.dto.response.UserConditionInfoDTO;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.repository.ProductRepository;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.UnderlyingConditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name="[UnderlyingConditions]",description = "")
@RestController
@RequestMapping("/underlying-conditions")
public class UnderlyingConditionController {

    @Autowired
    private UnderlyingConditionService underlyingConditionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // ============================================== USER =======================

    @Operation(summary = "Thêm bệnh nền cho người dùng")
    @PostMapping("/user/{userId}")
    public UnderlyingConditionResponseDTO addConditionToUser(@PathVariable Long userId,
                                                             @RequestBody UnderlyingConditionRequestDTO dto) {
        return underlyingConditionService.addConditionToUser(userId, dto);
    }

    @Operation(summary = "Cập nhật bệnh nền cho người dùng")
    @PutMapping("/user/{userId}/{conditionId}")
    public UnderlyingConditionResponseDTO updateConditionForUser(@PathVariable Long userId,
                                                                 @PathVariable Long conditionId,
                                                                 @RequestBody UnderlyingConditionRequestDTO dto) {
        return underlyingConditionService.updateConditionForUser(userId, conditionId, dto);
    }

    @Operation(summary = "Xoá bệnh nền khỏi người dùng")
    @DeleteMapping("/user/{userId}/{conditionId}")
    public void removeConditionFromUser(@PathVariable Long userId,
                                        @PathVariable Long conditionId) {
        underlyingConditionService.removeConditionFromUser(userId, conditionId);
    }

    @Operation(summary = "Lấy bệnh nền + thông tin bé & phụ huynh")
    @GetMapping("/user/{userId}")
    public UserConditionInfoDTO getConditionInfoByUser(@PathVariable Long userId) {
        return underlyingConditionService.getConditionsByUser(userId);
    }

    @Operation(summary = "Thông tin sức khoẻ + vaccine phù hợp của bé")
    @GetMapping("/user/{childId}/full-info")
    public ChildHealthInfoDTO getChildFullInfo(@PathVariable Long childId) {
        return underlyingConditionService.getChildFullInfo(childId);
    }



    // =================================================== PRODUCT =====================================

    @Operation(summary = "Thêm bệnh nền vào sản phẩm")
    @PostMapping("/product/{productId}")
    public ProductUnderlyingConditionDTO addConditionToProduct(
            @PathVariable Long productId,
            @RequestBody ProductUnderlyingConditionRequestDTO dto) {
        return productService.addUnderlyingConditionToProduct(productId, dto.getCondition());
    }


    @Operation(summary = "Cập nhật bệnh nền trong sản phẩm")
    @PutMapping("/product/{productId}")
    public ProductUnderlyingConditionDTO updateConditionForProduct(
            @PathVariable Long productId,
          //  @PathVariable String condition,
            @RequestBody ProductUnderlyingConditionRequestDTO dto) {
        return productService.updateUnderlyingConditionForProduct(productId,dto.getCondition());
    }


    @Operation(summary = "Xoá bệnh nền khỏi sản phẩm")
    @DeleteMapping("/product/{productId}/{condition}")
    public ProductUnderlyingConditionDTO removeConditionFromProduct(@PathVariable Long productId,
                                                                    @PathVariable String condition) {
        return productService.removeUnderlyingConditionFromProduct(productId, condition);
    }

    @Operation(summary = "Lấy danh sách bệnh nền theo sản phẩm")
    @GetMapping("/product/{productId}")
    public List<ProductUnderlyingConditionDTO> getConditionsByProduct(@PathVariable Long productId) {

        // Lấy sản phẩm một lần
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String productName = product.getTitle();      // tên sản phẩm
        String defaultDesc = null;                    // hoặc mô tả mặc định

        return product.getUnderlyingConditions()      // danh sách String
                .stream()
                .map(cond -> new ProductUnderlyingConditionDTO(
                        productId,
                        productName,
                        cond,
                        defaultDesc))
                .collect(Collectors.toList());
    }


    @Operation(summary = "Lấy một bệnh nền cụ thể trong sản phẩm")
    @GetMapping("/product/{productId}/{condition}")
    public ProductUnderlyingConditionDTO getConditionByProduct(@PathVariable Long productId,
                                                               @PathVariable String condition) {
        List<String> conditions = productService.getConditionsByProduct(productId);
        return conditions.stream()
                .filter(c -> c.equalsIgnoreCase(condition))
                .findFirst()
                .map(c -> new ProductUnderlyingConditionDTO(productId, null, c, null))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nền này trong sản phẩm"));
    }
}
