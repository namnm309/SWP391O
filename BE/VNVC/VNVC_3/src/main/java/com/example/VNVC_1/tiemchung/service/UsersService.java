package com.example.VNVC_1.tiemchung.service;

import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository userRepository;

    @Autowired
    public UsersService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }



    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users updateUser(Long id, Users userDetails) {
        Users user = getUserById(id);
        if (userDetails.getFullname() != null) {
            user.setFullname(userDetails.getFullname());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 1. Lấy hồ sơ trẻ em
    public List<Users> getChildProfiles(Long parentId) {
        return userRepository.findByParentUserId(parentId);
    }

    // 2. Lấy phản ứng sau tiêm
    public String getPostVaccinationReactions(Long userId) {
        return "Phản ứng sau tiêm của user ID " + userId + ": Không có phản ứng nghiêm trọng.";
    }

    // 3. Lấy lịch tiêm chủng
    public String getVaccinationSchedule(Long userId) {
        return "Lịch tiêm chủng của user ID " + userId + ": Ngày 10/02/2024 - Vaccine COVID-19.";
    }

    // 4. Lấy lịch sử tiêm chủng
    public String getVaccinationHistory(Long userId) {
        return "Lịch sử tiêm chủng của user ID " + userId + ": Đã tiêm 2 mũi vaccine COVID-19.";
    }

    // 5. Lấy thông tin cha mẹ
    public Users getParentInfo(Long userId) {
        return getUserById(userId);
    }

    // 6. Cập nhật hồ sơ trẻ em
    public Users updateChildProfile(Long childId, Users childDetails) {
        Users child = getUserById(childId);
        if (childDetails.getFullname() != null) {
            child.setFullname(childDetails.getFullname());
        }
        if (childDetails.getBirthDate() != null) {
            child.setBirthDate(childDetails.getBirthDate());
        }
        return userRepository.save(child);
    }

    // 7. Cập nhật hồ sơ cha mẹ
    public Users updateParentProfile(Long parentId, Users parentDetails) {
        Users parent = getUserById(parentId);
        if (parentDetails.getFullname() != null) {
            parent.setFullname(parentDetails.getFullname());
        }
        if (parentDetails.getEmail() != null) {
            parent.setEmail(parentDetails.getEmail());
        }
        if (parentDetails.getPhone() != null) {
            parent.setPhone(parentDetails.getPhone());
        }
        return userRepository.save(parent);
    }

    // 8. Cập nhật lịch booking
    public String updateBooking(Long bookingId, Object bookingDetails) {
        return "Lịch booking với ID " + bookingId + " đã được cập nhật.";
    }

    // 9. Xóa lịch booking
    public void deleteBooking(Long bookingId) {
        // Logic xóa lịch booking trong database
        if (userRepository.existsById(bookingId)) {
            userRepository.deleteBookingByUserId(bookingId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found with ID: " + bookingId);
        }
    }


}
