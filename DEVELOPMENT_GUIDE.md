# 💡 Development Tips & Best Practices

A comprehensive guide for developing and extending the AI Chatbot SaaS Platform.

---

## 🎯 Development Workflow

### Setting Up Your Development Environment

#### Prerequisites
- IntelliJ IDEA / VS Code / Eclipse
- Java 17 SDK
- Maven 3.8+
- Git
- Docker & Docker Compose

#### IDE Setup (IntelliJ IDEA)
1. File → Open → Select project root
2. Trust the project
3. Maven will auto-import
4. Configure SDK: File → Project Structure → SDK → Select Java 17
5. Enable Lombok: File → Settings → Plugins → Search "Lombok" → Install

#### Running Services from IDE
```
1. Start infrastructure: docker-compose up -d postgres elasticsearch redis
2. For each service:
   - Right-click on [ServiceName]Application.java
   - Select "Run"
   - Or use Run Configuration with Spring Boot
3. Services will run on their configured ports
```

---

## 🔍 Code Structure Best Practices

### Microservice Structure (Follow This Pattern)

```
service-name/
├── src/main/java/com/nsind/servicename/
│   ├── ServiceNameApplication.java ........ Main entry point
│   ├── config/ ............................ Configuration classes
│   │   ├── SecurityConfig.java
│   │   └── WebClientConfig.java
│   ├── controller/ ........................ REST endpoints
│   │   └── ServiceController.java
│   ├── service/ ........................... Business logic
│   │   ├── ServiceService.java
│   │   └── IntegrationService.java
│   ├── repository/ ........................ Data access
│   │   └── ServiceRepository.java
│   ├── entity/ ............................ Domain models
│   │   └── Entity.java
│   ├── dto/ .............................. Data transfer objects
│   │   ├── ServiceRequest.java
│   │   └── ServiceResponse.java
│   ├── exception/ ......................... Exception handling
│   │   └── GlobalExceptionHandler.java
│   └── util/ ............................. Utilities
│       └── ServiceUtil.java
├── src/main/resources/
│   ├── application.properties ............ Service configuration
│   └── application-{profile}.properties . Profile-specific config
├── src/test/java/
│   └── com/nsind/servicename/
│       ├── ServiceTest.java
│       └── IntegrationTest.java
└── pom.xml .............................. Maven configuration
```

### Naming Conventions

```java
// Classes
public class UserService { }           // Service classes
public class UserRepository { }         // Repository classes
public class UserController { }         // Controller classes
public class User { }                   // Entity classes
public class UserRequest { }            // DTO classes
public class GlobalExceptionHandler { } // Exception handlers

// Methods
public List<User> getAllUsers() { }            // Query methods
public Optional<User> findByEmail() { }        // Query methods
public void createUser() { }                   // Action methods
public void updateUser() { }                   // Action methods
public void deleteUser() { }                   // Action methods

// Variables
private String email;               // camelCase
private static final String API_KEY = "..."; // UPPER_CASE for constants
private static final int DEFAULT_PAGE_SIZE = 20;
```

---

## 🧪 Testing Strategy

### Unit Testing Example
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testCreateUserSuccess() {
        // Arrange
        UserRequest request = new UserRequest("test@example.com", "password");
        User expectedUser = new User();
        when(userRepository.save(any())).thenReturn(expectedUser);
        
        // Act
        User result = userService.createUser(request);
        
        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }
}
```

### Integration Testing Example
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testSignupEndpoint() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"test@example.com","password":"Pass123!"}
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.email").exists());
    }
}
```

---

## 🔒 Security Best Practices

### Handling Secrets
```java
// ❌ WRONG - Never hardcode secrets
private String apiKey = "sk-actual-key-123";

// ✅ CORRECT - Use environment variables
@Value("${openai.api-key}")
private String apiKey;

// ✅ CORRECT - Use @ConfigurationProperties
@ConfigurationProperties(prefix = "openai")
public class OpenAiConfig {
    private String apiKey;
    // getter/setter
}
```

### Input Validation
```java
// Validate at controller level
@PostMapping("/users")
public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest request) {
    // Spring validates with @Valid annotation
}

// Create custom validator if needed
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
```

### Password Security
```java
// Use BCrypt for password hashing
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10); // 10 rounds
}

// When storing password
String hashedPassword = passwordEncoder.encode(rawPassword);

// When verifying password
if (passwordEncoder.matches(rawPassword, hashedPassword)) {
    // Login successful
}
```

---

## 📝 Logging Best Practices

