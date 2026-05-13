<template>
  <Layout title="" :show-notification="false" hide-nav>
    <div class="notification-container">
      <!-- 顶部导航 -->
      <div class="top-nav">
        <van-icon name="arrow-left" size="22" class="nav-btn" @click="goBack" />
        <div class="nav-title">消息</div>
        <van-icon name="setting-o" size="22" class="nav-btn" @click="markAllAsRead" />
      </div>
      
      <!-- 分类Tab -->
      <div class="tab-container">
        <div 
          v-for="tab in tabs" 
          :key="tab.name"
          :class="['tab-item', { active: activeTab === tab.name }]"
          @click="activeTab = tab.name"
        >
          <span>{{ tab.label }}</span>
          <span class="tab-badge" v-if="tab.count > 0">{{ tab.count }}</span>
        </div>
      </div>
      
      <!-- 通知列表 -->
      <div class="notification-list">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text=""
          @load="loadNotifications"
        >
          <div
            v-for="notification in filteredNotifications"
            :key="notification.id"
            :class="['notification-item', { 'unread': !notification.isRead }]"
            @click="handleNotificationClick(notification)"
          >
            <!-- 通知图标 -->
            <div class="notification-icon" :class="notification.type">
              <van-icon :name="getNotificationIcon(notification.type)" size="24" />
            </div>
            
            <!-- 通知内容 -->
            <div class="notification-content">
              <div class="notification-header">
                <span class="notification-user">{{ notification.fromUser?.username || '用户' }}</span>
                <span class="notification-type">{{ getNotificationTypeText(notification.type) }}</span>
              </div>
              <div class="notification-text">{{ notification.content }}</div>
              <div class="notification-time">{{ formatTime(notification.createTime) }}</div>
            </div>
            
            <!-- 未读标记 -->
            <div class="unread-indicator" v-if="!notification.isRead"></div>
          </div>
        </van-list>
        
        <van-empty 
          v-if="filteredNotifications.length === 0 && !loading" 
          description="暂无消息" 
          image="search"
        />
        
        <div class="load-more-tip" v-if="finished && filteredNotifications.length > 0">
          - 没有更多了 -
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { Notification } from '../types'
import { notificationAPI } from '../api/notification'
import { adaptNotification } from '../utils/adapter'
import Layout from '../components/Layout.vue'

dayjs.extend(relativeTime)

const router = useRouter()
const activeTab = ref('all')
const notifications = ref<Notification[]>([])
const loading = ref(false)
const finished = ref(false)
const page = ref(1)

const tabs = [
  { label: '全部', name: 'all', count: 0 },
  { label: '点赞', name: 'like', count: 0 },
  { label: '评论', name: 'comment', count: 0 },
  { label: '关注', name: 'follow', count: 0 }
]

const filteredNotifications = computed(() => {
  if (activeTab.value === 'all') {
    return notifications.value
  }
  return notifications.value.filter(n => n.type === activeTab.value)
})

const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}

const getNotificationIcon = (type: number) => {
  switch (type) {
    case 1: return 'like-o'
    case 2: return 'chat-o'
    case 3: return 'friends-o'
    default: return 'bell'
  }
}

const getNotificationTypeText = (type: number) => {
  switch (type) {
    case 1: return '点赞'
    case 2: return '评论'
    case 3: return '关注'
    default: return '通知'
  }
}

const formatTime = (time: string) => {
  const now = dayjs()
  const target = dayjs(time)
  const diff = now.diff(target, 'minute')

  if (diff < 1) {
    return '刚刚'
  } else if (diff < 60) {
    return `${diff}分钟前`
  } else if (diff < 1440) {
    return `${Math.floor(diff / 60)}小时前`
  } else {
    return target.format('MM月DD日')
  }
}

const loadNotifications = async () => {
  if (loading.value) return

  loading.value = true

  try {
    const res: any = await notificationAPI.getNotifications(page.value, 10)
    const newNotifications = (res.records || []).map(adaptNotification)
    notifications.value = [...notifications.value, ...newNotifications]
    finished.value = newNotifications.length < 10
    page.value++
  } catch (error) {
    console.error('获取通知失败:', error)
  } finally {
    loading.value = false
  }
}

