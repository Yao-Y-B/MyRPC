package part2.Server.server.impl;

import lombok.AllArgsConstructor;
import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;
import part2.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port){
        try {
            //创建serversoket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动了");
            //服务器持续接受客户端的连接请求
            while(true){
                //如果没有连接，会堵塞在这里
                Socket socket = serverSocket.accept();
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        //停止服务端
        //优化停止流程
    }
}
