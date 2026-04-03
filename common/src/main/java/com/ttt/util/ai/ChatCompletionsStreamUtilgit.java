package com.ttt.util.ai;

import com.ttt.util.image.ImageUtil;
import com.volcengine.ark.runtime.exception.ArkHttpException;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.List;


public class ChatCompletionsStreamUtilgit {
    public static void main(String[] args) {

        String apiKey = System.getenv("ARK_API_KEY");
        ArkService service = ArkService.builder().apiKey(apiKey).build();

        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是一个专业的营养分析管理系统 AI 助手。你的任务是：\n" +
                "\n" +
               // "1.如果接收到菜名，准确分析出食材列表、过敏成分、热量、蛋白质、脂肪、碳水化合物、纤维素、维生素、矿物质。\n" +
                "2.如果接收到上传的图片时（图片的url或base64编码），你自己去获取到对应图片,然后分析出 菜品名称\n"+
                //"3.根据用户输入的健康情况（如是否有特定疾病、饮食禁忌、营养需求等）、商家上架菜品信息以及用户喜好（如喜欢的口味、食材偏好等），进行菜品推荐和菜品计划安排。推荐的菜品需明确菜名，菜品计划安排要清晰规划出早餐、午餐、晚餐和小吃的具体菜品搭配。\n" +
                "https://www.familydoctor.com.cn/yinshi/sck/food.html,你可以到这个网站上进行搜索\n"+
                "所有输出结果必须严格遵循 JSON 格式，确保数据准确且易于保存。").build();
        //final ChatMessage streamUserPhotoMessage=ChatMessage.builder().role(ChatMessageRole.USER).content(ImageUtil.getImageContentByPathBase("E:/ssm/shixun/imag/微信图片_20250603173358.png")).build();
        final ChatMessage streamUserPhotoMessage=ChatMessage.builder().role(ChatMessageRole.USER).content(ImageUtil.getImageContentByPath("https://img.familydoctor.com.cn/uploadimg/kuweb/2017/01/13/12/95cfb89759010000ab7059b8162f0000.jpeg")).build();
        //final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content("番茄炒蛋").build();
        streamMessages.add(streamSystemMessage);
        streamMessages.add(streamUserPhotoMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model("doubao-1.5-vision-lite-250315") // 替换 <MODEL> 为模型的Model ID , 查询Model ID：https://www.volcengine.com/docs/82379/1330310
                .messages(streamMessages)
                .build();

        try {
            service.streamChatCompletion(streamChatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .blockingForEach(
                            choice -> {
                                if (choice.getChoices().size() > 0) {
                                    System.out.print(choice.getChoices().get(0).getMessage().getContent());
                                }
                            }
                    );
        } catch (ArkHttpException e) {
            System.out.print(e.toString());
        }

        // shutdown service
        service.shutdownExecutor();
    }

}