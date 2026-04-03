<template>
    <div class="search-bar-container">
      <div class="search-bar">
        <el-input
          v-model="keyword"
          placeholder="搜索菜品/公告..."
          class="search-input"
          clearable
          @keyup.enter="onSearch"
        >
          <template #append>
            <el-button type="primary" @click="onSearch" :loading="isLoading">搜索</el-button>
          </template>
        </el-input>
      </div>
      <div v-if="searchResults.length > 0" class="search-results">
        <h3>搜索结果：</h3>
        <ul>
          <li v-for="dish in searchResults" :key="dish.id">{{ dish.name }} - {{ dish.category }}</li>
        </ul>
      </div>
      <el-empty v-else-if="!isLoading && searchPerformed && searchResults.length === 0" description="未找到相关菜品" />
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { ElMessage } from 'element-plus'
  import { searchDishes } from '../api/index'
  
  const keyword = ref('')
  const searchResults = ref([])
  const isLoading = ref(false)
  const searchPerformed = ref(false)

  const onSearch = async () => {
    if (!keyword.value.trim()) {
      ElMessage.warning('请输入搜索关键词');
      return;
    }
    isLoading.value = true;
    searchPerformed.value = true;
    try {
      const res = await searchDishes(keyword.value);
      if (res.code === 200 && res.data) {
        searchResults.value = res.data;
        ElMessage.success(`找到 ${res.data.length} 个菜品`);
      } else {
        searchResults.value = [];
        ElMessage.error(res.message || '搜索失败！');
      }
    } catch (error) {
      console.error('搜索菜品失败:', error);
      searchResults.value = [];
      ElMessage.error('搜索菜品失败！');
    } finally {
      isLoading.value = false;
    }
  }
  </script>
  
  <style scoped>
  .search-bar-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 20px;
  }
  .search-bar {
    width: 100%;
    display: flex;
    justify-content: center;
    margin: 0 0 8px 0;
  }
  .search-input {
    width: 520px;
    background: #fff;
    border-radius: 32px;
    box-shadow: 0 2px 8px rgba(74,144,226,0.10);
    border: 1.5px solid #b3d1f7;
    overflow: hidden;
  }
  :deep(.el-input__wrapper) {
    border-radius: 32px 0 0 32px !important;
    background: #f6faff;
    border: none;
    box-shadow: none;
  }
  :deep(.el-input__inner) {
    background: #f6faff;
    border-radius: 32px 0 0 32px;
    color: #4a90e2;
    font-size: 16px;
    height: 48px;
  }
  :deep(.el-button--primary) {
    background: #4a90e2;
    border-color: #4a90e2;
    border-radius: 0 32px 32px 0 !important;
    font-size: 16px;
    height: 48px;
    padding: 0 28px;
  }
  .search-results {
    width: 520px;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 2px 8px rgba(74,144,226,0.08);
    padding: 24px;
  }
  .search-results h3 {
    color: #4a90e2;
    margin-bottom: 15px;
  }
  .search-results ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  .search-results li {
    padding: 8px 0;
    border-bottom: 1px solid #eee;
    color: #2d3a4b;
  }
  .search-results li:last-child {
    border-bottom: none;
  }
  </style>
  