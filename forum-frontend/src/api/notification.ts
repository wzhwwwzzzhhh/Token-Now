import request from './request'
import { Notification, PaginationResponse } from '../types'

// 通知相关API
export const notificationAPI = {
  // 获取通知列表
  getNotifications: (page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<Notification>>(`/notification?page=${page}&size=${size}`)
  },
  // 标记通知为已读
  markAsRead: (id: number) => {
    return request.put<boolean>(`/notification/${id}/read`)
  },
  // 标记所有通知为已读
  markAllAsRead: () => {
    return request.put<boolean>('/notification/read-all')
  },
  // 获取未读通知数
  getUnreadCount: () => {
    return request.get<number>('/notification/unread-count')
  }
}