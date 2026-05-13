import { defineStore } from 'pinia'
import { userAPI } from '../api/user'
import { User, LoginForm, RegisterForm } from '../types'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
    token: localStorage.getItem('token'),
    loading: false,
    error: ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    currentUser: (state) => state.user
  },
  actions: {
    async login(data: LoginForm) {
      try {
        this.loading = true
        this.error = ''
        const user = await userAPI.login(data)
        this.token = user.token || null
        this.user = user
        if (this.token) {
          localStorage.setItem('token', this.token)
        }
        return user
      } catch (error: any) {
        this.error = error.message || '登录失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async register(data: RegisterForm) {
      try {
        this.loading = true
        this.error = ''
        const user = await userAPI.register(data)
        this.token = user.token || null
        this.user = user
        if (this.token) {
          localStorage.setItem('token', this.token)
        }
        return user
      } catch (error: any) {
        this.error = error.message || '注册失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
    },

    async getCurrentUserInfo() {
      if (!this.token) return

      try {
        this.loading = true
        const user = await userAPI.getCurrentUser()
        this.user = user
        return user
      } catch (error: any) {
        this.error = error.message || '获取用户信息失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    updateUserInfo(userInfo: User) {
      this.user = userInfo
    }
  }
})
