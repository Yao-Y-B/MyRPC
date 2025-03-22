package part2.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import part2.Server.provider.ServiceProvider;
import part2.common.Message.RpcRequest;
import part2.common.Message.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static part2.common.Message.RpcResponse.fail;
import static part2.common.Message.RpcResponse.success;

@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception{
        //调用服务
        RpcResponse response = getResponse(request);
        //将得到的响应返回给客户端：flush意味着消息会立即推送到网络层，不会缓存到本地
        ctx.channel().writeAndFlush(response);
        //ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //服务方法的具体实现
    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();
        //得到服务端相应的实现类
        Object service = serviceProvider.getService(interfaceName);
        if(service == null){
            System.out.println("Service not found for" + interfaceName);
            return RpcResponse.fail();
        }
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
