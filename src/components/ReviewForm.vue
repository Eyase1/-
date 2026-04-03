<template>
  <div class="review-form">
    <el-form :model="form" label-width="80px" @submit.prevent>
      <el-form-item label="菜品名称">
        <el-select v-model="form.dishId" placeholder="请选择菜品" filterable style="width: 220px" @change="onDishChange">
          <el-option v-for="item in dishes" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="评价内容">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="3"
          maxlength="200"
          show-word-limit
          placeholder="请输入评价内容"
          style="width: 350px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { addReview, getDishes } from '../api/index'
import { useUserStore } from '../stores/index'

const userStore = useUserStore()
const userId = userStore.userId

const dishes = ref([])

onMounted(async () => {
  const res = await getDishes(userId)
  
  if (res && res.data) {
    dishes.value = res.data.map(item => ({
      id: item.dish.id,
      name: item.dish.name
    }))
  }
})

const form = ref({
  dishId: '',
  content: ''
})

const emit = defineEmits(['dish-change', 'review-submitted'])
const onDishChange = (val) => {
  emit('dish-change', val)
}

async function submitReview() {
  if (!form.value.dishId) {
    ElMessage.warning('请选择菜品')
    return
  }
  if (!form.value.content) {
    ElMessage.warning('请输入评价内容')
    return
  }
  const reviewData = {
    dishId: form.value.dishId,
    userId:userId,
    reviewContent: form.value.content
  }
  const res = await addReview(reviewData)
  if (res.status === 200) {
    ElMessage.success('评价提交成功！')
    emit('review-submitted',"allComment")
    form.value.dishId = ''
    form.value.content = ''
  } else {
    ElMessage.error(res.message || '评价提交失败！')
  }
}
</script>

<style scoped>
.review-form {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 32px 12px 32px;
  margin-bottom: 24px;
  max-width: 520px;
}
:deep(.el-form-item__label) {
  color: #4a90e2;
  font-weight: bold;
}
:deep(.el-input__inner), :deep(.el-textarea__inner) {
  background: #e6f0fa;
  border-radius: 8px;
  color: #2d3a4b;
}
:deep(.el-button--primary) {
  background: #4a90e2;
  border-color: #4a90e2;
}
</style> 