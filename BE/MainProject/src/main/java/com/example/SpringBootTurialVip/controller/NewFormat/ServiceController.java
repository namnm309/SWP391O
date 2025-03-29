package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.response.LiteChildResponse;
import com.example.SpringBootTurialVip.dto.response.ProductSuggestionDTO;
import com.example.SpringBootTurialVip.dto.response.ServiceSuggestionResponse;
import com.example.SpringBootTurialVip.entity.Product;
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
import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("/services/by-age-group")
    @Operation(summary = "Gợi ý vaccine + bé theo nhóm tuổi", description = "Nếu đã đăng nhập sẽ trả thêm danh sách bé phù hợp và số mũi còn lại.")
    public ResponseEntity<ServiceSuggestionResponse> getServiceByAgeGroup(@RequestParam AgeGroup ageGroup) {
        ServiceSuggestionResponse response = new ServiceSuggestionResponse();
        response.setAgeGroup(ageGroup.name());
        response.setAgeGroupLabel(ageGroup.getLabel());

        List<Product> products = productRepository.findByTargetGroup(ageGroup);

        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long parentId = jwt.getClaim("id");

            List<User> children = userRepository.findByParentid(parentId);
            List<LiteChildResponse> matched = new ArrayList<>();

            for (User child : children) {
                if (child.getBod() == null) continue;
                Period period = Period.between(child.getBod(), LocalDate.now());
                int age = period.getYears() * 12 + period.getMonths();

                if (age >= ageGroup.getMinMonth() && age <= ageGroup.getMaxMonth()) {
                    LiteChildResponse dto = new LiteChildResponse();
                    dto.setId(child.getId());
                    dto.setName(child.getFullname());
                    dto.setAgeInMonths(age);
                    matched.add(dto);
                }
            }
            response.setMatchingChildren(matched);

            List<ProductSuggestionDTO> suggestionList = new ArrayList<>();
            for (Product p : products) {
                int totalRemaining = 0;
                for (LiteChildResponse child : matched) {
                    int taken = orderDetailRepository.countDosesTaken(p.getId(), child.getId());
                    int remaining = Math.max(0, p.getNumberOfDoses() - taken);
                    totalRemaining += remaining;
                }

                ProductSuggestionDTO dto = new ProductSuggestionDTO();
                dto.setId(p.getId());
                dto.setTitle(p.getTitle());
                dto.setTargetGroup(p.getTargetGroup().name());
                dto.setNumberOfDoses(p.getNumberOfDoses());
                dto.setRemainingDoses(totalRemaining);
                suggestionList.add(dto);
            }
            response.setVaccines(suggestionList);

        } catch (Exception e) {
            List<ProductSuggestionDTO> basicList = products.stream().map(p -> {
                ProductSuggestionDTO dto = new ProductSuggestionDTO();
                dto.setId(p.getId());
                dto.setTitle(p.getTitle());
                dto.setTargetGroup(p.getTargetGroup().name());
                dto.setNumberOfDoses(p.getNumberOfDoses());
                dto.setRemainingDoses(0);
                return dto;
            }).toList();

            response.setVaccines(basicList);
            response.setMatchingChildren(List.of());
        }

        return ResponseEntity.ok(response);
    }

}
