package com.example.SpringBootTurialVip.service.serviceimpl;


import com.example.SpringBootTurialVip.constant.PredefinedRole;
import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.request.VerifyAccountRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.mapper.UserMapper;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Slf4j
@Service//Layer này sẽ gọi xuống layer repository
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //Khai báo đối tượng mapper
    @Autowired
    private UserMapper userMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    //Tạo tài khoản
    public User createUser(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);//Sử dụng class AppException để báo lỗi đã define tại ErrorCode

        User user=userMapper.toUser(request);//Khi có mapper

        //Mã hóa password user
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles=new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        //Set role cho tai khoan mac dinh duoc tao la Customer
        user.setRoles(roles);

        //Tao ma code de xac thuc tai khoan
        user.setVerificationcode(generateVerificationCode());

        //Set time cho ma code het han
        user.setVerficationexpiration(LocalDateTime.now().plusMinutes(15));

        //Dat cho mac dinh cho tai khoan chua duoc xac thuc
        user.setEnabled(false);

        //Gui ma xac thuc qua email
        sendVerificationEmail(user);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userRepository.save(user);

    }

    //Method xac thuc account de cho phep dang nhap
    public void verifyUser(VerifyAccountRequest verifyAccountRequest) {
        User optionalUser = userRepository.findByEmail(verifyAccountRequest.getEmail());
        if (optionalUser != null) {
            User user = optionalUser;
            if (user.getVerficationexpiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationcode().equals(verifyAccountRequest.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationcode(null);
                user.setVerficationexpiration(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //Method cho phep gui lai ma code
    public void resendVerificationCode(String email) {
        User optionalUser = userRepository.findByEmail(email);
        if (optionalUser != null ) {
            User user = optionalUser;
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationcode(generateVerificationCode());
            user.setVerficationexpiration(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //Method gui email
    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationcode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Chào mừng bạn đến với web vaccine của chúng tôi!</h2>"
                + "<p style=\"font-size: 16px;\">Mời bạn nhập mã code phía dưới để xác thực tài khoản :</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Mã Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    //Method tạo mã xác thực
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


    //Danh sách user
    @PreAuthorize("hasRole('ADMIN')")//Chỉ cho phép admin
    public List<User> getUsers(){
        log.info("PreAuthorize đã chặn nếu ko có quyền truy cập nên ko thấy dòng này trong console ," +
                "chỉ có Admin mới thấy đc dòng này sau khi đăng nhập ");
        return userRepository.findAll();
    }




    @PostAuthorize("hasRole('ADMIN') || returnObject.username == authentication.name")//Post sẽ run method trc r check sau
    //Như khai báo thì chỉ cho phép truy cập nếu id kiếm trùng id đang login
    //Kiếm user băằng ID
    public UserResponse getUserById(Long id){
        return userMapper.toUserResponse(userRepository.findById(id).
                orElseThrow(()->new RuntimeException("User not found!")));//Nếu ko tìm thấy báo lỗi
    }



    //Lấy thông tin hiện tại đang log in
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        //Tên user đang log in
        String name=context.getAuthentication().getName();

        User user =userRepository.findByUsername(name).orElseThrow(
                () -> new AppException((ErrorCode.USER_NOT_EXISTED)));

                return userMapper.toUserResponse(user);
    }



    //Cập nhật thông tin
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public User updateUserByResetToken(User user) {
        return userRepository.save(user);
    }

    //Xóa user
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    //Tạo hồ sơ trẻ
    //Tạo tài khoản
    public User createChild(ChildCreationRequest request){

        if(userRepository.existsByFullnameAndBod(
                //request.getParentid()
                request.getFullname(),
                request.getBod()))
            throw new AppException(ErrorCode.CHILD_EXISTED);//Sử dụng class AppException để báo lỗi đã define tại ErrorCode

        User child=userMapper.toUser(request);//Khi có mapper



        HashSet<Role> roles=new HashSet<>();

        roleRepository.findById(PredefinedRole.CHILD_ROLE).ifPresent(roles::add);

        //Set role cho tai khoan mac dinh duoc tao la Customer
        child.setRoles(roles);

        // Lấy user hiện tại từ SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //Dat cho mac dinh cho tai khoan
        child.setEnabled(true);


        // Tìm User hiện tại theo username
        User parent = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Set parentId cho child
        child.setParentid(parent.getId());

        // Gán parentId từ user hiện tại
        request.setParentid(parent.getId());



        try {
            child = userRepository.save(child);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.CHILD_EXISTED);
        }

        return userRepository.save(child);
    }

    //Xem hồ sơ trẻ em chỉ xem đc con của customer
    public List<ChildResponse> getChildInfo() {
        // Lấy username của user đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tìm user hiện tại theo username (phải là Customer)
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách trẻ thuộc về Customer
        List<User> children = userRepository.findByParentid(customer.getId());

        // Chuyển đổi dữ liệu sang DTO (ChildResponse)
        return children.stream().map(userMapper::toChildResponse).collect(Collectors.toList());
    }

    public List<ChildResponse> updateChildrenByParent(ChildCreationRequest request) {
        // Lấy username từ người dùng đang đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm Customer theo username
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách Child dựa trên parentId
        List<User> children = userRepository.findByParentid(customer.getId());

        // Cập nhật thông tin tất cả Child
        for (User child : children) {
            child.setFullname(request.getFullname());
            child.setBod(request.getBod());
            child.setGender(request.getGender());
            child.setHeight(request.getHeight());
            child.setWeight(request.getWeight());
        }

        // Lưu vào DB
        userRepository.saveAll(children);

        return children.stream().map(userMapper::toChildResponse).collect(Collectors.toList());
    }

    //
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserResetToken(String email, String resetToken) {
        User user = userRepository.findByEmail(email);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }


}
