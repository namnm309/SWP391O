package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.response.LiteChildResponse;
import com.example.SpringBootTurialVip.dto.response.ProductSuggestionDTO;
import com.example.SpringBootTurialVip.dto.response.ServiceSuggestionResponse;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.UnderlyingCondition;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.enums.AgeGroup;
import com.example.SpringBootTurialVip.repository.OrderDetailRepository;
import com.example.SpringBootTurialVip.repository.ProductRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
@Tag(name="[Service]",description = "")
public class ServiceController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


//    @GetMapping("/services/by-age-group")
//    @Operation(summary = "Gợi ý vaccine + bé theo nhóm tuổi",
//            description = "Nếu đã đăng nhập sẽ trả thêm danh sách bé phù hợp và số mũi còn lại.")
//    public ResponseEntity<ServiceSuggestionResponse> getServiceByAgeGroup(@RequestParam AgeGroup ageGroup) {
//        ServiceSuggestionResponse response = new ServiceSuggestionResponse();
//        response.setAgeGroup(ageGroup.name());
//        response.setAgeGroupLabel(ageGroup.getLabel());
//
//        // Lọc sản phẩm theo targetGroup
//        List<Product> products = productRepository.findByTargetGroup(ageGroup);
//
//        try {
//            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            Long parentId = jwt.getClaim("id");
//
//            List<User> children = userRepository.findByParentid(parentId);
//            List<LiteChildResponse> matched = new ArrayList<>();
//
//            // Lọc các trẻ em phù hợp với nhóm tuổi đã chọn
//            for (User child : children) {
//                if (child.getBod() == null) continue;
//                Period period = Period.between(child.getBod(), LocalDate.now());
//                int age = period.getYears() * 12 + period.getMonths();
//
//                // Kiểm tra nếu độ tuổi của bé nằm trong nhóm tuổi đã chọn
//                if (age >= ageGroup.getMinMonth() && age <= ageGroup.getMaxMonth()) {
//                    LiteChildResponse dto = new LiteChildResponse();
//                    dto.setId(child.getId());
//                    dto.setName(child.getFullname());
//                    dto.setAgeInMonths(age);
//                    matched.add(dto);
//                }
//            }
//            response.setMatchingChildren(matched);
//
//            List<ProductSuggestionDTO> suggestionList = new ArrayList<>();
//            try {
//                for (Product p : products) {
//                    int totalRemaining = 0;
//
//                    // Tính tổng số liều còn lại cho mỗi sản phẩm và từng đứa trẻ
//                    for (LiteChildResponse child : matched) {
//                        int taken = orderDetailRepository.countDosesTaken(p.getId(), child.getId());
//                        int remaining = Math.max(0, p.getNumberOfDoses() - taken);
//                        totalRemaining += remaining;
//                    }
//
//                    // Tạo đối tượng DTO để trả về thông tin sản phẩm
//                    ProductSuggestionDTO dto = new ProductSuggestionDTO();
//                    dto.setId(p.getId());
//                    dto.setTitle(p.getTitle());
//
//                    // Xử lý targetGroup nếu là List<AgeGroup>
//                    if (p.getTargetGroup() != null && !p.getTargetGroup().isEmpty()) {
//                        // Kiểm tra nếu sản phẩm phù hợp với bất kỳ nhóm tuổi nào trong targetGroup
//                        String targetGroups = p.getTargetGroup().stream()
//                                .map(AgeGroup::name)  // Lấy tên nhóm tuổi
//                                .collect(Collectors.joining(", "));  // Ghép các nhóm tuổi lại thành một chuỗi
//                        dto.setTargetGroup(targetGroups);
//                    } else {
//                        dto.setTargetGroup("No target group available");  // Nếu không có targetGroup
//                    }
//
//                    dto.setNumberOfDoses(p.getNumberOfDoses());
//                    dto.setRemainingDoses(totalRemaining);
//                    suggestionList.add(dto);
//                }
//
//                response.setVaccines(suggestionList);
//
//            } catch (Exception e) {
//                // Xử lý lỗi nếu có
//                List<ProductSuggestionDTO> basicList = products.stream()
//                        .map(p -> {
//                            ProductSuggestionDTO dto = new ProductSuggestionDTO();
//                            dto.setId(p.getId());
//                            dto.setTitle(p.getTitle());
//
//                            // Nếu không có targetGroup hợp lệ, gán giá trị mặc định
//                            dto.setTargetGroup("No target group available");
//
//                            dto.setNumberOfDoses(p.getNumberOfDoses());
//                            dto.setRemainingDoses(0);  // Nếu có lỗi, không có liều còn lại
//                            return dto;
//                        })
//                        .collect(Collectors.toList());  // Thay .toList() để tương thích với các phiên bản Java trước 16
//
//                response.setVaccines(basicList);
//                response.setMatchingChildren(List.of());  // Nếu có lỗi, trả về danh sách trống cho matching children
//            }
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            // Xử lý lỗi ngoài các phần bên trong
//            response.setVaccines(new ArrayList<>());
//            response.setMatchingChildren(List.of());  // Trả về danh sách trống nếu có lỗi ngoài
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
    //==============================================================================
