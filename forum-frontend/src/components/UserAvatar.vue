<template>
  <div class="user-avatar" @click="goToProfile" v-if="user">
    <van-image
      :src="user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
      round
      fit="cover"
      :class="['avatar', size]"
    />
    <div class="username" v-if="showUsername">{{ user.username }}</div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { User } from '../types'

const props = defineProps<{
  user: User
  size?: 'small' | 'medium' | 'large'
  showUsername?: boolean
}>()

const router = useRouter()

const goToProfile = () => {
  router.push(`/user/${props.user.id}`)
}
</script>

<style scoped lang="scss">
.user-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  
  .avatar {
    transition: transform 0.3s;
    
    &:hover {
      transform: scale(1.05);
    }
    
    &.small {
      width: 32px;
      height: 32px;
    }
    
    &.medium {
      width: 48px;
      height: 48px;
    }
    
    &.large {
      width: 64px;
      height: 64px;
    }
  }
  
  .username {
    font-size: 12px;
    color: #666;
    margin-top: 4px;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 80px;
  }
}
</style>