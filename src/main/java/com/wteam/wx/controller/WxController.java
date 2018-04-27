package com.wteam.wx.controller;

import com.wteam.car.base.BaseController;
import com.wteam.car.bean.Msg;
import com.wteam.car.bean.entity.User;
import com.wteam.car.service.UserService;
import com.wteam.wx.bean.WxToken;
import com.wteam.wx.bean.WxUser;
import com.wteam.wx.utils.Oauth2Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Controller
public class WxController extends BaseController {


    private final UserService userService;

    @Autowired
    public WxController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 微信登陆
     *
     * @param code
     * @param state
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "wxPay/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Msg login(String code, String state, HttpSession session, HttpServletResponse response) {
        if (code == null || code.trim().isEmpty()) {
            return Msg.failed("参数有误");
        }

        //通过appSecret获取token
        WxToken token = Oauth2Utils.get_access_token(code);
        if (token == null) {
            try {
                Properties p = new Properties();
                p.load(WxController.class.getClassLoader().getResourceAsStream("wx.properties"));
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    response.sendRedirect(p.getProperty("errorPage"));
                } else {
                    response.sendRedirect(p.getProperty("homePage"));
                }
            } catch (IOException e) {
                log.error("读取wx.properties配置文件出错", e);
            }
            return null;
        }

        //通过token拉去用户信息
        WxUser wxUser = Oauth2Utils.get_wx_user(token);


        // 判断学生是否已经存在
        Optional<User> optionalUser = userService.findById(wxUser.getOpenid());
        User user = optionalUser.orElse(new User(wxUser.getOpenid(), wxUser.getSex()));

        //更新学生信息
        user.setHeadimgurl(wxUser.getHeadimgurl());
        user.setNickName(wxUser.getNickname());
        userService.save(user);
        try {
            session.setAttribute("user", user);
            if (state == null || state.trim().isEmpty()) {
                Properties p = new Properties();
                p.load(WxController.class.getClassLoader().getResourceAsStream("wx.properties"));
                response.sendRedirect(p.getProperty("homePage"));
                return null;
            }
            response.sendRedirect(state);
        } catch (IOException e) {
            log.error("读取wx.properties配置文件出错", e);
        }
        return null;
    }


    //用户网页登录
    @RequestMapping(name = "webLoginUser", method = RequestMethod.GET)
    @ResponseBody
    public Msg webLoginUrl(String callbackUrl) {
        return Msg.successData(Oauth2Utils.generateUrl(callbackUrl));
    }


}
