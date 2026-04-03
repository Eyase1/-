import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import path from 'node:path'

// https://vite.dev/config/
export default defineConfig(
  () => {
    
    return{
      resolve: {
        alias: {
          '@': '/src',
        },
      },
      plugins: [vue()],

    server: {
      port: 8033,
     
      proxy: {
        '/app-dev': {
      //rget: 'http://9.tcp.vip.cpolar.cn:11434',
          target: 'http://localhost:81',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/app-dev/, '')
        },
        '/image':{
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/image/, '')
        }
      }
    }
    }
  }
 )