const markAllAsRead = async () => {
  try {
    await notificationAPI.markAllAsRead()
    notifications.value.forEach(notification => {
      notification.isRead = true
    })
    showToast('全部已读')
  } catch (error) {
    showToast('操作失败')
  }
}

const handleNotificationClick = async (notification: Notification) => {
  if (!notification.isRead) {
    try {
      await notificationAPI.markAsRead(notification.id)
      notification.isRead = true
    } catch (error) {
      // ignore
    }
  }

  if (notification.type === 1 || notification.type === 2) {
    router.push(`/post/${notification.targetId}`)
  } else if (notification.type === 3) {
    router.push(`/user/${notification.targetId}`)
  }
}

onMounted(() => {
  loadNotifications()
})
</script>

<style scoped lang="scss">
.notification-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  
  .top-nav {
    position: sticky;
    top: 0;
    z-index: 100;
    background: linear-gradient(135deg, #00C9B7, #00A89C);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    padding-top: calc(12px + env(safe-area-inset-top));
    
    .nav-btn {
      color: #fff;
      cursor: pointer;
      padding: 4px;
      
      &:active {
        opacity: 0.7;
      }
    }
    
    .nav-title {
      font-size: 17px;
      font-weight: 600;
      color: #fff;
    }
  }
  
  .tab-container {
    position: sticky;
    top: 0;
    z-index: 99;
    background: #fff;
    display: flex;
    align-items: center;
    padding: 0 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    
    .tab-item {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      padding: 14px 0;
      font-size: 15px;
      color: #666;
      font-weight: 500;
      cursor: pointer;
      position: relative;
      transition: all 0.3s ease;
      
      &.active {
        color: #00C9B7;
        font-weight: 600;
        
        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 50%;
          transform: translateX(-50%);
          width: 24px;
          height: 3px;
          background: linear-gradient(135deg, #00C9B7, #00A89C);
          border-radius: 2px;
        }
      }
      
      .tab-badge {
        background: #ff4757;
        color: #fff;
        font-size: 10px;
        padding: 1px 6px;
        border-radius: 10px;
        min-width: 16px;
        text-align: center;
        line-height: 1.4;
      }
      
      &:active {
        opacity: 0.7;
      }
    }
  }
  
  .notification-list {
    padding: 12px 16px;
    padding-bottom: 80px;
    
    .notification-item {
      display: flex;
      align-items: flex-start;
      gap: 12px;
      background: #fff;
      border-radius: 16px;
      padding: 16px;
      margin-bottom: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
      cursor: pointer;
      position: relative;
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.98);
        opacity: 0.9;
      }
      
      &.unread {
        background: linear-gradient(135deg, #f0faf9, #fff);
        
        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 4px;
          height: 60%;
          background: linear-gradient(135deg, #00C9B7, #00A89C);
          border-radius: 0 2px 2px 0;
        }
      }
      
      .notification-icon {
        width: 44px;
        height: 44px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
        
        &.like {
          background: linear-gradient(135deg, #fff0f0, #ffe0e0);
          color: #ff6b6b;
          
          :deep(.van-icon) {
            color: #ff6b6b;
          }
        }
        
        &.comment {
          background: linear-gradient(135deg, #f0faf9, #e0f5f3);
          color: #00C9B7;
          
          :deep(.van-icon) {
            color: #00C9B7;
          }
        }
        
        &.follow {
          background: linear-gradient(135deg, #f0f0ff, #e0e0ff);
          color: #6c5ce7;
          
          :deep(.van-icon) {
            color: #6c5ce7;
          }
        }
      }
      
      .notification-content {
        flex: 1;
        min-width: 0;
        
        .notification-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 6px;
          
          .notification-user {
            font-size: 14px;
            font-weight: 600;
            color: #333;
          }
          
          .notification-type {
            font-size: 12px;
            color: #999;
            background: #f5f5f5;
            padding: 2px 8px;
            border-radius: 8px;
          }
        }
        
        .notification-text {
          font-size: 13px;
          color: #666;
          line-height: 1.5;
          margin-bottom: 6px;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        
        .notification-time {
          font-size: 12px;
          color: #bbb;
        }
      }
      
      .unread-indicator {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #00C9B7;
        flex-shrink: 0;
        margin-top: 6px;
      }
    }
    
    .load-more-tip {
      text-align: center;
      color: #ccc;
      font-size: 13px;
      padding: 20px 0;
    }
  }
}
</style>