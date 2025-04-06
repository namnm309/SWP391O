package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.UnderlyingConditionRequestDTO;
import com.example.SpringBootTurialVip.dto.response.ChildHealthInfoDTO;
import com.example.SpringBootTurialVip.dto.response.ProductSuggestionDTO;
import com.example.SpringBootTurialVip.dto.response.UnderlyingConditionResponseDTO;
import com.example.SpringBootTurialVip.dto.response.UserConditionInfoDTO;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.UnderlyingCondition;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.OrderDetailRepository;
import com.example.SpringBootTurialVip.repository.ProductRepository;
import com.example.SpringBootTurialVip.repository.UnderlyingConditionRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.UnderlyingConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UnderlyingConditionServiceImpl implements UnderlyingConditionService {

    @Autowired
    private UnderlyingConditionRepository conditionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public UnderlyingConditionResponseDTO addConditionToUser(Long userId, UnderlyingConditionRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (conditionRepository.existsByUserIdAndConditionName(userId, dto.getConditionName())) {
            throw new RuntimeException("Người dùng đã có bệnh nền này rồi.");
        }

        UnderlyingCondition condition = new UnderlyingCondition();
        condition.setConditionName(dto.getConditionName());
        condition.setConditionDescription(dto.getNote());
        condition.setUser(user);

        UnderlyingCondition saved = conditionRepository.save(condition);
        return mapToResponse(saved);
    }

    @Override
    public UnderlyingConditionResponseDTO updateConditionForUser(Long userId, Long conditionId, UnderlyingConditionRequestDTO dto) {
        UnderlyingCondition condition = conditionRepository.findById(conditionId)
                .orElseThrow(() -> new RuntimeException("Condition not found"));

        if (!condition.getUser().getId().equals(userId)) {
            throw new RuntimeException("Condition does not belong to user");
        }

        condition.setConditionName(dto.getConditionName());
        condition.setConditionDescription(dto.getNote());

        UnderlyingCondition updated = conditionRepository.save(condition);
        return mapToResponse(updated);
    }

    @Override
    public void removeConditionFromUser(Long userId, Long conditionId) {
        UnderlyingCondition condition = conditionRepository.findById(conditionId)
                .orElseThrow(() -> new RuntimeException("Condition not found"));

        if (!condition.getUser().getId().equals(userId)) {
            throw new RuntimeException("Condition does not belong to user");
        }

        conditionRepository.delete(condition);
    }

    @Override
    public UserConditionInfoDTO getConditionsByUser(Long userId) {
        User child = userRepository.findByIdDirect(userId);
        if (child == null) throw new RuntimeException("Child not found");

        User parent = userRepository.findByIdDirect(child.getParentid());

        int ageMonths = Period.between(child.getBod(), LocalDate.now()).getYears()*12
                + Period.between(child.getBod(), LocalDate.now()).getMonths();

        List<UnderlyingConditionResponseDTO> condDtos =
                conditionRepository.findByUserId(userId)
                        .stream()
                        .map(this::mapToResponse)   // hàm map đã có
                        .collect(Collectors.toList());

        UserConditionInfoDTO dto = new UserConditionInfoDTO();
        dto.setChildId(child.getId());
        dto.setChildName(child.getFullname());
        dto.setChildAgeMonths(ageMonths);

        if (parent != null) {
            dto.setParentId(parent.getId());
            dto.setParentName(parent.getFullname());
        }

        dto.setConditions(condDtos);
        return dto;
    }

    @Override
    public List<UnderlyingConditionResponseDTO> getUserUnderlyingConditions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUnderlyingConditions()
                .stream()
                .map(condition -> {
                    UnderlyingConditionResponseDTO dto = new UnderlyingConditionResponseDTO();
                    dto.setId(condition.getId());
                    dto.setConditionName(condition.getConditionName());
                    dto.setNote(condition.getConditionDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChildHealthInfoDTO getChildFullInfo(Long childId) {

        User child = userRepository.findByIdDirect(childId);
        if (child == null) throw new RuntimeException("Child not found");

        int ageMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
                + Period.between(child.getBod(), LocalDate.now()).getMonths();

        // ==== 1. bệnh nền ====
        List<UnderlyingConditionResponseDTO> condDtos =
                conditionRepository.findByUserId(childId)
                        .stream()
                        .map(this::mapToResponse)   // hàm map đã có
                        .collect(Collectors.toList());

        // ==== 2. vaccine phù hợp ====
        Set<String> condSet = child.getUnderlyingConditions()
                .stream()
                .map(UnderlyingCondition::getConditionName)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // lấy vaccine cùng nhóm tuổi
        List<Product> suitable = productRepository.findSuitableProductsForAge(ageMonths);

        List<ProductSuggestionDTO> vaccineDtos = suitable.stream()
                // a. loại trùng bệnh nền
                .filter(p -> p.getUnderlyingConditions().isEmpty() ||
                        p.getUnderlyingConditions().stream()
                                .noneMatch(c -> condSet.contains(c.toLowerCase())))
                // b. map sang DTO
                .map(p -> {
                    int taken = orderDetailRepository.countDosesTaken(p.getId(), childId);
                    ProductSuggestionDTO dto = new ProductSuggestionDTO();
                    dto.setId(p.getId());
                    dto.setTitle(p.getTitle());
                    dto.setNumberOfDoses(p.getNumberOfDoses());
                    dto.setRemainingDoses(Math.max(0, p.getNumberOfDoses() - taken));
                    return dto;
                })
                .collect(Collectors.toList());

        // ==== 3. gộp vào DTO trả về ====
        ChildHealthInfoDTO info = new ChildHealthInfoDTO();
        info.setChildId(child.getId());
        info.setChildName(child.getFullname());
        info.setAgeMonths(ageMonths);
        info.setHeight(child.getHeight());
        info.setWeight(child.getWeight());
        info.setConditions(condDtos);
        info.setVaccines(vaccineDtos);

        return info;
    }



    private UnderlyingConditionResponseDTO mapToResponse(UnderlyingCondition condition) {
        UnderlyingConditionResponseDTO dto = new UnderlyingConditionResponseDTO();
        dto.setId(condition.getId());
        dto.setConditionName(condition.getConditionName());
        dto.setNote(condition.getConditionDescription());
        return dto;
    }
}
