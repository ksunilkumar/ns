# FRONTEND_SETUP.md - Complete Frontend Setup Guide

## 🎨 Frontend Architecture - React + Tailwind CSS

A complete, production-ready React frontend dashboard for the AI Chatbot SaaS platform.

## 📋 Pages Created

### 1. **Home Page** (`/`)
- Landing page with feature overview
- Pricing plans display
- Call-to-action buttons
- Professional design with gradients

### 2. **Login Page** (`/login`)
- Email and password input
- Show/hide password toggle
- Form validation
- Link to signup page
- Beautiful gradient background

### 3. **Signup Page** (`/signup`)
- First name, last name, email, company
- Password strength validation
- Terms & conditions checkbox
- Responsive form layout
- Email verification ready

### 4. **Dashboard** (`/dashboard`)
- Welcome message
- 4 stat cards (Documents, Chatbots, Messages, Users)
- 2 charts (Documents uploaded, Chat messages)
- Recent activity timeline
- Quick action buttons
- Analytics visualization

### 5. **Documents Page** (`/documents`)
- Drag & drop file upload area
- Document list with status badges
- File details (type, size, created date)
- Chunk count display
- Delete functionality
- Delete confirmation dialog

### 6. **Chatbots Page** (`/chatbots`)
- Create chatbot form
- Chatbot cards with preview
- API key display with copy button
- Get widget button
- Test chat button
- Delete chatbot functionality

### 7. **Settings Page** (`/settings`)
- 4 tabs: Profile, Notifications, Security, Billing
- Profile settings form
- Notification preferences
- Security options (password change, 2FA)
- Billing information & upgrade button
- Tab-based navigation

### 8. **Navigation Bar**
- Logo and branding
- Responsive menu
- User email display
- Quick links to all pages
- Logout button

## 🛠️ Component Structure

### Context Providers
- **AuthContext** - Authentication state management
  - User login/signup/logout
  - Token management
  - Auto-redirect on unauthorized

### Protected Routes
- **ProtectedRoute** - Wraps authenticated pages
  - Checks authentication status
  - Shows loading spinner while verifying
  - Redirects to login if not authenticated

### API Client
- **axios instance** with interceptors
- **Request interceptor** - Adds JWT token to headers
- **Response interceptor** - Handles 401 errors
- **API methods** for all backend endpoints

## 📦 Installation & Setup

### 1. Install Dependencies
```bash
cd frontend
npm install
```

### 2. Configure Environment
```bash
# Create .env file
cat > .env << EOF
VITE_API_URL=http://localhost:8080/api/v1
EOF
```

### 3. Start Development Server
```bash
npm run dev
# Available at http://localhost:3000
```

### 4. Build for Production
```bash
npm run build
# Output in dist/ folder
```

## 🔌 API Integration

All API calls made to backend services:

### Auth Endpoints
```javascript
POST /auth/signup         // Register new user
POST /auth/login         // Login user
POST /auth/refresh       // Refresh token
GET  /auth/validate      // Validate token
```

### Document Endpoints
```javascript
POST   /documents/upload        // Upload file
POST   /documents/process/{id}  // Process document
GET    /documents/{userId}      // Get user documents
GET    /documents/{id}/details  // Get document details
DELETE /documents/{id}          // Delete document
```

### Chatbot Endpoints
```javascript
POST   /chatbot/create           // Create chatbot
POST   /chatbot/chat             // Send message
GET    /chatbot/{userId}         // Get user chatbots
GET    /chatbot/{id}/details     // Get chatbot details
DELETE /chatbot/{id}             // Delete chatbot
```

### Widget Endpoints
```javascript
POST /widget/generate-script           // Generate widget code
GET  /widget/installation/{id}/{key}   // Get installation code
```

## 🎨 Design System

