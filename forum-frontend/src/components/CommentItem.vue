<template>
  <div class="comment-item">
    <van-image
      :src="comment.user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
      round
      fit="cover"
      class="avatar"
    />
    <div class="comment-content">
      <div class="comment-header">
        <div class="username">{{ comment.user.username }}</div>
        <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
      </div>
      <div class="comment-text">{{ comment.content }}</div>
      <div class="comment-actions">
        <LikeButton 
          target-type="comment" 
          :target-id="comment.id" 
          :count="comment.likeCount || 0"
          :is-liked="comment.isLiked"
          @like-change="handleLikeChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import dayjs from 'dayjs'
import { Comment } from '../types'
import LikeButton from './LikeButton.vue'

const props = defineProps<{
  comment: Comment
}>()

const emit = defineEmits<{
  (e: 'like-change', commentId: number, liked: boolean, count: number): void
}>()

const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const handleLikeChange = (liked: boolean, count: number) => {
  emit('like-change', props.comment.id, liked, count)
}
</script>

<style scoped lang="scss">
.comment-item {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
  
  .avatar {
    width: 36px;
    height: 36px;
    flex-shrink: 0;
  }
  
  .comment-content {
    flex: 1;
    
    .comment-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 4px;
      
      .username {
        font-size: 14px;
        font-weight: 500;
        color: #333;
      }
      
      .comment-time {
        font-size: 12px;
        color: #999;
      }
    }
    
    .comment-text {
      font-size: 14px;
      color: #333;
      line-height: 1.5;
      margin-bottom: 8px;
    }
    
    .comment-actions {
      display: flex;
      align-items: center;
    }
  }
}
</style>