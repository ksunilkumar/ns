import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Menu, X, LogOut, Home, FileText, MessageSquare, Settings } from 'lucide-react'
import { useState } from 'react'

export function Navbar() {
  const { user, logout, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [isOpen, setIsOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  if (!isAuthenticated) {
    return (
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <Link to="/" className="flex items-center gap-2">
              <div className="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg"></div>
              <span className="text-xl font-bold gradient-text">ChatBot AI</span>
            </Link>
            <div className="hidden md:flex gap-6">
              <Link to="/login" className="text-gray-600 hover:text-gray-900">Login</Link>
              <Link to="/signup" className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600">Sign Up</Link>
            </div>
            <button className="md:hidden" onClick={() => setIsOpen(!isOpen)}>
              {isOpen ? <X /> : <Menu />}
            </button>
          </div>
          {isOpen && (
            <div className="md:hidden pb-4 border-t">
              <Link to="/login" className="block py-2 text-gray-600">Login</Link>
              <Link to="/signup" className="block py-2 text-gray-600">Sign Up</Link>
            </div>
          )}
        </div>
      </nav>
    )
  }

  return (
    <nav className="bg-white shadow-sm sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <Link to="/dashboard" className="flex items-center gap-2">
            <div className="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg"></div>
            <span className="text-xl font-bold gradient-text">ChatBot AI</span>
          </Link>

          <div className="hidden md:flex items-center gap-8">
            <Link to="/dashboard" className="flex items-center gap-2 text-gray-600 hover:text-gray-900">
              <Home size={20} />
              Dashboard
            </Link>
            <Link to="/documents" className="flex items-center gap-2 text-gray-600 hover:text-gray-900">
              <FileText size={20} />
              Documents
            </Link>
            <Link to="/chatbots" className="flex items-center gap-2 text-gray-600 hover:text-gray-900">
              <MessageSquare size={20} />
              Chatbots
            </Link>
            <Link to="/settings" className="flex items-center gap-2 text-gray-600 hover:text-gray-900">
              <Settings size={20} />
              Settings
            </Link>
          </div>

          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">{user?.email}</span>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 bg-red-50 text-red-600 px-4 py-2 rounded-lg hover:bg-red-100"
            >
              <LogOut size={18} />
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  )
}

