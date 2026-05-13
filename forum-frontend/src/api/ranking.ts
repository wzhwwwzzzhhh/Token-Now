import request from './request'
import { User } from '../types'

export interface LikeRankItem {
  userId: number
  username: string
  avatar: string
  likeCount: number
  rank: number
}

// 排行榜相关API
export const rankingAPI = {
  // 帖子排行榜
  getPostRanking: (type: string = 'all', page: number = 1, size: number = 20) => {
    return request.get<any>(`/rankings/posts?type=${type}&page=${page}&size=${size}`)
  },
  // 点赞排行榜（用户获赞排行）
  getLikeRanking: (period: string = 'weekly', limit: number = 10) => {
    return request.get<LikeRankItem[]>(`/rankings/like?period=${period}&limit=${limit}`)
  }
}