//@GetMapping("/services/by-age-group")
//@Operation(summary = "Gợi ý vaccine + bé theo nhóm tuổi", description = "Nếu đã đăng nhập sẽ trả thêm danh sách bé phù hợp và số mũi còn lại.")
//public ResponseEntity<ServiceSuggestionResponse> getServiceByAgeGroup(@RequestParam AgeGroup ageGroup) {
//    ServiceSuggestionResponse response = new ServiceSuggestionResponse();
//    response.setAgeGroup(ageGroup.name());
//    response.setAgeGroupLabel(ageGroup.getLabel());
//
//    // Lọc sản phẩm theo targetGroup
//    List<Product> products = productRepository.findByTargetGroup(ageGroup);
//
//    try {
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long parentId = jwt.getClaim("id");
//
//        List<User> children = userRepository.findByParentid(parentId);
//        List<LiteChildResponse> matchedChildren = new ArrayList<>();
//
//        // Lọc các trẻ em phù hợp với nhóm tuổi đã chọn
//        for (User child : children) {
//            if (child.getBod() == null) continue;
//            int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
//                    + Period.between(child.getBod(), LocalDate.now()).getMonths();
//
//            if (ageInMonths >= ageGroup.getMinMonth() && ageInMonths <= ageGroup.getMaxMonth()) {
//                LiteChildResponse childDto = new LiteChildResponse();
//                childDto.setId(child.getId());
//                childDto.setName(child.getFullname());
//                childDto.setAgeInMonths(ageInMonths);
//                matchedChildren.add(childDto);
//            }
//        }
//
//        response.setMatchingChildren(matchedChildren);
//
//        List<ProductSuggestionDTO> suggestionList = new ArrayList<>();
//
//        // Xử lý các sản phẩm vaccine gợi ý cho từng trẻ em
//        for (Product p : products) {
//            int totalRemainingDoses = 0;
//
//            // Tính tổng số liều còn lại cho mỗi sản phẩm và từng đứa trẻ
//            for (LiteChildResponse child : matchedChildren) {
//                int takenDoses = orderDetailRepository.countDosesTaken(p.getId(), child.getId());
//                int remainingDoses = Math.max(0, p.getNumberOfDoses() - takenDoses);
//                totalRemainingDoses += remainingDoses;
//            }
//
//            // Tạo DTO cho sản phẩm gợi ý
//            ProductSuggestionDTO dto = new ProductSuggestionDTO();
//            dto.setId(p.getId());
//            dto.setTitle(p.getTitle());
//
//            // Kiểm tra nếu sản phẩm phù hợp với bất kỳ nhóm tuổi nào trong targetGroup
//            String targetGroups = p.getTargetGroup().stream()
//                    .map(pg -> pg.getAgeGroup().name()) // Lấy tên của AgeGroup từ ProductAgeGroup
//                    .collect(Collectors.joining(", "));
//            dto.setTargetGroup(targetGroups);
//
//            dto.setNumberOfDoses(p.getNumberOfDoses());
//            dto.setRemainingDoses(totalRemainingDoses);
//            suggestionList.add(dto);
//        }
//
//        response.setVaccines(suggestionList);
//
//    } catch (Exception e) {
//        // Xử lý lỗi nếu có
//        List<ProductSuggestionDTO> defaultList = products.stream()
//                .map(p -> {
//                    ProductSuggestionDTO dto = new ProductSuggestionDTO();
//                    dto.setId(p.getId());
//                    dto.setTitle(p.getTitle());
//                    dto.setTargetGroup("No target group available");
//                    dto.setNumberOfDoses(p.getNumberOfDoses());
//                    dto.setRemainingDoses(0); // Nếu có lỗi, không có mũi tiêm còn lại
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        response.setVaccines(defaultList);
//        response.setMatchingChildren(List.of()); // Không có trẻ em phù hợp trong trường hợp lỗi
//    }
//
//    return ResponseEntity.ok(response);
//}


    //===============================================================
    @GetMapping("/services/by-age-group")
    @Operation(summary = "Gợi ý vaccine + bé theo nhóm tuổi",
            description = "Nếu đã đăng nhập sẽ trả thêm danh sách bé phù hợp và số mũi còn lại.")
    public ResponseEntity<ServiceSuggestionResponse> getServiceByAgeGroup(@RequestParam AgeGroup ageGroup) {

        ServiceSuggestionResponse response = new ServiceSuggestionResponse();
        response.setAgeGroup(ageGroup.name());
        response.setAgeGroupLabel(ageGroup.getLabel());

        // Lọc sản phẩm theo targetGroup
        List<Product> products = productRepository.findByTargetGroup(ageGroup);

        try {
            Jwt jwt  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long parentId = jwt.getClaim("id");

            List<User> children = userRepository.findByParentid(parentId);
            List<LiteChildResponse> matchedChildren = new ArrayList<>();

            // ==== 1. Lọc các bé phù hợp nhóm tuổi ===================================================
            for (User child : children) {
                if (child.getBod() == null) continue;
                int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
                        + Period.between(child.getBod(), LocalDate.now()).getMonths();
                if (ageInMonths >= ageGroup.getMinMonth() && ageInMonths <= ageGroup.getMaxMonth()) {
                    LiteChildResponse dto = new LiteChildResponse();
                    dto.setId(child.getId());
                    dto.setName(child.getFullname());
                    dto.setAgeInMonths(ageInMonths);
                    matchedChildren.add(dto);
                }
            }
            response.setMatchingChildren(matchedChildren);

            // 🔹 map childId → set bệnh nền
            Map<Long, Set<String>> childCondMap = children.stream()
                    .collect(Collectors.toMap(User::getId,
                            c -> c.getUnderlyingConditions()
                                    .stream()
                                    .map(UnderlyingCondition::getConditionName)
                                    .filter(Objects::nonNull)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.toSet())));

            List<ProductSuggestionDTO> suggestionList = new ArrayList<>();

            // ==== 2. Xử lý từng vaccine =============================================================
            for (Product p : products) {

                // 🔹 Kiểm tra xung đột bệnh nền
                boolean compatibleWithAnyChild = p.getUnderlyingConditions().isEmpty();
                if (!compatibleWithAnyChild) {
                    for (LiteChildResponse child : matchedChildren) {
                        Set<String> conds = childCondMap.getOrDefault(child.getId(), Set.of());
                        boolean conflict = p.getUnderlyingConditions()
                                .stream()
                                .anyMatch(c -> conds.contains(c.toLowerCase()));
                        if (!conflict) {        // bé này không xung đột
                            compatibleWithAnyChild = true;
                            break;
                        }
                    }
                }
                if (!compatibleWithAnyChild) continue; // bỏ vaccine xung đột với tất cả bé

                int totalRemainingDoses = 0;
                for (LiteChildResponse child : matchedChildren) {
                    int taken = orderDetailRepository.countDosesTaken(p.getId(), child.getId());
                    totalRemainingDoses += Math.max(0, p.getNumberOfDoses() - taken);
                }

                ProductSuggestionDTO dto = new ProductSuggestionDTO();
                dto.setId(p.getId());
                dto.setTitle(p.getTitle());
                dto.setTargetGroup(
                        p.getTargetGroup().stream()
                                .map(pg -> pg.getAgeGroup().name())
                                .collect(Collectors.joining(", "))
                );
                dto.setNumberOfDoses(p.getNumberOfDoses());
                dto.setRemainingDoses(totalRemainingDoses);
                suggestionList.add(dto);
            }

            response.setVaccines(suggestionList);

        } catch (Exception e) {
            // fallback nếu lỗi
            List<ProductSuggestionDTO> defaultList = products.stream().map(p -> {
                ProductSuggestionDTO dto = new ProductSuggestionDTO();
                dto.setId(p.getId());
                dto.setTitle(p.getTitle());
                dto.setTargetGroup("No target group available");
                dto.setNumberOfDoses(p.getNumberOfDoses());
                dto.setRemainingDoses(0);
                return dto;
            }).collect(Collectors.toList());

            response.setVaccines(defaultList);
            response.setMatchingChildren(List.of());
        }

        return ResponseEntity.ok(response);
    }





}
