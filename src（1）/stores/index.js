import { defineStore } from 'pinia'


// User Store
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    isLoggedIn: !!localStorage.getItem('token'),
    userId:localStorage.getItem('userId')||'',
    username: !!localStorage.getItem('token')? localStorage.getItem('username') || '未登录':'未登录'
  }),

  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
      this.isLoggedIn = !!token
    },

    logout() {
      this.clearToken()
      localStorage.removeItem('username')
      localStorage.removeItem('userId')
    },

    clearToken() {
      this.token = ''
      localStorage.removeItem('token')
      this.isLoggedIn = false
      this.username = '未登录'
    },

    setLoginStatus({ isLoggedIn, username,userId }) {
      this.isLoggedIn = isLoggedIn
      this.username = username
      this.userId=userId
      localStorage.setItem('username', username)
      localStorage.setItem('userId',userId)
    },

    getToken() {
      return this.token
    },
    getUserId(){
      return this.userId;
    }
  }
})

// Counter Store
export const useCounterStore = defineStore('counter', {
  state: () => ({
    counter: 0
  }),

  actions: {
    increment() {
      this.counter++
    },

    decrement() {
      this.counter--
    }
  },

  getters: {
    doubleCount: (state) => state.counter * 2
  }
})