### Colors
- **Primary**: Blue (#3b82f6)
- **Secondary**: Purple (#a855f7)
- **Success**: Green (#10b981)
- **Warning**: Yellow (#f59e0b)
- **Error**: Red (#ef4444)
- **Neutral**: Gray (#6b7280)

### Typography
- **Headings**: Bold weights (600-900)
- **Body**: Regular weights (400-500)
- **Monospace**: Code snippets

### Components
- Gradient backgrounds
- Glass-morphism cards
- Smooth transitions
- Responsive grid layouts
- Icon integration (Lucide React)
- Toast notifications

## 🔐 Security Features

### Authentication
- JWT token storage in localStorage
- Automatic token injection in requests
- Token refresh on expiration
- Logout clears all tokens

### Input Validation
- Email format validation
- Password strength requirements
- File type validation
- File size limits
- XSS prevention (React sanitization)

### API Security
- HTTPS-ready (configure in .env)
- CORS handled by backend
- Error messages don't expose secrets
- Tokens not logged in console

## 📱 Responsive Design

### Breakpoints
- Mobile: < 640px
- Tablet: 640px - 1024px
- Desktop: > 1024px

### Layout Strategy
- Mobile-first design
- Flexible grid system
- Adaptive components
- Touch-friendly buttons
- Readable font sizes

## 📊 State Management

### Context API Usage
```javascript
// Auth context provides:
- user (current user object)
- loading (auth check status)
- isAuthenticated (boolean)
- signup(formData) (async function)
- login(email, password) (async function)
- logout() (function)
```

### Local Storage
```javascript
// Stored items:
- accessToken (JWT)
- refreshToken (JWT)
- userId (UUID)
- user (JSON object)
```

## 🎯 Key Features

### Dashboard Analytics
- Real-time statistics
- Interactive charts
- Recent activity feed
- Quick action buttons
- Performance metrics

### Document Management
- Bulk upload support
- File preview
- Status tracking
- Progress indication
- Delete with confirmation

### Chatbot Management
- Easy creation flow
- API key generation
- Widget installation code
- Test chat interface
- Settings customization

### User Settings
- Profile information
- Security preferences
- Notification settings
- Billing management
- Password management

## 🚀 Deployment

### Vercel Deployment
```bash
npm install -g vercel
vercel
# Follow prompts
# Set VITE_API_URL in Vercel dashboard
```

### Docker Deployment
```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "run", "preview"]
```

### Self-Hosted
```bash
npm run build
# Deploy dist/ folder to web server
```

## 🧪 Testing

### Manual Testing Flow
1. Open http://localhost:3000
2. Go to Signup page
3. Create account with test email
4. Login with credentials
5. Upload sample document
6. Create test chatbot
7. Send test message
8. Update profile settings
9. Verify all pages work

### Browser DevTools
- Check API calls in Network tab
- Verify tokens in Application/Storage
- Monitor console for errors
- Test responsive design

## 🐛 Troubleshooting

### Port 3000 Already in Use
```bash
# Change port in vite.config.js
# Or kill process:
lsof -i :3000
kill -9 <PID>
```

### API Connection Issues
```bash
# Verify backend is running
curl http://localhost:8080/swagger-ui.html

# Check VITE_API_URL in .env
# Restart dev server
npm run dev
```

### Build Errors
```bash
# Clear dependencies
rm -rf node_modules
npm install

# Clear vite cache
rm -rf node_modules/.vite

# Try build again
npm run build
```

## 📚 Technologies Used

### Core
- **React 18** - UI framework
- **Vite** - Build tool
- **React Router** - Routing

### Styling
- **Tailwind CSS** - Utility CSS
- **PostCSS** - CSS processing
- **Lucide React** - Icons

### Data & Visualization
- **Axios** - HTTP client
- **Recharts** - Charts & graphs
- **React Hot Toast** - Notifications

## 📖 File Organization

```
src/
├── api/              # API client setup
├── contexts/         # React contexts
├── components/       # Reusable components
├── pages/            # Page components
├── hooks/            # Custom hooks
├── utils/            # Helper functions
├── App.jsx           # Root component
└── main.jsx          # Entry point
```

## 🔄 User Flow

```
Home Page
    ↓
Sign Up / Login
    ↓
Dashboard (Overview)
    ├→ Documents (Upload & manage)
    ├→ Chatbots (Create & manage)
    └→ Settings (Profile & preferences)
```

## ✅ Checklist for Production

- [ ] Update API URL in .env
- [ ] Configure HTTPS
- [ ] Setup error tracking (Sentry)
- [ ] Add analytics (Google Analytics)
- [ ] Optimize images
- [ ] Minify code
- [ ] Setup CDN
- [ ] Add security headers
- [ ] Test on mobile devices
- [ ] Performance optimization
- [ ] Accessibility audit
- [ ] SEO optimization

## 📞 Support

For issues or questions:
1. Check console for errors
2. Verify backend is running
3. Check network requests
4. Review API responses
5. Check localStorage for tokens

## 📄 License

MIT License - Use freely for your projects

---

**Frontend Version**: 1.0.0
**Status**: Production Ready ✅
**Last Updated**: 2024

