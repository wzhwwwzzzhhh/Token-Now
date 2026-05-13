<template>
  <Layout title="" :show-notification="false">
    <div class="home-container">
      <!-- 顶部标题栏 -->
      <div class="header-bar">
        <div class="header-title">论坛</div>
        <van-icon name="search" class="search-icon" @click="goToSearch" />
      </div>
      
      <!-- Tab导航 - 胶囊按钮样式 -->
      <div class="tab-container">
        <div 
          v-for="tab in tabs" 
          :key="tab.name"
          :class="['tab-item', { active: activeTab === tab.name }]"
          @click="activeTab = tab.name"
        >
          {{ tab.label }}
        </div>
      </div>
      
      <!-- 帖子列表 -->
      <div class="post-list-container">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text=""
          @load="loadPosts"
          :immediate-check="false"
        >
          <PostCard
            v-for="post in posts"
            :key="post.id"
            :post="post"
            @like-change="handleLikeChange(post.id)"
          />
        </van-list>
        
        <van-empty 
          v-if="posts.length === 0 && !loading" 
          description="暂无帖子内容" 
          image="search"
        />
        
        <div class="load-more-tip" v-if="finished && posts.length > 0">
          - 没有更多了 -
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Post } from '../types'
import { postAPI } from '../api/post'
import { adaptPost } from '../utils/adapter'
import Layout from '../components/Layout.vue'
import PostCard from '../components/PostCard.vue'

const router = useRouter()
const activeTab = ref('follow')
const posts = ref<Post[]>([])
const loading = ref(false)
const finished = ref(false)
const page = ref(1)

const tabs = [
  { label: '关注', name: 'follow' },
  { label: '热门', name: 'hot' },
  { label: '板块', name: 'square' }
]

const goToSearch = () => {
  console.log('搜索')
}

const handleLikeChange = (postId: number) => {
  console.log('点赞状态变化:', postId)
}

const loadPosts = async () => {
  if (loading.value) return

  loading.value = true

  try {
    const res: any = await postAPI.getPosts(page.value, 10)
    const newPosts = (res.records || res || []).map(adaptPost)
    posts.value = [...posts.value, ...newPosts]
    finished.value = newPosts.length < 10
    page.value++
  } catch (error) {
    console.error('获取帖子失败:', error)
  } finally {
    loading.value = false
  }
}

watch(activeTab, () => {
  page.value = 1
  finished.value = false
  posts.value = []
  loadPosts()
})

onMounted(() => {
  loadPosts()
})
</script>

<style scoped lang="scss">
.home-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  
  .header-bar {
    position: sticky;
    top: 0;
    z-index: 100;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 12px 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    
    .header-title {
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }
    
    .search-icon {
      position: absolute;
      right: 16px;
      font-size: 22px;
      color: #666;
      padding: 4px;
      cursor: pointer;
      
      &:active {
        opacity: 0.7;
      }
    }
  }
  
  .tab-container {
    position: sticky;
    top: 46px;
    z-index: 99;
    background-color: #f7f8fa;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    padding: 12px 16px;
    
    .tab-item {
      flex: 1;
      max-width: 100px;
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #fff;
      border-radius: 18px;
      font-size: 14px;
      color: #666;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
      
      &.active {
        color: #00C9B7;
        font-weight: 600;
        position: relative;
        
        &::after {
          content: '';
          position: absolute;
          bottom: 6px;
          left: 50%;
          transform: translateX(-50%);
          width: 20px;
          height: 2px;
          background-color: #00C9B7;
          border-radius: 1px;
        }
      }
      
      &:active {
        opacity: 0.8;
      }
    }
  }
  
  .post-list-container {
    padding: 12px 16px;
    padding-bottom: 80px;
    
    .load-more-tip {
      text-align: center;
      color: #ccc;
      font-size: 13px;
      padding: 20px 0;
    }
  }
}
</style>