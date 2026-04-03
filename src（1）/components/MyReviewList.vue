<template>
  <div class="my-review-list">
    <div class="my-review-title">我的菜品评价</div>
    <el-empty v-if="!reviews.length" description="暂无评价" />
    <el-timeline v-else>
      <el-timeline-item
        v-for="review in reviews"
        :key="review.id"
        :timestamp="review.time"
        placement="top"
        color="#4a90e2"
      >
        <div class="review-item">
          <span class="dish-name">{{ review.dishName }}</span>
          <span class="review-content">{{ review.reviewContent }}</span>
          <span class="review-time">{{ truncate(review.reviewTime,10)  }}</span>
          <el-button
            v-if="review.user === username"
            size="small"
            type="danger"
            @click="handleDelete(review.id)"
            style="margin-left: 8px;"
          >删除</el-button>
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserReviews, deleteReview } from '../api/index'
import { useUserStore } from '../stores/index'

const userStore = useUserStore();
const userId = userStore.userId

const reviews = ref([]);
const truncate = (str, len) => {
  if (!str) return ''
  return str.length > len ? str.slice(0, len) + '...' : str
}
// 获取评论数据
async function fetchReviews() {
  try {
    if (userId) {
      const res = await getUserReviews(userId);
      if (res.status === 200 && res.data) {
        reviews.value = res.data.data;
      } else {
        ElMessage.error(res.message || '获取评论数据失败');
      }
    } else {
      ElMessage.warning('用户未登录或用户名不可用，无法获取评论。');
    }
  } catch (error) {
    console.error('获取评论数据失败:', error);
    ElMessage.error('获取评论数据失败');
  }
}

// 删除评论
async function handleDelete(reviewId) {
  try {
    const res = await deleteReview(reviewId);
    if (res.code === 200) {
      ElMessage.success('评论删除成功！');
      // 重新获取评论或从本地数组中移除
      reviews.value = reviews.value.filter(r => r.id !== reviewId);
    } else {
      ElMessage.error(res.message || '评论删除失败！');
    }
  } catch (error) {
    console.error('删除评论失败:', error);
    ElMessage.error('删除评论失败！');
  }
}

onMounted(fetchReviews)
</script>

<style scoped>
.my-review-list {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 32px 12px 32px;
  margin-bottom: 24px;
  max-width: 520px;
}
.my-review-title {
  font-size: 18px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 16px;
}
.review-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 4px 0;
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
  word-break: break-all;
}
</style> 