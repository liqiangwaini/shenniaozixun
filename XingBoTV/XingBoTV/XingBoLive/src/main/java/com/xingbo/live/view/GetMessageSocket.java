package com.xingbo.live.view;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class GetMessageSocket implements AsyncHttpClient.WebSocketConnectCallback,WebSocket.StringCallback,CompletedCallback{
    public static final int WEB_SOCKET_CONNECTING = 0;
    public static final int WEB_SOCKET_CLOSE = 1;
    public static final int WEB_SOCKET_OPEN = 2;

    private String socketUrl;
    private int webSocketState;
    public WebSocket socket;
    public Timer timer;
    public TimerTask timerTask;
    public boolean isDestroy=false;

    public GetMessageSocket(String socketUrl){
        this.socketUrl=socketUrl;
        initSocket();
    }

    //初始化socket链接
    private void initSocket() {
        if (socketUrl != null) {
            //连接聊天室
            webSocketState = WEB_SOCKET_CONNECTING;
            AsyncHttpClient.getDefaultInstance().websocket(socketUrl, "my-protocol", this);
        }
    }

    //webSocket对接完成
    @Override
    public void onCompleted(Exception ex, WebSocket webSocket) {
        if (ex != null) {
            ex.printStackTrace();
            webSocketState = WEB_SOCKET_CLOSE;
            return;
        }
        socket = webSocket;
        webSocket.setStringCallback(this);
        webSocket.setClosedCallback(this);
        webSocketState = WEB_SOCKET_OPEN;
    }

    //webSocket关闭
    @Override
    public void onCompleted(Exception e) {
        webSocketState = WEB_SOCKET_CLOSE;
    }

    @Override
    public void onStringAvailable(String msg) {
        callback.getSocketMessage(msg);
    }

    //定时检查WebSocket状态
    public void checkWebSocketState() {
        if (timer == null) {
            timer = new Timer(true);
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!isDestroy && webSocketState == WEB_SOCKET_CLOSE) {
                        initSocket();
                    }
                }
            };
        }
        timer.schedule(timerTask, 10000, 10000);
    }

    public void destroy(){
        isDestroy = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();

            timerTask = null;
        }
        if (socket != null) {
            socket.close();
        }
        socketUrl = null;
        socket = null;
    }

    OnMessageCallback callback;
    public interface OnMessageCallback{
        public void getSocketMessage(String msg);
    }
    public void setOnSocketCallback(OnMessageCallback callback){
        this.callback=callback;
    }
}
