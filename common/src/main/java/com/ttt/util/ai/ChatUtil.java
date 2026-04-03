package com.ttt.util.ai;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import com.coze.openapi.client.chat.*;
import com.coze.openapi.client.chat.model.Chat;
import com.coze.openapi.client.chat.model.ChatPoll;
import com.coze.openapi.client.chat.model.ChatStatus;
import com.coze.openapi.client.connversations.message.model.Message;
import com.coze.openapi.service.auth.TokenAuth;
import com.coze.openapi.service.config.Consts;
import com.coze.openapi.service.service.CozeAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttt.req.ai.FoodAnalyzerReq;
import com.ttt.resp.ai.FoodAnalyzerResp;


public class ChatUtil {
  public static String getInformationByAi(FoodAnalyzerReq foodAnalyzerReq) throws Exception {
    String token = System.getenv("COZE_API_TOKEN");
    String botID = "7515752084990328832";
    String uid = "123";
    String content=foodAnalyzerReq.toString();
    TokenAuth authCli = new TokenAuth(token);

    CozeAPI coze =
        new CozeAPI.Builder()
            .baseURL(Consts.COZE_CN_BASE_URL)
            .auth(authCli)
            .readTimeout(10000)
            .build();

    CreateChatReq req =
        CreateChatReq.builder()
            .botID(botID)
            .userID(uid)
            .messages(Collections.singletonList(Message.buildUserQuestionText(content)))
            .build();

    CreateChatResp chatResp = coze.chat().create(req);
    System.out.println(chatResp);
    Chat chat = chatResp.getChat();
    String chatID = chat.getID();
    String conversationID = chat.getConversationID();

    long timeout = 10L;
    long start = System.currentTimeMillis() / 1000;
    while (ChatStatus.IN_PROGRESS.equals(chat.getStatus())) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }

      if ((System.currentTimeMillis() / 1000) - start > timeout) {
        System.out.println(coze.chat().cancel(CancelChatReq.of(conversationID, chatID)));
        break;
      }
      RetrieveChatResp resp = coze.chat().retrieve(RetrieveChatReq.of(conversationID, chatID));
      System.out.println(resp);
      chat = resp.getChat();
      if (ChatStatus.COMPLETED.equals(chat.getStatus())) {
        break;
      }
    }

    ChatPoll chat2 = coze.chat().createAndPoll(req);
    String result="";
    for(Message msg : chat2.getMessages()) {
      if(msg.getType().getValue().equals("answer")){
        result=msg.getContent();break;
      }
    }
    System.out.println(result);
    return result;
  }
}