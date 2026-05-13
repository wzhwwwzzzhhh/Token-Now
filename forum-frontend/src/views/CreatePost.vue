<template>
  <Layout title="" :show-notification="false" hide-nav>
    <div class="create-post-container">
      <!-- 顶部导航 -->
      <div class="top-nav">
        <van-icon name="arrow-left" size="22" class="nav-btn" @click="goBack" />
        <div class="nav-title">发布帖子</div>
        <van-button 
          class="publish-btn" 
          size="small" 
          round 
          @click="handleSubmit" 
          :loading="loading"
          :disabled="!form.title.trim() || !form.content.trim()"
        >
          发布
        </van-button>
      </div>
      
      <!-- 表单内容 -->
      <div class="form-content">
        <!-- 标题输入 -->
        <div class="form-section">
          <van-field
            v-model="form.title"
            placeholder="添加标题"
            class="title-input"
            :border="false"
            maxlength="50"
            show-word-limit
          />
        </div>
        
        <!-- 内容输入 -->
        <div class="form-section">
          <van-field
            v-model="form.content"
            type="textarea"
            placeholder="分享你的想法..."
            class="content-input"
            :border="false"
            :rows="6"
            maxlength="2000"
            show-word-limit
          />
        </div>
        
        <!-- 图片上传 -->
        <div class="form-section">
          <div class="section-label">添加图片</div>
          <div class="image-upload-grid">
            <div 
              v-for="(img, index) in images" 
              :key="index" 
              class="image-item"
            >
              <van-image
                :src="img"
                fit="cover"
                class="uploaded-image"
              />
              <div class="image-delete" @click="removeImage(index)">
                <van-icon name="cross" size="12" />
              </div>
            </div>
            <div 
              v-if="images.length < 9" 
              class="upload-btn"
              @click="uploadImage"
            >
              <van-icon name="photograph" size="28" color="#ccc" />
              <span class="upload-text">添加图片</span>
            </div>
          </div>
        </div>
        
        <!-- 分类选择 -->
        <div class="form-section">
          <div class="section-label">选择分类</div>
          <div class="category-tags">
            <div 
              v-for="category in categories" 
              :key="category"
              :class="['category-tag', { active: selectedCategory === category }]"
              @click="selectedCategory = category"
            >
              {{ category }}
            </div>
          </div>
        </div>
        
        <!-- 标签输入 -->
        <div class="form-section">
          <div class="section-label">添加标签</div>
          <div class="tag-input-container">
            <div class="tag-list">
              <span 
                v-for="(tag, index) in tags" 
                :key="index" 
                class="tag-item"
              >
                #{{ tag }}
                <van-icon name="cross" size="12" @click="removeTag(index)" />
              </span>
            </div>
            <van-field
              v-model="tagInput"
              placeholder="输入标签后按回车"
              class="tag-input"
              :border="false"
              @keyup.enter="addTag"
            />
          </div>
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showLoadingToast, closeToast } from 'vant'
import { PostForm } from '../types'
import { postAPI } from '../api/post'
import { uploadAPI } from '../api/upload'
import Layout from '../components/Layout.vue'

const router = useRouter()
const form = ref<PostForm>({
  title: '',
  content: ''
})
const loading = ref(false)
const images = ref<string[]>([])
const selectedCategory = ref('')
const tags = ref<string[]>([])
const tagInput = ref('')

const categories = ['技术', '生活', '问答', '分享', '求助', '其他']

const goBack = () => {
  if (form.value.title.trim() || form.value.content.trim() || images.value.length > 0) {
    showToast('确定要放弃编辑吗？')
  }
  router.back()
}

const fileInputRef = ref<HTMLInputElement | null>(null)

const uploadImage = () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  input.onchange = async (e: Event) => {
    const target = e.target as HTMLInputElement
    const file = target.files?.[0]
    if (!file) return

    showLoadingToast({ message: '上传中...', forbidClick: true })
    try {
      const url = await uploadAPI.uploadFile(file)
      images.value.push(url)
      closeToast()
      showToast('图片已添加')
    } catch (error) {
      closeToast()
      showToast('图片上传失败')
    }
  }
  input.click()
}

