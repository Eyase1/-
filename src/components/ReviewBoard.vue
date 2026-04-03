<template>
  <div class="review-board">
    <div class="review-title">菜品评价</div>
    <div class="timeline-container">
      <el-timeline>
        <el-timeline-item
          v-for="(review, idx) in reviews"
          :key="idx"
          :timestamp="review.time"
          placement="top"
          color="#4a90e2"
        >
          <div class="review-item" @click="showDialog(review)">
            <span class="dish-name">{{ review.dishName }}</span>
            <span class="review-content">{{ truncate(review.reviewContent, 36) }}</span>
          </div>
        </el-timeline-item>
      </el-timeline>
    </div>
    <el-dialog v-model="dialogVisible" title="评价详情" width="420px" :close-on-click-modal="true">
      <div class="dialog-dish">{{ dialogReview.dishName }}</div>
      <div class="dialog-time">{{ truncate(dialogReview.reviewTime,10) }}</div>
      <div class="dialog-content">{{ dialogReview.reviewContent }}</div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getReviewBoardData } from '../api/index' // 引入API

const reviews = ref([]); // 初始化为空数组
const dialogVisible = ref(false)
const dialogReview = ref({})

// 获取评论板数据
async function fetchReviewBoardData() {
  try {
    const res = await getReviewBoardData();
    if (res.status === 200 && res.data) {
      reviews.value = res.data.data;
    } else {
      ElMessage.error(res.message || '获取评论板数据失败');
    }
  } catch (error) {
    console.error('获取评论板数据失败:', error);
    ElMessage.error('获取评论板数据失败');
  }
}

onMounted(fetchReviewBoardData);

const showDialog = (review) => {
  dialogReview.value = review
  dialogVisible.value = true
}
const truncate = (str, len) => {
  if (!str) return ''
  return str.length > len ? str.slice(0, len) + '...' : str
}
</script>

<style scoped>
.review-board {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px;
  margin-top: 24px;
  height: 400px;
  width: 100%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
}
.review-title {
  font-size: 20px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 16px;
  flex-shrink: 0;
}
.timeline-container {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 8px;
}

/* 自定义滚动条样式 */
.timeline-container::-webkit-scrollbar {
  width: 6px;
}

.timeline-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.timeline-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.timeline-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.review-item {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 4px 0;
  transition: background 0.15s;
}
.review-item:hover {
  background: #e6f0fa;
  border-radius: 8px;
}
.dish-name {
  color: #fff;
  background: #4a90e2;
  font-weight: bold;
  font-size: 15px;
  border-radius: 6px;
  padding: 2px 12px;
  margin-right: 8px;
}
.review-content {
  color: #2d3a4b;
  font-size: 15px;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.dialog-dish {
  color: #4a90e2;
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 8px;
}
.dialog-time {
  color: #7baee6;
  font-size: 14px;
  margin-bottom: 12px;
}
.dialog-content {
  color: #2d3a4b;
  font-size: 16px;
  line-height: 1.7;
}
</style> 