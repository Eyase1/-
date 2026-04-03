<template>
  <div class="nutrition-table-panel">
    <div class="nutrition-table-title">当日营养情况</div>
    <el-table :data="nutritionData" border style="width: 100%">
      <el-table-column prop="meal" label="餐次" width="80" />
      <el-table-column prop="calories" label="热量 (kcal)" width="110" />
      <el-table-column prop="protein" label="蛋白质 (g)" width="110" />
      <el-table-column prop="fat" label="脂肪 (g)" width="100" />
      <el-table-column prop="carbohydrate" label="碳水 (g)" width="110" />
      <el-table-column prop="fiber" label="纤维 (g)" width="100" />
      <el-table-column prop="vitamins" label="维生素" width="100" />
      <el-table-column prop="minerals" label="矿物质" width="100" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDailyNutrition } from '../api/index' // 引入API
import { useUserStore } from '../stores/index' // 引入用户store

const userStore = useUserStore();
const username = userStore.username;

const nutritionData = ref([]); // 初始化为空数组

onMounted(async () => {
  try {
    if (username) {
      // 示例：获取今天的营养数据，日期格式可能需要根据后端要求调整
      const today = new Date().toISOString().slice(0, 10); // YYYY-MM-DD
      const res = await getDailyNutrition(username, today);
      if (res.code === 200 && res.data) {
        nutritionData.value = res.data;
      } else {
        ElMessage.error(res.message || '获取每日营养数据失败');
      }
    } else {
      ElMessage.warning('用户未登录或用户名不可用，无法获取每日营养数据。');
    }
  } catch (error) {
    console.error('获取每日营养数据失败:', error);
    ElMessage.error('获取每日营养数据失败');
  }
});
</script>

<style scoped>
.nutrition-table-panel {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 24px 12px 24px;
  margin-bottom: 24px;
  max-width: 520px;
}
.nutrition-table-title {
  font-size: 18px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 16px;
}
:deep(.el-table) {
  background: #e6f0fa;
  border-radius: 10px;
}
:deep(.el-table th) {
  background: #d2e6fa;
  color: #4a90e2;
  font-weight: bold;
}
:deep(.el-table td) {
  color: #2d3a4b;
}
</style> 