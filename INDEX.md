# 📖 AI Chatbot SaaS Platform - Documentation Index

Welcome to the AI Chatbot SaaS Platform! This index will help you navigate the complete documentation.

## 🚀 Quick Navigation

### Start Here
- **[QUICKSTART.md](./QUICKSTART.md)** - Get started in 5 minutes ⭐ START HERE
- **[PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)** - Complete project overview

### Detailed Guides
- **[SETUP_GUIDE.md](./SETUP_GUIDE.md)** - Complete installation & configuration
- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - System design & data flows

### Configuration
- **[.env.example](./.env.example)** - Environment configuration template

---

## 📚 Documentation by Role

### For First-Time Users
1. Read [QUICKSTART.md](./QUICKSTART.md) - 5 minute setup
2. Run `docker-compose up -d` - Start the system
3. Visit http://localhost:8080/swagger-ui.html - Explore APIs
4. Read [SETUP_GUIDE.md](./SETUP_GUIDE.md) - Understand configuration

### For System Architects
1. Start with [ARCHITECTURE.md](./ARCHITECTURE.md) - Understand design
2. Review entity relationship diagrams
3. Study data flow diagrams
4. Review scalability section

### For Backend Developers
1. Review [ARCHITECTURE.md](./ARCHITECTURE.md) - System design
2. Explore each service folder structure
3. Check application.properties for configuration
4. Review service-specific code

### For DevOps Engineers
1. Review docker-compose.yml
2. Study Docker image configurations
3. Check environment variables
4. Review deployment instructions in SETUP_GUIDE.md

### For Frontend Developers
1. Study Widget API Service (Port 8086)
2. Review API documentation at http://localhost:8080/swagger-ui.html
3. Check authentication flow in Auth Service
4. Plan React dashboard integration

---

## 🏗️ Project Structure Reference

```
AI Chatbot SaaS Platform
├── 📄 Documentation
│   ├── QUICKSTART.md ........................ 5-minute setup guide
│   ├── SETUP_GUIDE.md ....................... Complete setup documentation
│   ├── ARCHITECTURE.md ...................... System design & diagrams
│   ├── PROJECT_SUMMARY.md ................... Project overview
│   ├── INDEX.md (this file) ................. Navigation guide
│   └── .env.example ......................... Configuration template
│
├── 🔧 Microservices (9 services)
│   ├── api-gateway/ ......................... Central API Gateway (Port 8080)
│   ├── auth-service/ ........................ Authentication (Port 8081)
│   ├── payment-service/ ..................... Payment Processing (Port 8082)
│   ├── document-service/ .................... Document Handling (Port 8083)
│   ├── embedding-service/ ................... Vector Embeddings (Port 8084)
│   ├── chatbot-service/ ..................... Chatbot Logic (Port 8085)
│   ├── widget-api-service/ .................. Widget Generation (Port 8086)
│   ├── user-service/ ........................ User Management (Port 8087)
│   └── common-lib/ .......................... Shared Libraries
│
├── 🐳 Infrastructure
│   ├── docker-compose.yml ................... Multi-container setup
│   ├── pom.xml .............................. Maven parent POM
│   └── PostgreSQL, Elasticsearch, Redis .... Supporting services
│
└── 📁 Resources
    ├── uploads/ ............................. User uploaded files
    └── Configuration files ................. Application properties
```

---

## 📊 Service Overview

### Core Services

| Service | Port | Purpose | API Docs |
|---------|------|---------|----------|
| API Gateway | 8080 | Central routing & auth | http://localhost:8080/swagger-ui.html |
| Auth Service | 8081 | User authentication | http://localhost:8081/swagger-ui.html |
| Payment Service | 8082 | Payment processing | http://localhost:8082/swagger-ui.html |
| Document Service | 8083 | File processing | http://localhost:8083/swagger-ui.html |
| Embedding Service | 8084 | Vector generation | http://localhost:8084/swagger-ui.html |
| Chatbot Service | 8085 | Chat logic | http://localhost:8085/swagger-ui.html |
| Widget Service | 8086 | Widget generation | http://localhost:8086/swagger-ui.html |
| User Service | 8087 | User profiles | http://localhost:8087/swagger-ui.html |

### Infrastructure Services

| Service | Port | Purpose |
|---------|------|---------|
| Eureka Registry | 8761 | Service discovery |
| PostgreSQL | 5432 | Primary database |
| Elasticsearch | 9200 | Vector storage |
| Redis | 6379 | Caching layer |

