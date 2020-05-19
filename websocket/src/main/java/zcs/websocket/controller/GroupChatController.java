package zcs.websocket.controller;

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
public class GroupChatController {
    private static ConcurrentHashMap<String, List<Session>> groupInfoMap = new ConcurrentHashMap<>();

    //    建立连接 新成员加入
    @OnOpen
    public void onOpen(Session session,@PathParam("sid") String sid) {
        List<Session> sessionList = groupInfoMap.computeIfAbsent(sid, k -> new ArrayList<>());
        sessionList.add(session);
    }

    //    发送信息
    @OnMessage
    public void onMessage(@PathParam("sid") String sid, @PathParam("username") String userName, String message) {
        List<Session> sessionList = groupInfoMap.get(sid);
        sessionList.forEach(item->{
            String text=userName+":"+message;
            try {
                item.getBasicRemote().sendText(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        List<Session> sessionList = groupInfoMap.get(sid);
        sessionList.remove(session);
    }

    @OnError
    public void onError(Throwable error){
        System.out.println(error);
    }
}
