package com.wteam.wx.controller;

import com.apidoc.annotation.*;
import com.mysql.jdbc.StringUtils;
import com.wteam.car.base.BaseController;
import com.wteam.car.bean.Msg;
import com.wteam.car.bean.entity.User;
import com.wteam.car.service.UserService;
import com.wteam.wx.bean.WxToken;
import com.wteam.wx.bean.WxUser;
import com.wteam.wx.utils.Oauth2Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;


@Controller
@Api(name = "微信登录接口", mapping = "/")
public class WxController extends BaseController {


    private final UserService userService;

    @Autowired
    public WxController(UserService userService) {
        this.userService = userService;
    }


    //微信网页登录
    @GetMapping(value = "wxWeb/login")
    @ResponseBody
    public Msg login(String code, String state, HttpSession session, HttpServletResponse response) {
        if (code == null || code.trim().isEmpty()) {
            return Msg.failed("参数有误");
        }
        try {
            Properties p = new Properties();
            //加载配置文件
            p.load(WxController.class.getClassLoader().getResourceAsStream("wx.properties"));
            User user = (User) session.getAttribute("user");
            //学生是否已经登录
            if (user == null) {
                //通过appSecret获取token
                WxToken token = Oauth2Utils.get_access_token(code);
                if (token == null) {
                    response.sendRedirect(p.getProperty("homePage"));
                    return null;
                } else {
                    //通过token拉去用户信息
                    WxUser wxUser = Oauth2Utils.get_wx_user(token);

                    // 判断用户是否已经存在
                    Optional<User> optionalUser = userService.findById(wxUser.getOpenid());
                    user = optionalUser.orElse(new User(wxUser.getOpenid(), wxUser.getSex()));

                    //更新用户信息
                    user.setHeadimgurl(wxUser.getHeadimgurl());
                    user.setNickName(wxUser.getNickname());
                    userService.save(user);

                    //存入session
                    session.setAttribute("user", user);
                }
            }
            //是否自定义跳转链接
            if (StringUtils.isNullOrEmpty(state)) {
                response.sendRedirect(p.getProperty("homePage"));
                return null;
            }
            response.sendRedirect(state);
        } catch (IOException e) {
            log.error("读取wx.properties配置文件出错或response.sendRedirect()出错", e);
        }
        return null;
    }


    /**
     * @param callbackUrl 回调url
     * @return
     */
    @GetMapping(value = "wxWeb/getLoginUrl")
    @ApiAction(name = "生成微信登录授权url", mapping = "wxWeb/getLoginUrl")
    @ApiReqParams({@ApiParam(name = "callbackUrl", description = "回调url")})
    @ApiRespParams({
            @ApiParam(name = "code", defaultValue = "1"),
            @ApiParam(name = "data", description = "微信登录url"),
    })
    @ResponseBody
    public Msg getWebLoginUrl(@RequestBody String callbackUrl) {
        return Msg.successDataMsg(Oauth2Utils.generateUrl(callbackUrl));
    }


}
