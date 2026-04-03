package com.graduatedesign.cozeproxyservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录页面控制器
 */
@RestController
public class LoginController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        String html = getLoginPageHtml();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
    }

    private String getLoginPageHtml() {
        return """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 智能体对话系统</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            width: 100%;
            max-width: 400px;
        }

        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .login-header h1 {
            color: #667eea;
            font-size: 2em;
            margin-bottom: 10px;
        }

        .login-header p {
            color: #666;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: bold;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            outline: none;
            transition: border-color 0.3s;
        }

        .form-group input:focus {
            border-color: #667eea;
        }

        .btn {
            width: 100%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px;
            border-radius: 8px;
            font-size: 1em;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            margin-top: 10px;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
        }

        .status {
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 15px;
            display: none;
        }

        .status.success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status.error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .status.show {
            display: block;
        }

        .switch-form {
            text-align: center;
            margin-top: 20px;
            color: #666;
        }

        .switch-form a {
            color: #667eea;
            text-decoration: none;
            cursor: pointer;
        }

        .switch-form a:hover {
            text-decoration: underline;
        }

        .tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 2px solid #e0e0e0;
        }

        .tab {
            flex: 1;
            padding: 10px;
            text-align: center;
            cursor: pointer;
            color: #666;
            border-bottom: 2px solid transparent;
            transition: all 0.3s;
        }

        .tab.active {
            color: #667eea;
            border-bottom-color: #667eea;
            font-weight: bold;
        }

        .form-container {
            display: none;
        }

        .form-container.active {
            display: block;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>🤖 智能体对话系统</h1>
            <p>欢迎使用</p>
        </div>

        <div class="tabs">
            <div class="tab active" onclick="switchTab('login')">登录</div>
            <div class="tab" onclick="switchTab('register')">注册</div>
        </div>

        <div id="status" class="status"></div>

        <!-- 登录表单 -->
        <div id="loginForm" class="form-container active">
            <div class="form-group">
                <label>用户名</label>
                <input type="text" id="loginUsername" placeholder="请输入用户名">
            </div>
            <div class="form-group">
                <label>密码</label>
                <input type="password" id="loginPassword" placeholder="请输入密码">
            </div>
            <button class="btn" onclick="login()">登录</button>
        </div>

        <!-- 注册表单 -->
        <div id="registerForm" class="form-container">
            <div class="form-group">
                <label>用户名</label>
                <input type="text" id="registerUsername" placeholder="请输入用户名">
            </div>
            <div class="form-group">
                <label>密码</label>
                <input type="password" id="registerPassword" placeholder="请输入密码">
            </div>
            <div class="form-group">
                <label>邮箱（可选）</label>
                <input type="email" id="registerEmail" placeholder="请输入邮箱">
            </div>
            <div class="form-group">
                <label>手机号（可选）</label>
                <input type="tel" id="registerPhone" placeholder="请输入手机号">
            </div>
            <button class="btn" onclick="register()">注册</button>
        </div>
    </div>

    <script>
        const USER_SERVICE_URL = 'http://localhost:8084';

        function switchTab(tab) {
            // 切换标签
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.form-container').forEach(f => f.classList.remove('active'));
            
            if (tab === 'login') {
                document.querySelector('.tab:first-child').classList.add('active');
                document.getElementById('loginForm').classList.add('active');
            } else {
                document.querySelector('.tab:last-child').classList.add('active');
                document.getElementById('registerForm').classList.add('active');
            }
            
            // 清空状态
            document.getElementById('status').classList.remove('show');
        }

        async function login() {
            const username = document.getElementById('loginUsername').value.trim();
            const password = document.getElementById('loginPassword').value.trim();

            if (!username || !password) {
                showStatus('请输入用户名和密码', 'error');
                return;
            }

            try {
                const response = await fetch(`${USER_SERVICE_URL}/user/login`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ username, password })
                });

                const result = await response.json();

                if (result.code === 200) {
                    // 保存token和用户信息
                    localStorage.setItem('token', result.data.token);
                    localStorage.setItem('username', result.data.username);
                    localStorage.setItem('userId', result.data.userId);
                    
                    showStatus('登录成功，正在跳转...', 'success');
                    
                    // 跳转到主页面
                    setTimeout(() => {
                        window.location.href = result.data.redirectUrl || '/';
                    }, 1000);
                } else {
                    showStatus(result.message || '登录失败', 'error');
                }
            } catch (error) {
                showStatus('登录失败: ' + error.message, 'error');
            }
        }

        async function register() {
            const username = document.getElementById('registerUsername').value.trim();
            const password = document.getElementById('registerPassword').value.trim();
            const email = document.getElementById('registerEmail').value.trim();
            const phone = document.getElementById('registerPhone').value.trim();

            if (!username || !password) {
                showStatus('请输入用户名和密码', 'error');
                return;
            }

            try {
                const response = await fetch(`${USER_SERVICE_URL}/user/register`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ username, password, email, phone })
                });

                const result = await response.json();

                if (result.code === 200) {
                    showStatus('注册成功，请登录', 'success');
                    // 切换到登录标签
                    setTimeout(() => {
                        switchTab('login');
                        document.getElementById('loginUsername').value = username;
                    }, 1000);
                } else {
                    showStatus(result.message || '注册失败', 'error');
                }
            } catch (error) {
                showStatus('注册失败: ' + error.message, 'error');
            }
        }

        function showStatus(message, type) {
            const statusDiv = document.getElementById('status');
            statusDiv.textContent = message;
            statusDiv.className = `status ${type} show`;
        }

        // 检查是否已登录
        if (localStorage.getItem('token')) {
            window.location.href = '/';
        }
    </script>
</body>
</html>
            """;
    }
}

