package part2.Client.rpcClient.Impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part2.Client.netty.handler.NettyClientHandler;
import part2.Client.netty.nettyInitializer.NettyClientInitializer;
import part2.Client.rpcClient.RpcClient;
import part2.common.Message.RpcRequest;
import part2.common.Message.RpcResponse;

import java.io.Serializable;

public class NettyRpcClient  implements RpcClient {
    private String host;
    private int port;

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    public NettyRpcClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    //Netty客户端初始化
    static{
        //bootstrap: Netty用于启动客户端的对象，负责设置与服务器的连接配置
        //eventLoopGroup: Netty用于处理I/O操作的线程池（Nio是基于NIO实现的）
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try{
            //创建channelFuture对象代表操作事件， sync方法表示堵塞直到connect完成
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //channel表示一个连接的单位，类似socket
            Channel channel = channelFuture.channel();
            //异步发送数据
            channel.writeAndFlush(request);
            //sync()堵塞获取结果:服务端返回结果后连接才会关闭
            channel.closeFuture().sync();
            //阻塞的获得结果是通过给channel设计别名来获取特定名字下的channel内容（在handler中设置）
            //AttributeKey是线程隔离的，不会有线程安全问题
            //当前场景：堵塞获取结果
            //其他场景：可以添加监听器来异步获取结果
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
