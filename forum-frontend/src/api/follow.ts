import request from './request'

// 关注相关API
export const followAPI = {
  // 关注用户
  follow: (userId: number) => {
    return request.post<boolean>(`/follows/${userId}`)
  },
  // 取消关注
  unfollow: (userId: number) => {
    return request.delete<boolean>(`/follows/${userId}`)
  },
  // 检查是否已关注
  checkFollow: (userId: number) => {
    return request.get<boolean>(`/follows/check/${userId}`)
  },
  // 获取关注列表
  getFollowingList: (page: number = 1, size: number = 20) => {
    return request.get(`/follows/following?page=${page}&size=${size}`)
  },
  // 获取粉丝列表
  getFollowersList: (page: number = 1, size: number = 20) => {
    return request.get(`/follows/followers?page=${page}&size=${size}`)
  }
}