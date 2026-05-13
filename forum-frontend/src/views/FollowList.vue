<template>
  <Layout title="关注/粉丝">
    <van-tabs v-model:active="activeTab" class="follow-tabs">
      <van-tab title="我的关注">
        <van-list
          v-model:loading="followingLoading"
          :finished="followingFinished"
          finished-text="没有更多了"
          @load="loadFollowing"
          class="user-list"
        >
          <div class="user-item" v-for="user in followingList" :key="user.id">
            <van-image
              :src="user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
              round
              fit="cover"
              class="avatar"
            />
            <div class="user-info" @click="goToProfile(user.id)">
              <div class="username">{{ user.username }}</div>
              <div class="bio" v-if="user.bio">{{ user.bio }}</div>
            </div>
            <FollowButton :user-id="user.id" :is-following="true" @follow-change="handleFollowChange(user.id)" />
          </div>
        </van-list>
        <van-empty v-if="followingList.length === 0 && !followingLoading" description="暂无关注" />
      </van-tab>
      <van-tab title="我的粉丝">
        <van-list
          v-model:loading="followersLoading"
          :finished="followersFinished"
          finished-text="没有更多了"
          @load="loadFollowers"
          class="user-list"
        >
          <div class="user-item" v-for="user in followersList" :key="user.id">
            <van-image
              :src="user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
              round
              fit="cover"
              class="avatar"
            />
            <div class="user-info" @click="goToProfile(user.id)">
              <div class="username">{{ user.username }}</div>
              <div class="bio" v-if="user.bio">{{ user.bio }}</div>
            </div>
            <FollowButton :user-id="user.id" :is-following="user.isFollowing" @follow-change="handleFollowerFollowChange(user.id)" />
          </div>
        </van-list>
        <van-empty v-if="followersList.length === 0 && !followersLoading" description="暂无粉丝" />
      </van-tab>
    </van-tabs>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { followAPI } from '../api/follow'
import { User } from '../types'
import Layout from '../components/Layout.vue'
import FollowButton from '../components/FollowButton.vue'

const route = useRoute()
const router = useRouter()

const activeTab = ref(0)
const followingList = ref<User[]>([])
const followersList = ref<User[]>([])
const followingLoading = ref(false)
const followingFinished = ref(false)
const followersLoading = ref(false)
const followersFinished = ref(false)

const goToProfile = (userId: number) => {
  router.push(`/user/${userId}`)
}

const loadFollowing = async () => {
  if (followingLoading.value) return

  followingLoading.value = true
  try {
    const res: any = await followAPI.getFollowingList(1, 20)
    const users: User[] = (res.records || []).map((u: any) => ({
      ...u,
      isFollowing: true
    }))
    followingList.value = users
    followingFinished.value = true
  } catch (error) {
    console.error('获取关注列表失败:', error)
  } finally {
    followingLoading.value = false
  }
}

const loadFollowers = async () => {
  if (followersLoading.value) return

  followersLoading.value = true
  try {
    const res: any = await followAPI.getFollowersList(1, 20)
    followersList.value = (res.records || []).map((u: any) => ({
      id: u.id,
      username: u.username,
      avatar: u.avatar,
      bio: u.bio,
      isFollowing: u.isFollowing ?? false
    }))
    followersFinished.value = true
  } catch (error) {
    console.error('获取粉丝列表失败:', error)
  } finally {
    followersLoading.value = false
  }
}

const handleFollowChange = (userId: number) => {
  // 从关注列表中移除用户
  const followingIndex = followingList.value.findIndex(user => user.id === userId)
  if (followingIndex > -1) {
    followingList.value.splice(followingIndex, 1)
  }
}

const handleFollowerFollowChange = (userId: number) => {
  // 更新粉丝的关注状态
  const follower = followersList.value.find(user => user.id === userId)
  if (follower) {
    follower.isFollowing = !follower.isFollowing
  }
}

watch(() => activeTab.value, (newTab) => {
  if (newTab === 0 && followingList.length === 0) {
    loadFollowing()
  } else if (newTab === 1 && followersList.length === 0) {
    loadFollowers()
  }
})

onMounted(() => {
  if (activeTab.value === 0) {
    loadFollowing()
  } else {
    loadFollowers()
  }
})
</script>

<style scoped lang="scss">
.follow-tabs {
  background: #fff;
  margin-top: 12px;
}

.user-list {
  padding: 12px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  }
  
  .avatar {
    width: 48px;
    height: 48px;
    flex-shrink: 0;
  }
  
  .user-info {
    flex: 1;
    cursor: pointer;
    
    .username {
      font-size: 16px;
      font-weight: 500;
      color: #333;
      margin-bottom: 4px;
    }
    
    .bio {
      font-size: 14px;
      color: #666;
      line-height: 1.4;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>