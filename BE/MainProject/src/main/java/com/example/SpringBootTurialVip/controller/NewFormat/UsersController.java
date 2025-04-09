package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.constant.PredefinedRole;
import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.*;
import com.example.SpringBootTurialVip.entity.UnderlyingCondition;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.entity.UserRelationship;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.enums.RelativeType;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.UnderlyingConditionRepository;
import com.example.SpringBootTurialVip.repository.UserRelationshipRepository;
import com.example.SpringBootTurialVip.service.AuthenticationService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.service.StaffService;
import com.example.SpringBootTurialVip.service.serviceimpl.FileStorageService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.rmi.server.LogStream.log;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name="[User]",description = "")
@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
@Slf4j
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRelationshipRepository userRelationshipRepository;

    @Autowired
    private UnderlyingConditionRepository underlyingConditionRepository;



    //API hiển thị thông tin cá nhân để truy xuất giỏ hàng và ... - OK
    @Operation(summary = "API hiển thị profile")
    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

    @Operation(summary = "API hiển thị profile cá nhân (kèm danh sách trẻ)")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getLoggedInUserDetailss() {
        return ResponseEntity.ok(userService.getMyInfoWithChildren());
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Xem profile của user bất kỳ (kèm danh sách trẻ) — chỉ khi user không phải là trẻ")
    @GetMapping("/user-info/{userId}")
    public ResponseEntity<UserResponse> getUserInfoById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfoWithChildrenById(userId));
    }





    //API tạo hồ sơ trẻ em - OK
//    @Operation(summary = "API tạo hồ sơ trẻ em, dựa theo token để xác định parent")
//    @PostMapping("/child/create")
//    public ApiResponse<ChildResponse> createChild(@RequestBody @Valid ChildCreationRequest childCreationRequest,
//                                                  @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
//
//        // Lấy thông tin user đang đăng nhập
//        UserResponse loggedInUser = getLoggedInUserDetails();
//        Long parentId = loggedInUser.getId();
//
//        // Gán parentId vào request
//        childCreationRequest.setParentid(parentId);
//
//        // Gọi service để tạo child
//        ChildResponse childResponse = userService.addChild(childCreationRequest,avatar);
//
//        // Trả về response
//        return new ApiResponse<>(0, "Child created successfully", childResponse);
//    }
    @Operation(summary = "API tạo hồ sơ trẻ em, dựa theo token để xác định parent")
    @PostMapping(value = "/child/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ChildResponse> createChild(
            @RequestParam String fullname,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
            @RequestParam String gender,
            @RequestParam Double height,
            @RequestParam Double weight,
            @RequestParam RelativeType relationshipType,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        // Lấy thông tin user đang đăng nhập
        UserResponse loggedInUser = getLoggedInUserDetails();
        Long parentId = loggedInUser.getId();

        // Tạo đối tượng request
        ChildCreationRequest childCreationRequest = new ChildCreationRequest();
        childCreationRequest.setFullname(fullname);
        childCreationRequest.setBod(bod);
        childCreationRequest.setGender(gender);
        childCreationRequest.setHeight(height);
        childCreationRequest.setWeight(weight);
        childCreationRequest.setRelationshipType(relationshipType);
        childCreationRequest.setParentid(parentId);

        // Gọi service để tạo child
        ChildResponse childResponse = userService.addChild(childCreationRequest, avatar);

        // Trả về response
        return new ApiResponse<>(0, "Child created successfully", childResponse);
    }


    //API xem hồ sơ trẻ em ( dựa theo token ) - OK
    @Operation(summary = "Xem thông tin trẻ của mình ")
    //@GetMapping("/my-children")
    public ResponseEntity<List<ChildResponse>> getMyChildren() {
        return ResponseEntity.ok(userService.getChildInfo());
    }

//    @Operation(summary = "Cập nhật thông tin cho trẻ (customer) ",description = "Truyền vào userid là id của trẻ , parentid se")
//    @PutMapping("/update-my-children")
//    public ResponseEntity<ChildResponse> updateMyChildren(@RequestBody @Valid ChildUpdateRequest request) {
//        ChildResponse updatedChild = userService.updateChildrenByParent(request);
//        return ResponseEntity.ok(updatedChild);
//    }



    //API Xem thông tin cá nhân - OK
    @Operation(summary = "APi xem profile")
    //@GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    //API update thông tin user
