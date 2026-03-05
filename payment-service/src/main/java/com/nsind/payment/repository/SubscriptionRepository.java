package com.nsind.payment.repository;

import com.nsind.payment.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    Optional<Subscription> findByUserId(String userId);
    List<Subscription> findAllByUserId(String userId);
    List<Subscription> findByStatus(Subscription.SubscriptionStatus status);
}

