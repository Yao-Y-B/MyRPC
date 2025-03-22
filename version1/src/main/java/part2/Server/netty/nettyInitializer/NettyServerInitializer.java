package part2.Server.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;
import part2.Server.netty.handler.NettyServerHandler;
import part2.Server.provider.ServiceProvider;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //初始化，每个SocketChannel都有独立的管道，用于定义数据的处理流程
        ChannelPipeline pipeline = ch.pipeline();
        /*
         * 消息格式【长度】【消息体】
         * 参数：
         * Integer.MAX_VALUE: 最大帧长度
         * 0,4 ：长度字段的起始位置和长度
         * 0,4： 去掉长度后实际数据的偏移量
         * */
        //帧解码：为了解决粘包和拆包问题：解决数据包边界不明确时。
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

        //LengthFieldPrepender：在发送数据前，自动计算消息体的长度并将其加到消息的前面，
        // 消息长度作为4字节的字段
        pipeline.addLast(new LengthFieldPrepender(4));

        //将Java对象转换为字节流的编码器（使用默认序列化）
        pipeline.addLast(new ObjectEncoder());

        //解码器
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        //最后将处理类加入容器
        pipeline.addLast(new NettyServerHandler(serviceProvider));

    }
}
