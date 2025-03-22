package part2.Client.rpcClient;

import part2.common.Message.RpcRequest;
import part2.common.Message.RpcResponse;

//将共性抽取出来，通过request获取response
public interface RpcClient {

    RpcResponse sendRequest(RpcRequest rpcRequest);
}
