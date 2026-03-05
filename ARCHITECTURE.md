# AI Chatbot SaaS Platform - System Architecture & Design

## 📐 System Architecture Overview

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           CLIENT LAYER                                   │
│  ┌─────────────┐    ┌──────────────┐    ┌──────────────────────────┐  │
│  │ Web Browser │    │Mobile App    │    │ Third-party Website      │  │
│  │  (React)    │    │  (Native)    │    │ (Chatbot Widget)         │  │
│  └──────┬──────┘    └──────┬───────┘    └────────────┬─────────────┘  │
│         │                  │                         │                  │
└─────────┼──────────────────┼─────────────────────────┼──────────────────┘
          │                  │                         │
          │ HTTPS            │ REST/WebSocket          │ Widget SDK
          │                  │                         │
┌─────────▼──────────────────▼─────────────────────────▼──────────────────┐
│                      API GATEWAY (Port 8080)                             │
│     (Spring Cloud Gateway - Central Entry Point)                         │
│  ┌────────────────────────────────────────────────────────────────────┐ │
│  │ Request Routing │ JWT Validation │ Rate Limiting │ Load Balancing │ │
│  └────────────────────────────────────────────────────────────────────┘ │
└─────────┬──────────────────────────────────────────────────────────────┬─┘
          │                                                               │
    ┌─────▼──────────────────────────────────────────────────────────────▼──┐
    │                     MICROSERVICES LAYER                               │
    │                                                                        │
    │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │
    │  │  Auth        │  │  Payment     │  │  Document    │               │
    │  │  Service     │  │  Service     │  │  Service     │               │
    │  │  (8081)      │  │  (8082)      │  │  (8083)      │               │
    │  └──────────────┘  └──────────────┘  └──────────────┘               │
    │                                                                        │
    │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │
    │  │  Embedding   │  │  Chatbot     │  │  Widget API  │               │
    │  │  Service     │  │  Service     │  │  Service     │               │
    │  │  (8084)      │  │  (8085)      │  │  (8086)      │               │
    │  └──────────────┘  └──────────────┘  └──────────────┘               │
    │                                                                        │
    │  ┌──────────────┐  ┌──────────────┐                                 │
    │  │  User        │  │  Eureka      │                                 │
    │  │  Service     │  │  Registry    │                                 │
    │  │  (8087)      │  │  (8761)      │                                 │
    │  └──────────────┘  └──────────────┘                                 │
    │                                                                        │
    └────────┬─────────────────────────────────────────────────┬───────────┘
             │                                                 │
    ┌────────▼─────────────────────────────────────────────────▼──────────┐
    │                    DATA LAYER                                        │
    │                                                                      │
    │  ┌──────────────────────┐  ┌──────────────────────┐               │
    │  │  PostgreSQL 15       │  │  Elasticsearch 8.10  │               │
    │  │  (Primary Database)  │  │  (Vector Search DB)  │               │
    │  │                      │  │                      │               │
    │  │ - Users             │  │ - Embeddings         │               │
    │  │ - Subscriptions      │  │ - Document Chunks    │               │
    │  │ - Documents          │  │ - Vector Vectors     │               │
    │  │ - Chatbots          │  │ - Search Indices     │               │
    │  │ - Chat Messages      │  │                      │               │
    │  └──────────────────────┘  └──────────────────────┘               │
    │                                                                      │
    │  ┌──────────────────────┐  ┌──────────────────────┐               │
    │  │  Redis 7             │  │  File Storage        │               │
    │  │  (Cache Layer)       │  │  (Local/S3/GCS)      │               │
    │  │                      │  │                      │               │
    │  │ - Session Cache      │  │ - Uploaded Files     │               │
    │  │ - API Cache          │  │ - Generated Assets   │               │
    │  │ - Rate Limit Tokens  │  │                      │               │
    │  └──────────────────────┘  └──────────────────────┘               │
    │                                                                      │
    └──────────────────────────────────────────────────────────────────────┘
             │                                                 │
             └─────────────────────┬───────────────────────────┘
                                   │
                    ┌──────────────▼──────────────┐
                    │  EXTERNAL SERVICES          │
                    │  ┌──────────────────────┐   │
                    │  │ OpenAI API           │   │
                    │  │ (Embeddings & Chat)  │   │
                    │  └──────────────────────┘   │
                    │  ┌──────────────────────┐   │
                    │  │ Razorpay API         │   │
                    │  │ (Payment Processing) │   │
                    │  └──────────────────────┘   │
                    │  ┌──────────────────────┐   │
                    │  │ Email Service        │   │
                    │  │ (Notifications)      │   │
                    │  └──────────────────────┘   │
                    └──────────────────────────────┘
