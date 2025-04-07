package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ConsultRequestDTO;
import com.example.SpringBootTurialVip.dto.request.StaffProcessDTO;
import com.example.SpringBootTurialVip.entity.*;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import com.example.SpringBootTurialVip.enums.RequestStatus;
import com.example.SpringBootTurialVip.repository.*;
import com.example.SpringBootTurialVip.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ConsultationRequestRepository consultationRequestRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UnderlyingConditionRepository underlyingConditionRepository;

    @Override
    public void createConsultationRequest(ConsultRequestDTO consultRequestDTO) {
        // Lưu yêu cầu tư vấn của khách hàng vào database
        ConsultationRequest consultationRequest = new ConsultationRequest();
        consultationRequest.setParentName(consultRequestDTO.parentName());
        consultationRequest.setPhone(consultRequestDTO.phone());
        consultationRequest.setEmail(consultRequestDTO.email());
        consultationRequest.setChildName(consultRequestDTO.childName());
        consultationRequest.setChildDob(consultRequestDTO.childDob());
        consultationRequest.setNote(consultRequestDTO.note());
        consultationRequest.setCreatedAt(LocalDateTime.now());

        consultationRequestRepository.save(consultationRequest);
    }

    @Override
    public void processConsultationRequest(StaffProcessDTO staffProcessDTO) {
        // Lấy thông tin yêu cầu tư vấn từ database
        ConsultationRequest request = consultationRequestRepository.findById(staffProcessDTO.requestId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tư vấn"));

        // Lấy thông tin phụ huynh từ database
        User parent = userRepository.findById(staffProcessDTO.parentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ huynh"));

        // Xử lý bệnh nền và vaccine cho mỗi trẻ
        for (Long childId : staffProcessDTO.childProductMap().keySet()) {
            User child = userRepository.findById(childId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ"));

            // Tạo đơn hàng cho trẻ
            ProductOrder productOrder = new ProductOrder();
            productOrder.setUser(parent);
            productOrder.setOrderDate(LocalDate.now());
            productOrder.setStatus(OrderStatus.ORDER_RECEIVED.getName());
            productOrderRepository.save(productOrder);

            // Tạo chi tiết đơn hàng cho vaccine
            for (Long productId : staffProcessDTO.childProductMap().get(childId)) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(productOrder.getOrderId());
                orderDetail.setProduct(product);
                orderDetail.setChild(child);
                orderDetail.setStatus(OrderDetailStatus.CHUA_TIEM);
                orderDetailRepository.save(orderDetail);
            }

            // Xử lý bệnh nền cho trẻ
            for (String condition : staffProcessDTO.childConditions().get(childId)) {
                UnderlyingCondition underlyingCondition = new UnderlyingCondition();
                underlyingCondition.setUser(child);
                underlyingCondition.setConditionName(condition);
                underlyingCondition.setConditionDescription("Mô tả bệnh nền " + condition);
                underlyingConditionRepository.save(underlyingCondition);
            }
        }
    }

    @Override
    public void saveConsultationRequest(ConsultationRequest consultationRequest) {
        consultationRequestRepository.save(consultationRequest);
    }

    @Override
    public List<ConsultationRequest> getAllConsultationRequests() {
        return consultationRequestRepository.findAll();
    }

    @Override
    public void updateRequestStatus(Long id, RequestStatus status) {
        ConsultationRequest request = consultationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại"));
        request.setStatus(status);
        consultationRequestRepository.save(request);
    }
}
