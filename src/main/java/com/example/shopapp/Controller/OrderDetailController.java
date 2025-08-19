package com.example.shopapp.Controller;


import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;
import com.example.shopapp.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}order_details")
@RequiredArgsConstructor

public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    @PostMapping()
    //Thêm mới 1 order detail
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            //return ResponseEntity.ok().body(newOrderDetail);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // lấy ra 1 order detail theo 1 id nào đó
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));

        //return ResponseEntity.ok(orderDetail);

    }

    //lấy ra danh sách các order_details của 1 order nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("orderId") Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);

        //return ResponseEntity.ok(orderDetails );
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO){
//        return ResponseEntity.ok(" update orderDetail with id " + id + " "
//
//                +"newOrderDetailData:"+ newOrderDetailData); // trả về json nhưng tại vì dùng @data nên được chuyển về thành chuỗi trả ra
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body("Delete Order detail with id : "+id+" successfully");

        // return ResponseEntity.noContent().build();

        //return ResponseEntity.ok(" delete orderDetail with id " + id);
    }



}