<template>
  <el-dialog
    v-model="dialogVisible"
    :title=" '添加菜品'"
    width="520px"
    @close="reset"
  >
    <el-form :model="form" label-width="90px" :disabled="loading">
      <el-form-item label="菜品名称">
        <el-input v-model="form.name" :placeholder="form.name ?? '请输入菜品名称'" />
      </el-form-item>
      <el-form-item label="菜品类别">
        <el-input v-model="form.category" :placeholder="form.category ?? '如川菜、粤菜等'" />
      </el-form-item>
      <el-form-item label="图片">
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-change="handleFileChange"
        >
          <el-button>上传图片</el-button>
        </el-upload>
        <img v-if="form.imageUrl" :src="form.imageUrl" class="dish-img-preview" />
      </el-form-item>
      <el-form-item>
        <el-button @click="analyzeDishHandler" :loading="loading">自动生成营养成分</el-button>
        <span class="tip">（输入菜品名或上传图片后可自动生成）</span>
      </el-form-item>
      <el-form-item label="原料">
        <el-input v-model="form.ingredients" />
      </el-form-item>
      <el-form-item label="过敏源">
        <el-input v-model="form.allergies" />
      </el-form-item>
      <el-form-item label="热量">
        <el-input v-model="form.calories" />
      </el-form-item>
      <el-form-item label="蛋白质">
        <el-input v-model="form.protein" />
      </el-form-item>
      <el-form-item label="脂肪">
        <el-input v-model="form.fat" />
      </el-form-item>
      <el-form-item label="碳水">
        <el-input v-model="form.carbohydrate" />
      </el-form-item>
      <el-form-item label="纤维">
        <el-input v-model="form.fiber" />
      </el-form-item>
      <el-form-item label="维生素">
        <el-input v-model="form.vitamins" />
      </el-form-item>
      <el-form-item label="矿物质">
        <el-input v-model="form.minerals" />
      </el-form-item>
      <el-form-item label="适合餐次">
        <el-input v-model="form.meal_type" :placeholder="form.meal_type ?? '如早餐/午餐/晚餐'" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submit" :loading="loading">添加</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, defineExpose, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import {  getDishById, uploadDishImage, createDish } from '../api/index'
import { analyzeDish } from '../api/index'
const props = defineProps({
  dishId: { type: [String, Number], default: null }
})
const emit = defineEmits(['dish-action-completed'])

const dialogVisible = ref(false);
const isEdit = ref(false);

const form = reactive({
  name: '',
  category: '',
  imageUrl: '',
  ingredients: '',
  allergies: '',
  calories: '',
  protein: '',
  fat: '',
  carbohydrate: '',
  fiber: '',
  vitamins: '',
  minerals: '',
  meal_type: ''
})
const loading = ref(false)
const file = ref(null)

watch(() => props.dishId, async (newId) => {
  if (newId) {
    isEdit.value = true;
    try {
      const res = await getDishById(newId);
      if (res.code === 200 && res.data) {
        Object.assign(form, res.data);
      } else {
        ElMessage.error(res.message || '获取菜品详情失败');
        reset();
      }
    } catch (error) {
      console.error('获取菜品详情失败:', error);
      ElMessage.error('获取菜品详情失败');
      reset();
    }
  } else {
    isEdit.value = false;
    reset();
  }
}, { immediate: true });

const beforeUpload = () => false
const handleFileChange = (uploadFile) => {
  file.value = uploadFile.raw
  form.imageUrl = URL.createObjectURL(uploadFile.raw)
}

const analyzeDishHandler = async () => {
  if (!form.name && !form.imageUrl) {
    ElMessage.warning('请输入菜品名称或上传图片')
    return
  }
  loading.value = true


  let foodname = form.name || ''
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
      Object.assign(form, res.data.data)
    } else {
      ElMessage.error(res.data.message || '分析失败')
    }
  } catch (e) {
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}


const submit = async () => {
  if (!form.name) {
    ElMessage.warning('菜品名称不能为空')
    return
  }
  if (!file.value) {
    ElMessage.warning('请上传图片')
    return
  }
  loading.value = true;
  console.log(form)

  try {
    // 1. 先上传图片
    const imgRes = await uploadDishImage(file.value);
    console.log(imgRes.data)
    if (imgRes.status && imgRes.status === 200&&imgRes.data) {
      form.imageUrl = imgRes.data; 
    } else {
      ElMessage.error('图片上传失败');
      loading.value = false;
      return;
    }
    // 2. 再提交菜品信息
  
    const res = await createDish(form);
    if (res.data && res.data.code === 200) {
      ElMessage.success('添加成功！');
      emit('dish-action-completed'); // 通知父组件刷新
        // 立即关闭弹窗
  emit('update:modelValue', false)
    } else {
      ElMessage.error(res.data.message || '添加失败！');
    }
  } catch (e) {
    ElMessage.error('请求失败');
  } finally {
    loading.value = false;
  }
};

const reset = () => {
  Object.assign(form, {
    name: '',
    category: '',
    imageUrl: '',
    ingredients: '',
    allergies: '',
    calories: '',
    protein: '',
    fat: '',
    carbohydrate: '',
    fiber: '',
    vitamins: '',
    minerals: '',
    mealType: ''
  })
  file.value = null
  loading.value = false
  isEdit.value = false;
};

// 暴露方法给父组件调用
const open = (dishId = null) => {
  if (dishId) {
    isEdit.value = true;
  } else {
    isEdit.value = false;
    reset();
  }
  dialogVisible.value = true;
};

const closeDialog = () => {
  emit('update:modelValue', false)
}

defineExpose({ open, close: closeDialog });
</script>

<style scoped>
.upload-demo {
  display: inline-block;
  margin-right: 12px;
}
.dish-img-preview {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
  margin-left: 10px;
  border: 1.5px solid #e6f0fa;
}
.tip {
  color: #7baee6;
  font-size: 13px;
  margin-left: 8px;
}
</style> 