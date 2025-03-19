package part1.common.service;

import part1.common.pojo.User;

//定义调用所需要的服务接口
public interface UserService {
    //根据id查询用户
    User getUserByUserId(Integer id);
    //新增一个功能
    Integer insertUserId(User user);
}
