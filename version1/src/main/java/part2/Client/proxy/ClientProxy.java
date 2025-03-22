package part2.Client.proxy;

import lombok.AllArgsConstructor;
import part2.Client.rpcClient.Impl.NettyRpcClient;
import part2.Client.rpcClient.Impl.SimpleSocketRpcClient;
import part2.common.Message.RpcRequest;
import part2.common.Message.RpcResponse;
import part2.Client.rpcClient.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler{

    private RpcClient rpcClient;

    public ClientProxy(String host, int port, int choose){
        switch (choose){
            case 0:
                try {
                    rpcClient = new NettyRpcClient(host, port);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("NettyRpcClient initialization failed");
                }
                //System.out.println("NettyRpcClient initialized" + rpcClient);
                break;
            case 1:
                try {
                    rpcClient = new SimpleSocketRpcClient(host, port);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("SimpleSocketRpcClient initialization failed");
                }
                //System.out.println("SimpleSocketRpcClient initialized" + rpcClient);
                //break;
        }
    }

    public ClientProxy(String host, int port){
        rpcClient = new NettyRpcClient(host, port);
    }

    //jdk动态代理，每一次代理对象调用方法，都会通过此方法增强 （反射获取request对象， socket发送到服务端）
    //核心逻辑：用于封装请求并处理服务端响应
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())//获取服务类名
                .methodName(method.getName())//获取方法名
                .params(args)//获取参数
                .paramsType(method.getParameterTypes()).build();
        //与服务端进行通信
        RpcResponse response = rpcClient.sendRequest(request);
        //获取服务端返回的结果并且给调用者
        return response.getData();
    }

    //动态生成一个实现指定接口的代理对象？获得代理对象
    public <T>T getProxy(Class<T> clazz){
        //使用Proxy.newProxyInstance动态创建一个代理对象
        //传入类加载器，需要代理的类和调用处理程序
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        //System.out.println("Invoking method: "+ method.getName());
        return (T)o;
        //小屁是世界上最好的小屁，我最喜欢这个小屁，我爱你，我在看足球我也喜欢小屁，我是最好的小屁，我是最好的小屁,我爱小屁，小屁you are the best
    }

}

