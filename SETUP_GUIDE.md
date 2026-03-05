# AI Chatbot SaaS Platform - Setup & Documentation

## 🚀 Project Overview

This is a complete, production-ready microservices-based SaaS platform that allows businesses to create AI chatbots trained on their custom data. The system includes user authentication, payment processing, document management, vector embeddings, and a deployable chatbot widget.

## 📋 Table of Contents
1. [Architecture](#architecture)
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
4. [Running the Services](#running-the-services)
5. [API Documentation](#api-documentation)
6. [Database Schema](#database-schema)
7. [Configuration](#configuration)
8. [Deployment](#deployment)

## 🏗️ Architecture

### Microservices Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway (Port 8080)                   │
│                    (Spring Cloud Gateway)                        │
└────┬────────────────┬────────────────┬────────────────┬──────────┘
     │                │                │                │
┌────▼────┐  ┌────────▼─────┐  ┌──────▼──────┐  ┌────▼────────┐
│   Auth   │  │  Payment     │  │  Document   │  │  Chatbot    │
│ Service  │  │  Service     │  │  Service    │  │  Service    │
│ (8081)   │  │  (8082)      │  │  (8083)     │  │  (8085)     │
└────┬────┘  └────────┬─────┘  └──────┬──────┘  └────┬────────┘
     │                │               │              │
     └────────────────┴───────────────┴──────────────┘
                       │
            ┌──────────▼──────────┐
            │   Common Database   │
            │    (PostgreSQL)     │
            └─────────────────────┘

Other Services:
- Embedding Service (8084) - OpenAI Integration
- Vector Search Service - Elasticsearch
- Widget API Service (8086) - JavaScript Widget Generation
- User Service (8087) - Profile Management
- Eureka Registry (8761) - Service Discovery
```

## 📦 Microservices Breakdown

| Service | Port | Purpose |
|---------|------|---------|
| **api-gateway** | 8080 | Central entry point, request routing, JWT validation |
| **auth-service** | 8081 | User registration, login, token generation & validation |
| **payment-service** | 8082 | Razorpay integration, subscription management |
| **document-service** | 8083 | File upload, text extraction, chunking |
| **embedding-service** | 8084 | OpenAI API integration, vector generation |
| **chatbot-service** | 8085 | Chatbot creation, message handling, LLM integration |
| **widget-api-service** | 8086 | JavaScript widget generation & serving |
| **user-service** | 8087 | User profile management |
| **eureka-server** | 8761 | Service discovery & registration |

## 🔧 Prerequisites

### System Requirements
- **OS**: Windows, macOS, or Linux
- **Java**: JDK 17 or higher
- **Maven**: 3.8.0 or higher
- **Docker**: 20.10+ (for containerized setup)
- **Docker Compose**: 1.29+ (for multi-container orchestration)

### External APIs & Services
- **OpenAI API Key** - For embedding generation
- **Razorpay Account** - For payment processing
- PostgreSQL 15
- Elasticsearch 8.10
- Redis 7

## 💾 Installation

### Step 1: Clone the Repository
```bash
cd ns_aiBot
```

### Step 2: Install Dependencies
```bash
mvn clean install -DskipTests
```

### Step 3: Create Environment Configuration

Create a `.env` file in the root directory:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_USER=root
DB_PASSWORD=password
DB_NAME=ai_chatbot_db

# JWT Configuration
JWT_SECRET=your-super-secret-key-change-in-production-min-32-chars-long-xxx-at-least-32
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# OpenAI Configuration
OPENAI_API_KEY=sk-your-actual-openai-api-key-here
OPENAI_API_URL=https://api.openai.com/v1/embeddings
OPENAI_MODEL=text-embedding-ada-002

# Razorpay Configuration
RAZORPAY_KEY_ID=your_razorpay_test_key_id
RAZORPAY_KEY_SECRET=your_razorpay_test_key_secret

# Eureka Discovery
EUREKA_URL=http://localhost:8761/eureka

# File Upload
FILE_UPLOAD_DIR=uploads
WIDGET_API_URL=http://localhost:8086/api/v1/widget
```

## 🚀 Running the Services

### Option A: Using Docker Compose (Recommended)

1. **Build JAR files**:
   ```bash
   mvn clean package -DskipTests
   ```

2. **Start all services**:
   ```bash
   docker-compose up -d
   ```

3. **Check service status**:
   ```bash
   docker-compose ps
   ```

4. **View logs**:
   ```bash
   docker-compose logs -f auth-service
   ```

5. **Stop all services**:
   ```bash
   docker-compose down
   ```

### Option B: Local Development Setup

#### 1. Start Infrastructure Services

```bash
# Start PostgreSQL
docker run -d \
  --name pg-db \
  -p 5432:5432 \
  -e POSTGRES_USER=root \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=ai_chatbot_db \
  postgres:15-alpine

# Start Elasticsearch
docker run -d \
  --name es-db \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  docker.elastic.co/elasticsearch/elasticsearch:8.10.0

# Start Redis
docker run -d \
  --name redis-cache \
  -p 6379:6379 \
  redis:7-alpine
```

#### 2. Run Each Microservice

Open separate terminal windows for each service:

```bash
# Terminal 1: Auth Service
cd auth-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

# Terminal 2: Payment Service
cd payment-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"

# Terminal 3: Document Service
cd document-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8083"

# Terminal 4: Embedding Service
cd embedding-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8084"

# Terminal 5: Chatbot Service
cd chatbot-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8085"

# Terminal 6: Widget API Service
cd widget-api-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8086"

# Terminal 7: User Service
cd user-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8087"

# Terminal 8: API Gateway (Start last)
cd api-gateway
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8080"
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication Endpoints

#### Register User
```bash
POST /auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "firstName": "John",
  "lastName": "Doe",
  "companyName": "Acme Corp"
}

Response:
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "userId": "uuid",
    "email": "user@example.com",
    "companyName": "Acme Corp",
    "subscriptionPlan": "FREE"
  }
}
```

#### Login User
```bash
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}

Response: Same as signup
```

#### Validate Token
```bash
GET /auth/validate
Authorization: Bearer YOUR_JWT_TOKEN

Response:
{
  "success": true,
  "message": "Token is valid"
}
```

### Document Endpoints

#### Upload Document
```bash
POST /documents/upload
Authorization: Bearer YOUR_JWT_TOKEN
X-User-Id: user_uuid
Content-Type: multipart/form-data

Form Data:
  file: <binary file content>

Response:
{
  "success": true,
  "data": {
    "id": "doc_uuid",
    "fileName": "document.pdf",
    "fileType": ".pdf",
    "fileSize": 1024000,
    "status": "UPLOADED",
    "chunkCount": 0
  }
}
```

#### Process Document
```bash
POST /documents/process/{documentId}
Authorization: Bearer YOUR_JWT_TOKEN
X-User-Id: user_uuid

Response:
{
  "success": true,
  "message": "Document processing started"
}
```

#### Get User Documents
```bash
GET /documents/{userId}
Authorization: Bearer YOUR_JWT_TOKEN

Response:
{
  "success": true,
  "data": [
    {
      "id": "doc_uuid",
      "fileName": "document.pdf",
      "status": "COMPLETED",
      "chunkCount": 45
    }
  ]
}
```

### Payment Endpoints

#### Create Payment Order
```bash
POST /payments/order
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "userId": "user_uuid",
  "plan": "PROFESSIONAL",
  "paymentFrequency": "MONTHLY",
  "amount": 999
}

Response:
{
  "success": true,
  "data": {
    "orderId": "order_id",
    "keyId": "key_id",
    "amount": "999",
    "currency": "INR"
  }
}
```

#### Verify Payment
```bash
POST /payments/verify
Content-Type: application/json

{
  "orderId": "razorpay_order_id",
  "paymentId": "razorpay_payment_id",
  "signature": "razorpay_signature"
}

Response:
{
  "success": true,
  "message": "Payment verified successfully"
}
```

### Chatbot Endpoints

#### Create Chatbot
```bash
POST /chatbot/create
Authorization: Bearer YOUR_JWT_TOKEN
X-User-Id: user_uuid

{
  "name": "Support Bot",
  "description": "Customer support chatbot"
}

Response:
{
  "success": true,
  "data": {
    "id": "bot_uuid",
    "userId": "user_uuid",
    "name": "Support Bot",
    "apiKey": "api_key_uuid"
  }
}
```

#### Send Message
```bash
POST /chatbot/chat
Content-Type: application/json

{
  "chatbotId": "bot_uuid",
  "userId": "user_uuid",
  "message": "Hello, how can I help?",
  "apiKey": "api_key_uuid"
}

Response:
{
  "success": true,
  "data": {
    "messageId": "msg_uuid",
    "response": "I'm here to help you with...",
    "timestamp": 1640000000000
  }
}
```

### Widget Endpoints

#### Generate Widget Script
```bash
POST /widget/generate-script
Content-Type: application/json

{
  "chatbotId": "bot_uuid",
  "apiKey": "api_key_uuid",
  "widgetPosition": "bottom-right",
  "widgetTheme": "light"
}

Response:
{
  "success": true,
  "data": "(function() { ... })()"
}
```

#### Get Widget Installation Code
```bash
GET /widget/installation/{chatbotId}/{apiKey}

Response:
<script src="http://localhost:8086/api/v1/widget/chatbot.js"></script>
<script>
ChatBot.init({
  chatbotId: 'bot_uuid',
  apiKey: 'api_key_uuid'
})
</script>
```

## 💾 Database Schema

### Core Tables

#### Users
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    company_name VARCHAR(255) UNIQUE,
    subscription_plan VARCHAR(50) DEFAULT 'FREE',
    is_active BOOLEAN DEFAULT true,
    is_email_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Subscriptions
```sql
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    plan VARCHAR(50) NOT NULL,
    payment_frequency VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Documents
```sql
CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    file_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'UPLOADED',
    extracted_text TEXT,
    chunk_count INTEGER DEFAULT 0,
    processing_started_at TIMESTAMP,
    processing_completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Chatbots
```sql
CREATE TABLE chatbots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    api_key VARCHAR(255) UNIQUE NOT NULL,
    system_prompt TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Chat Messages
```sql
CREATE TABLE chat_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    chatbot_id UUID NOT NULL REFERENCES chatbots(id),
    user_message TEXT NOT NULL,
    bot_response TEXT NOT NULL,
    context TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## ⚙️ Configuration

### JWT Configuration
Update in each service's `application.properties`:
```properties
jwt.secret=your-secure-secret-key-min-32-chars
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

### Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_chatbot_db
spring.datasource.username=root
spring.datasource.password=password
```

### OpenAI Configuration
```properties
openai.api-key=sk-your-api-key
openai.api-url=https://api.openai.com/v1/embeddings
openai.model=text-embedding-ada-002
```

### Razorpay Configuration
```properties
razorpay.key-id=your-key-id
razorpay.key-secret=your-key-secret
```

## 🌐 Swagger UI Documentation

Access API documentation at:
- API Gateway: http://localhost:8080/swagger-ui.html
- Auth Service: http://localhost:8081/swagger-ui.html
- Payment Service: http://localhost:8082/swagger-ui.html
- Document Service: http://localhost:8083/swagger-ui.html
- Chatbot Service: http://localhost:8085/swagger-ui.html
- Widget API: http://localhost:8086/swagger-ui.html

## 🐳 Docker Deployment

### Build Docker Images
```bash
mvn clean package -DskipTests
docker-compose build
```

### Deploy Services
```bash
docker-compose up -d
```

### Monitor Services
```bash
docker-compose logs -f
```

### Scale Services
```bash
docker-compose up -d --scale auth-service=3
```

## 🔒 Security Best Practices

1. **JWT Secret**: Use a strong, randomly generated secret (minimum 32 characters)
2. **HTTPS**: Always use HTTPS in production
3. **API Keys**: Store API keys in environment variables, not in code
4. **CORS**: Configure CORS properly for your domain
5. **Rate Limiting**: Implement rate limiting on API Gateway
6. **Password Security**: Use BCrypt or Argon2 for password hashing
7. **SQL Injection**: Use prepared statements (handled by Spring Data JPA)

## 📊 Performance Considerations

1. **Caching**: Redis is configured for session caching
2. **Database Indexing**: Add indexes on frequently queried columns
3. **Elasticsearch**: Use for fast full-text search
4. **Async Processing**: Document processing runs asynchronously
5. **Load Balancing**: Use load balancer before API Gateway in production

## 🐛 Troubleshooting

### Service Won't Start
- Check if ports are already in use
- Verify database connection
- Check logs for detailed error messages

### Database Connection Error
```bash
# Check PostgreSQL is running
docker ps | grep postgres
# Reset database
docker exec pg-db psql -U root -d postgres -c "DROP DATABASE ai_chatbot_db; CREATE DATABASE ai_chatbot_db;"
```

### JWT Token Expired
Use the refresh token endpoint:
```bash
POST /api/v1/auth/refresh
Authorization: Bearer <refresh_token>
```

## 📞 Support

For issues and questions:
1. Check service logs: `docker-compose logs <service-name>`
2. Verify environment variables
3. Check database connectivity
4. Review API documentation in Swagger UI

## 📄 License

MIT License - Use freely for commercial projects

