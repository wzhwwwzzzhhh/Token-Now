<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup lang="ts">
// App组件
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  background-color: #f7f8fa;
  color: #333;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  line-height: 1.5;
}

#app {
  min-height: 100vh;
}

/* Vant 组件主题色覆盖 - 青绿色 */
:root {
  --van-primary-color: #00C9B7;
  --van-success-color: #00C9B7;
  --van-warning-color: #ff9500;
  --van-danger-color: #ee0a24;
}

/* 按钮样式统一为青绿色 */
.van-button--primary {
  background: linear-gradient(135deg, #00C9B7, #00A89C);
  border: none;
  
  &.van-button--disabled {
    opacity: 0.6;
  }
}

/* 导航栏返回按钮 */
.van-nav-bar .van-icon-arrow-left,
.van-nav-bar .van-icon-arrow-left::before {
  color: #00C9B7 !important;
}

/* Tab激活状态 */
.van-tab--active {
  color: #00C9B7 !important;
}

.van-tabs__line {
  background-color: #00C9B7 !important;
}

/* 复选框和单选框 */
.van-checkbox--checked .van-icon,
.van-radio--checked .van-icon {
  background-color: #00C9B7;
  border-color: #00C9B7;
}

/* 开关 */
.van-switch--on {
  background-color: #00C9B7;
}

/* 标签 */
.van-tag--primary {
  background-color: #00C9B7;
}

/* 进度条 */
.van-progress__portion {
  background-color: #00C9B7;
}

/* 加载中 */
.van-loading__spinner {
  color: #00C9B7;
}

/* 徽标 */
.van-badge--select {
  background-color: #00C9B7;
}

/* 步骤条 */
.van-step--process .van-circle-container {
  color: #00C9B7;
}

/* 面包屑 */
.van-breadcrumb .van-breadcrumb__separator {
  color: #00C9B7;
}

/* 分页选中 */
.van-pagination__item--active {
  color: #00C9B7;
}

/* 搜索框取消按钮 */
.van-search__action {
  color: #00C9B7;
}

/* 下拉菜单标题 */
.van-dropdown-menu__title--active {
  color: #00C9B7;
}

/* 评分星星 */
.van-rate__icon--full {
  color: #00C9B7;
}

/* 空状态按钮 */
.van-empty__bottom .van-button--primary {
  background: linear-gradient(135deg, #00C9B7, #00A89C);
}

/* Toast提示 */
.van-toast--text {
  background-color: rgba(0, 0, 0, 0.75);
}
</style>