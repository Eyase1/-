<template>
    <el-card class="login-card">


   <el-tabs v-model="activeName" class="login-tabs" @tab-click="handleClick" stretch>
     <el-tab-pane label="登录" name="login">
       <div class="form-container">
      
         <el-input v-model="loginname" class="form-input" placeholder="请输入用户名或邮箱" prefix-icon="User" type="email"/>
         <el-input v-model="loginpassword" type="password"  class="form-input" placeholder="请输入密码" prefix-icon="Lock" />
         <el-button type="primary"  class="form-button" @click="btn_login">登录</el-button>
         <div class="form-links">
           <el-link type="primary">忘记密码</el-link>

           <el-link type="primary" @click="router.push('/home')">暂时跳过</el-link>
         </div>
       </div>


     </el-tab-pane>

     <el-tab-pane label="注册" name="register">
       <div class="form-container">
         <el-input v-model="registername" class="form-input" placeholder="请输入邮箱" prefix-icon="User" type="email" />
         <div class="verification-container">
           <el-input v-model="verificationCode" class="verification-input" placeholder="请输入验证码" prefix-icon="Message" />
           <el-button
             type="primary"
             class="verification-button"
             @click="sendVerificationCode"
             :disabled="isButtonDisabled">
             {{ buttonText }}
           </el-button>
         </div>
         <el-button type="primary" class="form-button" @click="handleVerifyCode">确认</el-button>
       </div>
     </el-tab-pane>
   </el-tabs>
    </el-card>
 </template>
 <script setup>
  import { ref, onUnmounted } from 'vue'
 import {login,register, sendCode, verifyCode} from "../../api/index"
 import { useRoute, useRouter } from 'vue-router';

 import { ElMessage } from 'element-plus';
 import { useUserStore } from '../../stores/index'

 const activeName = ref('login')
 const loginname = ref('')
 const loginpassword = ref('')
 const registername = ref('')
 const registerpassword = ref('')
 const relname = ref('')
 const useremail = ref('')
 const checkpassword = ref('')
 const router=useRouter()
 const route=useRoute()
 const passwordError=()=>{
   ElMessage.error('账号或密码错误！')
 }
 const checkPassword=()=>{
   ElMessage.error('密码不一致')
 }
 const notNUllUsernameAndPassword=()=>{
   ElMessage.error('用户名与密码不能为空')
 }
 const oneOfUsername=()=>{
   ElMessage.error('用户名已存在')
 }
 const successRegister=()=>{
   ElMessage({
     message: '注册成功',
     type: 'success',
   })
 }

 const handleClick = (tab, event) => {
   console.log(tab, event)
 }

 const store = useUserStore()
 const btn_login = async function () {
   try {
     const info = { username: loginname.value, password: loginpassword.value }
     const response = await login(info)
     console.log('登录响应:', response)

     if (response.data.code === 200) {
      console.log(response.data.data.id)
       if (response.data.data.token) {
         store.setToken(response.data.data.token)
         store.setLoginStatus({
           isLoggedIn: true,
           username: loginname.value,
           userId:response.data.data.id
         })
         ElMessage.success(response.data.message || '登录成功')

         const redirectPath = route.query.redirect?.toString()
         if (redirectPath) {
           router.push(redirectPath)
         } else {
           router.push('/home')
         }

       } else {
         console.error('登录响应中没有 token')
         ElMessage.error('登录失败：服务器响应异常')
       }
     } else {
       console.log('登录失败:', response.data)
       ElMessage.error(response.data.message || '账号或密码错误')
     }
   } catch (error) {
     console.error('登录出错:', error)
     ElMessage.error('登录失败：' + error.message)
   }
 }

//  const btn_register = async function () {
//    if (registername.value === '' || registerpassword.value === '') {
//      notNUllUsernameAndPassword()
//      return
//    }
//    if (registerpassword.value !== checkpassword.value) {
//      checkPassword()
//      return
//    }
//    try {
//      const info = {
//        username: registername.value,
//        password: registerpassword.value
//      }
//      const response = await register(info)
//      if (response.data.code === 200) {
//        successRegister()
//        activeName.value = 'login' // 注册成功后切换到登录标签
//        resetRegisterForm()
//      } else {
//        oneOfUsername()
//      }
//    } catch (error) {
//      console.error('注册出错:', error)
//      ElMessage.error('注册失败：' + error.message)
//    }
//  }

