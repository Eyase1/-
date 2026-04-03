<template>
    <el-carousel height="280px" class="carousel">
      <el-carousel-item v-for="(item, idx) in images" :key="idx">
        <img :src="item" class="carousel-img" />
      </el-carousel-item>
    </el-carousel>
  </template>
  
  <script setup>
  const images = [
    'http://localhost:8080/image.png',
    'https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg',
    'https://images.pexels.com/photos/461382/pexels-photo-461382.jpeg',
  ]
  </script>
  
  <style scoped>
  .carousel {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 2px 8px rgba(74,144,226,0.08);
    margin: 24px 0;
  }
  .carousel-img {
    width: 100%;
    height: 280px;
    object-fit: cover;
    border-radius: 16px;
  }
  </style>
  