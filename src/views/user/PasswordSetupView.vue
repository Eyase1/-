<template>
    <div class="register-container">
      <h2>设置密码</h2>
      <el-form :model="form" label-width="80px" class="register-form">
        <el-form-item label="邮箱">
          <el-input v-model="form.email" disabled />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%;">
            <el-option label="男" value="male" />
            <el-option label="女" value="female" />
            <el-option label="保密" value="secret" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { register } from '../../api/index'
  import { ElMessage } from 'element-plus'
  
  const route = useRoute()
  const router = useRouter()
  const loading = ref(false)
  
  const form = ref({
    email: '',
    username: '',
    password: '',
    gender: 'secret'
  })
  
  onMounted(() => {
    // 从路由参数获取邮箱
    form.value.email = route.query.email || ''
  })
  
  const handleRegister = async () => {
    if (!form.value.username || !form.value.password) {
      ElMessage.warning('请填写用户名和密码')
      return
    }
    loading.value = true
    try {
      const res = await register({
        code:route.query.code,
        email: form.value.email,
        username: form.value.username,
        password: form.value.password,
        gender: form.value.gender
      })
      if (res.data && res.data.code === 200) {
        ElMessage.success('注册成功，请登录')
        router.push('/login') // 跳转到登录页
      } else {
        ElMessage.error(res.data.message || '注册失败')
      }
    } catch (e) {
      ElMessage.error('请求失败')
    } finally {
      loading.value = false
    }
  }
  </script>
  
  <style scoped>
  .register-container {
    max-width: 400px;
    margin: 60px auto;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(74,144,226,0.08);
    padding: 32px 36px 18px 36px;
  }
  .register-form {
    margin-top: 18px;
  }
  </style>