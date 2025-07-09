package com.example.shopapp.Controller;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.dto.UserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> createUser (@Valid @RequestBody UserDTO userDTO,
                                         BindingResult result
      ){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()               // Lấy ra tất cả các FieldError
                        .stream()                    // Chuyển thành stream
                        .map(fieldError -> fieldError.getDefaultMessage()) // Lấy ra thông báo lỗi
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // kiem tra password va retype_password co giong nhau khong ?
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Passwords do not match");
            }
            return ResponseEntity.ok(" Register successful");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody UserLoginDTO userLoginDTO){
        // kiem tra thong tin dang nhap va sinh token

        // tra ve token trong response
        return ResponseEntity.ok(" some token ");
    }


}
