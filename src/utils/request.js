import axios from "axios";
import { useUserStore } from "../stores/index";
import { ElMessage } from "element-plus";



const request = axios.create({
    baseURL: "/app-dev/api/",
    timeout: 130000,
});

// 请求拦截器
request.interceptors.request.use(
    config => {
        const store = useUserStore();
        const token = store.getToken();
        
        // 如果有token则添加到请求头，使用自定义的 token key
        if (token) {
            config.headers["token"] = token;  // 使用自定义的 token key
            config.headers["Authorization"] = `Bearer ${token}`;  // 保留原有的 Authorization
        }
        
        return config;
    },
    error => {
        console.error("请求拦截器错误:", error);
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    // token 过期或无效
                    const store = useUserStore();
                    store.clearToken();
                    ElMessage.error("登录已过期，请重新登录");
                    // 跳转到登录页
                    window.location.href = "/login";
                    break;
                case 403:
                    ElMessage.error("没有权限访问");
                    break;
                case 404:
                    ElMessage.error("请求的资源不存在");
                    break;
                case 500:
                    ElMessage.error("服务器错误");
                    break;
                default:
                    ElMessage.error("请求失败");
            }
        } else {
            ElMessage.error("网络错误，请检查您的网络连接");
        }
        return Promise.reject(error);
    }
);

export default request;