### Logging Levels
```java
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// ERROR: System errors, exceptions
logger.error("Failed to process order: {}", orderId, exception);

// WARN: Potential issues, deprecated usage
logger.warn("Database connection pool is at 80% capacity");

// INFO: Important business events
logger.info("User registered successfully: {}", email);

// DEBUG: Detailed diagnostic info (development)
logger.debug("Processing document chunks: {} chunks for {}", count, docId);

// TRACE: Very detailed flow (rarely used)
logger.trace("Executing SQL query: {}", query);

// Using Lombok @Slf4j
@Slf4j
public class MyService {
    public void doSomething() {
        log.info("Doing something");
    }
}
```

### Structured Logging Example
```java
@RestController
@Slf4j
public class UserController {
    
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        log.info("User registration attempt: email={}", request.getEmail());
        
        try {
            User user = userService.register(request);
            log.info("User registered successfully: userId={}, email={}", 
                user.getId(), user.getEmail());
            return ResponseEntity.ok(user);
        } catch (DuplicateEmailException e) {
            log.warn("Registration failed: duplicate email: {}", request.getEmail());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error during registration: email={}", 
                request.getEmail(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
```

---

## 🚀 Performance Optimization

### Database Query Optimization
```java
// ❌ N+1 Query Problem
List<User> users = userRepository.findAll();
for (User user : users) {
    user.getOrders().size(); // Causes additional queries
}

// ✅ SOLUTION: Use @EntityGraph
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = "orders")
    List<User> findAll();
}

// ✅ SOLUTION: Use JOIN FETCH in JPQL
@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();
```

### Caching Strategy
```java
@Service
public class UserService {
    
    // Cache results for 5 minutes
    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    // Invalidate cache when user is updated
    @CacheEvict(value = "users", key = "#id")
    public User updateUser(String id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(request.getName());
        return userRepository.save(user);
    }
    
    // Clear all users cache
    @CacheEvict(value = "users", allEntries = true)
    public void clearUserCache() { }
}
```

### Connection Pooling
```properties
# In application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

---

## 🔄 API Development Guidelines

### RESTful Endpoint Design
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    // GET - Retrieve all (with pagination)
    @GetMapping
    public Page<UserDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getUsers(PageRequest.of(page, size));
    }
    
    // GET - Retrieve by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // POST - Create new
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest req) {
        UserDto user = userService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    // PUT - Replace entire resource
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest req) {
        UserDto user = userService.update(id, req);
        return ResponseEntity.ok(user);
    }
    
    // PATCH - Partial update
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdate(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        UserDto user = userService.partialUpdate(id, updates);
        return ResponseEntity.ok(user);
    }
    
    // DELETE - Remove resource
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Error Response Handling
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        
        ApiErrorResponse response = ApiErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
        
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ApiErrorResponse response = ApiErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Invalid input parameters")
            .validationErrors(errors)
            .path(request.getRequestURI())
            .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
```

---

## 🔌 Integrating External APIs

### OpenAI Integration Pattern
```java
@Service
@Slf4j
public class OpenAiService {
    
    @Value("${openai.api-key}")
    private String apiKey;
    
    @Value("${openai.api-url}")
    private String apiUrl;
    
    private final WebClient webClient;
    private final RestTemplate restTemplate;
    
    // Method 1: Using WebClient (Reactive)
    public Mono<EmbeddingResponse> generateEmbeddingAsync(String text) {
        return webClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + apiKey)
            .bodyValue(new EmbeddingRequest(text))
            .retrieve()
            .bodyToMono(EmbeddingResponse.class)
            .doOnError(error -> log.error("OpenAI API error: {}", error.getMessage()));
    }
    
    // Method 2: Using RestTemplate (Blocking)
    public EmbeddingResponse generateEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<EmbeddingRequest> request = 
            new HttpEntity<>(new EmbeddingRequest(text), headers);
        
        try {
            return restTemplate.postForObject(apiUrl, request, EmbeddingResponse.class);
        } catch (HttpClientErrorException e) {
            log.error("OpenAI API error: {}", e.getMessage());
            throw new ExternalServiceException("Failed to generate embedding", e);
        }
    }
}
```

### Razorpay Integration Pattern
```java
@Service
@Slf4j
public class RazorpayPaymentService {
    
    @Value("${razorpay.key-id}")
    private String keyId;
    
    @Value("${razorpay.key-secret}")
    private String keySecret;
    
    private RazorpayClient client;
    
    @PostConstruct
    public void init() throws RazorpayException {
        this.client = new RazorpayClient(keyId, keySecret);
    }
    
    public OrderResponse createOrder(BigDecimal amount, String currency) {
        try {
            JSONObject request = new JSONObject();
            request.put("amount", amount.multiply(new BigDecimal("100")).longValue());
            request.put("currency", currency);
            request.put("receipt", "receipt_" + UUID.randomUUID());
            
            Order order = client.Orders.create(request);
            
            log.info("Order created: {}", order.get("id"));
            return new OrderResponse(
                order.get("id").toString(),
                keyId,
                amount.toString(),
                currency
            );
        } catch (RazorpayException e) {
            log.error("Failed to create order: {}", e.getMessage());
            throw new PaymentException("Failed to create payment order", e);
        }
    }
}
```

