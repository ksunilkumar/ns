# ============================================================
# AI Chatbot SaaS Platform - Production Configuration Guide
# ============================================================

## 🔒 SECURITY - CRITICAL

### 1. Secrets Management (Use AWS Secrets Manager / HashiCorp Vault)
```bash
# DO NOT hardcode these in properties files!

# Example with AWS Secrets Manager:
aws secretsmanager create-secret \
  --name ai-chatbot/openai-api-key \
  --secret-string "sk-..."

# Retrieve in code:
AWS SecretsManager -> GetSecretValue(secret-name)
```

### 2. JWT Configuration
```properties
# Generate strong secret keys:
# OpenSSL: openssl rand -hex 32

jwt.secret=${JWT_SECRET_KEY}
jwt.refresh.secret=${JWT_REFRESH_SECRET_KEY}
jwt.expiration.ms=${JWT_EXPIRATION_MS:3600000}
jwt.refresh.expiration.ms=${JWT_REFRESH_EXPIRATION_MS:604800000}
```

### 3. Database Credentials
```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

# Example: jdbc:postgresql://prod-db.aws.com:5432/chatbot_prod
```

### 4. External API Keys
```properties
openai.api-key=${OPENAI_API_KEY}
openai.organization.id=${OPENAI_ORG_ID:}

razorpay.key.id=${RAZORPAY_KEY_ID}
razorpay.key.secret=${RAZORPAY_KEY_SECRET}
```

### 5. Elasticsearch Configuration
```properties
spring.elasticsearch.uris=${ELASTICSEARCH_URI}
spring.elasticsearch.username=${ELASTICSEARCH_USERNAME}
spring.elasticsearch.password=${ELASTICSEARCH_PASSWORD}
```

### 6. Redis Configuration
```properties
spring.redis.host=${REDIS_HOST}
spring.redis.port=${REDIS_PORT:6379}
spring.redis.password=${REDIS_PASSWORD}
spring.redis.ssl=${REDIS_SSL:false}
```

---

## 📊 MONITORING & OBSERVABILITY

### 1. Actuator Endpoints
```properties
# Enable health checks
management.endpoints.web.exposure.include=health,metrics,info,prometheus
management.endpoint.health.show-details=when-authorized
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true
```

### 2. Prometheus Metrics
```properties
management.metrics.export.prometheus.enabled=true
management.metrics.tags.application=${spring.application.name}
management.metrics.tags.environment=${ENVIRONMENT:development}
```

### 3. Distributed Tracing
```properties
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://zipkin:9411
```

### 4. Logging
```properties
logging.level.root=INFO
logging.level.com.nsind=DEBUG
logging.file.name=/var/log/chatbot/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

---

## 🔄 RESILIENCE & PERFORMANCE

### 1. Circuit Breaker Configuration
```properties
# For external API calls
resilience4j.circuitbreaker.instances.openai.register-health-indicator=true
resilience4j.circuitbreaker.instances.openai.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.openai.wait-duration-in-open-state=30s
resilience4j.circuitbreaker.instances.openai.permitted-calls-in-half-open-state=3
```

### 2. Retry Configuration
```properties
resilience4j.retry.instances.api-retry.max-attempts=3
resilience4j.retry.instances.api-retry.wait-duration=500ms
resilience4j.retry.instances.api-retry.retry-exceptions=java.io.IOException,org.springframework.web.client.ResourceAccessException
```

### 3. Timeout Configuration
```properties
# API timeouts
server.servlet.session.timeout=30m
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

---

## 🌍 ENVIRONMENT VARIABLES TEMPLATE

