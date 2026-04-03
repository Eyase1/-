<!-- <template>
  <div class="dishes-bg">
    <Navbar/>
    <div class="dishes-title-row">
      <div class="dishes-title">菜品展示</div>
    </div>
    <DishCard ref="dishCardRef" />
    <button class="fab" @click="dialogVisible = true">
      <i class="el-icon-plus">+</i>
    </button>
    <DishForm v-model="dialogVisible" @dish-action-completed="refreshDishes" />
  </div>
</template> -->

<!-- <script setup>
import Navbar from '../components/Navbar.vue'
import DishCard from '../components/DishCard.vue'
import DishForm from '../components/DishForm.vue'
import { ref } from 'vue'

const dialogVisible = ref(false)
const dishCardRef = ref(null)

const refreshDishes = () => {
  if (dishCardRef.value) {
    dishCardRef.value.refresh()
  }
}
</script> -->

<!-- <style scoped>

.dishes-bg {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  min-height: 100vh;
  background: transparent;
  overflow-x: hidden;
  padding-bottom: 80px;
}

.dishes-title-row {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 1400px;
  margin: 0 auto 24px auto;
  padding: 32px 0 0 0;
  box-sizing: border-box;
}

.dishes-title {
  font-size: 32px;
  font-weight: bold;
  color: #3578e5;
  margin-right: 100px;
}

.fab {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #4a90e2;
  color: #fff;
  border: none;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  font-size: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  z-index: 1000;
}

.fab:hover {
  background-color: #3578e5;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}
</style>  -->

<!-- <style scoped>
.dishes-bg {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  min-height: 100vh;
  background: transparent;
  overflow-x: hidden;
  padding-bottom: 80px;
  margin-left:300px;
}

.dishes-title-row {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  margin: 24px 0;
}

.dishes-title {
  font-size: 32px;
  font-weight: bold;
  color: #3578e5;
}

/* 新增 .dishes-grid 样式 */
.dishes-grid {
  display: flex;
  flex-wrap: wrap; /* 允许换行 */
  justify-content: center; /* 水平居中 */
  gap: 24px; /* 卡片之间的间距 */
  width: 100%;
  margin: 24px 0;
}

.fab {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #4a90e2;
  color: #fff;
  border: none;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  font-size: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  z-index: 1000;
}

.fab:hover {
  background-color: #3578e5;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}
</style> -->
<template>
  <div class="dishes-bg">
    <Navbar />
    <div class="dishes-title-row">
      <div class="dishes-title">菜品展示</div>
    </div>
    <div class="main-content">
      <DishCard ref="dishCardRef" />
    </div>
    <button class="fab" @click="dialogVisible = true">
      <i class="el-icon-plus">+</i>
    </button>
    <DishForm v-model="dialogVisible" @dish-action-completed="refreshDishes" />
  </div>
</template>

<script setup>
import Navbar from '../components/Navbar.vue'
import DishCard from '../components/DishCard.vue'
import DishForm from '../components/DishForm.vue'
import { ref } from 'vue'

const dialogVisible = ref(false)
const dishCardRef = ref(null)

const refreshDishes = () => {
  if (dishCardRef.value) {
    dishCardRef.value.refresh()
  }
}
</script>

<style scoped>
.dishes-bg {
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
  background: transparent;
  overflow-x: hidden;
  padding-bottom: 80px;
}

.dishes-title-row {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 1400px;
  margin: 0 auto 24px auto;
  padding: 32px 0 0 0;
  box-sizing: border-box;
}

.dishes-title {
  font-size: 32px;
  font-weight: bold;
  color: #3578e5;
}

.main-content {
  display: flex;
  justify-content: center;
  width: 1200px;
  padding: 0 20px;
  box-sizing: border-box;
}

.fab {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #4a90e2;
  color: #fff;
  border: none;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  font-size: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  z-index: 1000;
}

.fab:hover {
  background-color: #3578e5;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}
</style>