```

## 🔄 Data Flow Diagrams

### User Registration & Authentication Flow

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ POST /auth/signup
       ├─ Email
       ├─ Password
       └─ Company Info
       │
       ▼
┌──────────────────────┐
│  API Gateway         │
│  (Request Validation)│
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│  Auth Service        │
│  ┌────────────────┐  │
│  │ Validate Input │  │
│  │ Hash Password  │  │
│  │ Create User    │  │
│  └────────┬───────┘  │
└───────────┼──────────┘
            │
            ▼
    ┌──────────────┐
    │  PostgreSQL  │
    │  (Save User) │
    └──────┬───────┘
           │
           ▼
┌──────────────────────┐
│  JWT Token Generate  │
│  - Access Token      │
│  - Refresh Token     │
└──────────┬───────────┘
           │
           ▼
┌─────────────┐
│   Client    │ ◄─ Return Tokens
└─────────────┘
```

### Document Upload & Processing Flow

```
┌──────────────┐
│   Client     │
└──────┬───────┘
       │ Upload File (PDF/DOC/etc)
       │
       ▼
┌────────────────────────┐
│  API Gateway           │
│  JWT Validation        │
└──────┬─────────────────┘
       │
       ▼
┌────────────────────────────────┐
│  Document Service              │
│  ┌──────────────────────────┐  │
│  │ 1. Validate File         │  │
│  │ 2. Save File to Storage  │  │
│  │ 3. Create DB Record      │  │
│  └────────────┬─────────────┘  │
└───────────────┼────────────────┘
                │
                ▼
    ┌──────────────────┐
    │  PostgreSQL      │
    │  (Save Metadata) │
    └──────┬───────────┘
           │
           ▼
┌────────────��─────────────────────┐
│  Async Processing Queue          │
│  (File Processing Task)          │
└──────┬───────────────────────────┘
       │
       ▼
┌──────────────────────────────────┐
│  Document Service - Worker       │
│  ┌───────────────────────────┐   │
│  │ 1. Extract Text           │   │
│  │    - PDF → PDFBox         │   │
│  │    - DOCX → Docx4j        │   │
│  │    - XLSX → Apache POI    │   │
│  │                           │   │
│  │ 2. Split into Chunks      │   │
│  │    - Size: 1024 chars     │   │
│  │    - Overlap: 200 chars   │   │
│  │                           │   │
│  │ 3. Save Chunks to DB      │   │
│  └───────────┬───────────────┘   │
└──────────────┼────────────────────┘
               │
               ▼
    ┌──────────────────────┐
    │  PostgreSQL          │
    │  document_chunks     │
    └──────┬───────────────┘
           │
           ▼
┌──────────────────────────────────┐
│  Embedding Service               │
│  ┌────────────────────────────┐  │
│  │ For Each Chunk:            │  │
│  │ 1. Send to OpenAI API      │  │
│  │ 2. Get Vector (1536 dims)  │  │
│  │ 3. Store in Elasticsearch  │  │
│  └────────────┬───────────────┘  │
└───────────────┼──────────────────┘
                │
                ▼
    ┌──────────────────────────┐
    │  Elasticsearch           │
    │  chatbot_embeddings idx  │
    └──────┬───────────────────┘
           │
           ▼
┌──────────────┐
│  Client      │ ◄─ Notification: Processing Complete
└──────────────┘
```

### Chat Interaction Flow

