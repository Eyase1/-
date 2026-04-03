<template>
  <div class="dishes-container">
    <div class="dishes-grid">
      <div v-for="dish in dishes" :key="dish.dish.id" class="dish-card" @mouseenter="showInfo = dish.dish.id"
        @mouseleave="showInfo = null">
        <img :src="getFullImageUrl(dish.dish.imageUrl)" class="dish-img" />
        <div class="dish-info">
          <div class="dish-name">{{ dish.dish.name }}</div>
          <div class="dish-category">{{ dish.category?.name }}</div>
        </div>
        <transition name="fade">
          <div v-if="showInfo === dish.dish.id" class="nutrition-float">
            <div class="nutri-title">营养成分</div>
            <div class="nutri-item"><b>原料：</b>{{ dish.dish.ingredients }}</div>
            <div class="nutri-item"><b>过敏源：</b>{{ dish.dish.allergies }}</div>
            <div class="nutri-item"><b>热量：</b>{{ dish.dish.calories }} kcal</div>
            <div class="nutri-item"><b>蛋白质：</b>{{ dish.dish.protein }} g</div>
            <div class="nutri-item"><b>脂肪：</b>{{ dish.dish.fat }} g</div>
            <div class="nutri-item"><b>碳水：</b>{{ dish.dish.carbohydrate }} g</div>
            <div class="nutri-item"><b>纤维：</b>{{ dish.dish.fiber }} g</div>
            <div class="nutri-item"><b>维生素：</b>{{ dish.dish.vitamins }}</div>
            <div class="nutri-item"><b>矿物质：</b>{{ dish.dish.minerals }}</div>
            <div class="nutri-item"><b>适合餐次：</b>{{ dish.dish.mealType }}</div>
          </div>
        </transition>
        <el-button class="collect-btn" :class="{
          'favorite-active': dish.favorite,
          'favorite-inactive': !dish.favorite
        }" type="warning" size="large" circle @click.stop="handleCollect(dish)" title="收藏">
          <el-icon>
            <svg v-if="dish.favorite" viewBox="0 0 24 24" width="22" height="22" fill="#FFD700">
              <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" />
            </svg>
            <svg v-else viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="#FFD700" stroke-width="2">
              <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" />
            </svg>
          </el-icon>
        </el-button>

        <div class="dish-actions">
          <el-button size="small" type="danger" @click.stop="handleDelete(dish)">删除</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, defineExpose, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { collectDish, deleteDish, getDishes } from '../api/index'
import { useUserStore } from '../stores/index'

const emit = defineEmits(['edit', 'dish-action-completed'])

const userStore = useUserStore();
const isAdminUser = computed(() => userStore.role === 'admin');
const userId = userStore.userId;
const showInfo = ref(null)
const dishes = ref([])
const dishFormRef = ref(null)
const editDishId = ref(null)

// const props = defineProps({
//   dishId: { type: [String, Number], default: null }
// })
// watch(() => props.dishId, async (newId) => {
//   if (newId) {
//     isEdit.value = true;
//     // ...请求并赋值
//   } else {
//     isEdit.value = false;
//     reset();
//   }
// }, { immediate: true });

// 获取菜品数据
const fetchDishes = async () => {
  try {
    console.log(userId);
    const res = await getDishes(userId);
    //console.log(res);
    if (res.status === 200 && res.data) {
      dishes.value = res.data;

    } else {
      ElMessage.error(res.message || '获取菜品列表失败');
    }
  } catch (error) {
    console.error('获取菜品列表失败:', error);
    ElMessage.error('获取菜品列表失败');
  }
}

onMounted(fetchDishes);

// 暴露刷新方法给父组件
defineExpose({
  refresh: fetchDishes
});

const handleCollect = async (dish) => {
  try {
    console.log(dish.dish)
    const res = await collectDish(dish.dish.id, userId);
    console.log(res)
    if (res.status === 200) {
      ElMessage.success(dish.favorite ? '取消收藏成功' : '收藏成功')
      // 重新获取数据以更新收藏状态
      await fetchDishes();
      emit('dish-action-completed');
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    console.error('操作失败:', error);
    ElMessage.error('操作失败');
  }
}

const handleDelete = async (dish) => {
  ElMessageBox.confirm('确定要删除该菜品吗？', '提示', { type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteDish(dish.dish.id);
        if (res.status === 200) {
          ElMessage.success('删除成功');
          // 重新获取数据
          await fetchDishes();
          emit('dish-action-completed');
        } else {
          ElMessage.error(res.message || '删除失败');
        }
      } catch (error) {
        console.error('删除菜品失败:', error);
        ElMessage.error('删除菜品失败');
      }
    })
    .catch(() => { /* User cancelled */ });
}

const getFullImageUrl = (url) => {
  if (!url) return '';
  // 如果已经是完整url就直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) return url;
  return 'http://localhost:8080/image/' + url.replace(/^\/+/, '');
};

</script>

<style scoped>
.dishes-container {
  width: 100%;
  max-width: 1200px;
}

.dishes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 32px;
  padding: 0 0 40px 0;
  box-sizing: border-box;
  width: 1200px;
  height: 800px;
}

.dish-card {
  position: relative;
  width: 260px;
  height: 340px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 2px 12px 0 rgba(74, 144, 226, 0.10);
  overflow: hidden;
  transition: box-shadow 0.2s;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 auto;
}

.dish-card:hover {
  box-shadow: 0 6px 24px 0 rgba(74, 144, 226, 0.18);
}

.dish-img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 18px 18px 0 0;
}

.dish-info {
  padding: 18px 16px 0 16px;
  width: 100%;
  text-align: center;
}

.dish-name {
  font-size: 20px;
  font-weight: bold;
  color: #4a90e2;
  margin-bottom: 6px;
}

.dish-category {
  font-size: 15px;
  color: #7baee6;
}

.nutrition-float {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(230, 240, 250, 0.98);
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(74, 144, 226, 0.18);
  z-index: 10;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 28px 24px 64px 24px;
  font-size: 15px;
  color: #2d3a4b;
}

.nutri-title {
  font-size: 18px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 10px;
}

.nutri-item {
  margin-bottom: 2px;
  word-break: break-all;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.collect-btn {
  position: absolute;
  right: 16px;
  bottom: 16px;
  z-index: 20;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  backdrop-filter: blur(4px);
}

.collect-btn.favorite-active {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 12px rgba(255, 193, 7, 0.2);
}

.collect-btn.favorite-active:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(255, 193, 7, 0.25);
}

.collect-btn.favorite-inactive {
  background: rgba(255, 255, 255, 0.9);
}

.collect-btn.favorite-inactive:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.collect-btn .el-icon svg {
  transition: all 0.3s ease;
}

.collect-btn.favorite-active .el-icon svg {
  fill: #FFB800;
  filter: drop-shadow(0 2px 4px rgba(255, 184, 0, 0.2));
}

.collect-btn.favorite-inactive .el-icon svg {
  stroke: #666;
  fill: none;
}

.collect-btn:hover .el-icon svg {
  transform: scale(1.1);
}

.dish-actions {
  position: absolute;
  bottom: 16px;
  left: 16px;
  z-index: 30;
  display: flex;
  gap: 8px;
}
</style>