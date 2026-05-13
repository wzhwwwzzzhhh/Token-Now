import { Post, Comment, User, Notification } from '../types'

export interface BackendPost {
  id: number
  title: string
  content: string
  userId: number
  username: string
  userAvatar: string
  likeCount: number
  commentCount: number
  viewCount: number
  isLiked: boolean
  isFollowing: boolean
  createTime: string
  updateTime: string
}

export interface BackendComment {
  id: number
  postId: number
  userId: number
  username: string
  userAvatar: string
  content: string
  likeCount: number
  isLiked: boolean
  createTime: string
}

export interface BackendNotification {
  id: number
  userId: number
  fromUserId: number
  fromUsername: string
  fromAvatar: string
  type: number
  targetId: number
  content: string
  isRead: number
  createTime: string
}

export function adaptPost(backend: BackendPost): Post {
  return {
    id: backend.id,
    title: backend.title,
    content: backend.content,
    userId: backend.userId,
    user: {
      id: backend.userId,
      username: backend.username,
      avatar: backend.userAvatar,
      isFollowing: backend.isFollowing
    } as User,
    likeCount: backend.likeCount,
    commentCount: backend.commentCount,
    viewCount: backend.viewCount,
    createTime: backend.createTime,
    isLiked: backend.isLiked
  }
}

export function adaptComment(backend: BackendComment): Comment {
  return {
    id: backend.id,
    postId: backend.postId,
    userId: backend.userId,
    user: {
      id: backend.userId,
      username: backend.username,
      avatar: backend.userAvatar
    } as User,
    content: backend.content,
    likeCount: backend.likeCount,
    createTime: backend.createTime,
    isLiked: backend.isLiked
  }
}

export function adaptNotification(backend: BackendNotification): Notification {
  return {
    id: backend.id,
    userId: backend.userId,
    type: backend.type,
    content: backend.content,
    targetId: backend.targetId,
    isRead: backend.isRead === 1,
    createTime: backend.createTime,
    fromUser: {
      id: backend.fromUserId,
      username: backend.fromUsername,
      avatar: backend.fromAvatar
    } as User
  }
}
