package part2.Server.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import part2.Server.netty.nettyInitializer.NettyServerInitializer;
import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;

@AllArgsConstructor
public class NettyRPCServer implements RpcServer {
    //Netty中通过ServerBootstrap来监听请求
    private  ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        //netty中NioEvent线程池,boss建立连接，work处理请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.println("Netty服务端启动");
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //初始化
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceProvider));

            //同步堵塞
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
