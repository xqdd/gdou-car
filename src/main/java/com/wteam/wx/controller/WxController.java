package com.wteam.wx.controller;

import com.apidoc.annotation.*;
import com.apidoc.enumeration.Method;
import com.apidoc.enumeration.ParamType;
import com.mysql.jdbc.StringUtils;
import com.wteam.car.base.BaseController;
import com.wteam.car.bean.entity.User;
import com.wteam.car.bean.interact.response.Msg;
import com.wteam.car.service.UserService;
import com.wteam.wx.bean.*;
import com.wteam.wx.utils.Oauth2Utils;
import com.wteam.wx.utils.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;


@RestController
@Api(name = "微信相关接口")
@RequestMapping(produces = "application/json; charset=utf-8")
public class WxController extends BaseController {


    private final UserService userService;

    @Autowired
    public WxController(UserService userService) {
        this.userService = userService;
    }


    //微信网页登录
    @GetMapping(value = "wxWeb/login")
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
                    //通过token拉去用户信息8
                    WxUser wxUser = Oauth2Utils.get_wx_user(token);

                    // 判断用户是否已经存在
                    Optional<User> optionalUser = userService.findById(wxUser.getUnionid());
                    if (!optionalUser.isPresent()) {
                        user = new User(wxUser.getUnionid(), wxUser.getSex());
                        user.setWebOpenid(wxUser.getOpenid());
                    }else{
                        user = optionalUser.get();
                    }

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
    @ApiAction(name = "生成微信网页登录授权url", mapping = "wxWeb/getLoginUrl")
    @ApiReqParams(type = ParamType.URL_PARAM, value = {@ApiParam(name = "callbackUrl", description = "回调url")})
    @ApiRespParams({
            @ApiParam(name = "code", defaultValue = "1"),
            @ApiParam(name = "data"),
    })
    public Msg getWebLoginUrl(String callbackUrl) {
        return Msg.successData(Oauth2Utils.generateUrl(callbackUrl));
    }


    @GetMapping(value = "wxWeb/isLogined")
    @ApiAction(name = "检查是否已经登录", mapping = "wxWeb/isLogined")
    @ApiRespParams({
            @ApiParam(name = "code", description = "1（是）/0（否）"),
    })
    public Msg isLogined(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return Msg.failed();
        } else {
            return Msg.success();
        }
    }


    @GetMapping(value = "wxMiniApp/login")
    @ApiAction(name = "1.小程序登录", mapping = "wxMiniApp/login")
    @ApiReqParams(type = ParamType.URL_PARAM, value = {@ApiParam(name = "code", description = "用户登录凭证")})
    @ApiRespParams({
            @ApiParam(name = "code", description = "1或0"),
            @ApiParam(name = "data", description = "登录成功或登录失败"),
    })
    public Msg miniAppLogin(String code, HttpSession session) {
        if (StringUtils.isNullOrEmpty(code)) {
            return Msg.failed("code不能为空");
        }
        Object loginInfo = Oauth2Utils.login_mini_app(code);
        if (loginInfo instanceof WxAppletLoginInfo) {
            session.setAttribute("miniAppLoginInfo", loginInfo);
            return Msg.success("登录成功");
        } else {
            return Msg.failedAndDebug("登录失败", (String) loginInfo);
        }
    }


    @PostMapping(value = "wxMiniApp/saveUser")
    @ApiAction(name = "2.小程序保存（添加/更新）用户数据", mapping = "wxMiniApp/saveUser", method = Method.POST)
    @ApiReqParams(type = ParamType.JSON, value = {
            @ApiParam(name = "encryptedData", description = "包括敏感数据在内的完整用户信息的加密数据", required = true),
            @ApiParam(name = "iv", description = "加密算法的初始向量", required = true),
    })
    @ApiRespParams({
            @ApiParam(name = "code", description = "1或0"),
            @ApiParam(name = "data", description = "操作成功或操作失败"),
    })
    public Msg miniAppSaveUser(@RequestBody @Valid WxAppletEncryption encryption,
                               @SessionAttribute("miniAppLoginInfo") WxAppletLoginInfo loginInfo) {

        WxAppletUserInfo userInfo = WxUtils.decryptData(encryption, loginInfo);
        if (userInfo == null) {
            return Msg.failedAndDebug("登录失败", "加密数据解密失败");
        }

        Optional<User> optionalUser = userService.findById(userInfo.getUnionId());
        User saveUser;
        if (optionalUser.isPresent()) {
            saveUser = optionalUser.get();
        } else {
            saveUser = new User();
            saveUser.setUnionId(loginInfo.getUnionid());
        }
        saveUser.setHeadimgurl(userInfo.getAvatarUrl());
        saveUser.setSex(userInfo.getGender());
        saveUser.setNickName(userInfo.getNickName());
        userService.save(saveUser);
        return Msg.success("操作成功");
    }


    @ApiAction(name = "登录测试接口", mapping = "wxWeb/userLogin")
    @GetMapping("wxWeb/userLogin")
    @ApiRespParams
    public Msg userLogin(HttpSession session) {
        Optional<User> optionalUser = userService.findById("oeskB1h-1hWC7LCwVGBwjvfHJ1Nk");
        if (optionalUser.isPresent()) {
            session.setAttribute("user", optionalUser.get());
            return Msg.success();
        } else {
            return Msg.failed();
        }

    }


    @GetMapping(value = "getUserInfo")
    @ApiAction(name = "获取用户数据（用于数据预加载）", mapping = "getUserInfo")
    @ApiRespParams({
            @ApiParam(name = "code", description = "1或0"),
            @ApiParam(name = "data", description = "用户数据"),
    })
    public Msg getUserInfo(@SessionAttribute("user") User user) {
        return Msg.successData(user);
    }

}
