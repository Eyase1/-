<template>
  <div class="nutrition-result">
    <div class="result-title">分析结果</div>
    <div class="result-row"><span class="label">菜品名称：</span>{{ result.name }}</div>
    <div class="result-row"><span class="label">类别：</span>{{ result.category }}</div>
    <div class="result-row"><span class="label">原料：</span>{{ result.ingredients }}</div>
    <div class="result-row"><span class="label">过敏源：</span>{{ result.allergies }}</div>
    <div class="result-row"><span class="label">热量：</span>{{ result.calories }} kcal</div>
    <div class="result-row"><span class="label">蛋白质：</span>{{ result.protein }} g</div>
    <div class="result-row"><span class="label">脂肪：</span>{{ result.fat }} g</div>
    <div class="result-row"><span class="label">碳水：</span>{{ result.carbohydrate }} g</div>
    <div class="result-row"><span class="label">纤维：</span>{{ result.fiber }} g</div>
    <div class="result-row"><span class="label">维生素：</span>{{ result.vitamins }}</div>
    <div class="result-row"><span class="label">矿物质：</span>{{ result.minerals }}</div>
    <div class="result-row"><span class="label">适合餐次：</span>{{ result.meal_type }}</div>
  </div>
</template>

<script setup>
const props = defineProps({
  result: Object
})
</script>

<style scoped>
.nutrition-result {
  background: #e6f0fa;
  border-radius: 12px;
  padding: 24px 28px;
  margin-top: 24px;
  width: 100%;
  box-sizing: border-box;
  box-shadow: 0 2px 8px rgba(74,144,226,0.06);
}
.result-title {
  font-size: 20px;
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 18px;
  letter-spacing: 1px;
}
.result-row {
  font-size: 16px;
  color: #2d3a4b;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}
.label {
  color: #4a90e2;
  font-weight: bold;
  width: 110px;
  display: inline-block;
}
</style> 