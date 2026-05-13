import request from './request'
import { User, LoginForm, RegisterForm, PaginationResponse } from '../types'

// 用户相关API（适配后端 UserVO，token 包含在 User 对象中）
export const userAPI = {
  // 登录
  login: (data: LoginForm) => {
    return request.post<User>('/users/login', data)
  },
  // 注册
  register: (data: RegisterForm) => {
    return request.post<User>('/users/register', data)
  },
  // 获取用户信息
  getUserInfo: (userId: number) => {
    return request.get<User>(`/users/${userId}`)
  },
  // 获取当前用户信息
  getCurrentUser: () => {
    return request.get<User>('/users/current')
  },
  // 更新用户信息
  updateUserInfo: (data: Partial<User>) => {
    return request.put<User>('/users/current', data)
  },
  // 获取用户关注列表
  getFollowing: (userId: number | 'me', page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<User>>(`/users/${userId}/following?page=${page}&size=${size}`)
  },
  // 获取用户粉丝列表
  getFollowers: (userId: number | 'me', page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<User>>(`/users/${userId}/followers?page=${page}&size=${size}`)
  },
  // 获取用户发布的帖子
  getUserPosts: (userId: number, page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<any>>(`/users/${userId}/posts?page=${page}&size=${size}`)
  }
}
