import request from './request'

// 点赞相关API
export const likeAPI = {
  // 帖子点赞/取消（一个接口切换）
  togglePost: (postId: number) => {
    return request.put<boolean>(`/likes/post/${postId}`)
  },
  // 评论点赞/取消（一个接口切换）
  toggleComment: (commentId: number) => {
    return request.put<boolean>(`/likes/comment/${commentId}`)
  },
  // 检查帖子是否已点赞
  checkPost: (postId: number) => {
    return request.get<boolean>(`/likes/post/${postId}/check`)
  },
  // 检查评论是否已点赞
  checkComment: (commentId: number) => {
    return request.get<boolean>(`/likes/comment/${commentId}/check`)
  }
}
