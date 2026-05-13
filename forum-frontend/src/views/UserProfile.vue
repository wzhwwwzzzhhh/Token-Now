<template>
  <Layout title="" :show-notification="false" hide-nav>
    <div class="profile-container">
      <!-- 顶部渐变头部 -->
      <div class="profile-header">
        <div class="header-top">
          <div class="header-left">
            <van-icon name="arrow-left" size="22" class="back-btn" @click="goBack" />
            <div class="header-title">论坛</div>
          </div>
          <div class="header-icons">
            <van-icon name="setting-o" size="22" />
            <van-icon name="scan" size="22" />
          </div>
        </div>
        
        <div class="user-info-section">
          <div class="avatar-wrapper" @click="handleAvatarClick">
            <van-image
              :src="userStore.user?.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
              round
              fit="cover"
              class="avatar"
            />
            <div class="avatar-overlay">
              <van-icon name="photograph" size="20" />
              <span>更换头像</span>
            </div>
            <input
              ref="fileInputRef"
              type="file"
              accept="image/*"
              class="file-input"
              @change="handleFileChange"
            />
          </div>
          <div class="user-details">
            <div class="username-row">
              <span class="username">{{ userStore.user?.username || '我的' }}</span>
              <div class="level-badge">
                <van-icon name="play-circle-o" size="12" />
                <span>等级</span>
                <van-icon name="arrow-down" size="10" />
              </div>
            </div>
            <div class="user-stats">
              <div class="stat-item">
                <span class="stat-value">{{ userStore.user?.followCount || 0 }}</span>
                <span class="stat-label">关注</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ userStore.user?.fansCount || 0 }}</span>
                <span class="stat-label">粉丝</span>
              </div>
            </div>
          </div>
          <van-button class="info-btn" size="small" round>
            编辑资料
          </van-button>
        </div>
      </div>
      
      <!-- 功能入口卡片 -->
      <div class="function-cards">
        <div class="card-item" @click="goToMyPosts">
          <div class="card-icon">
            <van-icon name="notes-o" size="28" color="#00C9B7" />
          </div>
          <span class="card-label">帖子</span>
        </div>
        <div class="card-item" @click="goToSignIn">
          <div class="card-icon">
            <van-icon name="clock-o" size="28" color="#00C9B7" />
          </div>
          <span class="card-label">签到</span>
        </div>
        <div class="card-item" @click="goToFollowing">
          <div class="card-icon">
            <van-icon name="friends-o" size="28" color="#00C9B7" />
          </div>
          <span class="card-label">关注</span>
        </div>
        <div class="card-item" @click="goToSettings">
          <div class="card-icon">
            <van-icon name="setting-o" size="28" color="#00C9B7" />
          </div>
          <span class="card-label">设置</span>
        </div>
      </div>
      
      <!-- 帖子列表 -->
      <div class="posts-section">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text=""
          @load="loadUserPosts"
          :immediate-check="false"
        >
          <div 
            v-for="post in posts" 
            :key="post.id" 
            class="post-item"
            @click="goToPost(post.id)"
          >
            <van-image
              :src="post.user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
              round
              fit="cover"
              class="post-avatar"
            />
            <div class="post-content">
              <div class="post-title">{{ post.title || '帖子标题' }}</div>
              <div class="post-time">{{ formatTime(post.createTime) }}</div>
              <div class="post-text">{{ post.content }}</div>
              <div class="post-stats">
                <span class="stat-item">
                  <van-icon name="like-o" size="14" />
                  {{ post.likeCount || 0 }}
                </span>
                <span class="stat-item">
                  <van-icon name="chat-o" size="14" />
                  {{ post.commentCount || 0 }}
                </span>
              </div>
            </div>
            <van-icon name="ellipsis" class="post-more" />
          </div>
        </van-list>
        
        <van-empty 
          v-if="posts.length === 0 && !loading" 
          description="暂无帖子" 
          image="search"
        />
        
        <div class="load-more-tip" v-if="finished && posts.length > 0">
        - 没有更多了 -
      </div>
    </div>

    <!-- 退出登录 -->
    <div class="logout-section">
      <van-button
        round
        block
        class="logout-btn"
        @click="handleLogout"
      >
        退出登录
      </van-button>
    </div>
  </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted, watchEffect } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Post } from '../types'
