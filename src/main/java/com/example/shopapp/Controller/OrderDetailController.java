package com.example.shopapp.Controller;


import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}order_details")
public class OrderDetailController {
    @PostMapping()
    //Thêm mới 1 order detail
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            return ResponseEntity.ok(" tạo đơn hàng chi tiết thành công ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // lấy ra 1 order detail theo 1 id nào đó
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(" getOrderDetail with id " + id);

    }

    //lấy ra danh sách các order_details của 1 order nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(" getOrderDetail with orderId " + orderId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData){
        return ResponseEntity.ok(" update orderDetail with id " + id + " "
                +"newOrderDetailData:"+ newOrderDetailData); // trả về json nhưng tại vì dùng @data nên được chuyển về thành chuỗi trả ra
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(" delete orderDetail with id " + id);
    }



    }
