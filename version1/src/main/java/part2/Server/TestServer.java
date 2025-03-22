package part2.Server;

import part2.common.service.Impl.UserServiceImpl;
import part2.common.service.UserService;
//妈的，苦苦找的空指针错误是因为import错地方了
import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;
import part2.Server.server.impl.NettyRPCServer;
import part2.Server.server.impl.SimpleRPCServer;

public class TestServer {
    public static void main(String[] args){
        ServiceProvider serviceProvider = new ServiceProvider();
        UserService userService = new UserServiceImpl();

        serviceProvider.providerServiceInterface(userService);

        //System.out.println("Registered Services: " + serviceProvider.getService(UserService.class.getCanonicalName()));
        RpcServer rpcServer = new NettyRPCServer(serviceProvider);

        rpcServer.start(9999);
    }
}
