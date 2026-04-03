package com.graduatedesign.cozeproxyservice.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 首页控制器
 * 提供前端页面
 */
@RestController
public class IndexController {

    /**
     * 根路径，返回前端页面
     */
    @GetMapping("/")
    public ResponseEntity<String> index() {
        try {
            // 尝试从 resources/static 目录读取前端HTML文件
            Resource resource = new ClassPathResource("static/index.html");
            if (resource.exists()) {
                String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_HTML);
                return new ResponseEntity<>(html, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            // 如果文件不存在，返回内嵌的HTML
        }
        
        // 如果静态文件不存在，返回内嵌的HTML（从frontend/index.html复制的内容）
        String html = getEmbeddedHtml();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(html, headers, HttpStatus.OK);
    }
    
    /**
     * 返回内嵌的前端HTML内容
     */
    private String getEmbeddedHtml() {
        // 这里返回完整的前端HTML内容
        // 由于内容很长，我们使用文本块来存储
        return """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>智能体对话系统</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }

        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
        }

        .header p {
            opacity: 0.9;
            font-size: 1.1em;
        }

        .main-content {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            padding: 30px;
        }

        @media (max-width: 968px) {
            .main-content {
                grid-template-columns: 1fr;
            }
        }

        .section {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .section h2 {
            color: #667eea;
            margin-bottom: 20px;
            font-size: 1.5em;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }

        .upload-area {
            border: 3px dashed #667eea;
            border-radius: 10px;
            padding: 30px;
            text-align: center;
            background: white;
            cursor: pointer;
            transition: all 0.3s;
            margin-bottom: 20px;
        }

        .upload-area:hover {
            background: #f0f4ff;
            border-color: #764ba2;
        }

        .upload-area.dragover {
            background: #e8f0fe;
            border-color: #764ba2;
        }

        .upload-icon {
            font-size: 3em;
            color: #667eea;
            margin-bottom: 10px;
        }

        #fileInput {
            display: none;
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1em;
            transition: transform 0.2s, box-shadow 0.2s;
            margin: 5px;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn:active {
            transform: translateY(0);
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-danger {
            background: #dc3545;
        }

        .btn-success {
            background: #28a745;
        }

        .file-list {
            max-height: 300px;
            overflow-y: auto;
            margin-top: 15px;
        }

        .file-item {
            background: white;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 8px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .file-item:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .file-info {
            flex: 1;
        }

        .file-name {
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }

        .file-meta {
            font-size: 0.85em;
            color: #666;
        }

        .file-actions {
            display: flex;
            gap: 5px;
        }

        .chat-container {
            display: flex;
            flex-direction: column;
            height: 500px;
        }

        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 15px;
            background: white;
            border-radius: 10px;
            margin-bottom: 15px;
            border: 1px solid #e0e0e0;
        }

        .message {
            margin-bottom: 15px;
            padding: 12px;
            border-radius: 10px;
            max-width: 80%;
            word-wrap: break-word;
        }

        .message.user {
            background: #667eea;
            color: white;
            margin-left: auto;
            text-align: right;
        }

        .message.assistant {
            background: #f0f0f0;
            color: #333;
        }

        .chat-input-area {
            display: flex;
            gap: 10px;
        }

        .chat-input {
            flex: 1;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            outline: none;
            transition: border-color 0.3s;
        }

        .chat-input:focus {
            border-color: #667eea;
        }

        .file-selector {
            margin-bottom: 15px;
        }

        .file-selector select {
            width: 100%;
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            background: white;
        }

        .loading {
            display: inline-block;
            width: 20px;
            height: 20px;
            border: 3px solid rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            border-top-color: white;
            animation: spin 1s ease-in-out infinite;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
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

        .empty-state {
            text-align: center;
            padding: 40px;
            color: #999;
        }

        .empty-state-icon {
            font-size: 4em;
            margin-bottom: 10px;
        }

        .user-info {
            position: absolute;
            top: 20px;
            right: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
            color: white;
        }

        .user-info span {
            font-size: 0.9em;
        }

        .logout-btn {
            background: rgba(255, 255, 255, 0.2);
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.3);
            padding: 5px 15px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 0.85em;
            transition: background 0.3s;
        }

        .logout-btn:hover {
            background: rgba(255, 255, 255, 0.3);
        }

        .header {
            position: relative;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="user-info" id="userInfo" style="display: none;">
                <span>欢迎, <strong id="usernameDisplay"></strong></span>
                <button class="logout-btn" onclick="logout()">退出登录</button>
            </div>
            <h1>🤖 智能体对话系统</h1>
            <p>上传文件并与智能体对话，基于文件内容获取答案</p>
        </div>

        <div class="main-content">
            <!-- 左侧：文件管理 -->
            <div class="section">
                <h2>📁 文件管理</h2>
                
                <div id="uploadStatus" class="status"></div>

                <div class="upload-area" id="uploadArea">
                    <div class="upload-icon">📤</div>
                    <p>点击或拖拽文件到此处上传</p>
                    <p style="font-size: 0.9em; color: #666; margin-top: 10px;">支持文本文件 (.txt, .md, .json 等)</p>
                    <input type="file" id="fileInput" accept=".txt,.md,.json,.xml,.html,.css,.js,.java,.py,.yaml,.yml,.properties,.log,.csv">
                </div>

                <button class="btn btn-secondary" onclick="loadFileList()">🔄 刷新文件列表</button>

                <div class="file-list" id="fileList">
                    <div class="empty-state">
                        <div class="empty-state-icon">📂</div>
                        <p>暂无文件</p>
                    </div>
                </div>
            </div>

            <!-- 右侧：对话区域 -->
            <div class="section">
                <h2>💬 智能对话</h2>

                <div class="file-selector">
                    <label style="display: block; margin-bottom: 5px; font-weight: bold; color: #667eea;">
                        选择文件（可选，用于基于文件的问答）
                    </label>
                    <select id="fileSelector">
                        <option value="">不使用文件（普通对话）</option>
                    </select>
                </div>

                <div class="chat-container">
                    <div class="chat-messages" id="chatMessages">
                        <div class="message assistant">
                            <strong>智能体：</strong> 你好！我是你的智能助手。你可以直接问我问题，或者先上传文件，然后基于文件内容提问。
                        </div>
                    </div>

                    <div class="chat-input-area">
                        <input 
                            type="text" 
                            id="chatInput" 
                            class="chat-input" 
                            placeholder="输入你的问题..."
                            onkeypress="handleKeyPress(event)"
                        >
                        <button class="btn" onclick="sendMessage()" id="sendBtn">发送</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE_URL = window.location.origin;
        const USER_SERVICE_URL = 'http://localhost:8084';

        // 初始化
        document.addEventListener('DOMContentLoaded', function() {
            // 检查登录状态
            checkLoginStatus();
            loadFileList();
            setupFileUpload();
        });

        // 检查登录状态
        async function checkLoginStatus() {
            const token = localStorage.getItem('token');
            if (!token) {
                // 未登录，跳转到登录页面
                window.location.href = '/login';
                return;
            }

            // 验证token
            try {
                const response = await fetch(`${USER_SERVICE_URL}/user/validate?token=${encodeURIComponent(token)}`);
                const result = await response.json();

                if (result.code === 200) {
                    // 显示用户信息
                    document.getElementById('usernameDisplay').textContent = result.data.username;
                    document.getElementById('userInfo').style.display = 'flex';
                } else {
                    // token无效，清除并跳转登录
                    localStorage.removeItem('token');
                    localStorage.removeItem('username');
                    localStorage.removeItem('userId');
                    window.location.href = '/login';
                }
            } catch (error) {
                console.error('验证token失败:', error);
                // 验证失败，跳转登录
                window.location.href = '/login';
            }
        }

        // 退出登录
        function logout() {
            if (confirm('确定要退出登录吗？')) {
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('userId');
                window.location.href = '/login';
            }
        }

        // 设置文件上传
        function setupFileUpload() {
            const uploadArea = document.getElementById('uploadArea');
            const fileInput = document.getElementById('fileInput');

            uploadArea.addEventListener('click', () => fileInput.click());
            
            uploadArea.addEventListener('dragover', (e) => {
                e.preventDefault();
                uploadArea.classList.add('dragover');
            });

            uploadArea.addEventListener('dragleave', () => {
                uploadArea.classList.remove('dragover');
            });

            uploadArea.addEventListener('drop', (e) => {
                e.preventDefault();
                uploadArea.classList.remove('dragover');
                const files = e.dataTransfer.files;
                if (files.length > 0) {
                    uploadFile(files[0]);
                }
            });

            fileInput.addEventListener('change', (e) => {
                if (e.target.files.length > 0) {
                    uploadFile(e.target.files[0]);
                }
            });
        }

        // 上传文件
        async function uploadFile(file) {
            const formData = new FormData();
            formData.append('file', file);

            const statusDiv = document.getElementById('uploadStatus');
            statusDiv.className = 'status';
            statusDiv.textContent = '上传中...';

            try {
                const response = await fetch(`${API_BASE_URL}/file/upload`, {
                    method: 'POST',
                    body: formData
                });

                const result = await response.json();

                if (result.code === 200) {
                    statusDiv.className = 'status success show';
                    statusDiv.textContent = `✅ 文件上传成功: ${result.data.fileName}`;
                    loadFileList();
                    setTimeout(() => {
                        statusDiv.classList.remove('show');
                    }, 3000);
                } else {
                    throw new Error(result.message || '上传失败');
                }
            } catch (error) {
                statusDiv.className = 'status error show';
                statusDiv.textContent = `❌ 上传失败: ${error.message}`;
            }
        }

        // 加载文件列表
        async function loadFileList() {
            try {
                const response = await fetch(`${API_BASE_URL}/file/list`);
                const result = await response.json();

                if (result.code === 200) {
                    displayFileList(result.data.files || []);
                    updateFileSelector(result.data.files || []);
                } else {
                    throw new Error(result.message || '获取文件列表失败');
                }
            } catch (error) {
                console.error('加载文件列表失败:', error);
                document.getElementById('fileList').innerHTML = 
                    `<div class="empty-state"><p>❌ 加载失败: ${error.message}</p></div>`;
            }
        }

        // 显示文件列表
        function displayFileList(files) {
            const fileListDiv = document.getElementById('fileList');
            
            if (files.length === 0) {
                fileListDiv.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon">📂</div>
                        <p>暂无文件</p>
                    </div>
                `;
                return;
            }

            fileListDiv.innerHTML = files.map(file => `
                <div class="file-item">
                    <div class="file-info">
                        <div class="file-name">${escapeHtml(file.fileName)}</div>
                        <div class="file-meta">
                            ${formatFileSize(file.fileSize)} • 
                            ${new Date(file.lastModified).toLocaleString('zh-CN')}
                        </div>
                    </div>
                    <div class="file-actions">
                        <button class="btn btn-success" onclick="viewFileContent('${file.fileName}')" title="查看内容">👁️</button>
                        <button class="btn btn-danger" onclick="deleteFile('${file.fileName}')" title="删除">🗑️</button>
                    </div>
                </div>
            `).join('');
        }

        // 更新文件选择器
        function updateFileSelector(files) {
            const selector = document.getElementById('fileSelector');
            selector.innerHTML = '<option value="">不使用文件（普通对话）</option>';
            
            files.forEach(file => {
                const option = document.createElement('option');
                option.value = file.fileName;
                option.textContent = file.fileName;
                selector.appendChild(option);
            });
        }

        // 查看文件内容
        async function viewFileContent(fileName) {
            try {
                const encodedFileName = encodeURIComponent(fileName);
                const response = await fetch(`${API_BASE_URL}/file/content?fileName=${encodedFileName}`);
                const result = await response.json();

                if (result.code === 200) {
                    const content = result.data.content;
                    const preview = content.length > 500 ? content.substring(0, 500) + '...' : content;
                    alert(`文件内容预览:\\n\\n${preview}${content.length > 500 ? '\\n\\n(内容已截断，完整内容请通过API获取)' : ''}`);
                } else {
                    throw new Error(result.message || '获取文件内容失败');
                }
            } catch (error) {
                alert(`获取文件内容失败: ${error.message}`);
            }
        }

        // 删除文件
        async function deleteFile(fileName) {
            if (!confirm(`确定要删除文件 "${fileName}" 吗？`)) {
                return;
            }

            try {
                const encodedFileName = encodeURIComponent(fileName);
                const response = await fetch(`${API_BASE_URL}/file/${encodedFileName}`, {
                    method: 'DELETE'
                });

                const result = await response.json();

                if (result.code === 200) {
                    alert('✅ 文件删除成功');
                    loadFileList();
                } else {
                    throw new Error(result.message || '删除失败');
                }
            } catch (error) {
                alert(`删除文件失败: ${error.message}`);
            }
        }

        // 发送消息
        async function sendMessage() {
            const input = document.getElementById('chatInput');
            const message = input.value.trim();
            const selectedFile = document.getElementById('fileSelector').value;

            if (!message) {
                return;
            }

            // 显示用户消息
            addMessage('user', message);
            input.value = '';

            // 显示加载状态
            const loadingId = addMessage('assistant', '正在思考...', true);
            const sendBtn = document.getElementById('sendBtn');
            sendBtn.disabled = true;
            sendBtn.innerHTML = '<span class="loading"></span>';

            try {
                const requestBody = {
                    message: message
                };

                if (selectedFile) {
                    requestBody.fileName = selectedFile;
                }

                const response = await fetch(`${API_BASE_URL}/coze/chat`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(requestBody)
                });

                const result = await response.json();

                // 移除加载消息
                removeMessage(loadingId);

                if (result.code === 200) {
                    addMessage('assistant', result.data);
                } else {
                    addMessage('assistant', `❌ 错误: ${result.message || '未知错误'}`);
                }
            } catch (error) {
                removeMessage(loadingId);
                addMessage('assistant', `❌ 请求失败: ${error.message}`);
            } finally {
                sendBtn.disabled = false;
                sendBtn.textContent = '发送';
            }
        }

        // 添加消息
        function addMessage(role, content, isLoading = false) {
            const messagesDiv = document.getElementById('chatMessages');
            const messageId = 'msg-' + Date.now();
            const messageDiv = document.createElement('div');
            messageDiv.id = messageId;
            messageDiv.className = `message ${role}`;
            
            if (role === 'assistant') {
                messageDiv.innerHTML = `<strong>智能体：</strong> ${escapeHtml(content)}`;
            } else {
                messageDiv.innerHTML = `<strong>你：</strong> ${escapeHtml(content)}`;
            }

            if (isLoading) {
                messageDiv.innerHTML += ' <span class="loading"></span>';
            }

            messagesDiv.appendChild(messageDiv);
            messagesDiv.scrollTop = messagesDiv.scrollHeight;

            return messageId;
        }

        // 移除消息
        function removeMessage(messageId) {
            const messageDiv = document.getElementById(messageId);
            if (messageDiv) {
                messageDiv.remove();
            }
        }

        // 处理回车键
        function handleKeyPress(event) {
            if (event.key === 'Enter') {
                sendMessage();
            }
        }

        // 工具函数
        function escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }

        function formatFileSize(bytes) {
            if (bytes === 0) return '0 B';
            const k = 1024;
            const sizes = ['B', 'KB', 'MB', 'GB'];
            const i = Math.floor(Math.log(bytes) / Math.log(k));
            return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
        }
    </script>
</body>
</html>
            """;
    }
}

