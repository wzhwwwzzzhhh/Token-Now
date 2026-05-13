import request from './request'
import { Comment, CommentForm, PaginationResponse } from '../types'

// 评论相关API
export const commentAPI = {
  // 获取帖子评论
  getCommentsByPostId: (postId: number, page: number = 1, size: number = 20) => {
    return request.get<PaginationResponse<Comment>>(`/comments?postId=${postId}&page=${page}&size=${size}`)
  },
  // 创建评论
  createComment: (data: { postId: number; content: string }) => {
    return request.post<Comment>(`/comments?postId=${data.postId}`, { content: data.content })
  },
  // 删除评论
  deleteComment: (id: number) => {
    return request.delete<boolean>(`/comments/${id}`)
  }
}