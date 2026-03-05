package com.nsind.payment.controller;

import com.nsind.payment.dto.CreateOrderRequest;
import com.nsind.payment.dto.OrderResponse;
import com.nsind.payment.dto.PaymentVerificationRequest;
import com.nsind.payment.entity.Subscription;
import com.nsind.payment.service.PaymentService;
import com.nsind.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment and subscription management")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order")
    @Operation(summary = "Create payment order")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) throws Exception {
        OrderResponse response = paymentService.createPaymentOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Order created successfully"));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify and activate payment")
    public ResponseEntity<ApiResponse<Void>> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        paymentService.verifyAndActivatePayment(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Payment verified successfully"));
    }

    @GetMapping("/subscription/{userId}")
    @Operation(summary = "Get user subscription")
    public ResponseEntity<ApiResponse<Subscription>> getSubscription(@PathVariable String userId) {
        Subscription subscription = paymentService.getUserSubscription(userId);
        return ResponseEntity.ok(ApiResponse.success(subscription, "Subscription retrieved"));
    }
}