```
┌──────────────┐
│   Website    │
│  (with widget)
└──────┬───────┘
       │ User Question
       │
       ▼
┌────────────────────────────┐
│  Widget JavaScript         │
│  (Chatbot.js)              │
└──────┬─────────────────────┘
       │
       ▼
┌────────────────────────────┐
│  API Gateway               │
│  (Widget API Service)      │
└──────┬─────────────────────┘
       │
       ▼
┌──────────────────────────────────┐
│  Chatbot Service                 │
│  ┌────────────────────────────┐  │
│  │ 1. Validate API Key        │  │
│  │ 2. Generate Embedding      │  │
│  │    (Same as document)      │  │
│  └────────────┬───────────────┘  │
└───────────────┼──────────────────┘
                │
                ▼
┌────────────────────────────────┐
│  Embedding Service             │
│  Send Question to OpenAI API   │
│  Get Vector (1536 dims)        │
└──────┬─────────────────────────┘
       │
       ▼
    ┌──────────────────────────┐
    │  Elasticsearch           │
    ��  Vector Similarity Search│
    │  (Find top 5 matches)    │
    └──────┬───────────────────┘
           │
           ▼
┌──────────────────────────────────┐
│  Chatbot Service                 │
│  ┌────────────────────────────┐  │
│  │ 1. Retrieve Context        │  │
│  │ 2. Build Prompt with       │  │
│  │    context + question      │  │
│  │ 3. Send to OpenAI Chat API │  │
│  │ 4. Get Response            │  │
│  └────────────┬───────────────┘  │
└───────────────┼──────────────────┘
                │
                ▼
    ┌──────────────────────┐
    │  OpenAI Chat API     │
    │  (GPT-3.5-turbo)     │
    │  Generate Response   │
    └──────┬───────────────┘
           │
           ▼
┌──────────────────────────────────┐
│  Chatbot Service                 │
│  ┌────────────────────────────┐  │
│  │ 1. Save Message to DB      │  │
│  │ 2. Format Response         │  │
│  │ 3. Return to Client        │  │
│  └────────────┬───────────────┘  │
└───────────────┼──────────────────┘
                │
                ▼
    ┌──────────────────┐
    │  PostgreSQL      │
    │  chat_messages   │
    └──────┬───────────┘
           │
           ▼
┌──────────────────────┐
│  Widget             │
│  Display Response    │
└──────────────────────┘
```

### Payment Processing Flow

```
┌──────────────┐
│   Client     │
└──────┬───────┘
       │ Click "Upgrade"
       │
       ▼
┌────────────────────────────┐
│  Dashboard                 │
│  Select Plan               │
│  (Starter/Professional)    │
└──────┬─────────────────────┘
       │
       ▼
┌────────────────────────────┐
│  API Gateway               │
└──────┬─────────────────────┘
       │ POST /payments/order
       │
       ▼
┌──────────────────────────────────┐
│  Payment Service                 │
│  ┌────────────────────────────┐  │
│  │ 1. Validate User           │  │
│  │ 2. Calculate Amount        │  │
│  │ 3. Create Razorpay Order   │  │
│  └────────────┬───────────────┘  │
└───────────────┼──────────────────┘
                │
                ▼
    ┌──────────────────────┐
    │  Razorpay API        │
    │  Create Order        │
    └──────┬───────────────┘
           │
           ▼
┌───────────────────────────��──────┐
│  Payment Service                 │
│  Save Order to DB                │
│  Return to Client                │
└──────┬─────────────────────────────┘
       │ Order ID, Amount, Key
       │
       ▼
┌──────────────────────────────────┐
│  Razorpay Checkout Widget        │
│  (On Client)                     │
│  User Enters Payment Details     │
└──────┬─────────────────────────────┘
       │ User Completes Payment
       │
       ▼
    ┌──────────────────────┐
    │  Razorpay Payment    │
    │  Gateway             │
    │  Process Payment     │
    └──────┬───────────────┘
           │
           ▼
┌─��────────────────────────────────┐
│  Client Webhook Handler          │
│  (Payment Verification)          │
│  ┌────────────────────────────┐  │
│  │ 1. Receive Callback        │  │
│  │ 2. Verify Signature        │  │
│  │ 3. Send to Backend         │  │
│  └────────────┬───────────────┘  │
���───────────────┼──────────────────┘
                │ POST /payments/verify
                │
                ▼
┌──────────────────────────────────┐
│  Payment Service                 │
│  ┌────────────────────────────┐  │
│  │ 1. Verify Signature        │  │
│  │    (HMAC-SHA256)           │  │
│  │ 2. Update Payment Status   │  │
│  │ 3. Create Subscription     │  │
│  │ 4. Update User Plan        │  │
│  └────────────┬───────────────┘  │
└───────────────┼──────────────────┘
                │
                ▼
    ┌──────────────────────────────┐
    │  PostgreSQL                  │
    │  - payments (update)         │
    │  - subscriptions (insert)    │
    │  - users (update plan)       │
    └─��────┬───────────────────────┘
           │
           ▼
┌──────────────┐
│   Client     │ ◄─ Success & Dashboard Access Granted
└──────────────┘
```

