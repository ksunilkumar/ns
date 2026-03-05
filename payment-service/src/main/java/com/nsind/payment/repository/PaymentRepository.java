package com.nsind.payment.repository;

import com.nsind.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByRazorpayOrderId(String orderId);
    List<Payment> findByUserId(String userId);
    List<Payment> findBySubscriptionId(String subscriptionId);
}

