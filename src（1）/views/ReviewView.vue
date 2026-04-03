<template>
  <div class="review-bg">
    <Navbar/>
    <div class="review-content">
      <div class="review-left">
        <ReviewForm @dish-change="handleDishChange"
        @review-submitted="handleSub"
        />
      </div>
      <div class="review-right">
        <ReviewList :dish-id="selectedDishId" />
      </div>
    </div>
  </div>
</template>

<script setup>
import ReviewForm from '../components/ReviewForm.vue'
import ReviewList from '../components/ReviewList.vue'

import { ref, onMounted } from 'vue'
import { getDishes, getReviewsByDishId } from '../api/index'

const selectedDishId = ref(null)




const handleDishChange = (dishId) => {
  selectedDishId.value = dishId
}
const handleSub=(dishId)=>{
  selectedDishId.value=dishId
}
</script>

<style scoped>
.review-bg {
  width: 100%;
  min-height: 100vh;
  background: none;
}
.review-content {
  margin-left: 150px;
  margin-top: 5px;
  max-width: 1100px;
  
  padding: 40px 0 0 0;
  display: flex;
  flex-direction: row;
  gap: 36px;
  align-items: flex-start;
}
.review-left {
  flex: 1;
  min-width: 320px;
}
.review-right {
  flex: 2;
  min-width: 320px;
}
@media (max-width: 900px) {
  .review-content {
    flex-direction: column;
    gap: 16px;
  }
}
</style> 