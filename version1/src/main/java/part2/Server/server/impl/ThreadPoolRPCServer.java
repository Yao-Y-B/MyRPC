package part2.Server.server.impl;

import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;
import part2.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRPCServer implements RpcServer {
    private final ThreadPoolExecutor threadPool;
    private ServiceProvider serviceProvider;
    //默认构造方法
    public ThreadPoolRPCServer(ServiceProvider serviceProvider){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }
    //自定义构造方法、
    public ThreadPoolRPCServer(ServiceProvider serviceProvider,
                               int maxCorePoolSize,
                               int maxPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue<Runnable> workQueue){
        threadPool = new ThreadPoolExecutor(maxCorePoolSize,maxPoolSize,keepAliveTime,unit,workQueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public  void start(int port){
        System.out.println("服务端启动了");
        try{
            //创建serversocket
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket,serviceProvider));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void stop(){

    }
}
