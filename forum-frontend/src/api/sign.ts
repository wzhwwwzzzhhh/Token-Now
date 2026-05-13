import request from './request'

// 签到相关API
export const signAPI = {
  // 签到
  signIn: () => {
    return request.post<boolean>('/sign/in')
  },
  // 获取当月签到日期列表
  getSignList: (year: number, month: number) => {
    return request.get<number[]>(`/sign?year=${year}&month=${month}`)
  },
  // 获取签到统计数据
  getSignStats: () => {
    return request.get<{ totalDays: number; continuousDays: number }>('/sign/stats')
  }
}
