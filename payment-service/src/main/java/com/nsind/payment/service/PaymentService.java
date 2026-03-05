package com.nsind.payment.service;

import com.nsind.payment.dto.CreateOrderRequest;
import com.nsind.payment.dto.OrderResponse;
import com.nsind.payment.dto.PaymentVerificationRequest;
import com.nsind.payment.entity.Payment;
import com.nsind.payment.entity.Subscription;
import com.nsind.payment.repository.PaymentRepository;
import com.nsind.payment.repository.SubscriptionRepository;
import com.nsind.common.exception.BadRequestException;
import com.nsind.common.exception.ResourceNotFoundException;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final RazorpayService razorpayService;
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public OrderResponse createPaymentOrder(CreateOrderRequest request) throws RazorpayException {
        try {
            String planId = request.getPlan().toUpperCase();
            String frequency = request.getPaymentFrequency().toUpperCase();

            BigDecimal amount = calculateAmount(planId, frequency);

            Order order = razorpayService.createOrder(amount, "INR", "RECEIPT_" + request.getUserId());

            Payment payment = Payment.builder()
                    .userId(request.getUserId())
                    .razorpayOrderId(order.get("id").toString())
                    .amount(amount)
                    .status(Payment.PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);

            OrderResponse response = OrderResponse.builder()
                    .orderId(order.get("id").toString())
                    .keyId(order.get("id").toString())
                    .amount(amount.toString())
                    .currency("INR")
                    .build();

            log.info("Payment order created for user: {} with order id: {}",
                    request.getUserId(), order.get("id"));
            return response;
        } catch (Exception e) {
            log.error("Error creating payment order: {}", e.getMessage());
            throw new BadRequestException("Failed to create payment order");
        }
    }

    @Transactional
    public void verifyAndActivatePayment(PaymentVerificationRequest request) {
        Payment payment = paymentRepository.findByRazorpayOrderId(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (!razorpayService.verifyPaymentSignature(request.getOrderId(),
                request.getPaymentId(), request.getSignature())) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Invalid signature");
            paymentRepository.save(payment);
            throw new BadRequestException("Payment verification failed");
        }

        payment.setRazorpayPaymentId(request.getPaymentId());
        payment.setRazorpaySignature(request.getSignature());
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        // Create subscription after payment success
        createSubscription(payment);

        log.info("Payment verified and activated for order: {}", request.getOrderId());
    }

    private void createSubscription(Payment payment) {
        Subscription subscription = Subscription.builder()
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(Subscription.SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusYears(1))
                .build();

        subscriptionRepository.save(subscription);
        payment.setSubscriptionId(subscription.getId());
        paymentRepository.save(payment);
    }

    private BigDecimal calculateAmount(String plan, String frequency) {
        BigDecimal monthlyAmount = switch (plan) {
            case "STARTER" -> new BigDecimal("299");
            case "PROFESSIONAL" -> new BigDecimal("999");
            case "ENTERPRISE" -> new BigDecimal("2999");
            default -> throw new BadRequestException("Invalid plan");
        };

        if ("YEARLY".equals(frequency)) {
            return monthlyAmount.multiply(new BigDecimal("12")).multiply(new BigDecimal("0.9")); // 10% discount
        }

        return monthlyAmount;
    }

    public Subscription getUserSubscription(String userId) {
        return subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No subscription found"));
    }
}

