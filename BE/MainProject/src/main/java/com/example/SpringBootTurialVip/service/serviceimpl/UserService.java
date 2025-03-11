package com.example.SpringBootTurialVip.service.serviceimpl;


import com.example.SpringBootTurialVip.constant.PredefinedRole;
import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.entity.UserRelationship;
import com.example.SpringBootTurialVip.enums.RelativeType;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.mapper.UserMapper;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import com.example.SpringBootTurialVip.repository.UserRelationshipRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private UserRelationshipRepository userRelationshipRepository;

    @Autowired
    private FileStorageService fileStorageService;

    //Tạo tài khoản
//    public User createUser(UserCreationRequest request,
//                           MultipartFile avatarFile){
//
//        if(userRepository.existsByUsername(request.getUsername()))
//            throw new AppException(ErrorCode.USER_EXISTED);//Sử dụng class AppException để báo lỗi đã define tại ErrorCode
//
//        User user=userMapper.toUser(request);//Khi có mapper
//
//        //Mã hóa password user
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        HashSet<Role> roles=new HashSet<>();
//
//        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
//
//        //Set role cho tai khoan mac dinh duoc tao la Customer
//        user.setRoles(roles);
//
//        //Tao ma code de xac thuc tai khoan
//        user.setVerificationcode(generateVerificationCode());
//
//        //Set time cho ma code het han
//        user.setVerficationexpiration(LocalDateTime.now().plusMinutes(15));
//
//        //Dat cho mac dinh cho tai khoan chua duoc xac thuc
//        user.setEnabled(false);
//
//        // Nếu có file ảnh avatar, upload lên Cloudinary trước khi lưu user
//        if (avatarFile != null && !avatarFile.isEmpty()) {
//            try {
//                byte[] avatarBytes = avatarFile.getBytes();
//                String avatarUrl = fileStorageService.uploadFile(avatarFile);
//                user.setAvatarUrl(avatarUrl); // Lưu URL ảnh vào User
//            } catch (IOException e) {
//                user.setAvatarUrl("null");
//            }
//        }
//
//
//        //Gui ma xac thuc qua email
//        sendVerificationEmail(user);
//
//        try {
//            user = userRepository.save(user);
//        } catch (DataIntegrityViolationException exception) {
//            throw new AppException(ErrorCode.USER_EXISTED);
//        }
//
//        return userRepository.save(user);
//
//    }
    public User createUser(UserCreationRequest request,
                           MultipartFile avatarFile){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        if(userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);

        if(userRepository.existsByPhone(request.getPhone()))
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);

        User user = userMapper.toUser(request);

        // Mã hóa password user
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        // Set role mặc định cho tài khoản là Customer
        user.setRoles(roles);

        // Tạo mã xác thực tài khoản
        user.setVerificationcode(generateVerificationCode());
        user.setVerficationexpiration(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(true);

        // Nếu có file ảnh avatar, upload lên Cloudinary trước khi lưu user
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarUrl = fileStorageService.uploadFile(avatarFile);
                user.setAvatarUrl(avatarUrl);
            } catch (IOException e) {
                user.setAvatarUrl("null");
            }
        }

        // Gửi mã xác thực qua email
        sendVerificationEmail(user);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userRepository.save(user);
    }




    //Tạo tài khoản cho staff
    public User createStaff(UserCreationRequest request,
                           MultipartFile avatarFile){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        //Sử dụng class AppException để báo lỗi đã define tại ErrorCode

        User user=userMapper.toUser(request);//Khi có mapper

        //Mã hóa password user
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles=new HashSet<>();

        roleRepository.findById(PredefinedRole.STAFF_ROLE).ifPresent(roles::add);

        //Dat cho mac dinh cho tai khoan chua duoc xac thuc
        user.setEnabled(true);

        // Nếu có file ảnh avatar, upload lên Cloudinary trước khi lưu user
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                byte[] avatarBytes = avatarFile.getBytes();
                String avatarUrl = fileStorageService.uploadFile(avatarFile);
                user.setAvatarUrl(avatarUrl); // Lưu URL ảnh vào User
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

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
            emailServiceImpl.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
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

    //@PostAuthorize("hasRole('ADMIN') || returnObject.username == authentication.name")//Post sẽ run method trc r check sau
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



    //Cập nhật thông tin ver cũ ( đang xài )
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }



    //Cập nhật thông tin ver mới
    public User updateUser(User user) {
        return userRepository.save(user);
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
    @Transactional
    public ChildResponse addChild(ChildCreationRequest request,
                         MultipartFile avatarFile) {
        // Lấy người tạo từ SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Tên của người tạo trẻ và có quan hệ vs trẻ : "+String.valueOf(username));
        User relative = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info("User hiện tại là :"+String.valueOf(relative));

        // Kiểm tra nếu `relationshipType` bị null thì báo lỗi
        if (request.getRelationshipType() == null) {
            throw new AppException(ErrorCode.INVALID_RELATIONSHIP_TYPE);
        }

        // Kiểm tra xem trẻ đã tồn tại hay chưa
        if (userRepository.existsByFullnameAndBod(request.getFullname(), request.getBod())) {
            throw new AppException(ErrorCode.CHILD_EXISTED);
        }

        // Chuyển đổi request thành User entity bằng mapper
        User child = userMapper.toUser(request);

        // Gán role cho trẻ
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.CHILD_ROLE).ifPresent(roles::add);
        child.setRoles(roles);
        //Dat cho mac dinh cho tai khoan chua duoc xac thuc
        child.setEnabled(true);

        // Nếu có file ảnh avatar, upload lên Cloudinary trước khi lưu user
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                byte[] avatarBytes = avatarFile.getBytes();
                String avatarUrl = fileStorageService.uploadFile(avatarFile);
                child.setAvatarUrl(avatarUrl); // Lưu URL ảnh vào User
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        try {
            child = userRepository.save(child);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.CHILD_EXISTED);
        }

        // Lưu quan hệ giữa người tạo và trẻ
        UserRelationship relationship = new UserRelationship();
        relationship.setRelationshipType(request.getRelationshipType());
        relationship.setChild(child);
        relationship.setRelative(relative);
        userRelationshipRepository.save(relationship);

        // Lấy danh sách quan hệ của trẻ
        List<UserRelationship> relationships = userRelationshipRepository.findByChild(child);

        // Trả về thông tin trẻ kèm quan hệ
        return new ChildResponse(child, relationships);
    }




    //Xem hồ sơ trẻ em chỉ xem đc con của customer