---

## 🔄 Common Workflows

### User Registration & Login Flow
```
1. User visits platform
2. Signup via /auth/signup endpoint
3. Receive JWT access & refresh tokens
4. Use access token for authenticated requests
5. Refresh token when access token expires
```
**Documentation**: See SETUP_GUIDE.md → API Documentation → Authentication

### Document Processing Flow
```
1. User uploads document via /documents/upload
2. System validates and stores file
3. Async process extracts text & creates chunks
4. Embeddings service generates vectors
5. Vectors stored in Elasticsearch
6. Ready for chatbot queries
```
**Documentation**: See ARCHITECTURE.md → Document Upload & Processing Flow

### Chat Interaction Flow
```
1. User sends message via chatbot widget
2. System generates embedding for question
3. Elasticsearch finds similar document chunks
4. Context retrieved and sent to OpenAI
5. LLM generates response with context
6. Response returned to user
```
**Documentation**: See ARCHITECTURE.md → Chat Interaction Flow

### Payment Processing Flow
```
1. User selects subscription plan
2. System creates Razorpay order
3. User pays via Razorpay checkout
4. Webhook verifies payment
5. Subscription activated
6. User plan upgraded
```
**Documentation**: See ARCHITECTURE.md → Payment Processing Flow

---

## 🧪 Testing the APIs

### Quick Test Commands
```bash
# 1. Register user
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Pass123!","firstName":"Test","lastName":"User"}'

# 2. Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Pass123!"}'

# 3. Create chatbot (use token from step 2)
curl -X POST http://localhost:8080/api/v1/chatbot/create \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "X-User-Id: <USER_ID>" \
  -d '{"name":"Support Bot","description":"Customer support"}'
```

**Full testing guide**: See SETUP_GUIDE.md → Test the API

---

## 🔐 Security Configuration

### Key Security Features
- ✅ JWT authentication with configurable expiration
- ✅ Password hashing with BCrypt
- ✅ API key generation for chatbot access
- ✅ Payment signature verification
- ✅ CORS configuration
- ✅ Input validation on all endpoints

### Security Checklist for Production
- [ ] Update JWT_SECRET to strong value
- [ ] Configure TLS/HTTPS
- [ ] Setup firewall rules
- [ ] Configure CORS properly
- [ ] Enable email verification
- [ ] Setup rate limiting
- [ ] Configure monitoring & alerts
- [ ] Regular security audits

**Security details**: See SETUP_GUIDE.md → Security Best Practices

---

## 🚀 Deployment Options

### Development (Docker Compose)
```bash
mvn clean package -DskipTests
docker-compose up -d
```
**Guide**: QUICKSTART.md → 5-Minute Quick Start

### Local Development (IDE)
Run each service from your IDE for better debugging
**Guide**: SETUP_GUIDE.md → Local Development Setup

### Kubernetes (Future)
Services are containerized and ready for K8s
**Guide**: ARCHITECTURE.md → Scalability & Performance

### Cloud Deployment
- AWS: EC2 + RDS + ElastiCache
- GCP: Compute Engine + Cloud SQL + Memorystore
- Azure: Virtual Machines + Database for PostgreSQL + Cache for Redis

**Detailed instructions**: SETUP_GUIDE.md → Production Deployment

---

## 📈 Performance & Scalability

### Scaling Strategy
- **Horizontal**: Add more service instances behind load balancer
- **Vertical**: Increase service instance resources
- **Database**: Replicate PostgreSQL, shard if needed
- **Cache**: Redis cluster for distributed caching
- **Search**: Elasticsearch sharding for large datasets

**Details**: See ARCHITECTURE.md → Scalability & Performance

### Monitoring
- Service health: http://localhost:8761/ (Eureka)
- Logs: `docker-compose logs -f <service>`
- Database: Connect to PostgreSQL
- Search: http://localhost:9200/ (Elasticsearch)
- Cache: `redis-cli` command line

---

## 🐛 Troubleshooting Guide

### Common Issues & Solutions

**Port Already in Use**
```bash
# Find and kill process
lsof -i :8080
kill -9 <PID>
```

**Docker Connection Issues**
```bash
# Restart Docker
docker-compose down
docker-compose up -d
```

**Database Connection Error**
```bash
# Check PostgreSQL
docker ps | grep postgres
docker-compose logs postgres
```

