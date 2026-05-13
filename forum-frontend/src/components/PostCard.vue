<template>
  <div class="post-card" @click="goToPost">
    <!-- 用户信息头部 -->
    <div class="post-header">
      <van-image
        :src="post.user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
        round
        fit="cover"
        class="avatar"
      />
      <div class="user-info">
        <div class="username">{{ post.user.username }}</div>
        <div class="user-meta">
          <span class="post-time">{{ formatTime(post.createTime) }}</span>
          <span class="separator">·</span>
          <span class="view-count">{{ post.viewCount || 0 }}浏览</span>
        </div>
      </div>
      <van-icon name="ellipsis" class="more-icon" @click.stop />
    </div>
    
    <!-- 帖子标题 -->
    <div class="post-title" v-if="post.title">{{ post.title }}</div>
    
    <!-- 帖子主体内容 -->
    <div class="post-content">{{ post.content }}</div>
    
    <!-- 帖子图片 -->
    <div class="post-images" v-if="post.image">
      <van-image
        :src="post.image"
        fit="cover"
        radius="12"
        class="post-image"
      />
    </div>
    
    <!-- 标签区域 -->
    <div class="tags" v-if="post.tags && post.tags.length > 0">
      <span 
        v-for="(tag, index) in post.tags.slice(0, 3)" 
        :key="index" 
        class="tag-item"
        @click.stop
      >
        #{{ tag }}
      </span>
    </div>
    
    <!-- 底部操作栏 -->
    <div class="post-footer">
      <div class="action-item" @click.stop="handleLikeClick">
        <van-icon :name="post.isLiked ? 'like' : 'like-o'" :class="{ liked: post.isLiked }" />
        <span :class="{ liked: post.isLiked }">{{ post.likeCount || 0 }}</span>
      </div>
      <div class="action-item" @click.stop="goToPost">
        <van-icon name="chat-o" />
        <span>{{ post.commentCount || 0 }}</span>
      </div>
      <div class="action-item" @click.stop="handleShare">
        <van-icon name="share-o" />
        <span>分享</span>
      </div>
      <div class="action-item" @click.stop="handleCollect">
        <van-icon name="star-o" />
        <span>收藏</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { Post } from '../types'
import { likeAPI } from '../api/like'
import { showToast } from 'vant'

dayjs.extend(relativeTime)

const props = defineProps<{
  post: Post
}>()

const emit = defineEmits<{
  (e: 'like-change', liked: boolean, count: number): void
}>()

const router = useRouter()

const goToPost = () => {
  router.push(`/post/${props.post.id}`)
}

const formatTime = (time: string) => {
  const now = dayjs()
  const target = dayjs(time)
  const diff = now.diff(target, 'hour')

  if (diff < 1) {
    return '刚刚'
  } else if (diff < 24) {
    return `${diff}小时前`
  } else {
    return target.format('MM月DD日')
  }
}

const handleLikeClick = async () => {
  try {
    const liked = await likeAPI.togglePost(props.post.id)
    const newCount = liked ? (props.post.likeCount || 0) + 1 : Math.max(0, (props.post.likeCount || 0) - 1)
    emit('like-change', liked, newCount)
  } catch (error) {
    showToast('操作失败，请重试')
  }
}

const handleShare = () => {
  console.log('分享帖子')
}

const handleCollect = () => {
  console.log('收藏帖子')
}
</script>

<style scoped lang="scss">
.post-card {
  background-color: #fff;
  border-radius: 16px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.98);
    background-color: #fafafa;
  }
  
  .post-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
    
    .avatar {
      width: 40px;
      height: 40px;
      flex-shrink: 0;
      border: 2px solid #f0faf9;
    }
    
    .user-info {
      flex: 1;
      min-width: 0;
      
      .username {
        font-size: 15px;
        color: #333;
        font-weight: 600;
        margin-bottom: 4px;
      }
      
      .user-meta {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 12px;
        color: #999;
        
        .separator {
          color: #ddd;
        }
      }
    }
    
    .more-icon {
      color: #ccc;
      font-size: 18px;
      cursor: pointer;
      flex-shrink: 0;
      
      &:active {
        opacity: 0.6;
      }
    }
  }
  
  .post-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-bottom: 8px;
    line-height: 1.4;
  }
  
  .post-content {
    font-size: 14px;
    line-height: 1.6;
    color: #666;
    margin-bottom: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
  }
  
  .post-images {
    margin-bottom: 12px;
    
    .post-image {
      width: 100%;
      max-height: 200px;
      object-fit: cover;
      border-radius: 12px;
    }
  }
  
  .tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 12px;
    
    .tag-item {
      display: inline-flex;
      align-items: center;
      padding: 4px 12px;
      background: linear-gradient(135deg, #f0faf9, #e6f9f7);
      color: #00C9B7;
      font-size: 12px;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.95);
        opacity: 0.8;
      }
    }
  }
  
  .post-footer {
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid #f5f5f5;
    
    .action-item {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 13px;
      color: #999;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:active {
        opacity: 0.6;
      }
      
      .van-icon {
        font-size: 18px;
        transition: all 0.3s ease;
      }
      
      span {
        transition: all 0.3s ease;
      }
      
      &.liked,
      .liked {
        color: #00C9B7;
        
        .van-icon {
          color: #00C9B7;
        }
      }
    }
  }
}
</style>