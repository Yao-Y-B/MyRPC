package part2.Client.rpcClient.Impl;

import part2.common.Message.RpcResponse;
import part2.common.Message.RpcRequest;
import part2.Client.rpcClient.RpcClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleSocketRpcClient implements RpcClient {
    private String host; //主机地址
    private int port; //端口号
    public SimpleSocketRpcClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        try{
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(rpcRequest);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
