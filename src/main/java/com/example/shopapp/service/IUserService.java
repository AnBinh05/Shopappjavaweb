package com.example.shopapp.service;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    User login(String phoneNumber, String password) ;

}
