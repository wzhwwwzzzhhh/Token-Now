// 用户类型（适配后端 UserVO）
export interface User {
  id: number
  username: string
  phone: string
  avatar?: string
  bio?: string
  followCount?: number
  fansCount?: number
  isFollowing?: boolean
  createTime?: string
  token?: string
}

// 帖子类型
export interface Post {
  id: number
  title: string
  content: string
  userId: number
  user: User
  likeCount: number
  commentCount: number
  viewCount?: number
  createTime: string
  isLiked?: boolean
  image?: string
  tags?: string[]
}

// 评论类型
export interface Comment {
  id: number
  postId: number
  userId: number
  user: User
  content: string
  likeCount: number
  createTime: string
  isLiked?: boolean
}

// 通知类型
export interface Notification {
  id: number
  userId: number
  type: number
  content: string
  targetId: number
  isRead: boolean
  createTime: string
  fromUser?: User
}

// 登录表单类型
export interface LoginForm {
  phone: string
  password: string
}

// 注册表单类型
export interface RegisterForm {
  phone: string
  username: string
  password: string
}

// 发布帖子表单类型
export interface PostForm {
  title: string
  content: string
}

// 评论表单类型
export interface CommentForm {
  content: string
}

// API响应类型
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 分页响应类型（适配后端 PageResult）
export interface PaginationResponse<T> {
  records: T[]
  total: number
  current: number
  size: number
  pages: number
}