//    @Operation(summary = "API cập nhật thông tin cá nhân")
//    @PutMapping(value = "/update-profile", consumes = {"multipart/form-data"})
//    public ResponseEntity<?> updateProfile(//@RequestBody UpdateProfileRequest userDetails
//                                           @Schema(description = "{\n" +
//                                                   "  \"fullname\": \"nguyen van a\",\n" +
//                                                   "  \"password\": \"123456789\",\n" +
//                                                   "  \"phone\": \"947325435\",\n" +
//                                                   "  \"bod\": \"2025-03-08T14:31:04.584Z\",\n" +
//                                                   "  \"gender\": \"male\"\n" +
//                                                   "}")
//                                           @RequestPart("user") String userJson,
//                                           @RequestPart(value = "avatar", required = false) MultipartFile avatar
//
//    ) throws IOException {
//        // Lấy thông tin từ SecurityContextHolder
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = jwt.getClaim("email"); // Lấy email từ token
//
//        System.out.println("DEBUG: Extracted email from token = " + email);
//
//        if (email == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User email not found in token");
//        }
//
//        // Lấy thông tin người dùng từ database
//        User existingUser = userService.getUserByEmail(email);
//        if (existingUser == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
//        }
//
//        //Covert JSON => Object
//        UserCreationRequest request = objectMapper.readValue(userJson, UserCreationRequest.class);
//
//        // Cập nhật thông tin mới (chỉ cho phép cập nhật một số trường)
//        existingUser.setPhone(request.getPhone());
//        existingUser.setFullname(request.getFullname());
//        existingUser.setBod(request.getBod());
//        existingUser.setGender(request.getGender());
//        if (avatar != null && !avatar.isEmpty()) {
//                byte[] avatarBytes = avatar.getBytes();
//                String avatarUrl = fileStorageService.uploadFile(avatar);
//                existingUser.setAvatarUrl(avatarUrl); // Lưu URL ảnh vào User
//        }
//
//        // Nếu user muốn đổi password, phải mã hóa
//        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
//            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//
//        // Lưu lại thông tin đã cập nhật
//        User updatedUser = userService.updateUser(existingUser);
//// Chuyển đổi User -> UserResponse
//        UserResponse userResponse = new UserResponse();
//
//        userResponse.setUsername(updatedUser.getUsername());
//        userResponse.setEmail(updatedUser.getEmail());
//        userResponse.setPhone(updatedUser.getPhone());
//        userResponse.setBod(updatedUser.getBod());
//        userResponse.setGender(updatedUser.getGender());
//        userResponse.setFullname(updatedUser.getFullname());
//        userResponse.setAvatarUrl(updatedUser.getAvatarUrl());
//
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(0, "User created successfully", userResponse);
//        return ResponseEntity.ok(apiResponse);
//    }

@Operation(summary = "API cập nhật thông tin cá nhân")
@PatchMapping(value = "/update-profile", consumes = {"multipart/form-data"})
public ResponseEntity<?> updateProfile(
        @RequestParam(required = false) String fullname,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
        @RequestParam(required = false) String gender,
        @RequestPart(value = "avatar", required = false) MultipartFile avatar
) throws IOException {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String email = jwt.getClaim("email");

    User user = userService.getUserByEmail(email);
    if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    boolean isUpdated = false;

    if (fullname != null && !fullname.isBlank()) {
        user.setFullname(fullname);
        isUpdated = true;
    }

    if (phone != null && !phone.isBlank()) {
        user.setPhone(phone);
        isUpdated = true;
    }

    if (bod != null) {
        user.setBod(bod);
        isUpdated = true;
    }

    if (gender != null && !gender.isBlank()) {
        user.setGender(gender);
        isUpdated = true;
    }

    if (avatar != null && !avatar.isEmpty()) {
        String avatarUrl = fileStorageService.uploadFile(avatar);
        user.setAvatarUrl(avatarUrl);
        isUpdated = true;
    }

    if (!isUpdated) {
        return ResponseEntity.badRequest().body("Hãy nhập thông tin cần cập nhật");
    }

    User updatedUser = userService.updateUser(user);

    return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thành công",
            new UserResponse(updatedUser)));
}



    //API: Update(Edit) thông tin `Child`