const removeImage = (index: number) => {
  images.value.splice(index, 1)
}

const addTag = () => {
  const tag = tagInput.value.trim()
  if (tag && !tags.value.includes(tag) && tags.value.length < 5) {
    tags.value.push(tag)
    tagInput.value = ''
  }
}

const removeTag = (index: number) => {
  tags.value.splice(index, 1)
}

const handleSubmit = async () => {
  if (!form.value.title.trim()) {
    showToast('请输入标题')
    return
  }
  if (!form.value.content.trim()) {
    showToast('请输入内容')
    return
  }

  try {
    loading.value = true

    const postData: PostForm = {
      title: form.value.title.trim(),
      content: form.value.content.trim()
    }

    await postAPI.createPost(postData)

    showToast('发布成功')
    router.push('/')
  } catch (error) {
    console.error('发布帖子失败:', error)
    showToast('发布失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.create-post-container {
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
    
    .publish-btn {
      background: rgba(255, 255, 255, 0.25);
      border: none;
      color: #fff;
      font-size: 14px;
      padding: 0 20px;
      height: 32px;
      
      &:disabled {
        opacity: 0.5;
      }
      
      &:active:not(:disabled) {
        opacity: 0.8;
      }
    }
  }
  
  .form-content {
    padding: 16px;
    padding-bottom: 80px;
    
    .form-section {
      background: #fff;
      border-radius: 16px;
      padding: 16px;
      margin-bottom: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
      
      .section-label {
        font-size: 14px;
        font-weight: 600;
        color: #333;
        margin-bottom: 12px;
      }
      
      .title-input {
        font-size: 18px;
        font-weight: 600;
        
        :deep(.van-field__control) {
          font-size: 18px;
          font-weight: 600;
        }
        
        :deep(.van-field__word-limit) {
          color: #999;
          font-size: 12px;
        }
      }
      
      .content-input {
        font-size: 15px;
        line-height: 1.6;
        
        :deep(.van-field__control) {
          font-size: 15px;
          line-height: 1.6;
          min-height: 120px;
        }
        
        :deep(.van-field__word-limit) {
          color: #999;
          font-size: 12px;
        }
      }
      
      .image-upload-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 10px;
        
        .image-item {
          position: relative;
          aspect-ratio: 1;
          border-radius: 12px;
          overflow: hidden;
          
          .uploaded-image {
            width: 100%;
            height: 100%;
          }
          
          .image-delete {
            position: absolute;
            top: 4px;
            right: 4px;
            width: 20px;
            height: 20px;
            background: rgba(0, 0, 0, 0.5);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            
            :deep(.van-icon) {
              color: #fff;
            }
          }
        }
        
        .upload-btn {
          aspect-ratio: 1;
          border: 2px dashed #e5e5e5;
          border-radius: 12px;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          gap: 8px;
          cursor: pointer;
          transition: all 0.3s ease;
          
          &:active {
            border-color: #00C9B7;
            background: #f0faf9;
          }
          
          .upload-text {
            font-size: 12px;
            color: #999;
          }
        }
      }
      
      .category-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        
        .category-tag {
          padding: 8px 16px;
          background: #f7f8fa;
          border-radius: 20px;
          font-size: 13px;
          color: #666;
          cursor: pointer;
          transition: all 0.3s ease;
          
          &.active {
            background: linear-gradient(135deg, #00C9B7, #00A89C);
            color: #fff;
            font-weight: 500;
          }
          
          &:active {
            transform: scale(0.95);
          }
        }
      }
      
      .tag-input-container {
        .tag-list {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
          margin-bottom: 10px;
          
          .tag-item {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 4px 12px;
            background: linear-gradient(135deg, #f0faf9, #e6f9f7);
            color: #00C9B7;
            font-size: 12px;
            border-radius: 12px;
            
            :deep(.van-icon) {
              cursor: pointer;
              
              &:active {
                opacity: 0.6;
              }
            }
          }
        }
        
        .tag-input {
          font-size: 13px;
          
          :deep(.van-field__control) {
            font-size: 13px;
          }
        }
      }
    }
  }
}
</style>