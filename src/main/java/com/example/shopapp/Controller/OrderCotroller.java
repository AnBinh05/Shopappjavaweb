package com.example.shopapp.Controller;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.models.Order;
import com.example.shopapp.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}orders")
@RequiredArgsConstructor
public class OrderCotroller {
    private final IOrderService orderService;
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
            Order orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user/{user_id}") // Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/user/4
    // lấy ra  tất cả order cúa người có userid = ?
    public ResponseEntity<?> getOrders( @Valid @PathVariable("user_id") long userid){
        try {
            List<Order> orders= orderService.findByUserId(userid);
            return ResponseEntity.ok(orders);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    //GET http://localhost:8088/api/v1/orders/user/4
    // lấy ra  chi tiết 1 order
    public ResponseEntity<?> getOrder( @Valid @PathVariable("id") long  orderId){
        try {
            Order existingOrder = orderService.getOrder( orderId);
            return ResponseEntity.ok(existingOrder);


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
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {

        //xóa mềm => cập nhật trường active = false
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");

    }



    }
