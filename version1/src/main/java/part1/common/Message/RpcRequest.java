package part1.common.Message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

//定义请求消息的字段
@Data
@Builder
public class RpcRequest implements Serializable {
    //服务类名(因为使用动态代理，外部给定信息是接口信息)
    private  String interfaceName;
    //方法名
    private  String methodName;
    //参数
    private  Object[] parameters;
    //参数类型
    private  Class<?>[] parameterTypes;
}
