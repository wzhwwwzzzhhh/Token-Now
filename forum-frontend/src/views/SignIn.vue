<template>
  <Layout title="签到" show-back>
    <div class="sign-page">
      <SignIn />
    </div>
  </Layout>
</template>

<script setup lang="ts">
import Layout from '../components/Layout.vue'
import SignIn from '../components/SignIn.vue'
</script>

<style scoped lang="scss">
.sign-page {
  padding: 12px;
  min-height: 100vh;
  background-color: #f7f8fa;
}
</style>
