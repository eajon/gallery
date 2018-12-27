package cn.no7player.mapper;

import cn.no7player.entity.*;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface UserDao extends Mapper<User>, MySqlMapper<User> {
    public User findUserInfo();
    public User findByUsername(String username);

}
