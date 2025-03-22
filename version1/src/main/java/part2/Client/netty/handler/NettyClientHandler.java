package part2.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import part2.common.Message.RpcResponse;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    //核心方法：读取服务端返回的数据
//    channelRead0：是核心方法，处理接收到的消息，<RpcResponse>表示服务器返回的响应数据的类型
//    ctx.channel() 获取当前的Channel
//    AttributeKey.valueOf("RPCResponse")用来存储和检索Channel上的属性
//
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response){
        //接收response，给channel设计别名，让sendRequest里读取response
        //将服务端返回的RpcResponse绑定到当前Channel属性中，以便后续逻辑能够通过Channel获取
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(response);

        //读取完毕后，关闭channel
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
