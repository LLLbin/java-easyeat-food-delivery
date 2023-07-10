package com.lllebin.service;

import com.lllebin.domain.User;
import com.lllebin.domain.UserExample;
import com.lllebin.mapper.UserMapper;
import com.lllebin.utils.BaseContextUtils;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: UserService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class UserService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private UserMapper userMapper;

    public List<User> getUserByPhone(String phone) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andPhoneEqualTo(phone);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList.size() == 0) {
            save(phone);
        }
        return userMapper.selectByExample(userExample);
    }

    public void save(String phone) {
        User user = new User();
        user.setId(snowflakeUtils.nextId());
        user.setName("用户" + snowflakeUtils.nextId());
        user.setPhone(phone);
        user.setStatus(1);
        userMapper.insertSelective(user);
    }

    public User getById() {
        return userMapper.selectByPrimaryKey(BaseContextUtils.getCurrentId());
    }
}