**Service Won't Start**
```bash
# Check logs
docker-compose logs -f auth-service
# Rebuild service
docker-compose up -d --build auth-service
```

**Full troubleshooting**: See SETUP_GUIDE.md → Troubleshooting

---

## 📚 API Documentation

### Access Swagger UI
Each service has interactive API documentation:
- [API Gateway](http://localhost:8080/swagger-ui.html)
- [Auth Service](http://localhost:8081/swagger-ui.html)
- [Payment Service](http://localhost:8082/swagger-ui.html)
- [Document Service](http://localhost:8083/swagger-ui.html)
- [Embedding Service](http://localhost:8084/swagger-ui.html)
- [Chatbot Service](http://localhost:8085/swagger-ui.html)
- [Widget Service](http://localhost:8086/swagger-ui.html)
- [User Service](http://localhost:8087/swagger-ui.html)

### API Endpoints Summary
**Auth**: 4 endpoints (signup, login, refresh, validate)
**Payment**: 3 endpoints (order, verify, subscription)
**Document**: 4 endpoints (upload, process, get, delete)
**Embedding**: 2 endpoints (generate, batch)
**Chatbot**: 4 endpoints (create, chat, get, delete)
**Widget**: 3 endpoints (generate, installation, js file)
**User**: 2 endpoints (get, update)

**Complete API reference**: See SETUP_GUIDE.md → API Documentation

---

## 🎓 Learning Path

### Beginner (First Day)
1. Read QUICKSTART.md
2. Start services with docker-compose
3. Test API endpoints
4. Create a user and chatbot

### Intermediate (First Week)
1. Read SETUP_GUIDE.md completely
2. Review ARCHITECTURE.md
3. Understand each microservice
4. Deploy locally to IDE
5. Add logging and debugging

### Advanced (Ongoing)
1. Study scalability strategies
2. Implement monitoring
3. Optimize database queries
4. Add new features
5. Deploy to cloud

---

## 🔗 External Resources

### Technologies Used
- [Spring Boot 3.2.0](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [PostgreSQL 15](https://www.postgresql.org/)
- [Elasticsearch 8.10](https://www.elastic.co/elasticsearch/)
- [Redis 7](https://redis.io/)
- [OpenAI API](https://openai.com/api/)
- [Razorpay](https://razorpay.com/)

### Learning Resources
- Spring Boot: https://spring.io/guides
- Microservices: https://microservices.io/
- Docker: https://docs.docker.com/
- Kubernetes: https://kubernetes.io/docs/
- REST APIs: https://restfulapi.net/

---

## 📞 Getting Help

### Documentation
1. **QUICKSTART.md** - For quick setup
2. **SETUP_GUIDE.md** - For detailed instructions
3. **ARCHITECTURE.md** - For system design
4. **Swagger UI** - For API details
5. Code comments - For implementation details

### Support Channels
- Check logs: `docker-compose logs <service>`
- Review error messages carefully
- Check database connectivity
- Verify environment variables
- Test with curl or Postman

---

## ✅ Next Steps

### Immediate (Today)
- [ ] Read QUICKSTART.md
- [ ] Run `docker-compose up -d`
- [ ] Test API endpoints
- [ ] Review Swagger documentation

### Short Term (This Week)
- [ ] Read all documentation
- [ ] Understand microservices architecture
- [ ] Deploy to IDE for debugging
- [ ] Customize configuration

### Medium Term (This Month)
- [ ] Implement frontend dashboard
- [ ] Add additional features
- [ ] Setup monitoring & logging
- [ ] Prepare for production

### Long Term (Ongoing)
- [ ] Optimize performance
- [ ] Scale infrastructure
- [ ] Add more integrations
- [ ] Continuous improvement

---

## 🎉 Summary

You now have access to a **complete, production-ready SaaS platform** for AI chatbot creation. The system includes:

✅ **9 microservices** - Fully implemented and documented
✅ **Complete documentation** - Quick start to architecture
✅ **Docker setup** - One-command deployment
✅ **API documentation** - Swagger UI for all services
✅ **Security features** - JWT, encryption, validation
✅ **Scalability** - Microservices ready for growth
✅ **Integration** - OpenAI & Razorpay integrated

**Start now**: Follow the path above, beginning with QUICKSTART.md

---

**Last Updated**: 2024
**Version**: 1.0.0
**Status**: Production Ready ✅

