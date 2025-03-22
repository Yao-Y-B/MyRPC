package part2.Client;

import part2.Client.proxy.ClientProxy;
import part2.common.pojo.User;
import part2.common.service.UserService;

public class TestClient {
    public static void main(String[] args){
        //创建ClientProxy对象： 初始化ClientProxy对象，连接到指定服务器地址和端口。
        //加入Netty后，choose参数换一下试试
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 9999, 0);

        //System.out.println("rpcClient in proxy:" + clientProxy.);
        //客户端动态生成UserService的代理对象
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user= " + user.toString());

        User u = User.builder().id(100).userName("彭誉萱xiao p").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入User的id" + id);
    }
}
