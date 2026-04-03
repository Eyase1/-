<template>
  <div class="user-info-form">
    <el-form :model="form" label-width="80px" @submit.prevent>
      <el-form-item label="用户名">
        <el-input v-model="form.username" :disabled="!editing"  style="width: 220px" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="form.gender" placeholder="请选择性别" style="width: 120px" :disabled="!editing">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model.number="form.age" type="number" min="0" style="width: 120px" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="身高(cm)">
        <el-input v-model.number="form.height" type="number" min="0" style="width: 120px" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="体重(kg)">
        <el-input v-model.number="form.weight" type="number" min="0" style="width: 120px" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" style="width: 220px" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="过敏信息">
        <el-input v-model="form.allergies" style="width: 220px" :disabled="!editing" />
      </el-form-item>
      <el-form-item label="饮食偏好">
        <el-input v-model="form.dietPreference" style="width: 220px" :disabled="!editing" />
      </el-form-item>
      <el-form-item>
        <el-button v-if="!editing" @click="editing = true">更改</el-button>
        <el-button v-if="editing" type="primary" @click="save">保存</el-button>
        <el-button v-if="editing" @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
    <div class="bmi-panel">
      <div class="bmi-title">BMI指数：<span class="bmi-value">{{ bmi }}</span></div>
      <div class="bmi-status">当前状态：<span :class="bmiStatusClass">{{ bmiStatus }}</span></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUser } from '../api/index'
import { useUserStore } from '../stores/index'

const userStore = useUserStore();
const username = userStore.username;
const userId=userStore.userId;

const form = ref({
  username: '',
  gender: '',
  age: null,
  height: null,
  weight: null,
  email: '',
  role:'',
  allergies:'',
  dietPreference:''
})
const original = ref({ ...form.value })
const editing = ref(false)

onMounted(async () => {
  if (userId) {
    try {
      const res = await getUserInfo(userId);
      console.log(res);
      if (res.status === 200 && res.data) {
       
        form.value = { ...res.data };
        original.value = { ...res.data };
      } else {
        ElMessage.error(res.message || '获取用户信息失败');
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      ElMessage.error('获取用户信息失败');
    }
  } else {
    ElMessage.warning('用户未登录或用户名不可用，无法获取个人信息。');
  }
})

const bmi = computed(() => {
  if (!form.value.height || !form.value.weight) return '--'
  const h = form.value.height / 100
  return (form.value.weight / (h * h)).toFixed(1)
})

const bmiStatus = computed(() => {
  if (bmi.value === '--') return '--'
  const v = parseFloat(bmi.value)
  if (v < 18.5) return '偏瘦'
  if (v < 24) return '正常'
  if (v < 28) return '超重'
  return '肥胖'
})

const bmiStatusClass = computed(() => {
  if (bmiStatus.value === '正常') return 'bmi-normal'
  if (bmiStatus.value === '偏瘦') return 'bmi-thin'
  if (bmiStatus.value === '超重') return 'bmi-over'
  if (bmiStatus.value === '肥胖') return 'bmi-fat'
  return ''
})

function save() {
  console.log(form.value)
  updateUser(form.value).then(res => {
    if (res.status === 200) {
      original.value = { ...form.value }
      editing.value = false
      ElMessage.success('用户信息更新成功！')
    } else {
      ElMessage.error(res.message || '用户信息更新失败');
    }
  }).catch(error => {
    console.error('更新用户信息失败:', error);
    ElMessage.error('更新用户信息失败');
  });
}
function cancel() {
  form.value = { ...original.value }
  editing.value = false
}
</script>

<style scoped>
.user-info-form {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 32px 12px 32px;
  margin-bottom: 24px;
  max-width: 420px;
}
:deep(.el-form-item__label) {
  color: #4a90e2;
  font-weight: bold;
}
:deep(.el-input__inner), :deep(.el-select .el-input__inner) {
  background: #e6f0fa;
  border-radius: 8px;
  color: #2d3a4b;
}
:deep(.el-button--primary) {
  background: #4a90e2;
  border-color: #4a90e2;
}
.bmi-panel {
  margin-top: 18px;
  background: #e6f0fa;
  border-radius: 10px;
  padding: 14px 18px;
  text-align: left;
}
.bmi-title {
  font-size: 16px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 6px;
}
.bmi-value {
  color: #2d3a4b;
  font-size: 18px;
  font-weight: bold;
}
.bmi-status {
  font-size: 15px;
  color: #4a90e2;
  margin-top: 2px;
}
.bmi-normal { color: #3bb368; font-weight: bold; }
.bmi-thin { color: #4a90e2; font-weight: bold; }
.bmi-over { color: #e6a23c; font-weight: bold; }
.bmi-fat { color: #f56c6c; font-weight: bold; }
</style> 