//    public List<ChildResponse> getChildInfo() {
//        // Lấy username của user đang đăng nhập
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        // Tìm user hiện tại theo username (phải là Customer)
//        User customer = userRepository.findByUsername(username)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        // Lấy danh sách trẻ thuộc về Customer
//        List<User> children = userRepository.findByParentid(customer.getId());
//
//        // Chuyển đổi dữ liệu sang DTO (ChildResponse)
//        return children.stream().map(userMapper::toChildResponse).collect(Collectors.toList());
//    }

    public List<ChildResponse> getChildInfo() {
        // Lấy username của người đang đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm Parent theo username
        User parent = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách trẻ của parent
        List<User> children = userRepository.findByParentid(parent.getId());

        // Chuyển đổi thành danh sách ChildResponse
        return children.stream().map(child -> {
            // Lấy danh sách quan hệ của trẻ
            List<UserRelationship> relationships = userRelationshipRepository.findByChild(child);

            // Trả về ChildResponse với danh sách relative
            return new ChildResponse(child, relationships);
        }).collect(Collectors.toList());
    }



    public ChildResponse updateChildrenByParent(ChildUpdateRequest request) {
        // Lấy username từ người dùng đang đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm Parent theo username
        User parent = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm đứa trẻ theo userId trong request
        User child = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem child có thuộc về parent không
        if (!child.getParentid().equals(parent.getId())) {
            throw new SecurityException("You do not have permission to update this child.");
        }

        // Cập nhật thông tin cho đúng child
        child.setFullname(request.getFullname());
        child.setBod(request.getBod());
        child.setGender(request.getGender());
        child.setHeight(request.getHeight());
        child.setWeight(request.getWeight());

        // Lưu vào DB
        userRepository.save(child);

        return userMapper.toChildResponse(child);
    }


    //
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //Thêm token xác nhận để nhập lại password
    public void updateUserResetToken(String email, String resetToken) {
        User user = userRepository.findByEmail(email);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }


    public ChildResponse getChildByUserId(Long childId) {
        // Lấy user đang đăng nhập từ SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User parent = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm trẻ theo ID
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new AppException(ErrorCode.CHILD_NOT_FOUND));

        // Kiểm tra xem user có phải là cha/mẹ của trẻ hay không
        boolean isParent = userRelationshipRepository.findByChildAndRelative(child, parent).isPresent();
        if (!isParent) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        // Lấy danh sách quan hệ của trẻ
        List<UserRelationship> relationships = userRelationshipRepository.findByChild(child);

        // Trả về thông tin trẻ
        return new ChildResponse(child, relationships);
    }


}


