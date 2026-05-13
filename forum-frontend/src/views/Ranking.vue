<template>
  <Layout title="排行榜">
    <div class="ranking-container">
      <!-- Tab 切换 -->
      <div class="tabs">
        <div
          :class="['tab', { active: activeTab === 'post' }]"
          @click="activeTab = 'post'"
        >
          <van-icon name="fire-o" />
          帖子排行
        </div>
        <div
          :class="['tab', { active: activeTab === 'like' }]"
          @click="activeTab = 'like'"
        >
          <van-icon name="like-o" />
          点赞排行
        </div>
      </div>

      <!-- 时间段切换 -->
      <div class="period-tabs">
        <div
          v-for="p in periods"
          :key="p.value"
          :class="['period', { active: period === p.value }]"
          @click="period = p.value"
        >
          {{ p.label }}
        </div>
      </div>

      <!-- 帖子排行榜 -->
      <template v-if="activeTab === 'post'">
        <div class="ranking-list" v-if="postRanking.length > 0">
          <div
            v-for="(post, index) in postRanking"
            :key="post.id"
            class="ranking-item"
            @click="goToPost(post.id)"
          >
            <div :class="['rank-number', { 'top-3': index < 3 }]">
              {{ index + 1 }}
            </div>
            <div class="item-info">
              <h3 class="item-title">{{ post.title }}</h3>
              <div class="item-meta">
                <span class="username">{{ post.user?.username }}</span>
                <span class="like-count">
                  <van-icon name="like-o" /> {{ post.likeCount || 0 }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <van-empty v-else description="暂无数据" />
      </template>

      <!-- 点赞排行榜（用户排行） -->
      <template v-if="activeTab === 'like'">
        <div class="ranking-list" v-if="likeRanking.length > 0">
          <div
            v-for="(item, index) in likeRanking"
            :key="item.userId"
            class="ranking-item"
            @click="goToUser(item.userId)"
          >
            <div :class="['rank-number', { 'top-3': index < 3 }]">
              {{ index + 1 }}
            </div>
            <van-image
              :src="item.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=default'"
              round
              fit="cover"
              class="user-avatar"
            />
            <div class="item-info">
              <h3 class="item-title">{{ item.username }}</h3>
              <div class="item-meta">
                <span class="like-count">
                  <van-icon name="like-o" /> {{ item.likeCount }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <van-empty v-else description="暂无数据" />
      </template>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Layout from '../components/Layout.vue'
import { rankingAPI, LikeRankItem } from '../api/ranking'
import { showToast } from 'vant'

const router = useRouter()

const activeTab = ref<'post' | 'like'>('post')
const period = ref('week')
const postRanking = ref<any[]>([])
const likeRanking = ref<LikeRankItem[]>([])
const loading = ref(false)

const periods = [
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '总榜', value: 'all' }
]

watch(activeTab, () => {
  period.value = 'week'
  fetchData()
})

watch(period, () => {
  fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    if (activeTab.value === 'post') {
      postRanking.value = await rankingAPI.getPostRanking(period.value, 20)
    } else {
      const periodMap: Record<string, string> = { week: 'weekly', month: 'monthly', all: 'all' }
      likeRanking.value = await rankingAPI.getLikeRanking(periodMap[period.value] || 'weekly', 20)
    }
  } catch (error) {
    console.error('获取排行榜失败:', error)
  } finally {
    loading.value = false
  }
}

const goToPost = (postId: number) => {
  router.push(`/post/${postId}`)
}

const goToUser = (userId: number) => {
  router.push(`/user/${userId}`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.ranking-container {
  padding: 12px;

  .tabs {
    display: flex;
    background: #fff;
    border-radius: 12px;
    padding: 4px;
    margin-bottom: 12px;

    .tab {
      flex: 1;
      text-align: center;
      padding: 10px 0;
      font-size: 14px;
      color: #999;
      border-radius: 10px;
      cursor: pointer;
      transition: all 0.3s ease;

      .van-icon {
        margin-right: 4px;
        font-size: 16px;
      }

      &.active {
        color: #fff;
        background: linear-gradient(135deg, #00C9B7, #00A89C);
        font-weight: 600;
      }
    }
  }

  .period-tabs {
    display: flex;
    gap: 8px;
    margin-bottom: 12px;

    .period {
      padding: 6px 16px;
      font-size: 13px;
      color: #666;
      background: #fff;
      border-radius: 16px;
      cursor: pointer;
      transition: all 0.3s ease;

      &.active {
        color: #00C9B7;
        background: #f0faf9;
        font-weight: 600;
      }

      &:active {
        opacity: 0.7;
      }
    }
  }

  .ranking-list {
    .ranking-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 14px 16px;
      margin-bottom: 8px;
      background: #fff;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:active {
        transform: scale(0.98);
        background: #fafafa;
      }

      .rank-number {
        width: 28px;
        height: 28px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        font-weight: 700;
        color: #999;
        flex-shrink: 0;

        &.top-3 {
          color: #fff;
          border-radius: 50%;
          font-size: 13px;
        }

        &.top-3:nth-child(1) {
          background: linear-gradient(135deg, #FFD700, #FFA500);
        }

        &.top-3:nth-child(2) {
          background: linear-gradient(135deg, #E8E8E8, #C0C0C0);
        }

        &.top-3:nth-child(3) {
          background: linear-gradient(135deg, #FFB347, #D4875E);
        }
      }

      .user-avatar {
        width: 40px;
        height: 40px;
        flex-shrink: 0;
      }

      .item-info {
        flex: 1;
        min-width: 0;

        .item-title {
          font-size: 15px;
          font-weight: 600;
          color: #333;
          margin: 0 0 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .item-meta {
          display: flex;
          align-items: center;
          gap: 12px;
          font-size: 12px;
          color: #999;

          .like-count {
            .van-icon {
              margin-right: 2px;
              color: #ff4757;
            }
          }
        }
      }
    }
  }
}
</style>
