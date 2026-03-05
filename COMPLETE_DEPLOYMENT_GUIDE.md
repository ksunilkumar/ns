# COMPLETE_DEPLOYMENT_GUIDE.md - Full Stack Setup

## 🚀 Complete AI Chatbot SaaS Platform - Full Stack Deployment

This guide covers running the entire platform: Backend (9 microservices) + Frontend (React)

## 📋 What You Have

### Backend Services (9 Microservices)
1. API Gateway (8080)
2. Auth Service (8081)
3. Payment Service (8082)
4. Document Service (8083)
5. Embedding Service (8084)
6. Chatbot Service (8085)
7. Widget API Service (8086)
8. User Service (8087)
9. Eureka Registry (8761)

### Infrastructure Services
- PostgreSQL 15 (5432)
- Elasticsearch 8.10 (9200)
- Redis 7 (6379)

### Frontend
- React Application (3000)

## ⚡ Quick Start (5 Steps)

### Step 1: Build Backend
```bash
cd ns_aiBot
mvn clean package -DskipTests
```

### Step 2: Start Backend Services
```bash
docker-compose up -d
# Wait 30 seconds for services to initialize
```

### Step 3: Setup Frontend
```bash
cd frontend
npm install
```

### Step 4: Create Frontend Environment
```bash
cat > .env << EOF
VITE_API_URL=http://localhost:8080/api/v1
EOF
```

### Step 5: Start Frontend
```bash
npm run dev
# Open http://localhost:3000
```

## 🎯 Testing Complete Flow

### 1. Register New User
```bash
# Go to http://localhost:3000/signup
# Fill form:
- Email: test@example.com
- Password: TestPass123!
- First Name: John
- Last Name: Doe
- Company: Test Corp
# Click "Create Account"
```

### 2. Login
```bash
# Redirects to dashboard after signup
# Or go to http://localhost:3000/login
# Use credentials from signup
```

### 3. Upload Document
```bash
# Go to Documents page
# Click "Upload Document"
# Select a PDF, TXT, or DOCX file
# View uploaded file in list
```

### 4. Create Chatbot
```bash
# Go to Chatbots page
# Click "Create Chatbot"
# Fill:
  - Name: My Support Bot
  - Description: Answers customer questions
# Click "Create"
```

### 5. Check Dashboard
```bash
# View stats and charts
# Check recent activity
# See all uploaded documents
```

## 📊 Architecture Overview

```
┌─────────────────────────────────────────┐
│         Frontend (React)                 │
│      http://localhost:3000               │
└────────────┬────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────┐
│       API Gateway                       │
│    http://localhost:8080                │
│   (Routes requests to services)         │
└────────────┬────────────────────────────┘
             │
    ┌────────┼────────┬────────┬─────────┐
    ▼        ▼        ▼        ▼         ▼
┌────────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐
│ Auth   │ │Pay   │ │Doc   │ │Chat  │ │User  │
│ 8081   │ │8082  │ │8083  │ │8085  │ │8087  │
└────────┘ └──────┘ └──────┘ └──────┘ └──────┘
    │        │        │        │         │
    └────────┴────────┴────────┴─────────┘
             │
    ┌────────┴────────┐
    ▼                 ▼
┌──────────┐     ┌───────────┐
│PostgreSQL│     │Elasticsearch│
│  5432    │     │   9200      │
└──────────┘     └───────────┘
```

## 🔧 Environment Configuration

### Backend (.env file)
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_USER=root
DB_PASSWORD=password
DB_NAME=ai_chatbot_db

# JWT
JWT_SECRET=your-super-secret-key-change-in-production-min-32-chars
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# OpenAI
OPENAI_API_KEY=sk-your-api-key

# Razorpay
RAZORPAY_KEY_ID=test_key_id
RAZORPAY_KEY_SECRET=test_key_secret

# Eureka
EUREKA_URL=http://localhost:8761/eureka

# Files
FILE_UPLOAD_DIR=uploads
WIDGET_API_URL=http://localhost:8086/api/v1
```

### Frontend (.env file)
```bash
VITE_API_URL=http://localhost:8080/api/v1
```

## 🌐 Port Reference

| Service | Port | URL |
|---------|------|-----|
| Frontend | 3000 | http://localhost:3000 |
| API Gateway | 8080 | http://localhost:8080 |
| Auth Service | 8081 | http://localhost:8081 |
| Payment Service | 8082 | http://localhost:8082 |
| Document Service | 8083 | http://localhost:8083 |
| Embedding Service | 8084 | http://localhost:8084 |
| Chatbot Service | 8085 | http://localhost:8085 |
| Widget Service | 8086 | http://localhost:8086 |
| User Service | 8087 | http://localhost:8087 |
| Eureka Registry | 8761 | http://localhost:8761 |
| PostgreSQL | 5432 | localhost:5432 |
| Elasticsearch | 9200 | http://localhost:9200 |
| Redis | 6379 | localhost:6379 |

## 📚 Accessing Services

### Frontend
```
http://localhost:3000/
```

### API Documentation (Swagger)
```
http://localhost:8080/swagger-ui.html
```

### Eureka Service Registry
```
http://localhost:8761/
```

### Elasticsearch
```
http://localhost:9200/
```

## ✅ Verification Checklist

### Backend Running?
```bash
# Check if all containers are running
docker-compose ps

# Should see:
✓ postgres
✓ elasticsearch
✓ redis
✓ eureka-server
✓ api-gateway
✓ auth-service
✓ payment-service
✓ document-service
✓ embedding-service
✓ chatbot-service
✓ widget-api-service
✓ user-service
```

### Services Healthy?
```bash
# API Gateway should respond
curl http://localhost:8080/swagger-ui.html

