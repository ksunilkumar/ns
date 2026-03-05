import React, { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts'
import { Plus, FileText, MessageSquare, TrendingUp, Users } from 'lucide-react'
import { Link } from 'react-router-dom'

const mockData = [
  { month: 'Jan', documents: 4, messages: 2400 },
  { month: 'Feb', documents: 3, messages: 1398 },
  { month: 'Mar', documents: 2, messages: 9800 },
  { month: 'Apr', documents: 6, messages: 3908 },
  { month: 'May', documents: 3, messages: 4800 },
  { month: 'Jun', documents: 8, messages: 3800 },
]

const stats = [
  { title: 'Total Documents', value: '24', icon: FileText, color: 'bg-blue-50', textColor: 'text-blue-600' },
  { title: 'Total Chatbots', value: '3', icon: MessageSquare, color: 'bg-purple-50', textColor: 'text-purple-600' },
  { title: 'Total Messages', value: '2,548', icon: TrendingUp, color: 'bg-green-50', textColor: 'text-green-600' },
  { title: 'Active Users', value: '156', icon: Users, color: 'bg-orange-50', textColor: 'text-orange-600' },
]

export function DashboardPage() {
  const { user } = useAuth()
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Simulate data loading
    setTimeout(() => setLoading(false), 1000)
  }, [])

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Welcome back, {user?.email}!</h1>
          <p className="text-gray-600">Here's what's happening with your chatbots today</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {stats.map((stat, index) => {
            const Icon = stat.icon
            return (
              <div key={index} className={`${stat.color} rounded-lg p-6 border border-gray-100`}>
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-gray-700 font-medium">{stat.title}</h3>
                  <Icon className={`${stat.textColor}`} size={24} />
                </div>
                <p className={`text-3xl font-bold ${stat.textColor}`}>{stat.value}</p>
              </div>
            )
          })}
        </div>

        {/* Charts Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Documents Uploaded Chart */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-xl font-bold text-gray-900 mb-6">Documents Uploaded</h2>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={mockData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="documents" fill="#3b82f6" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Messages Chart */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-xl font-bold text-gray-900 mb-6">Chat Messages</h2>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={mockData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Line type="monotone" dataKey="messages" stroke="#8b5cf6" strokeWidth={2} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* Upload Document */}
          <Link
            to="/documents"
            className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg p-6 border border-blue-200 hover:shadow-lg transition cursor-pointer"
          >
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-blue-500 rounded-lg flex items-center justify-center">
                <Plus className="text-white" size={24} />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Upload Document</h3>
                <p className="text-sm text-gray-600">Add files for training</p>
              </div>
            </div>
          </Link>

          {/* Create Chatbot */}
          <Link
            to="/chatbots"
            className="bg-gradient-to-br from-purple-50 to-purple-100 rounded-lg p-6 border border-purple-200 hover:shadow-lg transition cursor-pointer"
          >
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-purple-500 rounded-lg flex items-center justify-center">
                <Plus className="text-white" size={24} />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Create Chatbot</h3>
                <p className="text-sm text-gray-600">Build a new chatbot</p>
              </div>
            </div>
          </Link>

          {/* View Analytics */}
          <Link
            to="/analytics"
            className="bg-gradient-to-br from-green-50 to-green-100 rounded-lg p-6 border border-green-200 hover:shadow-lg transition cursor-pointer"
          >
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-green-500 rounded-lg flex items-center justify-center">
                <TrendingUp className="text-white" size={24} />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Analytics</h3>
                <p className="text-sm text-gray-600">View performance</p>
              </div>
            </div>
          </Link>
        </div>

        {/* Recent Activity */}
        <div className="mt-8 bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-bold text-gray-900 mb-6">Recent Activity</h2>
          <div className="space-y-4">
            {[
              { action: 'Document uploaded', name: 'product-manual.pdf', time: '2 hours ago' },
              { action: 'Chatbot created', name: 'Support Bot', time: '5 hours ago' },
              { action: 'Document processed', name: 'faq.docx', time: '1 day ago' },
            ].map((activity, index) => (
              <div key={index} className="flex items-center justify-between py-4 border-b last:border-0">
                <div>
                  <p className="font-medium text-gray-900">{activity.action}</p>
                  <p className="text-sm text-gray-600">{activity.name}</p>
                </div>
                <span className="text-sm text-gray-500">{activity.time}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

