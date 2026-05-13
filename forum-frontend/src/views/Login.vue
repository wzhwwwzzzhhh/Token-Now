<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
      <div class="circle circle-4"></div>
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
    </div>

    <!-- Logo 区域 -->
    <div class="logo-section">
      <div class="logo-icon">
        <van-icon name="chat-o" />
      </div>
      <h1 class="app-title">社区论坛</h1>
      <p class="app-desc">连接你我，分享生活</p>
    </div>

    <!-- 登录/注册卡片 -->
    <div class="form-card">
      <div class="tabs">
        <div
          :class="['tab-item', { active: activeTab === 0 }]"
          @click="activeTab = 0"
        >
          登录
          <div class="tab-indicator" v-if="activeTab === 0"></div>
        </div>
        <div
          :class="['tab-item', { active: activeTab === 1 }]"
          @click="activeTab = 1"
        >
          注册
          <div class="tab-indicator" v-if="activeTab === 1"></div>
        </div>
      </div>

      <!-- 登录表单 -->
      <transition name="slide-fade" mode="out-in">
        <div v-if="activeTab === 0" key="login" class="form-content">
          <van-form @submit="handleLogin">
            <van-field
              v-model="loginForm.phone"
              name="phone"
              label=""
              placeholder="请输入手机号"
              left-icon="phone-o"
              :rules="[{ required: true, message: '请输入手机号' }, { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' }]"
              class="custom-field"
            />
            <van-field
              v-model="loginForm.password"
              name="password"
              label=""
              type="password"
              placeholder="请输入密码"
              left-icon="lock"
              :rules="[{ required: true, message: '请输入密码' }, { min: 6, max: 20, message: '密码长度为6-20位' }]"
              class="custom-field"
            />
            <div class="form-options">
              <label class="remember-me">
                <input type="checkbox" v-model="rememberMe" />
                <span class="checkmark"></span>
                <span class="label-text">记住我</span>
              </label>
              <span class="forgot-pwd">忘记密码？</span>
            </div>
            <div class="form-actions">
              <van-button
                type="primary"
                native-type="submit"
                :loading="userStore.loading"
                block
                round
                class="submit-btn"
              >
                登 录
              </van-button>
            </div>
          </van-form>
        </div>

        <!-- 注册表单 -->
        <div v-else key="register" class="form-content">
          <van-form @submit="handleRegister">
            <van-field
              v-model="registerForm.phone"
              name="phone"
              label=""
              placeholder="请输入手机号"
              left-icon="phone-o"
              :rules="[{ required: true, message: '请输入手机号' }, { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' }]"
              class="custom-field"
            />
            <van-field
              v-model="registerForm.username"
              name="username"
              label=""
              placeholder="请输入昵称"
              left-icon="contact"
              :rules="[{ required: true, message: '请输入昵称' }, { min: 2, max: 12, message: '昵称长度为2-12个字符' }]"
              class="custom-field"
            />
            <van-field
              v-model="registerForm.password"
              name="password"
              label=""
              type="password"
              placeholder="请设置6-20位密码"
              left-icon="lock"
              :rules="[{ required: true, message: '请输入密码' }, { min: 6, max: 20, message: '密码长度为6-20位' }]"
              class="custom-field"
            />
            <div class="password-hint">
              密码需包含6-20个字符，建议使用字母+数字组合
            </div>
            <div class="form-actions">
              <van-button
                type="primary"
                native-type="submit"
                :loading="userStore.loading"
                block
                round
                class="submit-btn"
              >
                注 册
              </van-button>
            </div>
            <div class="agreement">
              注册即表示同意
              <span class="link">《用户协议》</span>
              和
              <span class="link">《隐私政策》</span>
            </div>
          </van-form>
        </div>
      </transition>
    </div>

    <!-- 第三方登录 -->
    <div class="third-login" v-if="activeTab === 0">
      <div class="divider">
        <span class="divider-text">其他登录方式</span>
      </div>
      <div class="third-icons">
        <div class="third-item">
          <div class="third-icon wechat">
            <van-icon name="wechat" />
          </div>
          <span>微信</span>
        </div>
        <div class="third-item">
          <div class="third-icon qq">
            <van-icon name="qq" />
          </div>
          <span>QQ</span>
        </div>
        <div class="third-item">
          <div class="third-icon weibo">
            <van-icon name="weibo" />
          </div>
          <span>微博</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '../stores/userStore'
import { LoginForm, RegisterForm } from '../types'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref(0)
const rememberMe = ref(false)
const loginForm = ref<LoginForm>({
  phone: '',
  password: ''
})
const registerForm = ref<RegisterForm>({
  phone: '',
  username: '',
  password: ''
})

