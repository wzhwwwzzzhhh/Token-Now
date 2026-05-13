<template>
  <Layout title="" :show-notification="false" hide-nav>
    <div class="post-detail-container">
      <!-- 顶部导航 -->
      <div class="top-nav">
        <van-icon name="arrow-left" size="22" class="nav-btn" @click="goBack" />
        <div class="nav-title">帖子详情</div>
        <van-icon name="ellipsis" size="22" class="nav-btn" />
      </div>
      
      <!-- 帖子封面图 -->
      <div class="post-cover" v-if="post?.image">
        <van-image
          :src="post.image"
          fit="cover"
          class="cover-image"
        />
        <div class="cover-overlay"></div>
      </div>
      
      <!-- 帖子内容区域 -->
      <div class="post-content-wrapper">
        <!-- 帖子主体 -->
        <div class="post-content" v-if="post">
          <!-- 标题 -->
          <h1 class="post-title">{{ post.title || '帖子标题' }}</h1>
          
          <!-- 作者信息卡片 -->
          <div class="author-card">
            <van-image
              :src="post.user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
              round
              fit="cover"
              class="author-avatar"
            />
            <div class="author-info">
              <div class="author-name">{{ post.user.username }}</div>
              <div class="author-meta">
                <span class="post-time">{{ formatTime(post.createTime) }}</span>
                <span class="separator">·</span>
                <span class="view-count">{{ post.viewCount || 0 }}浏览</span>
              </div>
            </div>
            <FollowButton 
              v-if="post.user.id !== 1" 
              :user-id="post.user.id" 
              :is-following="post.user.isFollowing"
              @follow-change="handleFollowChange"
            />
          </div>
          
          <!-- 帖子正文 -->
          <div class="post-body">{{ post.content }}</div>
          
          <!-- 帖子标签 -->
          <div class="post-tags" v-if="post.tags && post.tags.length > 0">
            <span 
              v-for="(tag, index) in post.tags" 
              :key="index" 
              class="tag-item"
            >
              #{{ tag }}
            </span>
          </div>
          
          <!-- 互动数据 -->
          <div class="post-actions">
            <div class="action-item" @click="handleLike">
              <van-icon :name="post.isLiked ? 'like' : 'like-o'" :class="{ liked: post.isLiked }" />
              <span :class="{ liked: post.isLiked }">{{ post.likeCount || 0 }}</span>
            </div>
            <div class="action-item">
              <van-icon name="chat-o" />
              <span>{{ post.commentCount || 0 }}</span>
            </div>
            <div class="action-item" @click="handleShare">
              <van-icon name="share-o" />
              <span>分享</span>
            </div>
            <div class="action-item" @click="handleCollect">
              <van-icon name="star-o" />
              <span>收藏</span>
            </div>
          </div>
        </div>
        
        <!-- 点赞排行榜 -->
        <div class="like-ranking-section" v-if="likeRanking.length > 0">
          <div class="ranking-header">
            <van-icon name="fire-o" class="ranking-fire" />
            <span class="ranking-title">点赞榜</span>
            <span class="ranking-subtitle">TOP {{ likeRanking.length }}</span>
          </div>
          <div class="ranking-avatars">
            <div
              v-for="(item, index) in likeRanking"
              :key="item.userId"
              class="ranking-user"
              @click="goToUser(item.userId)"
            >
              <div class="avatar-wrapper" :class="'rank-' + (index + 1)">
                <van-image
                  :src="item.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
                  round
                  fit="cover"
                  class="ranking-avatar"
                />
                <div class="rank-badge">{{ index + 1 }}</div>
              </div>
              <div class="ranking-username">{{ item.username }}</div>
            </div>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comments-section">
          <div class="section-header">
            <h2 class="section-title">评论</h2>
            <span class="comment-count">{{ comments.length }}条</span>
          </div>
          
          <van-list
            v-model:loading="commentLoading"
            :finished="commentFinished"
            finished-text=""
            @load="loadComments"
            class="comment-list"
          >
            <div 
              v-for="comment in comments" 
              :key="comment.id" 
              class="comment-item"
            >
              <van-image
                :src="comment.user.avatar || 'https://neeko-copilot.bytedance.net/api/text2image?prompt=default%20user%20avatar&size=256x256'"
                round
                fit="cover"
                class="comment-avatar"
              />
              <div class="comment-body">
                <div class="comment-header">
                  <span class="comment-user">{{ comment.user.username }}</span>
                  <span class="comment-time">{{ formatCommentTime(comment.createTime) }}</span>
                </div>
                <div class="comment-content">{{ comment.content }}</div>
                <div class="comment-footer">
                  <div class="comment-action" @click="handleCommentLike(comment)">
                    <van-icon :name="comment.isLiked ? 'like' : 'like-o'" :class="{ liked: comment.isLiked }" />
                    <span :class="{ liked: comment.isLiked }">{{ comment.likeCount || 0 }}</span>
                  </div>
                  <div class="comment-action">
                    <van-icon name="chat-o" />
                    <span>回复</span>
                  </div>
                </div>
              </div>
            </div>
          </van-list>
          
          <van-empty 
            v-if="comments.length === 0 && !commentLoading" 
            description="暂无评论，快来抢沙发吧" 
            image="search"
          />
        </div>
      </div>
      
      <!-- 底部评论输入栏 -->
      <div class="comment-input-bar">
        <van-field
          v-model="commentContent"
          placeholder="写下你的评论..."
          class="comment-input"
          :border="false"
        />
        <van-button 
          class="send-btn" 
          size="small" 
          round 
          @click="submitComment" 
          :disabled="!commentContent.trim()"
        >
          发送
        </van-button>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast } from 'vant'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { Post, Comment } from '../types'
