import request from './request'
import { Post, PaginationResponse } from '../types'
import { adaptPost, BackendPost } from '../utils/adapter'

export const personalAPI = {
  getMyPosts: (page: number = 1, size: number = 10) => {
    return request.get<PaginationResponse<BackendPost>>(`/personal/posts?page=${page}&size=${size}`)
  },
  getMyLikedPosts: (page: number = 1, size: number = 10) => {
    return request.get<PaginationResponse<BackendPost>>(`/personal/liked-posts?page=${page}&size=${size}`)
  }
}
