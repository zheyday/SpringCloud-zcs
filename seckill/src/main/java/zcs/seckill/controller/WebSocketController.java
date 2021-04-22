package zcs.seckill.controller;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/groupChat/{sid}/{username}")
public class WebSocketController {
    private static ConcurrentHashMap<String, WebSocketController> sessionMap = new ConcurrentHashMap<>();
    public  Session session;

    //    建立连接 新成员加入
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        sessionMap.put(sid,this);

    }

    //    接收信息
    @OnMessage
    public void onMessage(@PathParam("sid") String sid, @PathParam("username") String userName, String message) {
//        List<Session> sessionList = groupInfoMap.get(sid);
//        sessionList.forEach(item -> {
//            String text = userName + ":" + message;
//            try {
//                item.getBasicRemote().sendText(text);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        sessionMap.remove(sid);
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println(error);
    }

    public void sendMessage(String mes){
        try {
            this.session.getBasicRemote().sendText(mes);
            System.out.println(mes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendInfo(String mes,String sid){
        WebSocketController webSocketController = sessionMap.get(sid);
        if (webSocketController!=null)
            webSocketController.sendMessage(mes);
    }
}