//    @Operation(summary = "Cập nhật thông tin trẻ dựa theo ID", description = "API này cho phép cập nhật thông tin của một đứa trẻ dựa vào ID.")
//    @PutMapping(value="/children/{childId}/update",consumes = {"multipart/form-data"})
//    public ResponseEntity<ApiResponse<ChildResponse>> updateChildInfo(
//            @PathVariable Long childId,
//            @Schema(description = "{\n" +
//                    "  \"fullname\": \"nguyen van a\",\n" +
//                    "  \"bod\": \"2025-03-08T14:31:04.584Z\",\n" +
//                    "  \"gender\": \"male\"\n" +
//                    "  \"height\": \"100\"\n" +
//                    "  \"weight\": \"50\"\n" +
//                    "  \"relationshipType\": \"CHA_ME\"\n" +
//                    "}")
//            @RequestPart("user") String userJson,
//            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
//
//        try {
//            //map data từ string sang object
//            ChildCreationRequest child=objectMapper.readValue(userJson,ChildCreationRequest.class);
//
//            ChildResponse updatedChild = staffService.updateChildInfo(childId,child,avatar);
//            return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thành công", updatedChild));
//        } catch (AppException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    new ApiResponse<>(e.getErrorCode().getCode(),
//                            e.getMessage(), null));
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(500, "Lỗi hệ thống", null));
//        }
//    }

    @Operation(
            summary = "Cập nhật thông tin trẻ dựa theo ID (PATCH)",
            description = "API này cho phép cập nhật thông tin của một đứa trẻ dựa vào ID. Truyền từng trường cần thay đổi, phần còn lại giữ nguyên."
    )
    @PatchMapping(value = "/children/{childId}/update", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<ChildResponse>> updateChildInfoPatch(
            @PathVariable Long childId,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Double height,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) RelativeType relationshipType,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        try {
            // Tạo đối tượng child mới để giữ các field cần cập nhật
            ChildCreationRequest child = new ChildCreationRequest();
            child.setFullname(fullname);
            child.setBod(bod);
            child.setGender(gender);
            child.setHeight(height);
            child.setWeight(weight);
            child.setRelationshipType(relationshipType);

            ChildResponse updatedChild = staffService.updateChildInfo(childId, child, avatar);
            return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thành công", updatedChild));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(e.getErrorCode().getCode(), e.getMessage(), null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Lỗi hệ thống", null));
        }
    }



    //API : tìm user = id
    //API tìm kiếm user
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "APi tìm kiếm 1 tài khoản user ( 0 phải là trẻ em) = user id(staff,admin) ")
    @GetMapping("/{userId}")
    //Nhận 1 param id để tìm thông tin user đó
    UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    //API tìm kiếm trẻ = userid ,
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN')")
    @Operation(summary = "Lấy thông tin một trẻ theo userId", description = "API này chỉ trả về thông tin của trẻ nếu người dùng có quan hệ với trẻ đó.")
    @GetMapping("/child/{id}")
    public ResponseEntity<ApiResponse<ChildResponse>> getChildByUserId(@PathVariable Long id) {
        try {
            ChildResponse childResponse = userService.getChildByUserId(id);
            return ResponseEntity.ok(new ApiResponse<>(0, "Lấy thông tin trẻ thành công", childResponse));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(e.getErrorCode().getCode(), e.getMessage(), null));
        }
    }

    @Operation(summary = "Xem lịch sử tiêm chủng của 1 trẻ")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<VaccinationHistoryResponse>>> getVaccinationHistory(
            @RequestParam Long childId) {



        List<VaccinationHistoryResponse> history = orderService.getChildVaccinationHistory(childId);

        if (history.isEmpty()) {
            return ResponseEntity
                    .ok(new ApiResponse<>(1004, "Không tìm thấy lịch sử tiêm chủng cho trẻ", Collections.emptyList()));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch sử tiêm chủng của trẻ ", history));
    }

//    @Operation(summary = "Xem toàn bộ lịch sử tiêm chủng của tất cả trẻ thuộc khách hàng hiện tại")
//    @GetMapping("/history/my-children")
//    public ResponseEntity<ApiResponse<List<VaccinationHistoryResponse>>> getMyChildrenVaccinationHistory() {
//
//        // Lấy thông tin khách hàng từ JWT
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long customerId = jwt.getClaim("id");
//
//        List<VaccinationHistoryResponse> history = orderService.getCustomerVaccinationHistory(customerId);
//
//        if (history.isEmpty()) {
//            return ResponseEntity
//                    .ok(new ApiResponse<>(1004, "Không tìm thấy lịch sử tiêm chủng cho trẻ của bạn", Collections.emptyList()));
//        }
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch sử tiêm chủng của tất cả trẻ của bạn", history));
//    }




    //Lịch tiêm tiếp theo cho trẻ
    @Operation(summary = "Xem lịch tiêm chủng sắp tới của trẻ theo ID của trẻ ",
            description = "Lấy danh sách các mũi tiêm trong tương lai của trẻ.")
    @GetMapping("/upcoming/{childId}")
    public ResponseEntity<ApiResponse<List<UpcomingVaccinationResponse>>> getUpcomingVaccinations(
            @PathVariable Long childId) {

        List<UpcomingVaccinationResponse> upcomingVaccinations = orderService.getUpcomingVaccinations(childId);

        if (upcomingVaccinations.isEmpty()) {
            return ResponseEntity
                    .ok(new ApiResponse<>(1004, "Không có lịch tiêm nào trong tương lai của trẻ ", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm cảu trẻ trong tương lai ", upcomingVaccinations));
    }

    @Operation(
            summary = "Xem lịch tiêm chủng sắp tới của tất cả trẻ của 1 customer = id cảu customer",
            description = "Lấy danh sách các mũi tiêm trong tương lai của tất cả trẻ thuộc một phụ huynh."
    )
    @GetMapping("/upcoming/all/{parentId}")
    public ResponseEntity<ApiResponse<List<UpcomingVaccinationResponse>>> getUpcomingVaccinationsForAllChildrenByParentId(
            @PathVariable Long parentId) {

        List<UpcomingVaccinationResponse> upcomingVaccinations = orderService.getUpcomingVaccinationsForAllChildren(parentId);

        if (upcomingVaccinations.isEmpty()) {
            return ResponseEntity
                    .ok(new ApiResponse<>(1004, "Không có lịch tiêm nào trong tương lai của trẻ.", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm sắp tới của tất cả trẻ thuộc phụ huynh.", upcomingVaccinations));
    }

    @Operation(
            summary = "Xem lịch tiêm chủng sắp tới của tất cả trẻ của 1 mình",
            description = "Lấy danh sách các mũi tiêm trong tương lai của tất cả trẻ thuộc một phụ huynh."
    )
   // @GetMapping("/upcoming/all")
    public ResponseEntity<ApiResponse<List<UpcomingVaccinationResponse>>> getUpcomingVaccinationsForAllChildren(
            ) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long parentId= jwt.getClaim("id");

        List<UpcomingVaccinationResponse> upcomingVaccinations = orderService.getUpcomingVaccinationsForAllChildren(parentId);

        if (upcomingVaccinations.isEmpty()) {
            return ResponseEntity
                    .ok(new ApiResponse<>(1004, "Không có lịch tiêm nào trong tương lai của trẻ.", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm sắp tới của tất cả trẻ thuộc phụ huynh.", upcomingVaccinations));
    }

    @Operation(summary = "Change password",
            description = "Customer can update their password after receiving the default one via email.")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @Operation(summary = "Xem thông tin chi tiết trẻ của tôi (kèm lịch sử tiêm & phản ứng)")
    @GetMapping("/my-children/detail")
    public ResponseEntity<List<ChildWithInjectionInfoResponse>> getMyChildrenDetails() {
        return ResponseEntity.ok(userService.getMyChildrenWithInjectionDetails());
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN')")
    @Operation(summary = "Xem lịch tiêm sắp tới của các con")
    @GetMapping("/customer/upcoming-schedules")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getUpcomingSchedulesForParent(
            @Parameter(
                    description = "Ngày bắt đầu tìm lịch tiêm (format: yyyy-MM-dd)",
                    example = "2025-03-30"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false) OrderDetailStatus status) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long parentId = jwt.getClaim("id");

        // Nếu không truyền thì lấy ngày hôm nay
        if (fromDate == null) {
            fromDate = LocalDate.now();
        }

        // Chuyển LocalDate → LocalDateTime (mốc 00:00 của ngày)
        LocalDateTime fromDateTime = fromDate.atStartOfDay();

        List<OrderDetailResponse> list = orderService.getUpcomingSchedulesForParent(parentId, fromDateTime, status);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm sắp tới của các con", list));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Tạo tài khoản Customer mới (dành cho STAFF)")
    @PostMapping("/staff/create-customer")
    public ResponseEntity<ApiResponse<String>> createCustomerByStaff(@RequestBody CustomerCreationRequest request) {
        userService.createCustomerByStaff(request);
        return ResponseEntity.ok(new ApiResponse<>(0, "Tạo tài khoản Customer thành công", null));
    }


    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Tạo hồ sơ trẻ cho 1 Customer cụ thể (bởi Staff)")
    @PostMapping(value = "/staff/create-child/{parentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ChildResponse>> createChildForCustomerByStaff(
            @PathVariable Long parentId,
            @RequestParam String fullname,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
            @RequestParam String gender,
            @RequestParam Double height,
            @RequestParam Double weight,
            @RequestParam RelativeType relationshipType,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        ChildCreationRequest request = new ChildCreationRequest();
        request.setParentid(parentId);
        request.setFullname(fullname);
        request.setBod(bod);
        request.setGender(gender);
        request.setHeight(height);
        request.setWeight(weight);
        request.setRelationshipType(relationshipType);

        ChildResponse child = staffService.createChildForParent(parentId, request, avatar);
        return ResponseEntity.ok(new ApiResponse<>(0, "Tạo hồ sơ trẻ thành công", child));
    }




    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Xóa tài khoản Customer theo ID (chỉ dành cho STAFF/ADMIN)")
    @DeleteMapping("/staff/delete-customer/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(0, "Xóa tài khoản Customer thành công", null));
    }



    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Xóa hồ sơ trẻ theo ID (chỉ dành cho STAFF/ADMIN)")
    @DeleteMapping("/staff/delete-child/{childId}")
    public ResponseEntity<ApiResponse<String>> deleteChild(@PathVariable Long childId) {
        userService.deleteUser(childId);
        return ResponseEntity.ok(new ApiResponse<>(0, "Xóa hồ sơ trẻ thành công", null));
    }

    @Operation(summary = "Cập nhật thông tin customer (do Staff thực hiện)")
    @PatchMapping(value = "/staff/update-customer/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<UserResponse>> updateCustomerInfo(
            @PathVariable Long userId,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) MultipartFile avatar
    ) throws IOException {
        // Kiểm tra quyền của Staff
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String staffEmail = jwt.getClaim("email");

        User staffUser = userService.getUserByEmail(staffEmail);
        if (staffUser == null || staffUser.getRoles().stream().noneMatch(role -> role.getName().equals(PredefinedRole.STAFF_ROLE))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(403, "Chỉ Staff mới có thể thực hiện hành động này", null));
        }

        // Tìm user (customer) cần cập nhật
        User customer = userService.getUserByID(userId);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Customer không tồn tại", null));
        }

        // Nếu user là Staff hoặc Child, không thể cập nhật
        if (customer.getRoles().stream().anyMatch(role -> role.getName().equals(PredefinedRole.STAFF_ROLE)) ||
                customer.getRoles().stream().anyMatch(role -> role.getName().equals(PredefinedRole.CHILD_ROLE))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, "Không thể cập nhật thông tin của Staff hoặc Child", null));
        }

        // Cập nhật các trường của customer
        boolean isUpdated = false;
        if (fullname != null && !fullname.isBlank()) {
            customer.setFullname(fullname);
            isUpdated = true;
        }
        if (email != null && !email.isBlank()) {
            customer.setEmail(email);
            isUpdated = true;
        }
        if (phone != null && !phone.isBlank()) {
            customer.setPhone(phone);
            isUpdated = true;
        }
        if (bod != null) {
            customer.setBod(bod);
            isUpdated = true;
        }
        if (gender != null && !gender.isBlank()) {
            customer.setGender(gender);
            isUpdated = true;
        }
        if (enabled != null) {
            customer.setEnabled(enabled);
            isUpdated = true;
        }
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUrl = fileStorageService.uploadFile(avatar);
            customer.setAvatarUrl(avatarUrl);
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Không có thông tin cần cập nhật", null));
        }

        User updatedCustomer = userService.updateUser(customer);

        return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thông tin customer thành công", new UserResponse(updatedCustomer)));
    }

    @Operation(summary = "Cập nhật thông tin staff (do Admin thực hiện)")
    @PatchMapping(value = "/admin/update-staff/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<UserResponse>> updateStaffInfo(
            @PathVariable Long userId,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) MultipartFile avatar
    ) throws IOException {
        // Kiểm tra quyền của Admin
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String adminEmail = jwt.getClaim("email");

        User adminUser = userService.getUserByEmail(adminEmail);
        if (adminUser == null || adminUser.getRoles().stream().noneMatch(role -> role.getName().equals(PredefinedRole.ADMIN_ROLE))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(403, "Chỉ Admin mới có thể thực hiện hành động này", null));
        }

        // Tìm user (staff) cần cập nhật
        User staff = userService.getUserByID(userId);
        if (staff == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Staff không tồn tại", null));
        }

        // Nếu user là Customer hoặc Child, không thể cập nhật
        if (staff.getRoles().stream().anyMatch(role -> role.getName().equals(PredefinedRole.USER_ROLE)) ||
                staff.getRoles().stream().anyMatch(role -> role.getName().equals(PredefinedRole.CHILD_ROLE))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, "Không thể cập nhật thông tin của Customer hoặc Child", null));
        }

        // Cập nhật các trường của staff
        boolean isUpdated = false;
        if (fullname != null && !fullname.isBlank()) {
            staff.setFullname(fullname);
            isUpdated = true;
        }
        if (email != null && !email.isBlank()) {
            staff.setEmail(email);
            isUpdated = true;
        }
        if (phone != null && !phone.isBlank()) {
            staff.setPhone(phone);
            isUpdated = true;
        }
        if (bod != null) {
            staff.setBod(bod);
            isUpdated = true;
        }
        if (gender != null && !gender.isBlank()) {
            staff.setGender(gender);
            isUpdated = true;
        }
        if (enabled != null) {
            staff.setEnabled(enabled);
            isUpdated = true;
        }
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUrl = fileStorageService.uploadFile(avatar);
            staff.setAvatarUrl(avatarUrl);
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Không có thông tin cần cập nhật", null));
        }

        User updatedStaff = userService.updateUser(staff);

        return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thông tin staff thành công", new UserResponse(updatedStaff)));
    }


    @Operation(summary = "Cập nhật thông tin trẻ của khách hàng (do Staff thực hiện)")
    @PatchMapping(value = "/staff/update-child/{childId}", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody ChildRequest request) {

        // Kiểm tra quyền của Staff
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String staffEmail = jwt.getClaim("email");

        User staffUser = userService.getUserByEmail(staffEmail);
        if (staffUser == null || staffUser.getRoles().stream().noneMatch(role -> role.getName().equals(PredefinedRole.STAFF_ROLE))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(403, "Chỉ Staff mới có thể thực hiện hành động này", null));
        }

        // Tìm trẻ em cần cập nhật
        User child = userService.getUserByID(childId);
        if (child == null || !child.getRoles().stream().anyMatch(role -> role.getName().equals(PredefinedRole.CHILD_ROLE))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Trẻ không tồn tại hoặc không có vai trò CHILD", null));
        }

        // Cập nhật thông tin trẻ - chỉ cập nhật các trường có giá trị mới
        boolean isUpdated = false;

        if (request.getChildName() != null) {
            child.setFullname(request.getChildName());
            isUpdated = true;
        }
        if (request.getChildBod() != null) {
            child.setBod(request.getChildBod());
            isUpdated = true;
        }
        if (request.getChildGender() != null) {
            child.setGender(request.getChildGender());
            isUpdated = true;
        }
        if (request.getChildHeight() != null) {
            child.setHeight(request.getChildHeight());
            isUpdated = true;
        }
        if (request.getChildWeight() != null) {
            child.setWeight(request.getChildWeight());
            isUpdated = true;
        }

        // Cập nhật mối quan hệ nếu có sự thay đổi relationshipType
        if (request.getRelationshipType() != null) {
            // Tìm mối quan hệ giữa trẻ và người thân (relative) trong bảng user_relationship
            UserRelationship userRelationship = userRelationshipRepository.findByChildIdAndRelativeId(childId, child.getParentid())
                    .orElseThrow(() -> new RuntimeException("Mối quan hệ giữa trẻ và người thân không tồn tại"));

            // Cập nhật relationshipType nếu có sự thay đổi
            userRelationship.setRelationshipType(request.getRelationshipType());
            userRelationshipRepository.save(userRelationship);
            isUpdated = true;
        }

        // Cập nhật bệnh nền nếu có
        if (request.getChildConditions() != null && !request.getChildConditions().isEmpty()) {
            for (UnderlyingConditionRequestDTO conditionDTO : request.getChildConditions()) {
                UnderlyingCondition underlyingCondition = new UnderlyingCondition();
                underlyingCondition.setConditionName(conditionDTO.getConditionName());
                underlyingCondition.setConditionDescription(conditionDTO.getNote());
                underlyingCondition.setUser(child);
                underlyingConditionRepository.save(underlyingCondition);
                isUpdated = true;
            }
        }

        if (!isUpdated) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Không có thông tin cần cập nhật", null));
        }

        // Lưu lại thông tin đã cập nhật
        User updatedChild = userService.updateUser(child);

        return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thông tin trẻ thành công", new UserResponse(updatedChild)));
    }



















    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Tạo tài khoản Customer mới (dành cho STAFF)")
    @PostMapping("/staff/create-customer")
    public ResponseEntity<ApiResponse<String>> createCustomerByStaff(@RequestBody CustomerCreationRequest request) {
        userService.createCustomerByStaff(request);
        return ResponseEntity.ok(new ApiResponse<>(0, "Tạo tài khoản Customer thành công", null));
    }


    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Tạo hồ sơ trẻ cho 1 Customer cụ thể (bởi Staff)")
    @PostMapping(value = "/staff/create-child/{parentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ChildResponse>> createChildForCustomerByStaff(
            @PathVariable Long parentId,
            @RequestParam String fullname,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod,
            @RequestParam String gender,
            @RequestParam Double height,
            @RequestParam Double weight,
            @RequestParam RelativeType relationshipType,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        ChildCreationRequest request = new ChildCreationRequest();
        request.setParentid(parentId);
        request.setFullname(fullname);
        request.setBod(bod);
        request.setGender(gender);
        request.setHeight(height);
        request.setWeight(weight);
        request.setRelationshipType(relationshipType);

        ChildResponse child = staffService.createChildForParent(parentId, request, avatar);
        return ResponseEntity.ok(new ApiResponse<>(0, "Tạo hồ sơ trẻ thành công", child));
    }




    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Xóa tài khoản Customer theo ID (chỉ dành cho STAFF/ADMIN)")
    @DeleteMapping("/staff/delete-customer/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(0, "Xóa tài khoản Customer thành công", null));
    }



    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Xóa hồ sơ trẻ theo ID (chỉ dành cho STAFF/ADMIN)")
    @DeleteMapping("/staff/delete-child/{childId}")
    public ResponseEntity<ApiResponse<String>> deleteChild(@PathVariable Long childId) {
        userService.deleteUser(childId);
        return ResponseEntity.ok(new ApiResponse<>(0, "Xóa hồ sơ trẻ thành công", null));
    }














}
