package com.nsind.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.nsind.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class RazorpayService {

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    private RazorpayClient client;

    private void initClient() throws RazorpayException {
        if (client == null) {
            client = new RazorpayClient(keyId, keySecret);
        }
    }

    public Order createOrder(BigDecimal amount, String currency, String receiptId) throws RazorpayException {
        try {
            initClient();

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(new BigDecimal("100")).longValue());
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", receiptId);

            Order order = client.Orders.create(orderRequest);
            log.info("Order created: {}", order.get("id"));
            return order;
        } catch (RazorpayException e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new BadRequestException("Failed to create payment order");
        }
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;
            String calculatedSignature = generateHmacSHA256(data, keySecret);
            boolean verified = calculatedSignature.equals(signature);

            if (verified) {
                log.info("Payment signature verified for order: {}", orderId);
            } else {
                log.warn("Invalid payment signature for order: {}", orderId);
            }

            return verified;
        } catch (Exception e) {
            log.error("Error verifying signature: {}", e.getMessage());
            return false;
        }
    }

    private String generateHmacSHA256(String data, String secret) throws Exception {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKey);

        byte[] hash = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}

