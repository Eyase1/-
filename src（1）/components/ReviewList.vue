<template>
  <div class="review-list">
    <Navbar/>
    <div class="review-list-title">评价列表</div>
    <el-empty v-if="!reviews.length" description="暂无评价" />
    <el-timeline v-else>
      <el-timeline-item
        v-for="(review, idx) in reviews"
        :key="review.id"
        :timestamp="review.time"
        placement="top"
        color="#4a90e2"
      >
        <div class="review-item">
          <span class="review-user">{{ truncate(review.reviewTime,10) }}</span>
          <span class="review-content">{{ truncate(review.reviewContent, 50) }}</span>
          <el-input v-if="editingId === review.id" v-model="editContent" type="textarea" :rows="2" style="width: 260px; margin-right: 8px;" />
          
          <!-- <el-button v-if="editingId !== review.id" size="small" @click="startEdit(review)">编辑</el-button> -->
          <!-- <el-button v-if="editingId === review.id" size="small" type="primary" @click="saveEdit(review)">保存</el-button>
          <el-button v-if="editingId === review.id" size="small" @click="cancelEdit">取消</el-button> -->
          <el-button v-if="review.userId==userId" size="small" type="danger" @click="handleDelete(review)">删除</el-button>
          
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllReviews, deleteReview, updateReview, getReviewsByDishId } from '../api/index' // 引入API
import Navbar from './Navbar.vue';
import { useUserStore } from '@/stores';
const props = defineProps({
  dishId: [String, Number]
})

const reviews = ref([]);
const editingId = ref(null)
const editContent = ref('')
const userStore=useUserStore()
const userId=userStore.userId
console.log("test"+userId)
// 获取评论数据
async function fetchReviews() {
  try {
    const res = await getReviewsByDishId("allComment");
    if (res.status === 200 && res.data) {
      reviews.value = res.data.data;
    } else {
      ElMessage.error(res.message || '获取评论数据失败');
    }
  } catch (error) {
    console.error('获取评论数据失败:', error);
    ElMessage.error('获取评论数据失败');
  }
}

onMounted(fetchReviews);

watch(() => props.dishId, async (newId) => {
  if (newId) {
    const res = await getReviewsByDishId(newId)
    console.log(res.data)
    reviews.value = res.data.data || []
  }
}, { immediate: true })

// const startEdit = (review) => {
//   editingId.value = review.id
//   editContent.value = review.content
// }

// async function saveEdit(review) {
//   if (!editContent.value) {
//     ElMessage.warning('评价内容不能为空')
//     return
//   }
//   try {
//     const res = await updateReview(review.id, editContent.value);
//     if (res.code === 200) {
//       ElMessage.success('评价更新成功！');
//       // 更新本地数据
//       const index = reviews.value.findIndex(r => r.id === review.id);
//       if (index !== -1) {
//         reviews.value[index].content = editContent.value;
//       }
//       editingId.value = null;
//       editContent.value = '';
//     } else {
//       ElMessage.error(res.message || '评价更新失败！');
//     }
//   } catch (error) {
//     console.error('更新评论失败:', error);
//     ElMessage.error('更新评论失败！');
//   }
// }

// const cancelEdit = () => {
//   editingId.value = null
//   editContent.value = ''
// }

async function handleDelete(review) {
  ElMessageBox.confirm('确定要删除该评价吗？', '提示', { type: 'warning' })
    .then(async () => {
      try {
        
        const res = await deleteReview(review.id,userId);
        if (res.status === 200) {
          ElMessage.success('评论删除成功！');
          reviews.value = reviews.value.filter(r => r.id !== review.id);
        } else {
          ElMessage.error(res.message || '评论删除失败！');
        }
      } catch (error) {
        console.error('删除评论失败:', error);
        ElMessage.error('删除评论失败！');
      }
    })
    .catch(() => { /* 用户取消 */ });
}

const truncate = (str, len) => {
  if (!str) return ''
  return str.length > len ? str.slice(0, len) + '...' : str
}

// const reviewListRef = ref(null)
// const refreshReviews = () => {
//   if (reviewListRef.value) {
//     reviewListRef.value.fetchReviews() // 让 ReviewList 重新请求数据
//   }
// }

// defineExpose({ fetchReviews })
</script>

<style scoped>
.review-list {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 32px 12px 32px;
  min-width: 320px;
  max-width: 600px;
}
.review-list-title {
  font-size: 18px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 16px;
}
.review-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 4px 0;
  flex-wrap: wrap;
}
.review-user {
  color: #7baee6;
  font-weight: bold;
  font-size: 15px;
  margin-right: 8px;
}
.review-content {
  color: #2d3a4b;
  font-size: 15px;
  flex: 1;
  word-break: break-all;
}
</style> 