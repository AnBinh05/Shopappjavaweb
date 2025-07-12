package com.example.shopapp.Controller;

import com.example.shopapp.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}orders")
public class OrderCotroller {
    @PostMapping("")
    public ResponseEntity<?> CreateOrder(@Valid  @RequestBody OrderDTO orderDTO,
                                         BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()               // Lấy ra tất cả các FieldError
                        .stream()                    // Chuyển thành stream
                        .map(fieldError -> fieldError.getDefaultMessage()) // Lấy ra thông báo lỗi
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok("Create order successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{user_id}") // Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/user/4
    // lấy ra order cúa người có userid = ?
    public ResponseEntity<?> getOrders( @Valid @PathVariable("user_id") long userid){
        try {
            return ResponseEntity.ok("lấy ra danh sách oder từ user_id: " + userid);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    //PUT http://localhost:8088/api/v1/orders/2
    //công việc của admin
    public ResponseEntity<?> UpdateOrder( @Valid @PathVariable("id") long id,
                                         @Valid @RequestBody OrderDTO orderDTO){
        try {
            return ResponseEntity.ok("cập nhât thành công ");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {
        //xóa mềm => cập nhật trường active = false
        return ResponseEntity.ok("xoá thành công ");
    }



    }