## 🔐 Security Architecture

### Authentication Flow

```
1. User Registration/Login
   ├─ Password hashed with BCrypt
   └─ Salt rounds: 10

2. JWT Token Generation
   ├─ Access Token (1 hour)
   │  └─ Claims: user_id, email, plan
   ├─ Refresh Token (7 days)
   │  └─ Claims: user_id, type=refresh
   └─ Signing Algorithm: HS512

3. API Request Authentication
   ├─ Authorization: Bearer {access_token}
   ├─ API Gateway validates JWT
   ├─ Extracts user_id to X-User-Id header
   └─ Forwards to service

4. Token Expiration & Refresh
   ├─ Access token expires → 401 response
   ├─ Client sends refresh token
   ├─ Auth Service issues new access token
   └─ Repeat authentication flow
```

### Data Security

```
Encryption Layers:
├─ HTTPS/TLS for all communications
├─ Password hashing (BCrypt)
├─ Sensitive data in environment variables
├─ API keys never logged or exposed
└─ File upload validation

Database Security:
├─ SQL injection prevention (Prepared statements)
├─ User input validation
├─ Rate limiting on API Gateway
└─ CORS configuration
```

## 📊 Database Design

### Entity Relationship Diagram

```
Users (1) ──────────── (M) Subscriptions
  ├─ id (PK)              ├─ id (PK)
  ├─ email                ├─ user_id (FK)
  ├─ password             ├─ plan
  ├─ firstName            ├─ paymentFrequency
  ├─ lastName             ├─ amount
  ├─ companyName          ├─ status
  ├─ subscriptionPlan     ├─ startDate
  ├─ isActive             └─ endDate
  └─ isEmailVerified

Users (1) ──────────── (M) Documents
  ├─ id (PK)              ├─ id (PK)
  └─ ...                  ├─ user_id (FK)
                          ├─ fileName
                          ├─ filePath
                          ├─ fileType
                          ├─ fileSize
                          ├─ status
                          ├─ extractedText
                          └─ chunkCount

Documents (1) ──────── (M) DocumentChunks
  ├─ id (PK)              ├─ id (PK)
  └─ ...                  ├─ document_id (FK)
                          ├─ chunkIndex
                          ├─ chunkText
                          └─ embeddingId

Users (1) ──────────── (M) Chatbots
  ├─ id (PK)              ├─ id (PK)
  └─ ...                  ├─ user_id (FK)
                          ├─ name
                          ├─ description
                          ├─ apiKey (Unique)
                          ├─ systemPrompt
                          └─ isActive

Chatbots (1) ────────── (M) ChatMessages
  ├─ id (PK)              ├─ id (PK)
  └─ ...                  ├─ chatbot_id (FK)
                          ├─ user_id (FK)
                          ├─ userMessage
                          ├─ botResponse
                          └─ context

Users (1) ──────────── (M) Payments
  ├─ id (PK)              ├─ id (PK)
  └─ ...                  ├─ user_id (FK)
                          ├─ razorpayOrderId (Unique)
                          ├─ razorpayPaymentId
                          ├─ razorpaySignature
                          ├─ amount
                          └─ status
```

