<template>
  <div class="my-collections-panel">
    <div class="my-collections-title">我的菜品收藏</div>
    <div class="my-collections-list">
      <div 
        v-for="dish in collections" 
        :key="dish.id || dish.name" 
        class="dish-card"
      >
        <img :src="dish.imageUrl" class="dish-img" />
        <div class="dish-info">
          <div class="dish-name">{{ dish.name }}</div>
          <!-- <div class="dish-category">{{ dish.categoryName }}</div> -->
        </div>
        <el-button
          class="collect-btn"
          :class="{
            'favorite-active': favorite,
            'favorite-inactive': !favorite
          }"
          type="warning"
          size="large"
          circle
          @click.stop="handleCollect(dish)"
          title="收藏"
        >
          <el-icon>
            <svg v-if="favorite" viewBox="0 0 24 24" width="22" height="22" fill="#FFD700"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>
            <svg v-else viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="#FFD700" stroke-width="2"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>
          </el-icon>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserCollections, collectDish } from '../api/index'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/index'

const collections = ref([])
const userStore = useUserStore();
const userId = userStore.userId;
const favorite = ref(true)
onMounted(async () => {
  try {
    const res = await getUserCollections(userId)
    collections.value = res.data.data
  } catch (e) {
    collections.value = []
  }
})

const handleCollect = async (dish) => {
  try {
    const res = await collectDish(dish.id, userId);
    if (res.status === 200) {
      ElMessage.success( '收藏成功')
      // 重新获取收藏数据
      const res = await getUserCollections(userId)
      collections.value = res.data.data
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    console.error('操作失败:', error);
    ElMessage.error('操作失败');
  }
}
</script>

<style scoped>
.my-collections-panel {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 24px 32px 12px 32px;
  margin-bottom: 24px;
  max-width: 100%;
  height: 400px;
  display: flex;
  flex-direction: column;
}
.my-collections-title {
  font-size: 18px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 16px;
  flex-shrink: 0;
}
.my-collections-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: flex-start;
  overflow-y: auto;
  overflow-x: hidden;
  flex: 1;
  padding-right: 8px;
  align-content: flex-start;
}

/* 自定义滚动条样式 */
.my-collections-list::-webkit-scrollbar {
  width: 6px;
}

.my-collections-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.my-collections-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.my-collections-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.dish-card {
  position: relative;
  width: 200px;
  height: 120px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(74,144,226,0.10);
  overflow: hidden;
  transition: box-shadow 0.2s;
  cursor: pointer;
  display: flex;
  flex-direction: row;
  align-items: center;
  flex-shrink: 0;
}

.dish-card:hover {
  box-shadow: 0 6px 24px 0 rgba(74,144,226,0.18);
}

.dish-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  margin: 0 12px;
}

.dish-info {
  flex: 1;
  padding: 12px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.dish-name {
  font-size: 16px;
  font-weight: bold;
  color: #4a90e2;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dish-category {
  font-size: 13px;
  color: #7baee6;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.collect-btn {
  position: absolute;
  right: 8px;
  top: 8px;
  z-index: 20;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  backdrop-filter: blur(4px);
  width: 32px;
  height: 32px;
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
</style> 