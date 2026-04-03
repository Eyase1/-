<template>
    <div class="home-bg">
        <Navbar/>
      <div class="home-content">
        <SearchBar />
        <Carousel />
        <div class="review-calendar-row">
          <ReviewBoard/>
          <CalendarPanel style="width: 110%;"/>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import Navbar from '../components/Navbar.vue'
  import SearchBar from '../components/SearchBar.vue'
  import Carousel from '../components/Carousel.vue'
  import ReviewBoard from '../components/ReviewBoard.vue'
  import CalendarPanel from '../components/CalendarPanel.vue'
  
  // 移除handleSearch函数，因为SearchBar现在自己处理搜索逻辑
  // const handleSearch = (keyword) => {
  //   console.log('搜索：', keyword)
  // }
  </script>
  
  <style scoped>
  .home-bg {
    margin-left: 60px;
    width: 170%; /* 修正宽度 */
    padding: 0px 0 0 0;
    min-height: 100vh;
    background: none;
  }
  .home-content {
    max-width: 1100px;
    margin: 0 auto;
    padding: 40px 0 0 0;
    display: flex;
    flex-direction: column;
    gap: 32px;
  }
  .review-calendar-row {
    display: flex;
    flex-direction: row;
    gap: 24px;
    align-items: flex-start;
  }
  @media (max-width: 900px) {
    .review-calendar-row {
      flex-direction: column;
      gap: 16px;
    }
  }
  </style>
  