import Layout from '../components/Layout.vue'
import dayjs from 'dayjs'
import { useUserStore } from '../stores/userStore'
import { showToast, showLoadingToast, closeToast } from 'vant'
import { uploadAPI } from '../api/upload'
import { userAPI } from '../api/user'
import { personalAPI } from '../api/personal'
import { adaptPost } from '../utils/adapter'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const posts = ref<Post[]>([])
const loading = ref(false)
const finished = ref(false)
const page = ref(1)
const fileInputRef = ref<HTMLInputElement | null>(null)

// 检查登录状态
watchEffect(() => {
  if (route.path === '/user/me' && !userStore.isLoggedIn) {
    router.push('/login')
  }
})

// 点击头像触发文件选择
const handleAvatarClick = () => {
  fileInputRef.value?.click()
}

// 选择文件后上传
const handleFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  showLoadingToast({ message: '上传中...', forbidClick: true })

  try {
    const url = await uploadAPI.uploadFile(file)
    await userAPI.updateUserInfo({ avatar: url })
    if (userStore.user) {
      userStore.user.avatar = url
    }
    closeToast()
    showToast('头像更新成功')
  } catch (error) {
    closeToast()
    console.error('上传失败:', error)
    showToast('头像上传失败')
  }

  target.value = ''
}



const formatTime = (time: string) => {
  return dayjs(time).format('YYYY年MM月DD日')
}

const goToMyPosts = () => {
  console.log('查看我的帖子')
}

const goToSignIn = () => {
  router.push('/sign')
}

const goToFollowing = () => {
  router.push('/follow')
}

const goToSettings = () => {
  console.log('打开设置')
}

const goToPost = (postId: number) => {
  router.push(`/post/${postId}`)
}

const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}

const handleLogout = () => {
  userStore.logout()
  showToast('已退出登录')
  router.push('/login')
}

const loadUserPosts = async () => {
  if (loading.value) return

  loading.value = true

  try {
    const res: any = await personalAPI.getMyPosts(page.value, 10)
    const newPosts = (res.records || []).map(adaptPost)
    posts.value = [...posts.value, ...newPosts]
    finished.value = newPosts.length < 10
    page.value++
  } catch (error) {
    console.error('获取用户帖子失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (userStore.isLoggedIn) {
    // 获取用户信息
    userStore.getCurrentUserInfo().catch(error => {
      console.error('获取用户信息失败:', error)
      showToast('获取用户信息失败')
    })
  }
  loadUserPosts()
})
</script>

