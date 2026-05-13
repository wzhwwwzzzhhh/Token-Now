import request from './request'
import { Post, PostForm, PaginationResponse } from '../types'

// 帖子相关API
export const postAPI = {
  // 获取帖子列表
  getPosts: (page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<Post>>(`/posts?page=${page}&size=${size}`)
  },
  // 获取帖子详情
  getPostById: (id: number) => {
    return request.get<Post>(`/posts/${id}`)
  },
  // 创建帖子
  createPost: (data: PostForm) => {
    return request.post<Post>('/posts', data)
  },
  // 更新帖子
  updatePost: (id: number, data: PostForm) => {
    return request.put<Post>(`/posts/${id}`, data)
  },
  // 删除帖子
  deletePost: (id: number) => {
    return request.delete<boolean>(`/posts/${id}`)
  },
  // 搜索帖子
  searchPosts: (keyword: string, page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<Post>>(`/posts/search?keyword=${keyword}&page=${page}&size=${size}`)
  },
  // 获取帖子排行榜
  getRanking: (period: string, limit: number = 10) => {
    return request.get<Post[]>(`/posts/ranking?period=${period}&limit=${limit}`)
  }
}