const handleLogin = async (values: LoginForm) => {
  try {
    await userStore.login(values)
    showToast('登录成功')
    router.push('/')
  } catch (error) {
    showToast('手机号或密码错误')
  }
}

const handleRegister = async (values: RegisterForm) => {
  try {
    await userStore.register(values)
    showToast('注册成功')
    router.push('/')
  } catch (error) {
    showToast('注册失败，请稍后重试')
  }
}
</script>

<style scoped lang="scss">
// 青绿主题色
$primary: #00C9B7;
$primary-dark: #00A89C;
$primary-light: rgba(0, 201, 183, 0.1);
$primary-gradient: linear-gradient(135deg, #00C9B7, #00A89C);

.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0faf9 0%, #f7f8fa 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
  padding: 0 24px;
  padding-bottom: 40px;
}

// ======= 背景装饰 =======
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;

  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.15;
  }

  .circle-1 {
    width: 300px;
    height: 300px;
    background: $primary-gradient;
    top: -80px;
    right: -80px;
    animation: floatCircle 8s ease-in-out infinite;
  }

  .circle-2 {
    width: 200px;
    height: 200px;
    background: $primary-gradient;
    top: 120px;
    left: -60px;
    animation: floatCircle 10s ease-in-out infinite reverse;
  }

  .circle-3 {
    width: 120px;
    height: 120px;
    background: $primary-gradient;
    bottom: 180px;
    right: -30px;
    animation: floatCircle 6s ease-in-out infinite 2s;
  }

  .circle-4 {
    width: 80px;
    height: 80px;
    background: $primary-gradient;
    bottom: 60px;
    left: 10%;
    animation: floatCircle 7s ease-in-out infinite 1s;
  }

  .wave {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 60px;
    opacity: 0.06;
  }

  .wave-1 {
    background: $primary;
    border-radius: 50% 50% 0 0;
    bottom: 0;
    transform: scaleY(0.6);
    animation: waveMove 12s ease-in-out infinite;
  }

  .wave-2 {
    background: $primary-dark;
    border-radius: 50% 50% 0 0;
    bottom: -10px;
    transform: scaleY(0.4);
    animation: waveMove 10s ease-in-out infinite reverse;
  }
}

@keyframes floatCircle {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  33% {
    transform: translate(15px, -15px) scale(1.05);
  }
  66% {
    transform: translate(-10px, 10px) scale(0.95);
  }
}

@keyframes waveMove {
  0%, 100% {
    transform: scaleY(0.6) translateX(0);
  }
  25% {
    transform: scaleY(0.7) translateX(-5%);
  }
  50% {
    transform: scaleY(0.5) translateX(0);
  }
  75% {
    transform: scaleY(0.65) translateX(5%);
  }
}

// ======= Logo 区域 =======
.logo-section {
  position: relative;
  z-index: 1;
  text-align: center;
  padding-top: 60px;
  padding-bottom: 32px;

  .logo-icon {
    width: 72px;
    height: 72px;
    background: $primary-gradient;
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16px;
    box-shadow: 0 8px 24px rgba(0, 201, 183, 0.35);
    animation: logoBounce 2s ease-in-out infinite;

    :deep(.van-icon) {
      font-size: 36px;
      color: #fff;
    }
  }

  .app-title {
    font-size: 26px;
    font-weight: 700;
    color: #1a1a2e;
    margin: 0 0 8px;
    letter-spacing: 2px;
  }

  .app-desc {
    font-size: 14px;
    color: #999;
    margin: 0;
    letter-spacing: 1px;
  }
}

@keyframes logoBounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-6px);
  }
}

// ======= 表单卡片 =======
.form-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  background: #fff;
  border-radius: 20px;
  padding: 28px 24px 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  animation: cardIn 0.6s ease-out;
}

@keyframes cardIn {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.96);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

// Tab 切换
.tabs {
  display: flex;
  gap: 0;
  margin-bottom: 28px;
  background: #f5f7fa;
  border-radius: 12px;
  padding: 4px;
  position: relative;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 10px 0;
    font-size: 15px;
    font-weight: 500;
    color: #999;
    cursor: pointer;
    border-radius: 10px;
    position: relative;
    transition: all 0.3s ease;
    user-select: none;

    &.active {
      color: #fff;
      background: $primary-gradient;
      box-shadow: 0 2px 8px rgba(0, 201, 183, 0.3);
    }

    &:not(.active):active {
      background: rgba(0, 201, 183, 0.06);
    }
  }
}

// 表单切换动画
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

