import React, { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { chatbotAPI, widgetAPI } from '../api/client'
import { Plus, Copy, Trash2, MoreVertical, MessageSquare, Code } from 'lucide-react'
import toast from 'react-hot-toast'

export function ChatbotsPage() {
  const { user } = useAuth()
  const [chatbots, setChatbots] = useState([])
  const [loading, setLoading] = useState(true)
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [formData, setFormData] = useState({ name: '', description: '' })
  const [selectedChatbot, setSelectedChatbot] = useState(null)

  useEffect(() => {
    loadChatbots()
  }, [user?.id])

  const loadChatbots = async () => {
    try {
      const response = await chatbotAPI.getUserChatbots(user?.id)
      setChatbots(response.data.data || [])
    } catch (error) {
      toast.error('Failed to load chatbots')
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (e) => {
    e.preventDefault()

    if (!formData.name.trim()) {
      toast.error('Chatbot name is required')
      return
    }

    try {
      await chatbotAPI.createChatbot(formData.name, formData.description)
      toast.success('Chatbot created successfully!')
      setFormData({ name: '', description: '' })
      setShowCreateForm(false)
      loadChatbots()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to create chatbot')
    }
  }

  const handleDelete = async (botId) => {
    if (!confirm('Are you sure you want to delete this chatbot?')) return

    try {
      await chatbotAPI.deleteChatbot(botId)
      toast.success('Chatbot deleted')
      loadChatbots()
    } catch (error) {
      toast.error('Failed to delete chatbot')
    }
  }

  const copyToClipboard = (text) => {
    navigator.clipboard.writeText(text)
    toast.success('Copied to clipboard!')
  }

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
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Chatbots</h1>
            <p className="text-gray-600 mt-1">Create and manage your AI chatbots</p>
          </div>
          <button
            onClick={() => setShowCreateForm(!showCreateForm)}
            className="bg-gradient-to-r from-blue-500 to-purple-600 text-white px-6 py-3 rounded-lg hover:shadow-lg transition flex items-center gap-2"
          >
            <Plus size={20} />
            Create Chatbot
          </button>
        </div>

        {/* Create Form */}
        {showCreateForm && (
          <div className="bg-white rounded-lg shadow p-6 mb-8">
            <h2 className="text-xl font-bold text-gray-900 mb-4">Create New Chatbot</h2>
            <form onSubmit={handleCreate} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Chatbot Name</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g., Customer Support Bot"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Description (Optional)</label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Describe what this chatbot does..."
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                  rows="3"
                />
              </div>
              <div className="flex gap-4">
                <button
                  type="submit"
                  className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition"
                >
                  Create
                </button>
                <button
                  type="button"
                  onClick={() => setShowCreateForm(false)}
                  className="bg-gray-200 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-300 transition"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Chatbots List */}
        {chatbots.length === 0 ? (
          <div className="bg-white rounded-lg p-12 text-center">
            <MessageSquare className="mx-auto text-gray-400 mb-4" size={48} />
            <h3 className="text-lg font-semibold text-gray-900 mb-1">No chatbots yet</h3>
            <p className="text-gray-600">Create your first chatbot to get started</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {chatbots.map((bot) => (
              <div key={bot.id} className="bg-white rounded-lg shadow hover:shadow-lg transition overflow-hidden">
                <div className="p-6">
                  <div className="flex items-start justify-between mb-4">
                    <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                      <MessageSquare className="text-purple-600" size={24} />
                    </div>
                    <div className="flex gap-2">
                      <button className="p-2 hover:bg-gray-100 rounded-lg transition">
                        <MoreVertical size={18} className="text-gray-600" />
                      </button>
                      <button
                        onClick={() => handleDelete(bot.id)}
                        className="p-2 hover:bg-red-50 rounded-lg transition"
                      >
                        <Trash2 size={18} className="text-red-600" />
                      </button>
                    </div>
                  </div>
                  <h3 className="font-bold text-gray-900 text-lg mb-2">{bot.name}</h3>
                  <p className="text-gray-600 text-sm mb-4">{bot.description}</p>

                  {/* API Key */}
                  <div className="bg-gray-50 rounded-lg p-3 mb-4">
                    <p className="text-xs text-gray-600 mb-1">API Key</p>
                    <div className="flex items-center gap-2">
                      <code className="text-xs text-gray-900 truncate flex-1">{bot.apiKey.substring(0, 10)}...</code>
                      <button
                        onClick={() => copyToClipboard(bot.apiKey)}
                        className="p-1 hover:bg-white rounded transition"
                      >
                        <Copy size={16} className="text-gray-600" />
                      </button>
                    </div>
                  </div>

                  {/* Action Buttons */}
                  <div className="flex gap-2">
                    <button className="flex-1 bg-blue-50 text-blue-600 py-2 rounded-lg hover:bg-blue-100 transition text-sm font-medium flex items-center justify-center gap-1">
                      <Code size={16} />
                      Get Widget
                    </button>
                    <button className="flex-1 bg-purple-50 text-purple-600 py-2 rounded-lg hover:bg-purple-100 transition text-sm font-medium">
                      Test Chat
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

