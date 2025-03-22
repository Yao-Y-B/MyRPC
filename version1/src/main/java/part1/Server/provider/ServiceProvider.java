package part1.Server.provider;

import java.util.HashMap;
import java.util.Map;
/*
* 类的作用：
* ServiceProvider提供简单方式来注册和获取服务：
* 将服务对象和接口名称进行简单映射
* 实现服务的本地注册和动态获取
* */

//存放本地的服务
public class ServiceProvider {
    //集合汇总存放服务的实例，
    // 接口的全限定名(String类型)，接口的完整路径名
    // 接口对应的实现类实例(Object类型)
    private Map<String, Object> interfaceProvider;
    //为interfaceProvider字段分配HashMap实例
    public ServiceProvider(){
        this.interfaceProvider = new HashMap<>();
    }
    //本地注册服务
    public void providerServiceInterface(Object service){
        //接受服务实例service
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for(Class<?> clazz: interfaceName){
            //遍历service实现的所有接口
            interfaceProvider.put(clazz.getName(),service);
        }
    }
    //获得服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
