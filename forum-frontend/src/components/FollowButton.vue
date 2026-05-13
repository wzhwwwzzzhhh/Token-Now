<template>
  <van-button 
    :type="isFollowing ? 'default' : 'primary'" 
    size="small" 
    :loading="loading"
    @click="handleFollow"
    class="follow-button"
  >
    {{ isFollowing ? '已关注' : '+ 关注' }}
  </van-button>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { followAPI } from '../api/follow'
import { showToast } from 'vant'

const props = defineProps<{
  userId: number
  isFollowing?: boolean
}>()

const emit = defineEmits<{
  (e: 'follow-change', following: boolean): void
}>()

const isFollowing = ref(props.isFollowing || false)
const loading = ref(false)

const handleFollow = async () => {
  try {
    loading.value = true
    if (isFollowing.value) {
      await followAPI.unfollow(props.userId)
      isFollowing.value = false
    } else {
      await followAPI.follow(props.userId)
      isFollowing.value = true
    }
    emit('follow-change', isFollowing.value)
  } catch (error) {
    showToast('操作失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.follow-button {
  border-radius: 16px;
  padding: 0 16px;
  font-size: 12px;
  height: 28px;
  
  &.van-button--primary {
    background: linear-gradient(135deg, #00C9B7, #00A89C);
    border: none;
    color: #fff;
  }
  
  &.van-button--default {
    background: #f7f8fa;
    border: 1px solid #e5e5e5;
    color: #999;
  }
}
</style>