## 🔄 Event-Driven Architecture

### Event Bus (Potential Future Enhancement)

```
Document Processing Event
  ├─ Event: DocumentUploadedEvent
  │  ├─ documentId
  │  ├─ userId
  │  └─ fileType
  │
  └─ Subscribers:
     ├─ TextExtractionService (extract & chunk)
     ├─ VirusScanService (validate file)
     └─ AuditLoggingService (log event)

Embedding Generation Event
  ├─ Event: ChunkCreatedEvent
  │  ├─ chunkId
  │  ├─ documentId
  │  └─ text
  │
  └─ Subscribers:
     ├─ EmbeddingService (generate vectors)
     ├─ ElasticsearchService (index)
     └─ MetricsService (track)

Payment Event
  ├─ Event: PaymentVerifiedEvent
  │  ├─ paymentId
  │  ├─ userId
  │  └─ plan
  │
  └─ Subscribers:
     ├─ SubscriptionService (activate)
     ├─ EmailService (send confirmation)
     └─ AnalyticsService (track)
```

## 🚀 Scalability & Performance

### Horizontal Scaling

```
Current Setup (Single Instance):
┌─────────────────────────────────┐
│  Auth Service (8081)            │
│  Payment Service (8082)         │
│  Document Service (8083)        │
│  Embedding Service (8084)       │
│  Chatbot Service (8085)         │
└─────────────────────────────────┘

Future Setup (Scaled):
Load Balancer
  ├─ Auth Service Instance 1
  ├─ Auth Service Instance 2
  ├─ Auth Service Instance 3
  ├─ Chatbot Service Instance 1
  ├─ Chatbot Service Instance 2
  ├─ Chatbot Service Instance 3
  └─ ... (additional instances)

Database:
  ├─ Primary (Write)
  └─ Replicas (Read-Only) x N

Cache Layer (Redis):
  ├─ Cluster Mode
  └─ Sentinel for HA
```

### Performance Optimization Strategies

1. **Caching**: Redis for session & API response caching
2. **Database Indexing**: Indexes on frequently queried columns
3. **Elasticsearch**: Fast full-text & similarity search
4. **Async Processing**: Document processing in background
5. **Connection Pooling**: HikariCP for database connections
6. **API Gateway Caching**: Cache common responses
7. **CDN**: Static assets delivery
8. **Compression**: GZIP for API responses

## 🔗 Integration Points

### External APIs

```
OpenAI API
  ├─ POST /v1/embeddings
  │  └─ Generate vector embeddings (1536 dims)
  └─ POST /v1/chat/completions
     └─ Generate chat responses

Razorpay API
  ├─ POST /api/V1/orders
  │  └─ Create payment orders
  └─ GET /api/V1/orders/{id}
     └─ Fetch order details

Email Service (SMTP)
  ├─ Send verification emails
  ├─ Payment receipts
  └─ Notifications

Analytics (Future)
  ├─ Google Analytics
  ├─ Mixpanel
  └─ Custom event tracking
```

## 📈 Monitoring & Observability

### Logging Strategy

```
Service Level Logs:
├─ Application Logs
│  ├─ INFO: Business operations
│  ├─ DEBUG: Detailed execution
│  ├─ WARN: Potential issues
│  └─ ERROR: System failures
├─ Access Logs
│  ├─ Request method, path, status
│  └─ Response time, size
└─ Error Logs
   ├─ Stack traces
   └─ Request context

Centralized Logging (Future):
├─ ELK Stack (Elasticsearch, Logstash, Kibana)
├─ Splunk
└─ CloudWatch (AWS)

Metrics:
├─ Response time
├─ Error rate
├─ Request volume
├─ Database query time
└─ Cache hit rate
```

### Health Checks

```
Service Health Endpoints:
GET /actuator/health

Checks:
├─ Database connectivity
├─ Redis connectivity
├─ Elasticsearch connectivity
├─ External API availability
└─ Disk space
```

This completes the comprehensive system architecture documentation for the AI Chatbot SaaS Platform.