// 自定义输入框
.form-content {
  :deep(.custom-field) {
    padding: 12px 0;
    margin-bottom: 4px;
    border-bottom: 1px solid #f0f0f0;

    .van-field__left-icon {
      .van-icon {
        font-size: 20px;
        color: #bbb;
      }
    }

    .van-field__body {
      input {
        font-size: 15px;
        color: #333;

        &::placeholder {
          color: #ccc;
          font-size: 14px;
        }
      }
    }

    // 聚焦状态
    &.van-field--focus {
      border-bottom-color: $primary;

      .van-field__left-icon .van-icon {
        color: $primary;
      }
    }

    // 错误状态
    &.van-field--error {
      border-bottom-color: #ee0a24;

      .van-field__left-icon .van-icon {
        color: #ee0a24;
      }
    }
  }
}

// 表单选项
.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 12px;
  margin-bottom: 20px;

  .remember-me {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    user-select: none;

    input[type="checkbox"] {
      display: none;
    }

    .checkmark {
      width: 18px;
      height: 18px;
      border: 2px solid #ddd;
      border-radius: 4px;
      position: relative;
      transition: all 0.2s ease;
    }

    input:checked + .checkmark {
      background: $primary-gradient;
      border-color: $primary;

      &::after {
        content: '';
        position: absolute;
        top: 2px;
        left: 5px;
        width: 5px;
        height: 9px;
        border: solid #fff;
        border-width: 0 2px 2px 0;
        transform: rotate(45deg);
      }
    }

    .label-text {
      font-size: 13px;
      color: #999;
    }
  }

  .forgot-pwd {
    font-size: 13px;
    color: $primary;
    cursor: pointer;
    transition: opacity 0.2s;

    &:active {
      opacity: 0.7;
    }
  }
}

// 密码提示
.password-hint {
  font-size: 12px;
  color: #bbb;
  margin-top: 8px;
  margin-bottom: 4px;
  padding-left: 2px;
}

// 用户协议
.agreement {
  text-align: center;
  font-size: 12px;
  color: #bbb;
  margin-top: 16px;
  line-height: 1.6;

  .link {
    color: $primary;
    cursor: pointer;
    transition: opacity 0.2s;

    &:active {
      opacity: 0.7;
    }
  }
}

// 提交按钮
.form-actions {
  margin-top: 24px;

  .submit-btn {
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    letter-spacing: 4px;
    background: $primary-gradient;
    border: none;
    box-shadow: 0 4px 16px rgba(0, 201, 183, 0.35);
    transition: all 0.3s ease;

    &:active {
      transform: scale(0.98);
      box-shadow: 0 2px 8px rgba(0, 201, 183, 0.25);
    }

    // 加载状态样式
    &:deep(.van-loading) {
      .van-loading__spinner {
        color: #fff;
      }
    }
  }
}

// ======= 第三方登录 =======
.third-login {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  margin-top: 32px;
  animation: fadeIn 0.6s ease-out 0.3s both;

  .divider {
    display: flex;
    align-items: center;
    margin-bottom: 20px;

    &::before,
    &::after {
      content: '';
      flex: 1;
      height: 1px;
      background: #e8e8e8;
    }

    .divider-text {
      padding: 0 16px;
      font-size: 12px;
      color: #ccc;
      white-space: nowrap;
    }
  }

  .third-icons {
    display: flex;
    justify-content: center;
    gap: 32px;

    .third-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:active {
        transform: scale(0.9);
      }

      .third-icon {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;

        :deep(.van-icon) {
          font-size: 24px;
          color: #fff;
        }

        &.wechat {
          background: linear-gradient(135deg, #07C160, #06AD56);
          box-shadow: 0 4px 12px rgba(7, 193, 96, 0.3);
        }

        &.qq {
          background: linear-gradient(135deg, #12B7F5, #0D9FE0);
          box-shadow: 0 4px 12px rgba(18, 183, 245, 0.3);
        }

        &.weibo {
          background: linear-gradient(135deg, #FF8200, #E67300);
          box-shadow: 0 4px 12px rgba(255, 130, 0, 0.3);
        }
      }

      span {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// ======= 响应式 =======
@media (max-width: 375px) {
  .login-page {
    padding: 0 16px;
  }

  .logo-section {
    padding-top: 40px;

    .logo-icon {
      width: 60px;
      height: 60px;

      :deep(.van-icon) {
        font-size: 30px;
      }
    }

    .app-title {
      font-size: 22px;
    }
  }

  .form-card {
    padding: 20px 16px;
  }

  .third-login .third-icons {
    gap: 24px;
  }
}

@media (min-width: 768px) {
  .login-page {
    justify-content: center;
    padding: 0;
  }

  .logo-section {
    padding-top: 0;
    margin-bottom: 16px;
  }

  .bg-decoration {
    .circle-1 {
      width: 500px;
      height: 500px;
    }

    .circle-2 {
      width: 350px;
      height: 350px;
    }
  }
}
</style>
