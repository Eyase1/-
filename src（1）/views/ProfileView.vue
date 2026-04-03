<template>
  <div class="profile-bg">
    <Navbar/>
    <div class="profile-content">
      <div class="profile-left">
        <UserInfoForm />
        <div class="diet-nutrition-row">
          <DietPlanPanel />

        </div>
      </div>
      <div class="profile-right">
        <MyReviewList />
        <MyCollections />
      </div>
    </div>
  </div>
</template>

<script setup>
import Navbar from '../components/Navbar.vue'
import UserInfoForm from '../components/UserInfoForm.vue'
import DietPlanPanel from '../components/DietPlanPanel.vue'
import DailyNutritionTable from '../components/DailyNutritionTable.vue'
import MyReviewList from '../components/MyReviewList.vue'
import MyCollections from '../components/MyCollections.vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

</script>

<style scoped>
.profile-bg {
  width: 120%;
  min-height: 100vh;
  background: none;
}
.profile-content {
  max-width: 1100px;
 margin-left: 90px;
  padding: 40px 0 0 0;
  display: flex;
  flex-direction: row;
  gap: 36px;
  align-items: flex-start;
}
.profile-left {
  flex: 1;
  min-width: 320px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.diet-nutrition-row {
  display: flex;
  flex-direction: row;
  gap: 24px;
  align-items: flex-start;
}
.profile-right {
  flex: 1;
  min-width: 320px;
}
@media (max-width: 900px) {
  .profile-content {
    flex-direction: column;
    gap: 16px;
  }
  .diet-nutrition-row {
    flex-direction: column;
    gap: 16px;
  }
}
</style> 