# Auth Service should respond
curl http://localhost:8081/swagger-ui.html

# Database connection
docker exec -it ai-chatbot-postgres psql -U root -d ai_chatbot_db -c "SELECT version();"
```

### Frontend Running?
```bash
# Should be running
http://localhost:3000

# Check console for errors
# DevTools → Console tab
```

## 🧪 Integration Testing

### Test 1: User Registration
```bash
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123!",
    "firstName": "John",
    "lastName": "Doe",
    "companyName": "Test Corp"
  }'

# Expected: 201 Created with tokens
```

### Test 2: User Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123!"
  }'

# Expected: 200 OK with tokens
# Save accessToken for next requests
```

### Test 3: Create Chatbot
```bash
curl -X POST http://localhost:8080/api/v1/chatbot/create \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "X-User-Id: <USER_ID>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Support Bot",
    "description": "Customer support"
  }'

# Expected: 201 Created with chatbot data
```

## 🚀 Running Components Separately

### Run Only Backend
```bash
# Terminal 1: Start infrastructure
docker-compose up postgres elasticsearch redis eureka-server

# Terminal 2-9: Run each service
cd api-gateway && mvn spring-boot:run
# ... repeat for other services
```

### Run Only Frontend
```bash
cd frontend
npm install
npm run dev
# Available at http://localhost:3000
```

### Run Only Database
```bash
docker run -d \
  --name ai-chatbot-postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=root \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=ai_chatbot_db \
  postgres:15-alpine
```

## 📊 Monitoring

### View Backend Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f auth-service

# Last 100 lines
docker-compose logs --tail=100
```

### Check Database
```bash
# Connect to PostgreSQL
docker exec -it ai-chatbot-postgres psql -U root -d ai_chatbot_db

# List tables
\dt

# Exit
\q
```

### Check Elasticsearch
```bash
# Health status
curl http://localhost:9200/_cluster/health

# List indices
curl http://localhost:9200/_cat/indices
```

## 🛑 Stopping Services

### Stop All Services
```bash
docker-compose down
```

### Stop and Remove Data
```bash
docker-compose down -v
```

### Stop Only Frontend
```bash
# Press Ctrl+C in terminal where npm run dev is running
```

## 🔄 Rebuilding Services

### Rebuild All
```bash
# Clean and build
mvn clean package -DskipTests

# Rebuild Docker images
docker-compose down
docker-compose up -d --build
```

### Rebuild Single Service
```bash
# Example: Auth Service
cd auth-service
mvn clean package -DskipTests

# Rebuild container
docker-compose up -d --build auth-service
```

## 🌟 Key Features to Test

### 1. Authentication
- [ ] Sign up new account
- [ ] Login with credentials
- [ ] Token refresh works
- [ ] Logout clears tokens
- [ ] Protected routes redirect to login

### 2. Document Management
- [ ] Upload PDF, DOCX, TXT, XLSX
- [ ] View uploaded documents
- [ ] Delete documents
- [ ] See document status

### 3. Chatbot Creation
- [ ] Create chatbot
- [ ] Get API key
- [ ] Delete chatbot
- [ ] View chatbot list

### 4. User Settings
- [ ] Update profile
- [ ] Change preferences
- [ ] View billing info
- [ ] Manage notifications

### 5. Dashboard
- [ ] View statistics
- [ ] See charts
- [ ] Recent activity
- [ ] Quick actions

## 💡 Tips & Tricks

### Access Backend via Frontend
- All API calls go through API Gateway (8080)
- Frontend proxies requests automatically
- No need to call individual service ports

### View All Registered Services
```
http://localhost:8761/
```
Shows all registered microservices and their status

### Test API Endpoints
Use frontend UI or:
- Postman
- Insomnia
- curl commands
- Swagger UI at http://localhost:8080/swagger-ui.html

### View Running Processes
```bash
# On macOS/Linux
lsof -i -P -n

# On Windows
netstat -ano
```

## 🆘 Troubleshooting

### "Address already in use"
```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>
```

### "Connection refused"
```bash
# Ensure containers are running
docker-compose ps

# Restart all services
docker-compose restart
```

### "No JWT token found"
```bash
# Make sure to login first
# Token stored in localStorage
# Check DevTools → Application → Local Storage
```

### "CORS error"
```bash
# Frontend and backend must be accessible
# Frontend: http://localhost:3000
# Backend: http://localhost:8080
# Check VITE_API_URL in frontend .env
```

## 📚 Next Steps

1. **Test the platform thoroughly**
   - Create accounts
   - Upload documents
   - Create chatbots
   - Check dashboard

2. **Customize for your needs**
   - Update branding
   - Add more features
   - Integrate with your systems
   - Customize UI colors

3. **Deploy to production**
   - Use cloud provider (AWS, GCP, Azure)
   - Setup SSL certificates
   - Configure domain
   - Setup backups

4. **Monitor & maintain**
   - Setup logging
   - Configure alerts
   - Regular backups
   - Performance optimization

## 📞 Support

If you encounter issues:
1. Check logs: `docker-compose logs`
2. Verify ports are open
3. Ensure backend is running
4. Check frontend console (F12)
5. Review environment variables

## ✨ You're All Set!

Your complete AI Chatbot SaaS platform is ready:
- ✅ 9 Backend Microservices
- ✅ Complete React Frontend
- ✅ Database & Caching
- ✅ Vector Search
- ✅ Payment Integration
- ✅ Document Processing
- ✅ API Gateway
- ✅ Service Discovery

Happy chatbotting! 🚀

