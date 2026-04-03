<template>
    <div class="notice-board">
      <div class="notice-title">公告信息</div>
      <el-timeline>
        <el-timeline-item
          v-for="(notice, idx) in notices"
          :key="idx"
          :timestamp="notice.date"
          placement="top"
          color="#4a90e2"
        >
          <div class="notice-content">{{ notice.content }}</div>
        </el-timeline-item>
      </el-timeline>
    </div>
  </template>
  
  <script setup>
  const notices = [
    { content: '有梦想，就要努力去实现', date: '2023-04-25' },
    { content: '又是一年毕业季', date: '2023-04-25' },
    { content: '坚持路上，坚持有在心间', date: '2023-04-25' },
    { content: '挫折是另一个生命的开端', date: '2023-04-25' },
  ]
  </script>
  
  <style scoped>
  .notice-board {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 2px 8px rgba(74,144,226,0.08);
    padding: 24px;
    margin-top: 24px;
  }
  .notice-title {
    font-size: 20px;
    color: #4a90e2;
    font-weight: bold;
    margin-bottom: 16px;
  }
  .notice-content {
    color: #333;
    font-size: 15px;
  }
  </style>
  