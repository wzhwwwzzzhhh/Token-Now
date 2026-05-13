<template>
  <div class="layout-container">
    <!-- 顶部导航栏 - 首页不显示 -->
    <van-nav-bar
      v-if="!isHomePage && !hideNav"
      :title="title"
      :left-arrow="showBackButton"
      class="nav-bar"
      :border="false"
      @click-left="goBack"
    >
      <template #left>
        <van-icon 
          v-if="showBackButton" 
          name="arrow-left" 
          class="back-button"
          size="20"
        />
      </template>
      
      <template #right v-if="showNotification">
        <div class="notification-bell">
          <van-icon name="bell" size="20" />
          <span class="badge-dot" v-if="hasUnread"></span>
        </div>
      </template>
    </van-nav-bar>
    
    <!-- 主要内容区域 -->
    <div class="content">
      <slot></slot>
    </div>
    
    <!-- 底部导航栏 - 自定义设计 -->
    <div class="bottom-tabbar" v-if="showTabbar">
      <div 
        v-for="(item, index) in tabbarItems" 
        :key="index"
        :class="['tabbar-item', { active: currentRoute === item.path, 'center-item': item.isCenter }]"
        @click="handleTabClick(item)"
      >
        <!-- 中间的大+按钮 -->
        <template v-if="item.isCenter">
          <div class="center-button">
            <van-icon name="plus" />
          </div>
        </template>
        
        <!-- 普通导航项 -->
        <template v-else-if="item.path !== '/user/me'">
          <van-icon :name="currentRoute === item.path ? item.activeIcon : item.icon" />
          <span class="tabbar-text">{{ item.label }}</span>
        </template>
        
        <!-- 我的页面 -->
        <template v-else>
          <div v-if="userStore.isLoggedIn">
            <div class="user-avatar">
              <img :src="userStore.user?.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'" alt="用户头像" />
            </div>
            <span class="tabbar-text">{{ userStore.user?.username || '我的' }}</span>
          </div>
          <div v-else>
            <van-icon :name="currentRoute === item.path ? item.activeIcon : item.icon" />
            <span class="tabbar-text">{{ item.label }}</span>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/userStore'

const props = withDefaults(defineProps<{
  title: string
  showNotification?: boolean
  showBack?: boolean
  hideNav?: boolean
}>(), {
  showNotification: false,
  showBack: false,
  hideNav: false
})

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const currentRoute = computed(() => route.path)

// 是否是首页
const isHomePage = computed(() => {
  return route.path === '/'
})

// 是否显示返回按钮
const showBackButton = computed(() => {
  if (props.showBack) return true
  // 首页不显示返回按钮
  return route.path !== '/'
})

// 是否显示底部导航栏
const showTabbar = computed(() => {
  const hideTabbarRoutes = ['/login', '/create-post', '/post/create']
  return !hideTabbarRoutes.some(r => route.path.startsWith(r))
})

// 是否有未读消息
const hasUnread = computed(() => {
  // 这里可以从store获取实际的未读数
  return Math.random() > 0.5
})

// 返回上一页
const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}

// 底部导航项配置
const tabbarItems = [
  {
    label: '首页',
    path: '/',
    icon: 'home-o',
    activeIcon: 'home',
    isCenter: false
  },
  {
    label: '消息',
    path: '/notification',
    icon: 'chat-o',
    activeIcon: 'chat-o',
    isCenter: false
  },
  {
    label: '',
    path: '/create-post',
    icon: '',
    activeIcon: '',
    isCenter: true
  },
  {
    label: '发布',
    path: '/post/create',
    icon: 'edit',
    activeIcon: 'edit',
    isCenter: false
  },
  {
    label: '我的',
    path: '/user/me',
    icon: 'user-o',
    activeIcon: 'user',
    isCenter: false
  }
]

const handleTabClick = (item: any) => {
  if (item.path === '/user/me' && !userStore.isLoggedIn) {
    // 如果未登录，跳转到登录页面
    router.push('/login')
  } else if (item.path !== route.path) {
    router.push(item.path)
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f7f8fa;
  
  .nav-bar {
    background-color: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    
    :deep(.van-nav-bar__title) {
      color: #333;
      font-weight: 600;
      font-size: 17px;
    }
    
    .back-button {
      color: #00C9B7;
      font-weight: bold;
      
      &:active {
        opacity: 0.6;
      }
    }
    
    .notification-bell {
      position: relative;
      padding: 4px;
      
      :deep(.van-icon) {
        color: #666;
      }
      
      &:active {
        opacity: 0.6;
      }
      
      .badge-dot {
        position: absolute;
        top: 2px;
        right: 2px;
        width: 8px;
        height: 8px;
        background: #ff4757;
        border-radius: 50%;
        border: 1px solid white;
      }
    }
  }
  
  .content {
    flex: 1;
    padding-bottom: 70px;
  }
  
  .bottom-tabbar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 60px;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-around;
    padding: 0 10px;
    padding-bottom: env(safe-area-inset-bottom);
    box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.05);
    border-top: 1px solid #f0f0f0;
    z-index: 1000;
    
    .tabbar-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 2px;
      min-width: 60px;
      height: 100%;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:not(.center-item):active {
        opacity: 0.6;
      }
      
      &.center-item {
        .center-button {
          width: 48px;
          height: 48px;
          background: linear-gradient(135deg, #00C9B7, #00A89C);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 4px 12px rgba(0, 201, 183, 0.4);
          transition: all 0.3s ease;
          
          :deep(.van-icon) {
            color: white;
            font-size: 26px;
            font-weight: bold;
          }
          
          &:active {
            transform: scale(0.95);
            box-shadow: 0 2px 6px rgba(0, 201, 183, 0.3);
          }
        }
      }
      
      &.active:not(.center-item) {
        .tabbar-text {
          color: #00C9B7;
          font-weight: 600;
        }
        
        :deep(.van-icon) {
          color: #00C9B7;
        }
      }
      
      &.active.center-item {
        transform: translateY(-5px);
      }
      
      :deep(.van-icon) {
        font-size: 22px;
        color: #999;
        transition: color 0.3s ease;
      }
      
      .tabbar-text {
        font-size: 11px;
        color: #999;
        transition: all 0.3s ease;
        margin-top: 2px;
      }
      
      .user-avatar {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        overflow: hidden;
        
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 375px) {
  .layout-container {
    .bottom-tabbar {
      .tabbar-item {
        .center-button {
          width: 44px;
          height: 44px;
          
          :deep(.van-icon) {
            font-size: 24px;
          }
        }
        
        :deep(.van-icon) {
          font-size: 20px;
        }
        
        .tabbar-text {
          font-size: 10px;
        }
        
        .user-avatar {
          width: 22px;
          height: 22px;
        }
      }
    }
  }
}
</style>