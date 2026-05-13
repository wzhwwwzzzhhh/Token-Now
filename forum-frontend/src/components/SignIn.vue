<template>
  <div class="sign-in-card">
    <div class="card-header">
      <div class="header-left">
        <span class="title">签到</span>
        <span class="subtitle">坚持签到，记录每一天</span>
      </div>
      <div class="header-right">
        <span class="continuous-days" v-if="stats.continuousDays > 0">
          已连续 <b>{{ stats.continuousDays }}</b> 天
        </span>
      </div>
    </div>

    <div class="calendar">
      <div class="calendar-header">
        <van-icon name="arrow-left" class="arrow" @click="prevMonth" />
        <span class="month-label">{{ currentYear }}年{{ currentMonth }}月</span>
        <van-icon name="arrow" class="arrow" @click="nextMonth" />
      </div>

      <div class="weekdays">
        <span v-for="day in weekdays" :key="day">{{ day }}</span>
      </div>

      <div class="days-grid">
        <div
          v-for="(day, index) in dayList"
          :key="index"
          :class="['day-cell', {
            'empty': !day,
            'signed': day && signedDays.includes(day),
            'today': day === today && currentYear === todayYear && currentMonth === todayMonth,
            'future': day && (currentYear > todayYear || (currentYear === todayYear && currentMonth > todayMonth) || (day > today && currentYear === todayYear && currentMonth === todayMonth))
          }]"
        >
          <span class="day-number">{{ day || '' }}</span>
          <span v-if="day && signedDays.includes(day)" class="sign-dot">✓</span>
        </div>
      </div>
    </div>

    <van-button
      :type="todaySigned ? 'default' : 'primary'"
      :disabled="todaySigned"
      :loading="signingLoading"
      round
      block
      class="sign-btn"
      @click="handleSignIn"
    >
      {{ todaySigned ? '今日已签到' : '立即签到' }}
    </van-button>

    <div class="stats-row">
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalDays }}</span>
        <span class="stat-label">本月签到</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.continuousDays }}</span>
        <span class="stat-label">连续签到</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ remainingDays }}</span>
        <span class="stat-label">剩余天数</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { showToast } from 'vant'
import { signAPI } from '../api/sign'

const weekdays = ['日', '一', '二', '三', '四', '五', '六']

const today = new Date().getDate()
const todayYear = new Date().getFullYear()
const todayMonth = new Date().getMonth() + 1

const currentYear = ref(todayYear)
const currentMonth = ref(todayMonth)
const signedDays = ref<number[]>([])
const signingLoading = ref(false)
const todaySigned = ref(false)

const stats = ref({
  totalDays: 0,
  continuousDays: 0
})

const remainingDays = computed(() => {
  const daysInMonth = new Date(currentYear.value, currentMonth.value, 0).getDate()
  return daysInMonth - stats.value.totalDays
})

const dayList = computed(() => {
  const firstDay = new Date(currentYear.value, currentMonth.value - 1, 1).getDay()
  const daysInMonth = new Date(currentYear.value, currentMonth.value, 0).getDate()
  const list: (number | null)[] = []

  for (let i = 0; i < firstDay; i++) {
    list.push(null)
  }
  for (let i = 1; i <= daysInMonth; i++) {
    list.push(i)
  }

  return list
})

const prevMonth = () => {
  if (currentMonth.value === 1) {
    currentMonth.value = 12
    currentYear.value--
  } else {
    currentMonth.value--
  }
  fetchSignList()
}

const nextMonth = () => {
  if (currentMonth.value === 12) {
    currentMonth.value = 1
    currentYear.value++
  } else {
    currentMonth.value++
  }
  fetchSignList()
}

const fetchSignList = async () => {
  try {
    const days = await signAPI.getSignList(currentYear.value, currentMonth.value)
    signedDays.value = days
  } catch {
    signedDays.value = []
  }
}

const fetchStats = async () => {
  try {
    const data = await signAPI.getSignStats()
    stats.value = data
    todaySigned.value = data.continuousDays > 0 && signedDays.value.includes(today)
  } catch {
    // ignore
  }
}

const handleSignIn = async () => {
  signingLoading.value = true
  try {
    await signAPI.signIn()
    showToast('签到成功 🎉')
    await Promise.all([fetchSignList(), fetchStats()])
  } catch (error) {
    showToast('签到失败，请重试')
  } finally {
    signingLoading.value = false
  }
}

onMounted(() => {
  fetchSignList()
  fetchStats()
})
</script>

<style scoped lang="scss">
.sign-in-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20px;

    .header-left {
      .title {
        font-size: 18px;
        font-weight: 600;
        color: #333;
      }

      .subtitle {
        display: block;
        font-size: 12px;
        color: #999;
        margin-top: 4px;
      }
    }

    .header-right {
      .continuous-days {
        font-size: 13px;
        color: #00C9B7;
        background: #f0faf9;
        padding: 4px 12px;
        border-radius: 12px;

        b {
          font-size: 16px;
        }
      }
    }
  }

  .calendar {
    margin-bottom: 16px;

    .calendar-header {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 20px;
      margin-bottom: 16px;

      .month-label {
        font-size: 15px;
        font-weight: 600;
        color: #333;
      }

      .arrow {
        color: #999;
        font-size: 18px;
        cursor: pointer;
        padding: 4px;

        &:active {
          opacity: 0.6;
        }
      }
    }

    .weekdays {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      text-align: center;
      margin-bottom: 8px;

      span {
        font-size: 12px;
        color: #999;
        padding: 4px 0;
      }
    }

    .days-grid {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 2px;

      .day-cell {
        position: relative;
        aspect-ratio: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        font-size: 13px;
        color: #333;
        transition: all 0.2s ease;

        &.empty {
          visibility: hidden;
        }

        &.today {
          font-weight: 700;
          color: #00C9B7;
        }

        &.future {
          color: #ddd;
        }

        &.signed {
          background: linear-gradient(135deg, #00C9B7, #00A89C);
          color: #fff;
          font-weight: 600;

          .day-number {
            z-index: 1;
          }

          .sign-dot {
            position: absolute;
            top: -2px;
            right: -2px;
            width: 16px;
            height: 16px;
            background: #ff4757;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 10px;
            color: #fff;
            border: 2px solid #fff;
          }
        }

        &:not(.empty):not(.future):not(.signed):active {
          transform: scale(0.9);
        }
      }
    }
  }

  .sign-btn {
    height: 44px;
    font-size: 15px;
    font-weight: 600;
    letter-spacing: 2px;
    margin-bottom: 16px;

    &.van-button--primary {
      background: linear-gradient(135deg, #00C9B7, #00A89C);
      border: none;
      box-shadow: 0 4px 12px rgba(0, 201, 183, 0.3);
    }

    &.van-button--default {
      color: #999;
      border: 1px solid #e5e5e5;
    }
  }

  .stats-row {
    display: flex;
    justify-content: space-around;
    padding-top: 16px;
    border-top: 1px solid #f5f5f5;

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 4px;

      .stat-value {
        font-size: 22px;
        font-weight: 700;
        color: #00C9B7;
      }

      .stat-label {
        font-size: 12px;
        color: #999;
      }
    }
  }
}
</style>
