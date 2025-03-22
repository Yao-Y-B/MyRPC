package part2.Server.server.work;

//import jdk.jfr.internal.consumer.ObjectContext;
import lombok.AllArgsConstructor;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;
import part2.Server.provider.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;//网络连接
    private ServiceProvider serviceProvider;//本地服务注册中心

    @Override
    public void run(){
        try {
            //将响应数据（服务端返回的RpcResponse）通过网络连接发送给客户端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //从客户端的网络连接中接受数据，读取序列化的对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //读取客户传来的request
            RpcRequest request = (RpcRequest) ois.readObject();
            //反射调用服务方法获取返回值
            RpcResponse response = getResponse(request);
            //向客户端写入response
            oos.writeObject(response);
            oos.flush();
        }catch (IOException| ClassNotFoundException e){
            e.printStackTrace();

        }
    }

    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();
        //得到服务端相应的实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method = null;
        try{
            //获得方法的对象
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamsType());
            //通过反射调用方法
            Object invoke = method.invoke(service,rpcRequest.getParams());
            //封装响应对象并返回
            return RpcResponse.success(invoke);
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            System.out.println("方法执行出现错误");
            return RpcResponse.fail();
        }
    }
}
