import { defineStore } from 'pinia'
import { notificationAPI } from '../api/notification'
import { Notification } from '../types'

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: [] as Notification[],
    unreadCount: 0,
    loading: false,
    error: ''
  }),
  getters: {
    hasUnread: (state) => state.unreadCount > 0
  },
  actions: {
    async getNotifications() {
      try {
        this.loading = true
        this.error = ''
        const res = await notificationAPI.getNotifications()
        this.notifications = res.records
        return res
      } catch (error: any) {
        this.error = error.message || '获取通知失败'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async getUnreadCount() {
      try {
        const count = await notificationAPI.getUnreadCount()
        this.unreadCount = count
        return count
      } catch (error: any) {
        this.error = error.message || '获取未读通知数失败'
        return 0
      }
    },
    
    async markAsRead(id: number) {
      try {
        await notificationAPI.markAsRead(id)
        const notification = this.notifications.find(n => n.id === id)
        if (notification) {
          notification.isRead = true
        }
        if (this.unreadCount > 0) {
          this.unreadCount--
        }
      } catch (error: any) {
        this.error = error.message || '标记通知为已读失败'
        throw error
      }
    },
    
    async markAllAsRead() {
      try {
        await notificationAPI.markAllAsRead()
        this.notifications.forEach(notification => {
          notification.isRead = true
        })
        this.unreadCount = 0
      } catch (error: any) {
        this.error = error.message || '标记所有通知为已读失败'
        throw error
      }
    }
  }
})