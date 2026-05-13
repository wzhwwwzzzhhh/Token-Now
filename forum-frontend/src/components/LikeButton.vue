<template>
  <div class="like-button" @click="handleLike">
    <van-icon 
      :name="isLiked ? 'like' : 'like-o'" 
      :color="isLiked ? '#00C9B7' : '#999'"
      class="like-icon"
    />
    <span :class="{ 'liked': isLiked }" class="like-count">{{ count }}</span>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { likeAPI } from '../api/like'
import { showToast } from 'vant'

const props = defineProps<{
  targetType: 'post' | 'comment'
  targetId: number
  count: number
  isLiked?: boolean
}>()

const emit = defineEmits<{
  (e: 'like-change', liked: boolean, count: number): void
}>()

const isLiked = ref(props.isLiked || false)
const currentCount = ref(props.count)

const handleLike = async () => {
  try {
    const liked = props.targetType === 'post'
      ? await likeAPI.togglePost(props.targetId)
      : await likeAPI.toggleComment(props.targetId)
    isLiked.value = liked
    currentCount.value = liked ? currentCount.value + 1 : currentCount.value - 1
    emit('like-change', isLiked.value, currentCount.value)
  } catch (error) {
    showToast('操作失败，请重试')
  }
}
</script>

<style scoped lang="scss">
.like-button {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  
  &:active {
    opacity: 0.7;
  }
  
  .like-icon {
    font-size: 18px;
    transition: all 0.3s ease;
    
    &.liked {
      animation: likeAnimation 0.4s ease;
    }
  }
  
  .like-count {
    font-size: 13px;
    color: #999;
    font-weight: 500;
    transition: all 0.3s ease;
    
    &.liked {
      color: #00C9B7;
      font-weight: 600;
    }
  }
}

@keyframes likeAnimation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>