---

## 📊 Database Operations

### Proper Entity Design
```java
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_company", columnList = "company_name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlan subscriptionPlan;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Subscription> subscriptions;
    
    // Lifecycle callbacks
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

### Repository Queries
```java
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // Simple query methods
    Optional<User> findByEmail(String email);
    List<User> findBySubscriptionPlan(SubscriptionPlan plan);
    boolean existsByEmail(String email);
    long countBySubscriptionPlan(SubscriptionPlan plan);
    
    // Custom JPQL queries
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.isActive = true")
    Optional<User> findActiveUserByEmail(String email);
    
    // Native SQL queries
    @Query(value = "SELECT * FROM users WHERE created_at > :date", nativeQuery = true)
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);
    
    // Pagination
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    Page<User> findAllOrderByCreatedAt(Pageable pageable);
}
```

---

## 🎯 Adding New Features

### Step-by-Step Guide

#### 1. Define DTO (Data Transfer Object)
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewFeatureRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Value is required")
    @Positive(message = "Value must be positive")
    private Integer value;
}

@Data
public class NewFeatureResponse {
    private String id;
    private String name;
    private Integer value;
    private LocalDateTime createdAt;
}
```

#### 2. Create Entity
```java
@Entity
@Table(name = "new_features")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer value;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### 3. Create Repository
```java
@Repository
public interface NewFeatureRepository extends JpaRepository<NewFeature, String> {
    Optional<NewFeature> findByName(String name);
    List<NewFeature> findByValueGreaterThan(Integer value);
}
```

#### 4. Create Service
```java
@Service
@RequiredArgsConstructor
@Slf4j
public class NewFeatureService {
    
    private final NewFeatureRepository repository;
    
    public NewFeatureResponse create(NewFeatureRequest request) {
        NewFeature feature = NewFeature.builder()
            .name(request.getName())
            .value(request.getValue())
            .build();
        
        feature = repository.save(feature);
        log.info("New feature created: {}", feature.getId());
        
        return mapToResponse(feature);
    }
    
    public List<NewFeatureResponse> getAll() {
        return repository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    private NewFeatureResponse mapToResponse(NewFeature feature) {
        return NewFeatureResponse.builder()
            .id(feature.getId())
            .name(feature.getName())
            .value(feature.getValue())
            .createdAt(feature.getCreatedAt())
            .build();
    }
}
```

#### 5. Create Controller
```java
@RestController
@RequestMapping("/api/v1/features")
@RequiredArgsConstructor
@Slf4j
public class NewFeatureController {
    
    private final NewFeatureService service;
    
    @PostMapping
    public ResponseEntity<ApiResponse<NewFeatureResponse>> create(
            @Valid @RequestBody NewFeatureRequest request) {
        NewFeatureResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Feature created"));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<NewFeatureResponse>>> getAll() {
        List<NewFeatureResponse> features = service.getAll();
        return ResponseEntity.ok(ApiResponse.success(features, "Features retrieved"));
    }
}
```

---

## 🐳 Docker Development Tips

### Build Image for Single Service
```bash
# Build just auth-service
cd auth-service
mvn clean package -DskipTests
docker build -t auth-service:latest .
```

### Dockerfile Template
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose for Single Service Testing
```yaml
version: '3.9'

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: root
      POSTGRES_PASSWORD: password
      POSTGRES_DB: test_db
    ports:
      - "5432:5432"

  my-service:
    build: ./my-service
    ports:
      - "8081:8081"
    environment:
      DB_HOST: postgres
      DB_USER: root
      DB_PASSWORD: password
    depends_on:
      - postgres
```

---

## 📈 Performance Profiling

### Enable JVM Profiling
```properties
# In application.properties
spring.jpa.properties.hibernate.generate_statistics=true
spring.jpa.properties.hibernate.use_sql_comments=true
logging.level.org.hibernate.stat=DEBUG
```

### Monitor Slow Queries
```properties
# Log queries taking more than 200ms
spring.jpa.properties.hibernate.session.events.log=true
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=DEBUG
```

---

## 🚀 Deployment Checklist

Before deploying to production:

- [ ] All tests passing
- [ ] No hardcoded credentials
- [ ] Error handling complete
- [ ] Logging implemented
- [ ] HTTPS configured
- [ ] CORS configured
- [ ] Rate limiting enabled
- [ ] Database backups configured
- [ ] Monitoring setup
- [ ] Security audit completed
- [ ] Load testing done
- [ ] Disaster recovery plan

---

This guide covers most development scenarios. For additional help, refer to the official documentation or the project's comprehensive guides.

