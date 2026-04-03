<template>
  <el-header class="navbar">
    <div class="navbar-left">
      <span class="system-title">营养分析管理系统</span>
      <el-menu mode="horizontal" :default-active="activeIndex" class="navbar-menu" @select="handleSelect"
        :ellipsis="false">
        <el-menu-item index="home">首页</el-menu-item>
        <el-menu-item index="/dishes">菜品管理</el-menu-item>
        <el-menu-item index="/review">菜品评价</el-menu-item>
        <el-menu-item index="/analysis">菜品分析</el-menu-item>
        <el-menu-item index="/user-management">用户管理</el-menu-item>
        <el-menu-item index="/profile">个人中心</el-menu-item>
      </el-menu>
    </div>
    <!-- <div class="navbar-right"> 
        <span class="user-name">{{ username }}</span>
      </div> -->
  </el-header>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const routeMap = {
  '/home': 'home',
  '/dishes': '/dishes',
  '/review': '/review',
  '/profile': '/profile',
  '/user-management': '/user-management',
  '/analysis': '/analysis'
}

const activeIndex = ref(routeMap[route.path] || 'home')

watch(
  () => route.path,
  (newPath) => {
    activeIndex.value = routeMap[newPath] || 'home'
  }
)

const handleSelect = (key) => {
  activeIndex.value = key
  if (key === 'home') router.push('/home')
  else if (key === '/dishes') router.push('/dishes')
  else if (key === '/review') router.push('/review')
  else if (key === '/profile') router.push('/profile')
  else if (key === '/user-management') router.push('/user-management')
  else if (key === '/analysis') router.push('/analysis')
}
</script>

<style scoped>
.navbar {
  background: #e6f0fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 0 40px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1200;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 40px;
}

.system-title {
  font-size: 24px;
  font-weight: bold;
  color: #4a90e2;
  white-space: nowrap;
}

.navbar-menu {
  background: transparent;
  border-bottom: none;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
  display: flex;
  flex-wrap: nowrap;
}

:deep(.el-menu-item) {
  color: #4a90e2;
  font-size: 16px;
  background: transparent;
  transition: background 0.2s, color 0.2s;
  white-space: nowrap;
  padding: 0 20px;
}

:deep(.el-menu-item.is-active) {
  color: #fff !important;
  background: #4a90e2 !important;
  border-radius: 6px;
}

.navbar-right {
  font-size: 16px;
  color: #4a90e2;
  margin-left: 20px;
}

.user-name {
  background: #fff;
  padding: 6px 18px;
  border-radius: 16px;
  box-shadow: 0 1px 4px rgba(74, 144, 226, 0.08);
  white-space: nowrap;
}
</style>