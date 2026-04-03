<template>
  <div class="diet-plan-panel">
    <div class="plan-title">饮食计划管理</div>
    <div class="plan-calendar-row">
      <el-date-picker
        v-model="selectedDate"
        type="date"
        placeholder="选择日期"
        style="width: 180px; margin-right: 18px;"
        :disabled="editing"
      />
      <el-button type="primary" :loading="loading" @click="queryPlan" :disabled="editing || !selectedDate">查询计划</el-button>
      <el-button type="success" :loading="loading" @click="autoGenerate" :disabled="!editing || !selectedDate" style="margin-left: 12px;">自动生成饮食计划</el-button>
    </div>
    <el-form :model="planForm" label-width="80px" class="plan-form">
      <el-form-item label="早餐">
        <el-input v-model="planForm.breakfast" type="textarea" :rows="2" placeholder="请输入早餐内容" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="午餐">
        <el-input v-model="planForm.lunch" type="textarea" :rows="2" placeholder="请输入午餐内容" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="晚餐">
        <el-input v-model="planForm.dinner" type="textarea" :rows="2" placeholder="请输入晚餐内容" :disabled="!editing" />
      </el-form-item>
      <el-form-item>
        <el-button v-if="!editing" @click="startEditing" :disabled="!selectedDate">更改</el-button>
        <el-button v-if="editing" type="primary" @click="savePlan">保存计划</el-button>
        <el-button v-if="editing" @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>


  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/index'
import { getDietPlan, getPlan, saveDietPlan } from '../api/index'

const userStore = useUserStore()
const userId = userStore.userId
const selectedDate = ref(null) // 不默认显示当天日期
const loading = ref(false)
const planForm = reactive({
  breakfast: '',
  lunch: '',
  dinner: ''
})
const original = ref({ ...planForm })
const editing = ref(false) // 初始状态为false，显示更改按钮

const autoGenerate = async() => {
  if (!selectedDate.value) {
    ElMessage.warning('请先选择日期')
    return
  }
  loading.value = true
  try {
    const res = await getPlan(userId)
    console.log('API响应:', res)
    console.log('响应状态:', res.status)
    console.log('响应数据:', res.data)
    
    if(res.status === 200){
      if (res.data && res.data.data) {
        console.log('解析后的数据:', res.data.data)
        planForm.breakfast = res.data.data.breakfast_dish || '暂无早餐计划'
        planForm.lunch = res.data.data.lunch_dish || '暂无午餐计划'
        planForm.dinner = res.data.data.dinner_dish || '暂无晚餐计划'
        ElMessage.success('自动生成饮食计划成功！')
      } else {
        console.log('响应数据格式不正确:', res.data)
        ElMessage.warning('返回数据格式不正确')
      }
    } else {
      console.log('API返回错误状态:', res.status)
      ElMessage.info("请稍后重试")
    }
  } catch (error) {
    console.error('自动生成失败:', error)
    ElMessage.error('自动生成失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const savePlan = async () => {
  if (!selectedDate.value) {
    ElMessage.warning('请先选择日期')
    return
  }
  try {
    let planDate = ''
    if (selectedDate.value instanceof Date) {
      // 使用本地时间而不是UTC时间，避免时区问题
      const year = selectedDate.value.getFullYear()
      const month = String(selectedDate.value.getMonth() + 1).padStart(2, '0')
      const day = String(selectedDate.value.getDate()).padStart(2, '0')
      planDate = `${year}-${month}-${day}`
    } else if (typeof selectedDate.value === 'string' && selectedDate.value.length >= 10) {
      planDate = selectedDate.value.slice(0, 10)
    }
    const res = await saveDietPlan(userId, planDate, planForm)
    if (res && res.data && res.data.code === 200) {
      original.value = { ...planForm }
      editing.value = false
      ElMessage.success('饮食计划保存成功！')
    } else {
      ElMessage.error(res.data.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error('请求失败')
  }
}
const cancel = () => {
  planForm.breakfast = original.value.breakfast
  planForm.lunch = original.value.lunch
  planForm.dinner = original.value.dinner
  editing.value = false
}

const queryPlan = async () => {
  if (!selectedDate.value) {
    ElMessage.warning('请先选择日期')
    return
  }
  loading.value = true
  try {
    await fetchDietPlan()
    ElMessage.success('查询饮食计划成功！')
  } catch (error) {
    // ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const fetchDietPlan = async () => {
  let planDate = ''
  if (selectedDate.value instanceof Date) {
    // 使用本地时间而不是UTC时间，避免时区问题
    const year = selectedDate.value.getFullYear()
    const month = String(selectedDate.value.getMonth() + 1).padStart(2, '0')
    const day = String(selectedDate.value.getDate()).padStart(2, '0')
    planDate = `${year}-${month}-${day}`
  } else if (typeof selectedDate.value === 'string' && selectedDate.value.length >= 10) {
    planDate = selectedDate.value.slice(0, 10)
  }
  
  const formreq = {
    userId: userId,
    planDate: planDate
  }
  const res = await getDietPlan(formreq)
  if (res && res.data && res.data.data) {
    // 有数据时更新表单
    Object.assign(planForm, {
      breakfast: res.data.data.breakfastDish || '',
      lunch: res.data.data.lunchDish || '',
      dinner: res.data.data.dinnerDish || ''
    })
  } else {
    // 没有数据时清空表单
    Object.assign(planForm, {
      breakfast: '',
      lunch: '',
      dinner: ''
    })
  }
}

const startEditing = () => {
  if (!selectedDate.value) {
    ElMessage.warning('请先选择日期')
    return
  }
  editing.value = true
}

onMounted(() => {
  // 组件挂载时不再自动获取，等待用户主动查询
})

// 移除日期变化的自动监听
// watch(selectedDate, fetchDietPlan)
</script>

<style scoped>
.diet-plan-panel {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 32px 12px 32px;
  margin-bottom: 24px;
  max-width: 520px;
}
.plan-title {
  font-size: 18px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 16px;
}
.plan-calendar-row {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
}
.plan-form {
  margin-top: 0;
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