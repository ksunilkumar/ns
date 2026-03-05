import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken')
  const userId = localStorage.getItem('userId')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  if (userId) {
    config.headers['X-User-Id'] = userId
  }

  return config
})

// Handle response errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken')
      localStorage.removeItem('userId')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Auth API calls
export const authAPI = {
  signup: (data) => api.post('/auth/signup', data),
  login: (data) => api.post('/auth/login', data),
  validateToken: () => api.get('/auth/validate'),
}

// Document API calls
export const documentAPI = {
  uploadDocument: (file, userId) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/documents/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
  getUserDocuments: (userId) => api.get(`/documents/${userId}`),
  getDocument: (documentId) => api.get(`/documents/${documentId}/details`),
  deleteDocument: (documentId) => api.delete(`/documents/${documentId}`),
  processDocument: (documentId) => api.post(`/documents/process/${documentId}`),
}

// Chatbot API calls
export const chatbotAPI = {
  createChatbot: (name, description) =>
    api.post('/chatbot/create', { name, description }),
  getUserChatbots: (userId) => api.get(`/chatbot/${userId}`),
  getChatbot: (chatbotId) => api.get(`/chatbot/${chatbotId}/details`),
  sendMessage: (chatbotId, userId, message, apiKey) =>
    api.post('/chatbot/chat', { chatbotId, userId, message, apiKey }),
  deleteChatbot: (chatbotId) => api.delete(`/chatbot/${chatbotId}`),
}

// Widget API calls
export const widgetAPI = {
  generateScript: (chatbotId, apiKey, position, theme) =>
    api.post('/widget/generate-script', { chatbotId, apiKey, widgetPosition: position, widgetTheme: theme }),
  getInstallationCode: (chatbotId, apiKey) =>
    api.get(`/widget/installation/${chatbotId}/${apiKey}`),
}

// Payment API calls
export const paymentAPI = {
  createOrder: (userId, plan, frequency) =>
    api.post('/payments/order', { userId, plan, paymentFrequency: frequency }),
  verifyPayment: (orderId, paymentId, signature) =>
    api.post('/payments/verify', { orderId, paymentId, signature }),
  getSubscription: (userId) => api.get(`/payments/subscription/${userId}`),
}

// User API calls
export const userAPI = {
  getProfile: (userId) => api.get(`/users/${userId}`),
  updateProfile: (userId, data) => api.put(`/users/${userId}`, data),
}

export default api

