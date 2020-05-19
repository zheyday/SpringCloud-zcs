package zcs.user_center.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import zcs.commons.utils.CommonUtil;
import zcs.commons.utils.ResponseState;
import zcs.user_center.model.User;
import zcs.user_center.service.impl.UserServiceImpl;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zcs
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final CommonUtil commonUtil;
    private final UserServiceImpl userService;

    @Autowired
    public UserController(CommonUtil commonUtil, UserServiceImpl userService) {
        this.commonUtil = commonUtil;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return commonUtil.toJson(ResponseState.OK, "token", "admin-token");
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String userName, @RequestParam("password") String passWord, String phone, String email) {
        User user = new User(userName, passWord, phone, email);
        try {
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                return commonUtil.toJson(ResponseState.DUPLICATE);
            }
            return commonUtil.toJson(ResponseState.FAIL);
        }
        return commonUtil.success();
    }

    @GetMapping("/info")
    public String getInfo(@RequestParam("userId") Integer userId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("roles", "admin");
//        map.put("introduction", "I am a super administrator");
//        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//        map.put("phone", "19852854226");
//        map.put("name", "Super Admin");
        return commonUtil.toJson(ResponseState.OK, userService.getById(userId));
    }


}
