package com.example.shopapp.service;

import com.example.shopapp.Repository.RoleRepository;
import com.example.shopapp.Repository.UserRepository;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Role;
import com.example.shopapp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class UserService  implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra xem số điện thoại có tồn tại chưa ?
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
            //convert from userDTO => userEntity
            User newUser = User.builder()
                    .fullName(userDTO.getFullName())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .password(userDTO.getPassword())
                    .address(userDTO.getAddress())
                    .dateOfBirth(userDTO.getDateOfBirth())
                    .facebookAccountId(userDTO.getFacebookAccountId())
                    .googleAccountId(userDTO.getGoogleAccountId())

                    .build();
        // kiem tra role id da ton tai chua , neu chua thi neu ra ngoai le
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        // Kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
           // String encodedPassword = passwordEncoder.encode(password);
           // newUser.setPassword(encodedPassword);
        }







        return  userRepository.save(newUser);

    }

    @Override
    public User login(String phoneNumber, String password) {
        return null;
    }
}
