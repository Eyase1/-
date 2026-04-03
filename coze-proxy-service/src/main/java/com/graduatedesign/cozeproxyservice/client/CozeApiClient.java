package com.graduatedesign.cozeproxyservice.client;

import com.graduatedesign.cozeproxyservice.config.CozeConfig;
import com.graduatedesign.cozeproxyservice.dto.CozeChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class CozeApiClient {

    private static final Logger log = LoggerFactory.getLogger(CozeApiClient.class);
    private static final String DEFAULT_COZE_API_URL = "https://api.coze.cn/open_api/v2/chat";  // 中国区API地址

    @Autowired(required = false)
    private CozeConfig cozeConfig;

    private final RestTemplate restTemplate;

    @Autowired
    public CozeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Coze智能体聊天接口
     * 调用真实的Coze API，返回智能体回复
     */
    public CozeChatResponse chatWithBot(String botId, String message, String userId) {
        log.info("接收到聊天请求: message={}, userId={}, botId={}", message, userId, botId);

        // 如果没有提供botId，使用默认值
        if (botId == null || botId.trim().isEmpty()) {
            botId = "general_assistant_bot";
            log.info("使用默认botId: {}", botId);
        }

        // 如果没有提供userId，生成一个临时ID
        if (userId == null || userId.trim().isEmpty()) {
            userId = "user_" + System.currentTimeMillis();
            log.info("生成临时userId: {}", userId);
        }

        try {
            // 调用真实的Coze API
            return callCozeApi(botId, message, userId);  // 移除 static 引用
        } catch (Exception e) {
            log.error("Coze API调用失败: {}", e.getMessage(), e);
            // 出错时返回备用响应
            CozeChatResponse fallbackResponse = new CozeChatResponse();
            fallbackResponse.setCode(1);
            fallbackResponse.setMessage("API调用失败: " + e.getMessage());
            fallbackResponse.setContent("抱歉，智能助手暂时无法响应，请稍后再试。");
            return fallbackResponse;
        }
    }

    /**
     * 调用真实的Coze API
     */
    private CozeChatResponse callCozeApi(String botId, String message, String userId) {  // 移除 static
        try {
            // 构建API URL
            String apiUrl = getApiUrl();  // 移除 static 引用
            String accessToken = getAccessToken();  // 移除 static 引用

            log.info("准备调用Coze API: url={}, botId={}", apiUrl, botId);

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // 确保token没有多余空格和换行符
            String cleanToken = accessToken != null ? accessToken.trim().replaceAll("\\s+", "") : "";

            // 验证token格式
            if (cleanToken.isEmpty() || !cleanToken.startsWith("pat_")) {
                log.error("Token格式错误或为空！token长度: {}, 前缀: {}",
                        cleanToken.length(),
                        cleanToken.length() > 3 ? cleanToken.substring(0, 3) : "null");
            }

            headers.set("Authorization", "Bearer " + cleanToken);
            headers.set("Accept", "*/*");

            log.info("使用的完整token长度: {}, token前缀: {}",
                    cleanToken.length(),
                    cleanToken.length() > 10 ? cleanToken.substring(0, 10) : "null");

            // 构建请求体（Coze API标准格式）
            Map<String, Object> requestBody = new HashMap<>();

            // 生成或使用固定的conversation_id（用于多轮对话）
            String conversationId = userId + "_conversation"; // 基于userId生成固定的conversation_id

            requestBody.put("conversation_id", conversationId);
            requestBody.put("bot_id", String.valueOf(botId));
            requestBody.put("user", String.valueOf(userId)); // 注意：是user不是user_id
            requestBody.put("query", message); // 注意：是query不是messages数组
            requestBody.put("stream", false); // 不使用流式响应

            // 记录完整请求体用于调试（使用JSON格式）
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                String requestBodyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
                log.info("完整请求体JSON:\n{}", requestBodyJson);
            } catch (Exception e) {
                log.warn("无法格式化请求体: {}", e.getMessage());
            }

            log.info("构建的请求体: bot_id={}, user_id={}, message={}", botId, userId, message);

            // 构建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            log.debug("发送请求: {}", requestBody);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            // 记录详细日志
            logApiDetails(requestBody, responseEntity);  // 移除 static 引用

            // 处理响应
            return handleApiResponse(responseEntity);  // 移除 static 引用
        } catch (Exception e) {
            log.error("API调用异常: {}, 堆栈: {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            // 抛出异常让上层捕获
            throw new RuntimeException("Coze API调用失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从配置中获取API URL
     */
    private String getApiUrl() {  // 移除 static
        try {
            if (cozeConfig != null && cozeConfig.getApi() != null) {
                if (cozeConfig.getApi().getBaseUrl() != null && !cozeConfig.getApi().getBaseUrl().isEmpty()) {
                    String url = cozeConfig.getApi().getBaseUrl();
                    // 确保URL以斜杠结尾
                    if (!url.endsWith("/")) {
                        url += "/";
                    }
                    return url + "open_api/v2/chat";
                }
            }
        } catch (Exception e) {
            log.error("获取API URL失败: {}", e.getMessage());
        }
        return DEFAULT_COZE_API_URL;
    }

    /**
     * 从配置中获取访问令牌
     */
    private String getAccessToken() {  // 移除 static
        String token = null;

        // 优先从CozeConfig获取
        if (cozeConfig != null && cozeConfig.getApi() != null &&
                cozeConfig.getApi().getAccessToken() != null && !cozeConfig.getApi().getAccessToken().isEmpty()) {
            token = cozeConfig.getApi().getAccessToken();
            log.debug("从CozeConfig获取到token: {}...", token != null && token.length() > 10 ? token.substring(0, 10) : "null");
        }

        // 如果配置中没有，使用默认值（仅用于调试，实际应该配置）
        if (token == null || token.isEmpty()) {
            log.warn("未从配置中获取到token，使用默认值（可能无效）");
            token = "pat_1v5hLJB4mAifs044LlYVtfHp7pbYSPmJ5ZCalXQN9701vBmngPGdVToivHbBuGUn";
        }

        // 记录实际使用的token（只显示前10个字符，保护隐私）
        if (token != null && token.length() > 10) {
            log.info("使用的token前缀: {}...", token.substring(0, 10));
        } else {
            log.warn("Token为空或格式异常");
        }

        return token;
    }

    /**
     * 处理API响应
     */
    private CozeChatResponse handleApiResponse(ResponseEntity<Map> responseEntity) {  // 移除 static
        CozeChatResponse response = new CozeChatResponse();

        // 检查响应状态
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null) {
                // 记录完整响应体用于调试
                log.info("收到Coze API响应: {}", responseBody);

                // 首先检查code字段（Coze API使用code字段，0表示成功）
                Object codeObj = responseBody.get("code");
                int code = 0;
                if (codeObj != null) {
                    code = codeObj instanceof Number ? ((Number) codeObj).intValue() : 0;
                }

                log.info("Coze API响应code: {}", code);

                // 如果code不为0且不为200，说明有错误
                if (code != 0 && code != 200) {
                    String errorMsg = extractErrorMessage(responseBody);  // 移除 static 引用
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                        response.setCode(code);
                        response.setMessage(errorMsg);

                        // 针对4101错误码提供更详细的提示
                        String errorContent = "Coze API返回错误 (错误码: " + code + "): " + errorMsg;
                        if (code == 4101) {
                            errorContent += "\n\n可能的原因：\n" +
                                    "1. Token无效或已过期 - 请在Coze平台重新生成token\n" +
                                    "2. Token权限不足 - 请确保token有调用聊天API的权限\n" +
                                    "3. Token复制不完整 - 请完整复制token（从pat_开始到结尾）\n" +
                                    "4. 检查token是否在Coze平台的'个人访问令牌'中正确配置了API权限";
                        }

                        response.setContent(errorContent);
                        log.error("Coze API返回错误，错误码: {}, 错误信息: {}", code, errorMsg);
                        return response;
                    } else {
                        // 如果无法提取错误信息，使用默认消息
                        response.setCode(code);
                        response.setMessage("Coze API返回错误码: " + code);
                        response.setContent("Coze API返回错误码: " + code + "。请检查配置和网络连接。");
                        log.error("Coze API返回错误码: {}", code);
                        return response;
                    }
                }

                // 如果code为0或200，说明成功，尝试提取内容
                log.info("Coze API返回成功，开始提取内容...");
                String content = extractContent(responseBody);  // 移除 static 引用

                if (content != null && !content.isEmpty()) {
                    response.setContent(content);
                    response.setCode(0);
                    response.setMessage("success");
                    log.info("成功解析智能体回复: {}", content);
                    return response;
                }

                // 如果提取内容失败，但code是成功的，可能是响应格式不同
                log.warn("无法提取内容，但code表示成功。响应体: {}", responseBody);

                // 尝试从msg字段获取信息（如果msg不是"success"）
                Object msgObj = responseBody.get("msg");
                if (msgObj != null && !"success".equals(msgObj.toString())) {
                    String msg = msgObj.toString();
                    response.setContent(msg);
                    response.setCode(0);
                    response.setMessage("success");
                    log.info("从msg字段提取到内容: {}", msg);
                    return response;
                }

                // 未找到预期的响应格式，输出完整响应用于调试
                response.setCode(1);
                response.setMessage("无效的响应格式");
                // 将完整响应体转换为JSON字符串，方便调试
                String responseBodyStr = formatResponseBody(responseBody);  // 移除 static 引用
                response.setContent("无法解析智能体的回复。\n实际响应格式:\n" + responseBodyStr);
                log.warn("无效的API响应格式，完整响应: {}", responseBody);
            } else {
                response.setCode(1);
                response.setMessage("空响应");
                response.setContent("未收到智能体的回复。");
                log.warn("收到空的API响应");
            }
        } else {
            response.setCode(responseEntity.getStatusCode().value());
            response.setMessage("API调用失败: " + responseEntity.getStatusCode());
            response.setContent("智能助手服务暂时不可用。");
            log.error("API调用失败，状态码: {}", responseEntity.getStatusCode());
        }

        return response;
    }

    /**
     * 从响应中提取内容，支持多种响应格式
     */
    private String extractContent(Map<String, Object> responseBody) {  // 移除 static
        try {
            log.debug("开始解析响应内容，响应体: {}", responseBody);

            // 注意：code检查已在handleApiResponse中处理，这里不再重复检查

            // 格式1: Coze API实际格式 - 根级别的messages数组
            if (responseBody.containsKey("messages") && responseBody.get("messages") instanceof List) {
                List<?> messagesList = (List<?>) responseBody.get("messages");
                if (!messagesList.isEmpty()) {
                    // 优先查找type=answer的assistant消息（这是实际的回复）
                    for (int i = 0; i < messagesList.size(); i++) {
                        Object msgObj = messagesList.get(i);
                        if (msgObj instanceof Map) {
                            Map<String, Object> message = (Map<String, Object>) msgObj;
                            Object roleObj = message.get("role");
                            Object typeObj = message.get("type");

                            // 查找assistant角色且type为answer的消息
                            if ("assistant".equals(roleObj) && "answer".equals(typeObj) && message.containsKey("content")) {
                                Object contentObj = message.get("content");
                                if (contentObj != null) {
                                    String content = extractStringFromObject(contentObj);  // 移除 static 引用
                                    if (content != null && !content.trim().isEmpty()) {
                                        log.info("成功提取到智能体回复（type=answer）: {}", content);
                                        return content.trim();
                                    }
                                }
                            }
                        }
                    }

                    // 如果没找到type=answer的消息，查找assistant角色的消息
                    for (int i = messagesList.size() - 1; i >= 0; i--) {
                        Object msgObj = messagesList.get(i);
                        if (msgObj instanceof Map) {
                            Map<String, Object> message = (Map<String, Object>) msgObj;
                            Object roleObj = message.get("role");
                            if (("assistant".equals(roleObj) || "bot".equals(roleObj)) && message.containsKey("content")) {
                                Object contentObj = message.get("content");
                                if (contentObj != null) {
                                    String content = extractStringFromObject(contentObj);  // 移除 static 引用
                                    if (content != null && !content.trim().isEmpty()) {
                                        log.info("成功提取到智能体回复（assistant角色）: {}", content);
                                        return content.trim();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 格式2: Coze标准格式 - data.messages[].content
            if (responseBody.containsKey("data")) {
                Object dataObj = responseBody.get("data");
                if (dataObj instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) dataObj;

                    // 2.1 尝试从messages数组中提取（Coze API标准格式）
                    if (data.containsKey("messages") && data.get("messages") instanceof List) {
                        List<?> messagesList = (List<?>) data.get("messages");
                        if (!messagesList.isEmpty()) {
                            // 从后往前查找assistant的消息
                            for (int i = messagesList.size() - 1; i >= 0; i--) {
                                Object msgObj = messagesList.get(i);
                                if (msgObj instanceof Map) {
                                    Map<String, Object> message = (Map<String, Object>) msgObj;
                                    // 查找assistant角色的消息
                                    Object roleObj = message.get("role");
                                    if (("assistant".equals(roleObj) || "bot".equals(roleObj)) && message.containsKey("content")) {
                                        Object contentObj = message.get("content");
                                        if (contentObj != null) {
                                            String content = extractStringFromObject(contentObj);  // 移除 static 引用
                                            if (content != null && !content.trim().isEmpty()) {
                                                log.info("从data.messages提取到智能体回复: {}", content);
                                                return content.trim();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 1.2 尝试直接从data中获取content
                    if (data.containsKey("content")) {
                        Object contentObj = data.get("content");
                        if (contentObj != null) {
                            String content = extractStringFromObject(contentObj);  // 移除 static 引用
                            if (content != null && !content.isEmpty()) {
                                log.info("从data中提取到content: {}", content);
                                return content;
                            }
                        }
                    }

                    // 1.3 尝试从data中获取answer
                    if (data.containsKey("answer")) {
                        Object answerObj = data.get("answer");
                        if (answerObj != null) {
                            String answer = extractStringFromObject(answerObj);  // 移除 static 引用
                            if (answer != null && !answer.isEmpty()) {
                                log.info("从data中提取到answer: {}", answer);
                                return answer;
                            }
                        }
                    }

                    // 1.4 尝试从data中获取text
                    if (data.containsKey("text")) {
                        Object textObj = data.get("text");
                        if (textObj != null) {
                            String text = extractStringFromObject(textObj);  // 移除 static 引用
                            if (text != null && !text.isEmpty()) {
                                log.info("从data中提取到text: {}", text);
                                return text;
                            }
                        }
                    }
                } else if (dataObj instanceof String) {
                    // data可能是直接的字符串
                    String dataStr = (String) dataObj;
                    if (!dataStr.isEmpty()) {
                        log.info("data是直接字符串: {}", dataStr);
                        return dataStr;
                    }
                }
            }

            // 格式2: 直接包含content字段
            if (responseBody.containsKey("content")) {
                Object contentObj = responseBody.get("content");
                if (contentObj != null) {
                    String content = extractStringFromObject(contentObj);  // 移除 static 引用
                    if (content != null && !content.isEmpty()) {
                        log.info("从根节点提取到content: {}", content);
                        return content;
                    }
                }
            }

            // 格式3: 包含text字段
            if (responseBody.containsKey("text")) {
                Object textObj = responseBody.get("text");
                if (textObj != null) {
                    String text = extractStringFromObject(textObj);  // 移除 static 引用
                    if (text != null && !text.isEmpty()) {
                        log.info("提取到text: {}", text);
                        return text;
                    }
                }
            }

            // 格式4: 包含answer字段
            if (responseBody.containsKey("answer")) {
                Object answerObj = responseBody.get("answer");
                if (answerObj != null) {
                    String answer = extractStringFromObject(answerObj);  // 移除 static 引用
                    if (answer != null && !answer.isEmpty()) {
                        log.info("提取到answer: {}", answer);
                        return answer;
                    }
                }
            }

            log.warn("无法从响应中提取内容，尝试的格式都不匹配");

        } catch (Exception e) {
            log.error("提取内容时发生异常: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * 从对象中提取字符串，支持多种类型
     */
    private String extractStringFromObject(Object obj) {  // 移除 static
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        // 如果是其他类型，转换为字符串
        return obj.toString();
    }

    /**
     * 从响应中提取错误信息
     */
    private String extractErrorMessage(Map<String, Object> responseBody) {  // 移除 static
        try {
            // 优先检查msg字段（Coze API使用msg而不是message）
            if (responseBody.containsKey("msg")) {
                Object msgObj = responseBody.get("msg");
                if (msgObj != null) {
                    String msg = msgObj.toString();
                    if (!msg.isEmpty()) {
                        log.debug("从msg字段提取到错误信息: {}", msg);
                        return msg;
                    }
                }
            }

            // 检查message字段（备选）
            if (responseBody.containsKey("message")) {
                Object messageObj = responseBody.get("message");
                if (messageObj != null) {
                    String message = messageObj.toString();
                    if (!message.isEmpty()) {
                        log.debug("从message字段提取到错误信息: {}", message);
                        return message;
                    }
                }
            }

            // 检查error字段
            if (responseBody.containsKey("error")) {
                Object errorObj = responseBody.get("error");
                if (errorObj instanceof Map) {
                    Map<String, Object> error = (Map<String, Object>) errorObj;
                    if (error.containsKey("message")) {
                        return error.get("message").toString();
                    }
                    if (error.containsKey("msg")) {
                        return error.get("msg").toString();
                    }
                } else if (errorObj != null) {
                    return errorObj.toString();
                }
            }

            // 检查detail字段中的错误信息
            if (responseBody.containsKey("detail")) {
                Object detailObj = responseBody.get("detail");
                if (detailObj instanceof Map) {
                    Map<String, Object> detail = (Map<String, Object>) detailObj;
                    if (detail.containsKey("message")) {
                        return detail.get("message").toString();
                    }
                    if (detail.containsKey("msg")) {
                        return detail.get("msg").toString();
                    }
                }
            }

            // 检查data中的错误信息
            if (responseBody.containsKey("data")) {
                Object dataObj = responseBody.get("data");
                if (dataObj instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) dataObj;
                    if (data.containsKey("error")) {
                        return data.get("error").toString();
                    }
                    if (data.containsKey("msg")) {
                        return data.get("msg").toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error("提取错误信息时发生异常: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * 记录详细的API请求和响应日志
     */
    private void logApiDetails(Map<String, Object> requestBody, ResponseEntity<Map> responseEntity) {  // 移除 static
        log.info("========== Coze API 请求详情 ==========");
        log.info("请求URL: {}", getApiUrl());
        log.info("请求体: {}", requestBody);
        log.info("响应状态: {}", responseEntity.getStatusCode());
        log.info("响应头: {}", responseEntity.getHeaders());
        log.info("响应体: {}", responseEntity.getBody());
        log.info("========================================");
    }

    /**
     * 格式化响应体为可读的字符串
     */
    private String formatResponseBody(Map<String, Object> responseBody) {  // 移除 static
        try {
            // 使用ObjectMapper格式化JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBody);
        } catch (Exception e) {
            // 如果格式化失败，直接返回toString
            return responseBody.toString();
        }
    }
}