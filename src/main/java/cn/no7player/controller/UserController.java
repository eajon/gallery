package cn.no7player.controller;

import cn.no7player.entity.User;
import cn.no7player.exception.BusinessException;
import cn.no7player.jwt.JWTUtil;
import cn.no7player.response.ResponseBean;
import cn.no7player.service.UserService;
import cn.no7player.util.MyShiroRealm;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zl on 2015/8/27.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {


    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MyShiroRealm myShiroRealm;




    @PostMapping("/login")
    @ResponseBody
    public ResponseBean login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        User userBean = userService.findUserByUserName(username);
        if(userBean==null)
        {
            throw new BusinessException("用户不存在");
        }
        if (userBean.getPassword().equals(password)) {
            return new ResponseBean(0, "Login success", JWTUtil.sign(username, password));
        } else {
            throw new BusinessException("密码错误");
        }
    }


    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseBean unauthorized() {
        return new ResponseBean(401, "Unauthorized", null);
    }


}
