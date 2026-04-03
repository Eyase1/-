import request from '../utils/request'
import axios from 'axios'


// 登录接口
// 参数: { username: "admin", password: "123456" }
// 返回: { 
//   code: 200, 
//   message: "登录成功！",
//   data: { 
//     token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 
//   }
// }
export const login = (data) => {
    return request.post('user/login', data);
};

// 注册接口
// 参数: { username: "newuser", password: "123456" }
// 返回: { 
//   code: 200, 
//   message: "注册成功！",
//   data: null 
// }
export const register = (info) => {
    return request.post("user/register", info);
};



// 验证token
// 参数: 无 (token在header中)
// 返回: {
//   code: 200,
//   message: "验证成功",
//   data: {
//     valid: true,
//     username: "admin"
//   }
// }
export const verifyToken = () => {
    return request.post("verify-token");
};

export const getUserInfo=(info)=>{
    return request.get(`user/userInfo/${info}`)
}








// 发送验证码
export const sendCode = (email) => {
    console.log(email)
    return request.get(`/user/sendEmailCode?email=${encodeURIComponent(email)}`)
}

// 验证验证码
export const verifyCode = (email, code) => {
    return request.post(`/user/verfiy/${code}?email=${encodeURIComponent(email)}`)
}

// 完成注册
export const completeRegister = (email, password) => {
    console.log('ssddd',email)
    return request.post('/register', {
        email: email.email,
        password: email.password
    })
}

// 获取当前月份的饮食计划
export const getMonthDietPlans = (month) => {
    return request.get(`/diet-plans/${month}`)
}

// 获取菜品列表
export const getDishes = (info) => {
    //return request.get(`/dishes/${info}`)
    return request.get(`/dishes/${info}`)
}
// 创建菜品
export const createDish = (dish) => {
  return request.post('/dishes/addDish', dish);
};

// 删除菜品 (现在通过ID删除)
export const deleteDish = (id) => request.delete(`/dishes/${id}`)

// 获取单个菜品 by ID
export const getDishById = (id) => request.get(`/api/dishes/${id}`)

/**
 * 收藏菜品
 * @param {string} dishId 菜品ID
 * @returns Promise
 */
export const collectDish = (dishId, userId) => 
  request.post(`/dishes/favorite/${dishId}`, null, { params: { userId } });

export const getUserCollections = (userId) => request.post(`/user/collection/${userId}`)

// 获取每日营养数据 (示例)
export const getDailyNutrition = (username, date) => {
  return request.get(`/api/nutrition/daily/${username}/${date}`)
}

// 获取用户评论 (示例)
export const getUserReviews = (userId) => {
  return request.get(`/user/comments/user/${userId}`)
}

// 删除评论 (示例)
export const deleteReview = (reviewId, userId) => {
  return request.delete(`/user/comments/${reviewId}`, {
    params: { userId }
  });
}

// 获取所有评论 (示例)
export const getAllReviews = () => {
  return request.get('/api/reviews/all')
}

export const getDietPlan=(info)=>{
  return request.post('/user/getPlan',info)
}
// 更新评论
export const updateReview = (reviewId, newContent) => {
  return request.put(`/api/reviews/${reviewId}`, { content: newContent })
}

// 添加评论 (示例)
export const addReview = (reviewData) => {
  console.log(reviewData)
  return request.post('/user/comments/saveComment', reviewData)
}

// 搜索菜品 (示例)
export const searchDishes = (keyword) => {
  return request.get(`/api/dishes/search?keyword=${encodeURIComponent(keyword)}`)
}

// 获取评论板数据 (示例)
export const getReviewBoardData = () => {
  return request.get('/user/comments/dishes/allComment')
}

// 获取所有用户
export function getAllUsers() {
  return request({
    url: '/user/students',
    method: 'get'
  })
}

// 获取日历事件数据 (示例)
export const getCalendarEvents = (username, month) => {
  // 假设根据用户和月份获取日历事件
  return request.get(`/api/calendar/events/${username}/${month}`)
}

// 更新用户信息
export function updateUser(data) {
  return request({
    url: '/user/userInfo',
    method: 'put',
    data
  })
}

// 新建 axios 实例用于图片上传，配置跨域
export const uploadAxios = axios.create({
  baseURL: "/image/api/",
  timeout: 50000,
});

// 上传图片，返回图片url
export const uploadDishImage = (imageFile) => {
  const formData = new FormData();
  formData.append('file', imageFile);
  return uploadAxios.post('/dishes/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
};

// 菜品分析接口
export const analyzeDish = (foodname, url) => {
  return request.post('/ai/analyze', { foodname, url });
};

export const getReviewsByDishId = (dishId) => {
  return request.get(`/user/comments/dishes/${dishId}`);
};

export const saveDietPlan = (userId, date, plan) => {
  return request.post('/user/saveorupdate', {
    userId,
    planDate:date,
    breakfastDish:plan.breakfast,
    lunchDish:plan.lunch,
    dinnerDish:plan.dinner// 包含 breakfast, lunch, dinner
  })
}
export const getPlan=(userId)=>{
  return request.post(`/ai/getPlan`,{
    userid:userId
  })
}

// 假设后端接口为 /user/search，参数为 { keyword: 'xxx' }
export const searchUsers = (keyword) => {
  return request.post('/user/search', { keyword });
}



