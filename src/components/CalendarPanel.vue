<template>
  <div class="calendar-panel">
    <el-calendar v-model="calendarDate" class="calendar" @panel-change="onPanelChange">
      <template #dateCell="{ data }">
        <el-popover
          v-if="hasPlan(data.day)"
          placement="top"
          width="220"
          trigger="hover"
        >
          <template #reference>
            <div class="cell-with-plan">
              <span>{{ data.day.split('-')[2] }}</span>
              <span class="plan-dot"></span>
            </div>
          </template>
          <div class="plan-pop-title">饮食计划</div>
          <div class="plan-pop-item"><b>早餐：</b>{{ plans[data.day].breakfast }}</div>
          <div class="plan-pop-item"><b>午餐：</b>{{ plans[data.day].lunch }}</div>
          <div class="plan-pop-item"><b>晚餐：</b>{{ plans[data.day].dinner }}</div>
        </el-popover>
        <div v-else>{{ data.day.split('-')[2] }}</div>
      </template>
    </el-calendar>
    <div class="current-time">
      <span class="time-label">当前时间：</span>
      <span class="time-value">{{ time }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMonthDietPlans, getCalendarEvents } from '../api/index'
import { useUserStore } from '../stores/index'

const userStore = useUserStore();
const username = userStore.username;

const calendarDate = ref(new Date())
const time = ref('')
let timer = null
const plans = ref({});

function getMonthStr(date) {
  const y = date.getFullYear()
  const m = (date.getMonth() + 1).toString().padStart(2, '0')
  return `${y}-${m}`
}

async function fetchPlans(date) {
  if (!username) {
    ElMessage.warning('用户未登录或用户名不可用，无法获取日历计划。');
    return;
  }
  const month = getMonthStr(date)
  try {
    const res = await getCalendarEvents(username, month);
    if (res.code === 200 && res.data) {
      plans.value = res.data;
    } else {
      ElMessage.error(res.message || '获取日历计划失败');
    }
  } catch (error) {
    console.error('获取日历计划失败:', error);
    ElMessage.error('获取日历计划失败');
  }
}

const updateTime = () => {
  const now = new Date()
  const pad = (n) => n.toString().padStart(2, '0')
  time.value = `${now.getFullYear()}-${pad(now.getMonth()+1)}-${pad(now.getDate())} ` +
    `${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  fetchPlans(calendarDate.value)
})

onUnmounted(() => {
  clearInterval(timer)
})

const hasPlan = (dateStr) => !!plans.value[dateStr]

function onPanelChange({ year, month }) {
  const newDate = new Date(year, month);
  fetchPlans(newDate);
}
</script>

<style scoped>
.calendar-panel {

  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(74,144,226,0.08);
  padding: 18px 18px 10px 18px;
  width: 340px;
  min-width: 280px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 24px;
}
.calendar {
  width: 100%;
  border-radius: 12px;
  --el-calendar-selected-bg-color: #e6f0fa;
}
.cell-with-plan {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.plan-dot {
  width: 7px;
  height: 7px;
  background: #4a90e2;
  border-radius: 50%;
  margin-top: 2px;
  display: inline-block;
}
.plan-pop-title {
  color: #4a90e2;
  font-weight: bold;
  margin-bottom: 6px;
}
.plan-pop-item {
  color: #2d3a4b;
  font-size: 14px;
  margin-bottom: 2px;
}
.current-time {
  margin-top: 16px;
  font-size: 16px;
  color: #4a90e2;
  font-weight: bold;
  letter-spacing: 1px;
}
.time-label {
  color: #7baee6;
  font-weight: normal;
  font-size: 15px;
}
.time-value {
  color: #4a90e2;
  font-size: 17px;
  font-weight: bold;
}
</style> 