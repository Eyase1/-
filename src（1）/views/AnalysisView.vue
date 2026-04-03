<template>
  <div class="analysis-bg">
    <Navbar/>
    <div class="analysis-content">
      <div class="analysis-panel">
        <div class="input-section">
          <el-input v-model="dishName" placeholder="请输入菜品名称" class="input-name" clearable />
          <span class="or-text">或</span>
          <el-upload
            class="upload-demo"
            drag
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-change="handleFileChange"
          >
            <el-icon><upload-filled /></el-icon>
            <div class="el-upload__text">拖拽或点击上传图片</div>
          </el-upload>
          <img v-if="previewUrl" :src="previewUrl" style="max-width: 120px; max-height: 120px; margin-top: 10px; border-radius: 8px;" />
          <el-button type="primary" class="analyze-btn" :loading="loading" @click="analyzeDishHandler">分析</el-button>
        </div>
        <el-alert v-if="loading" title="菜品正在分析中..." type="info" show-icon class="analyzing-alert" />
        <NutritionResult v-if="result" :result="result" />
      </div>
    </div>
  </div>
</template>

<script setup>
import Navbar from '../components/Navbar.vue'
import NutritionResult from '../components/NutritionResult.vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { analyzeDish, uploadDishImage } from '../api/index'

const dishName = ref('')
const file = ref(null)
const previewUrl = ref('')
const loading = ref(false)
const result = ref(null)

const beforeUpload = () => false // 阻止自动上传
const handleFileChange = (uploadFile) => {
  file.value = uploadFile.raw
  previewUrl.value = URL.createObjectURL(uploadFile.raw)
}

const analyzeDishHandler = async () => {
  if (!dishName.value && !file.value) {
    ElMessage.warning('请输入菜品名称或上传图片')
    return
  }
  loading.value = true
  result.value = null

  let foodname = dishName.value || ''
  let url = ''

  if (file.value) {
    // 先上传图片，拿到url
    const imgRes = await uploadDishImage(file.value)
    if (imgRes.status === 200 && imgRes.data) {
      url = imgRes.data.data || imgRes.data // 视后端返回结构而定
    } else {
      ElMessage.error('图片上传失败')
      loading.value = false
      return
    }
  }

  // 调用分析接口
  try {
    const res = await analyzeDish(foodname, url)
    if (res.data && res.data.code === 200) {
      result.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '分析失败')
    }
  } catch (e) {
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.analysis-bg {
  width: 190%;
  min-height: 100vh;
  background: none;
}
.analysis-content {
  max-width: 700px;
  margin: 0 auto;
  padding: 40px 0 0 0;
}
.analysis-panel {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 36px 32px 32px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.input-section {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 18px;
  flex-wrap: wrap;
  justify-content: center;
}
.input-name {
  width: 200px;
}
.or-text {
  color: #7baee6;
  font-size: 15px;
  font-weight: bold;
}
.upload-demo {
  width: 180px;
}
.analyze-btn {
  height: 40px;
  min-width: 80px;
  font-size: 16px;
  background: #4a90e2;
  border-color: #4a90e2;
  border-radius: 8px;
}
.analyzing-alert {
  margin: 18px 0 0 0;
  width: 100%;
}
</style> 