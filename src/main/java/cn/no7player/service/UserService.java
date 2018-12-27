package cn.no7player.service;


import cn.no7player.entity.User;
import cn.no7player.mapper.generator.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    UserMapper userMapper;

    public User findUserByUserId(Long id) {
        User user = new User();
        user.setId(id);
        return userMapper.selectByPrimaryKey(user);
    }

    public User findUserByUserName(String name) {
        User user = new User();
        user.setUsername(name);
        return userMapper.selectOne(user);
    }
}
