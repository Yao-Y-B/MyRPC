package part2.Client;

import part2.common.Message.RpcRequest;
import part2.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
    //负责底层与服务端的通信，发送request，返回response
    public static RpcResponse sendRequest(String host, int port, RpcRequest request){
        try {
            //服务端的主机IP地址、端口号、请求对象
            Socket socket = new Socket(host, port);
            // 通过socket与服务端建立TCP连接
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // 通过ObjectOutputStream对象将请求对象序列化
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 通过ObjectInputStream对象将服务端返回的响应对象反序列化
            oos.writeObject(request);//将RpcRequest对象序列化并且发送给服务端
            oos.flush();
            //flush()方法用于刷新输出流，确保所有数据都被发送到服务端。
            //虽然OOS会自动刷新，但是flush()可以确保数据立即发送

            //从输入流中读取服务端返回的序列化对象并且反序列化为RpcResponse
            RpcResponse response = (RpcResponse) ois.readObject();
            return response;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }


    }
}
