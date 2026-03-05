# 🧪 API Testing & Integration Guide

## Overview

This guide provides comprehensive instructions for testing the AI Chatbot SaaS Platform APIs, including unit tests, integration tests, and end-to-end tests.

---

## 📋 Test Structure

```
project/
├── chatbot-service/
│   └── src/test/java/com/nsind/chatbot/
│       ├── service/
│       │   └── ChatbotServiceTest.java (Unit Tests)
│       └── controller/
│           └── ChatbotControllerIntegrationTest.java (Integration Tests)
├── payment-service/
│   └── src/test/...
├── document-service/
│   └── src/test/...
└── common-lib/
    └── src/test/...
```

---

## 🔧 Unit Testing

### Running Unit Tests

```bash
# Run all unit tests
mvn test

# Run specific service tests
mvn test -pl chatbot-service

# Run specific test class
mvn test -Dtest=ChatbotServiceTest

# Run with coverage report
mvn test jacoco:report
```

### Example Unit Test

```java
@DisplayName("ChatbotService Tests")
class ChatbotServiceTest {
    
    @Mock
    private ChatbotRepository chatbotRepository;
    
    @InjectMocks
    private ChatbotService chatbotService;
    
    @Test
    @DisplayName("Should create chatbot successfully")
    void testCreateChatbot_Success() {
        // Arrange
        when(chatbotRepository.save(any())).thenReturn(chatbot);
        
        // Act
        Chatbot result = chatbotService.createChatbot(userId, name, description);
        
        // Assert
        assertNotNull(result);
        verify(chatbotRepository).save(any());
    }
}
```

---

## 🧬 Integration Testing

### Running Integration Tests

```bash
# Run all integration tests
mvn verify -Dtest=*IntegrationTest

# Run specific integration test
mvn verify -Dtest=ChatbotControllerIntegrationTest

# Run with output
mvn verify -Dtest=ChatbotControllerIntegrationTest -DfailIfNoTests=false
```

### Test Properties Configuration

Create `src/test/resources/application-test.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    database-platform: org.hibernate.dialect.H2Dialect
```

---

## 🚀 End-to-End Testing

### Manual API Testing with cURL

```bash
# 1. Register User
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "SecurePass123"
  }'

# 2. Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "SecurePass123"
  }'

# 3. Create Chatbot
curl -X POST http://localhost:8080/api/v1/chatbot/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "X-User-Id: YOUR_USER_ID" \
  -d '{
    "name": "My Chatbot",
    "description": "Test chatbot"
  }'

# 4. Send Message
curl -X POST http://localhost:8080/api/v1/chatbot/chat \
  -H "Content-Type: application/json" \
  -d '{
    "apiKey": "your-api-key",
    "userId": "user-id",
    "chatbotId": "chatbot-id",
    "message": "Hello, how can you help?"
  }'
```

### Using Postman

1. **Import Collection**
   - File → Import → Select `api-collection.postman_collection.json`

2. **Set Environment Variables**
   - Create environment with:
     ```json
     {
       "base_url": "http://localhost:8080",
       "api_key": "your-api-key",
       "user_id": "your-user-id",
       "token": "your-jwt-token"
     }
     ```

3. **Run Tests**
   - Select collection → Run → Execute

### Using REST Client (VS Code)

Create `requests.http`:

```http
### Register
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "SecurePass123"
}

### Login
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "SecurePass123"
}

### Create Chatbot
POST http://localhost:8080/api/v1/chatbot/create
Content-Type: application/json
Authorization: Bearer {{token}}
X-User-Id: {{user_id}}

{
  "name": "My Chatbot",
  "description": "Test chatbot"
}
```

---

## 📊 Test Coverage

### Generate Coverage Report

```bash
# Using JaCoCo
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html  # macOS
start target\site\jacoco\index.html # Windows
```

### Coverage Targets

- **Overall**: 80%+
- **Critical Paths**: 90%+
- **Controllers**: 85%+
- **Services**: 85%+
- **Repositories**: 70%+

---

## 🔐 Security Testing

### SQL Injection Testing

```bash
# Test vulnerable endpoint
curl "http://localhost:8080/api/v1/chatbot/search?query='; DROP TABLE chatbots; --"

# Should fail gracefully without executing SQL
```

### XSS Prevention Testing

```bash
# Test with script injection
curl -X POST http://localhost:8080/api/v1/chatbot/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "<script>alert(\"XSS\")</script>",
    "description": "Test"
  }'

# Should sanitize input or reject
```

### Authentication Testing

```bash
# Without token
curl http://localhost:8080/api/v1/protected-endpoint

# With invalid token
curl -H "Authorization: Bearer invalid-token" \
  http://localhost:8080/api/v1/protected-endpoint

# Should return 401 Unauthorized
```

---

## 📈 Performance Testing

### Load Testing with JMeter

1. **Create Test Plan**
   - Number of Threads: 100
   - Ramp-up Period: 10s
   - Loop Count: 10

2. **Add HTTP Request**
   - URL: `http://localhost:8080/api/v1/chatbot/create`
   - Method: POST
   - Body: JSON payload

3. **Run and Analyze**
   - Average response time
   - Error rate
   - Throughput

### Load Testing with Apache Bench

```bash
# Simple load test
ab -n 1000 -c 100 http://localhost:8080/api/v1/health

# POST requests with file
ab -n 1000 -c 100 -p payload.json -T application/json \
  http://localhost:8080/api/v1/chatbot/chat
```

---

## ✅ Testing Checklist

### Unit Tests
- [ ] Service layer methods
- [ ] Error handling
- [ ] Business logic validation
- [ ] Edge cases

### Integration Tests
- [ ] API endpoints
- [ ] Database operations
- [ ] Service interactions
- [ ] Error responses

### End-to-End Tests
- [ ] User registration flow
- [ ] Login and authentication
- [ ] Document upload
- [ ] Chatbot creation
- [ ] Payment processing
- [ ] Message sending

### Performance Tests
- [ ] Load testing (1000+ requests)
- [ ] Stress testing (peak load)
- [ ] Soak testing (sustained load)
- [ ] Spike testing (sudden increase)

### Security Tests
- [ ] SQL injection prevention
- [ ] XSS prevention
- [ ] CSRF protection
- [ ] Authentication/Authorization
- [ ] Rate limiting

---

## 🐛 Debugging Tests

### Enable Debug Logging

```properties
logging.level.com.nsind=DEBUG
logging.level.org.springframework.test=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Run Single Test with Debug

```bash
mvn test -Dtest=ChatbotServiceTest#testCreateChatbot_Success -X
```

### Print Test Output

```bash
mvn test -Dtest=ChatbotServiceTest -DTEST_DEBUG=true
```

---

## 📚 Best Practices

### Do's
✅ Write tests for critical business logic
✅ Use descriptive test names
✅ Follow AAA pattern (Arrange, Act, Assert)
✅ Mock external dependencies
✅ Test error scenarios
✅ Keep tests independent
✅ Use parameterized tests for multiple inputs

### Don'ts
❌ Don't test framework code
❌ Don't create tight coupling between tests
❌ Don't use hardcoded values
❌ Don't ignore flaky tests
❌ Don't test UI in unit tests
❌ Don't make tests dependent on execution order

---

## 🔗 Related Documentation

- [Testing Guide](./TESTING_GUIDE.md)
- [Architecture Documentation](./ARCHITECTURE.md)
- [API Documentation](./API_DOCUMENTATION.md)
- [Security Guidelines](./SECURITY_GUIDELINES.md)

---

**Last Updated**: March 5, 2026  
**Status**: COMPLETE  
**Version**: 1.0