<style scoped lang="scss">
.profile-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  
  .profile-header {
    background: linear-gradient(135deg, #00C9B7, #00A89C);
    padding: 16px;
    padding-top: 50px;
    padding-bottom: 60px;
    border-radius: 0 0 24px 24px;
    
    .header-top {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 24px;
      
      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;
        
        .back-btn {
          color: #fff;
          cursor: pointer;
          
          &:active {
            opacity: 0.7;
          }
        }
      }
      
      .header-title {
        font-size: 24px;
        font-weight: 700;
        color: #fff;
      }
      
      .header-icons {
        display: flex;
        gap: 16px;
        
        :deep(.van-icon) {
          color: #fff;
          cursor: pointer;
          
          &:active {
            opacity: 0.7;
          }
        }
      }
    }
    
    .user-info-section {
      display: flex;
      align-items: center;
      gap: 16px;
      
      .avatar-wrapper {
        position: relative;
        cursor: pointer;
        border-radius: 50%;
        flex-shrink: 0;

        .avatar {
          width: 72px;
          height: 72px;
          border: 3px solid rgba(255, 255, 255, 0.5);
          display: block;
        }

        .avatar-overlay {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          border-radius: 50%;
          background: rgba(0, 0, 0, 0.5);
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          gap: 2px;
          opacity: 0;
          transition: opacity 0.3s ease;
          color: #fff;
          font-size: 11px;
        }

        &:hover .avatar-overlay {
          opacity: 1;
        }

        &:active .avatar-overlay {
          opacity: 0.8;
        }
      }

      .file-input {
        display: none;
      }
      
      .user-details {
        flex: 1;
        
        .username-row {
          display: flex;
          align-items: center;
          gap: 10px;
          margin-bottom: 8px;
          
          .username {
            font-size: 20px;
            font-weight: 600;
            color: #fff;
          }
          
          .level-badge {
            display: flex;
            align-items: center;
            gap: 4px;
            background: rgba(255, 255, 255, 0.25);
            padding: 2px 10px;
            border-radius: 12px;
            font-size: 12px;
            color: #fff;
            
            :deep(.van-icon) {
              color: #fff;
            }
          }
        }
        
        .user-stats {
          display: flex;
          gap: 20px;
          
          .stat-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            
            .stat-value {
              font-size: 16px;
              font-weight: 600;
              color: #fff;
            }
            
            .stat-label {
              font-size: 12px;
              color: rgba(255, 255, 255, 0.8);
            }
          }
        }
      }
      
      .info-btn {
        background: rgba(255, 255, 255, 0.25);
        border: none;
        color: #fff;
        font-size: 13px;
        padding: 0 16px;
        height: 32px;
        
        &:active {
          opacity: 0.8;
        }
      }
    }
  }
  
  .function-cards {
    background: #fff;
    margin: -30px 16px 16px;
    border-radius: 16px;
    padding: 20px 10px;
    display: flex;
    justify-content: space-around;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    position: relative;
    z-index: 10;
    
    .card-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      
      .card-icon {
        width: 52px;
        height: 52px;
        background: #f0faf9;
        border-radius: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
      }
      
      .card-label {
        font-size: 13px;
        color: #333;
        font-weight: 500;
      }
      
      &:active {
        .card-icon {
          transform: scale(0.95);
        }
      }
    }
  }
  
  .posts-section {
    padding: 0 16px;
    padding-bottom: 80px;
    
    .post-item {
      background: #fff;
      border-radius: 12px;
      padding: 16px;
      margin-bottom: 12px;
      display: flex;
      gap: 12px;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:active {
        opacity: 0.9;
      }
      
      .post-avatar {
        width: 44px;
        height: 44px;
        flex-shrink: 0;
      }
      
      .post-content {
        flex: 1;
        min-width: 0;
        
        .post-title {
          font-size: 15px;
          font-weight: 600;
          color: #333;
          margin-bottom: 4px;
        }
        
        .post-time {
          font-size: 12px;
          color: #999;
          margin-bottom: 6px;
        }
        
        .post-text {
          font-size: 13px;
          color: #666;
          line-height: 1.5;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          margin-bottom: 8px;
        }
        
        .post-stats {
          display: flex;
          gap: 16px;
          
          .stat-item {
            display: flex;
            align-items: center;
            gap: 4px;
            font-size: 12px;
            color: #999;
            
            :deep(.van-icon) {
              color: #bbb;
            }
          }
        }
      }
      
      .post-more {
        color: #ccc;
        font-size: 18px;
        flex-shrink: 0;
        align-self: flex-start;
      }
    }
    
    .load-more-tip {
      text-align: center;
      color: #ccc;
      font-size: 13px;
      padding: 20px 0;
    }
  }

  .logout-section {
    padding: 0 16px 80px;

    .logout-btn {
      height: 44px;
      font-size: 15px;
      color: #ff4757;
      background: #fff;
      border: 1px solid #f0f0f0;

      &:active {
        opacity: 0.8;
      }
    }
  }
}
</style>