Create `.env.production` file:
```bash
# Application
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
ENVIRONMENT=production

# Database
DATABASE_URL=jdbc:postgresql://prod-db:5432/chatbot_prod
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=secure_password_here
DATABASE_MAX_POOL_SIZE=20

# JWT
JWT_SECRET_KEY=your_long_random_secret_key_here
JWT_REFRESH_SECRET_KEY=your_long_random_refresh_key_here
JWT_EXPIRATION_MS=3600000
JWT_REFRESH_EXPIRATION_MS=604800000

# OpenAI
OPENAI_API_KEY=sk-your_openai_key_here
OPENAI_ORG_ID=org-xxxxx

# Razorpay
RAZORPAY_KEY_ID=your_key_id
RAZORPAY_KEY_SECRET=your_key_secret

# Elasticsearch
ELASTICSEARCH_URI=https://elasticsearch:9200
ELASTICSEARCH_USERNAME=elastic
ELASTICSEARCH_PASSWORD=changeme

# Redis
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password

# Services URLs
AUTH_SERVICE_URL=http://auth-service:8081
PAYMENT_SERVICE_URL=http://payment-service:8082
DOCUMENT_SERVICE_URL=http://document-service:8083

# Monitoring
ZIPKIN_ENDPOINT=http://zipkin:9411
PROMETHEUS_ENDPOINT=http://prometheus:9090

# Email (for notifications)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# File Upload
FILE_UPLOAD_DIR=/data/uploads
MAX_FILE_SIZE=10485760
ALLOWED_EXTENSIONS=pdf,txt,docx,xlsx,csv

# API Rate Limiting
RATE_LIMIT_ENABLED=true
RATE_LIMIT_REQUESTS_PER_MINUTE=100

# CORS
CORS_ALLOWED_ORIGINS=https://yourdomain.com
```

---

## 🐳 KUBERNETES DEPLOYMENT

### 1. ConfigMap for non-sensitive properties
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: chatbot-config
data:
  application.properties: |
    server.port=8080
    management.endpoints.web.exposure.include=health,metrics,prometheus
    spring.jpa.hibernate.ddl-auto=validate
```

### 2. Secret for sensitive data
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: chatbot-secrets
type: Opaque
data:
  jwt-secret: <base64_encoded_secret>
  openai-api-key: <base64_encoded_key>
  razorpay-secret: <base64_encoded_secret>
  database-password: <base64_encoded_password>
```

---

## 🔐 API KEY ROTATION STRATEGY

### 1. Gradual Rollout
- Issue new keys before expiring old ones
- Support both old and new keys for grace period (7 days)
- Log all key usage for audit trail

### 2. Automated Rotation
```java
@Scheduled(cron = "0 0 * * * 1") // Every Monday at midnight
public void rotateApiKeys() {
    List<ApiKey> expiringKeys = apiKeyRepository.findExpiringKeys();
    for (ApiKey key : expiringKeys) {
        String newKey = generateNewKey();
        key.setRotatedKey(newKey);
        key.setRotationDate(LocalDateTime.now());
        apiKeyRepository.save(key);
        // Notify users
    }
}
```

---

## 📋 PRE-PRODUCTION CHECKLIST

- [ ] All secrets stored in secure vault
- [ ] Database credentials using environment variables
- [ ] JWT secrets (not hardcoded)
- [ ] API keys for OpenAI and Razorpay secured
- [ ] HTTPS/TLS enabled for all endpoints
- [ ] Database backups configured
- [ ] Monitoring and alerting setup
- [ ] Logging aggregation configured
- [ ] Rate limiting enabled
- [ ] CORS properly configured
- [ ] Security headers configured
- [ ] Load testing completed
- [ ] Disaster recovery plan documented
- [ ] Incident response procedures ready

---

## 🚀 DEPLOYMENT COMMAND

```bash
# 1. Set environment variables
export $(cat .env.production | xargs)

# 2. Start all services
docker-compose -f docker-compose.prod.yml up -d

# 3. Run database migrations
./mvnw liquibase:update

# 4. Verify health
curl http://localhost:8080/actuator/health

# 5. Monitor logs
docker-compose -f docker-compose.prod.yml logs -f

# 6. Setup monitoring dashboard
# Access Prometheus: http://localhost:9090
# Access Grafana: http://localhost:3000
```

---

**Last Updated**: March 5, 2026  
**Status**: PRODUCTION READY  
**Compliance**: Enterprise-Grade Security