import { postAPI } from '../api/post'
import { commentAPI } from '../api/comment'
import { likeAPI } from '../api/like'
import { rankingAPI, LikeRankItem } from '../api/ranking'
import { adaptPost, adaptComment } from '../utils/adapter'
import Layout from '../components/Layout.vue'
import FollowButton from '../components/FollowButton.vue'

dayjs.extend(relativeTime)

const route = useRoute()
const router = useRouter()

const post = ref<Post | null>(null)
const comments = ref<Comment[]>([])
const commentLoading = ref(false)
const commentFinished = ref(false)
const commentPage = ref(1)
const commentContent = ref('')
const likeRanking = ref<LikeRankItem[]>([])

const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}

const goToUser = (userId: number) => {
  router.push(`/user/${userId}`)
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

const formatCommentTime = (time: string) => {
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

const handleLike = async () => {
  if (!post.value) return
  try {
    const liked = await likeAPI.togglePost(post.value.id)
    post.value.isLiked = liked
    post.value.likeCount = liked
      ? (post.value.likeCount || 0) + 1
      : Math.max(0, (post.value.likeCount || 0) - 1)
  } catch (error) {
    showToast('操作失败，请重试')
  }
}

const handleShare = () => {
  showToast('分享功能开发中')
}

const handleCollect = () => {
  showToast('收藏成功')
}

const handleFollowChange = (isFollowing: boolean) => {
  if (post.value) {
    post.value.user.isFollowing = isFollowing
  }
}

const handleCommentLike = async (comment: Comment) => {
  try {
    const liked = await likeAPI.toggleComment(comment.id)
    comment.isLiked = liked
    comment.likeCount = liked
      ? (comment.likeCount || 0) + 1
      : Math.max(0, (comment.likeCount || 0) - 1)
  } catch (error) {
    showToast('操作失败，请重试')
  }
}

const loadComments = async () => {
  if (commentLoading.value) return

  commentLoading.value = true

  try {
    const res: any = await commentAPI.getCommentsByPostId(Number(route.params.id), commentPage.value, 10)
    const newComments = (res.records || []).map(adaptComment)
    comments.value = [...comments.value, ...newComments]
    commentFinished.value = newComments.length < 10
    commentPage.value++
  } catch (error) {
    console.error('获取评论失败:', error)
  } finally {
    commentLoading.value = false
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) return

  try {
    const res: any = await commentAPI.createComment({
      postId: Number(route.params.id),
      content: commentContent.value.trim()
    })
    const newComment = adaptComment(res)
    comments.value.unshift(newComment)
    commentContent.value = ''
    if (post.value) {
      post.value.commentCount = (post.value.commentCount || 0) + 1
    }
    showToast('评论成功')
  } catch (error) {
    showToast('评论失败，请重试')
  }
}

const loadPostDetail = async () => {
  try {
    const res: any = await postAPI.getPostById(Number(route.params.id))
    post.value = adaptPost(res)
  } catch (error) {
    showToast('获取帖子详情失败')
  }
}

const loadLikeRanking = async () => {
  try {
    likeRanking.value = await rankingAPI.getLikeRanking('weekly', 5)
  } catch (error) {
    console.error('获取点赞排行榜失败:', error)
  }
}

onMounted(() => {
  loadPostDetail()
  loadComments()
  loadLikeRanking()
})
</script>

<style scoped lang="scss">
.post-detail-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding-bottom: 70px;
  
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
  
  .post-cover {
    position: relative;
    width: 100%;
    height: 200px;
    overflow: hidden;
    
    .cover-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .cover-overlay {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 80px;
      background: linear-gradient(transparent, rgba(0, 0, 0, 0.3));
    }
  }
  
  .post-content-wrapper {
    padding: 0 16px;
    
    .post-content {
      background: #fff;
      border-radius: 16px;
      padding: 20px;
      margin-top: -20px;
      position: relative;
      z-index: 10;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
      
      .post-title {
        font-size: 20px;
        font-weight: 700;
        color: #333;
        margin-bottom: 16px;
        line-height: 1.4;
      }
      
      .author-card {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 16px;
        background: linear-gradient(135deg, #f0faf9, #e6f9f7);
        border-radius: 12px;
        margin-bottom: 20px;
        
        .author-avatar {
          width: 48px;
          height: 48px;
          border: 2px solid #fff;
          box-shadow: 0 2px 8px rgba(0, 201, 183, 0.2);
        }
        
        .author-info {
          flex: 1;
          min-width: 0;
          
          .author-name {
            font-size: 15px;
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
          }
          
          .author-meta {
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
      }
      
      .post-body {
        font-size: 15px;
        color: #444;
        line-height: 1.8;
        margin-bottom: 20px;
        white-space: pre-line;
      }
      
      .post-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        margin-bottom: 20px;
        
        .tag-item {
          display: inline-flex;
          align-items: center;
          padding: 4px 12px;
          background: linear-gradient(135deg, #f0faf9, #e6f9f7);
          color: #00C9B7;
          font-size: 12px;
          border-radius: 12px;
        }
      }
      
      .post-actions {
        display: flex;
        justify-content: space-around;
        padding-top: 16px;
        border-top: 1px solid #f5f5f5;
        
        .action-item {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 14px;
          color: #999;
          cursor: pointer;
          transition: all 0.3s ease;
          
          &:active {
            opacity: 0.6;
          }
          
          .van-icon {
            font-size: 20px;
            transition: all 0.3s ease;
          }
          
          span {
            transition: all 0.3s ease;
          }
          
          .liked,
          span.liked {
            color: #00C9B7;
            
            .van-icon {
              color: #00C9B7;
            }
          }
        }
      }
    }
    
    .like-ranking-section {
      background: #fff;
      border-radius: 16px;
      padding: 16px 20px;
      margin-top: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);

      .ranking-header {
        display: flex;
        align-items: center;
        gap: 6px;
        margin-bottom: 14px;

        .ranking-fire {
          color: #ff6b35;
          font-size: 16px;
        }

        .ranking-title {
          font-size: 15px;
          font-weight: 600;
          color: #333;
        }

        .ranking-subtitle {
          font-size: 12px;
          color: #999;
          margin-left: auto;
        }
      }

      .ranking-avatars {
        display: flex;
        justify-content: space-around;
        align-items: flex-start;

        .ranking-user {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 6px;
          cursor: pointer;
          transition: transform 0.3s ease;

          &:active {
            transform: scale(0.92);
          }

          .avatar-wrapper {
            position: relative;

            .ranking-avatar {
              width: 44px;
              height: 44px;
              border: 2px solid #f0f0f0;
              transition: all 0.3s ease;
            }

            .rank-badge {
              position: absolute;
              top: -4px;
              right: -4px;
              width: 18px;
              height: 18px;
              border-radius: 50%;
              font-size: 10px;
              font-weight: 700;
              color: #fff;
              display: flex;
              align-items: center;
              justify-content: center;
              border: 1.5px solid #fff;
            }

            &.rank-1 {
              .ranking-avatar {
                border-color: #FFD700;
                box-shadow: 0 2px 8px rgba(255, 215, 0, 0.4);
              }
              .rank-badge {
                background: linear-gradient(135deg, #FFD700, #FFA500);
              }
            }

            &.rank-2 {
              .ranking-avatar {
                border-color: #E8E8E8;
                box-shadow: 0 2px 8px rgba(192, 192, 192, 0.4);
              }
              .rank-badge {
                background: linear-gradient(135deg, #E8E8E8, #C0C0C0);
              }
            }

            &.rank-3 {
              .ranking-avatar {
                border-color: #FFB347;
                box-shadow: 0 2px 8px rgba(255, 180, 71, 0.4);
              }
              .rank-badge {
                background: linear-gradient(135deg, #FFB347, #D4875E);
              }
            }

            &.rank-4, &.rank-5 {
              .ranking-avatar {
                border-color: #e5e5e5;
              }
              .rank-badge {
                background: #ccc;
              }
            }
          }

          .ranking-username {
            font-size: 11px;
            color: #666;
            max-width: 52px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            text-align: center;
          }
        }
      }
    }

    .comments-section {
      background: #fff;
      border-radius: 16px;
      padding: 20px;
      margin-top: 12px;
      margin-bottom: 20px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
      
      .section-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 16px;
        
        .section-title {
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }
        
        .comment-count {
          font-size: 13px;
          color: #999;
        }
      }
      
      .comment-list {
        .comment-item {
          display: flex;
          gap: 12px;
          padding: 16px 0;
          border-bottom: 1px solid #f5f5f5;
          
          &:last-child {
            border-bottom: none;
          }
          
          .comment-avatar {
            width: 40px;
            height: 40px;
            flex-shrink: 0;
          }
          
          .comment-body {
            flex: 1;
            min-width: 0;
            
            .comment-header {
              display: flex;
              align-items: center;
              gap: 8px;
              margin-bottom: 8px;
              
              .comment-user {
                font-size: 14px;
                font-weight: 600;
                color: #333;
              }
              
              .comment-time {
                font-size: 12px;
                color: #999;
              }
            }
            
            .comment-content {
              font-size: 14px;
              color: #444;
              line-height: 1.6;
              margin-bottom: 10px;
            }
            
            .comment-footer {
              display: flex;
              gap: 20px;
              
              .comment-action {
                display: flex;
                align-items: center;
                gap: 4px;
                font-size: 12px;
                color: #999;
                cursor: pointer;
                
                &:active {
                  opacity: 0.6;
                }
                
                .van-icon {
                  font-size: 14px;
                }
                
                .liked {
                  color: #00C9B7;
                }
              }
            }
          }
        }
      }
    }
  }
  
  .comment-input-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 10px 16px;
    display: flex;
    gap: 10px;
    align-items: center;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
    padding-bottom: calc(10px + env(safe-area-inset-bottom));
    z-index: 999;
    
    .comment-input {
      flex: 1;
      background: #f7f8fa;
      border-radius: 20px;
      padding: 8px 16px;
      font-size: 14px;
      
      :deep(.van-field__control) {
        font-size: 14px;
      }
    }
    
    .send-btn {
      background: linear-gradient(135deg, #00C9B7, #00A89C);
      border: none;
      color: #fff;
      font-size: 14px;
      padding: 0 20px;
      height: 36px;
      flex-shrink: 0;
      
      &:disabled {
        opacity: 0.5;
      }
      
      &:active:not(:disabled) {
        opacity: 0.8;
      }
    }
  }
}
</style>