//  const resetRegisterForm = () => {
//    registername.value = ''
//    registerpassword.value = ''
//    checkpassword.value = ''
//  }

 const verificationCode = ref('')
 const buttonText = ref('发送验证码')
 const isButtonDisabled = ref(false)
 let timer = null

 const sendVerificationCode = async () => {
   if (!registername.value) {
     ElMessage.warning('请输入邮箱地址')
     return
   }
   try {
     const response = await sendCode(registername.value)
     console.log(registername.value)
     console.log(response)
     if (response.data.code === 200) {
       ElMessage.success('验证码已发送到您的邮箱')
       // 开始倒计时
       isButtonDisabled.value = true
       let count = 60
       buttonText.value = `${count}秒后重试`

       timer = setInterval(() => {
         count--
         buttonText.value = `${count}秒后重试`
         if (count === 0) {
           clearInterval(timer)
           buttonText.value = '发送验证码'
           isButtonDisabled.value = false
         }
       }, 1000)
     } else {
       ElMessage.error(response.message || '发送验证码失败')
     }
   } catch (error) {
     ElMessage.error('发送验证码失败：' + error.message)
   }
 }

 const handleVerifyCode = async () => {
   if (!verificationCode.value) {
     ElMessage.warning('请输入验证码')
     return
   }
   try {
     const response = await verifyCode(registername.value, verificationCode.value)
     if (response.data.code === 200) {
       ElMessage.success('验证成功')
       router.push({
         path: '/password-setup',
         query: { email: registername.value,code:verificationCode.value }
       })
     } else {
       ElMessage.error(response.message || '验证码错误')
     }
   } catch (error) {
     ElMessage.error('验证失败：' + error.message)
   }
 }

 // 在组件卸载时清除定时器
 onUnmounted(() => {
   if (timer) {
     clearInterval(timer)
   }
 })
 </script>

<style scoped>
.login-container {
 
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #e8f4ff 0%, #f0f7ff 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at center, rgba(96, 165, 250, 0.1) 0%, transparent 50%);
  animation: rotate 30s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.login-card {
  margin-top: 160px;
  margin-left: 350px;
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

.login-tabs {
  padding: 20px;
}

.login-tabs :deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background: linear-gradient(90deg, transparent, #60a5fa, transparent);
}

.login-tabs :deep(.el-tabs__item) {
  font-size: 1.1rem;
  font-weight: 500;
  color: #64748b;
  transition: all 0.3s ease;
}

.login-tabs :deep(.el-tabs__item.is-active) {
  color: #3b82f6;
  font-weight: 600;
}

.login-tabs :deep(.el-tabs__active-bar) {
  background-color: #3b82f6;
  height: 3px;
  border-radius: 3px;
}

.form-container {
  padding: 20px 0;
}

.form-input {
  margin-bottom: 20px;
}

.form-input :deep(.el-input__wrapper) {
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.form-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.form-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.form-input :deep(.el-input__inner) {
  font-size: 1rem;
}

.form-input :deep(.el-input__prefix-inner) {
  color: #94a3b8;
}

.form-button {
  width: 100%;
  padding: 12px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #60a5fa, #3b82f6);
  border: none;
  transition: all 0.3s ease;
  margin-top: 10px;
}

.form-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.form-button:active {
  transform: translateY(0);
}

.form-links {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding: 0 10px;
}

.form-links :deep(.el-link) {
  font-weight: 500;
  transition: all 0.3s ease;
}

.form-links :deep(.el-link:hover) {
  transform: translateY(-1px);
}

.verification-container {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  align-items: center;
}

.verification-input {
  flex: 1;
}

.verification-input :deep(.el-input__wrapper) {
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  background: #f8fafc;
}

.verification-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.verification-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  background: white;
}

.verification-input :deep(.el-input__inner) {
  font-size: 1rem;
  letter-spacing: 1px;
}

.verification-input :deep(.el-input__prefix-inner) {
  color: #94a3b8;
}

.verification-button {
  min-width: 120px;
  height: 44px;
  padding: 0 20px;
  border-radius: 12px;
  background: linear-gradient(135deg, #60a5fa, #3b82f6);
  border: none;
  transition: all 0.3s ease;
  font-weight: 500;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  white-space: nowrap;
}

.verification-button:disabled {
  background: #e2e8f0;
  cursor: not-allowed;
  color: #94a3b8;
  transform: none;
  box-shadow: none;
}

.verification-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.verification-button:active:not(:disabled) {
  transform: translateY(0);
}

@media (max-width: 480px) {
  .login-card {
    margin: 1rem;
  }

  .login-tabs {
    padding: 15px;
  }

  .form-container {
    padding: 15px 0;
  }

  .form-input :deep(.el-input__wrapper) {
    padding: 10px 14px;
  }

  .form-button {
    padding: 10px;
  }

  .verification-container {
    flex-direction: column;
    gap: 10px;
  }

  .verification-button {
    width: 100%;
    height: 40px;
  }
}
</style> 