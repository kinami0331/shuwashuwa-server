package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.SUAccess;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;

import javax.validation.constraints.NotNull;

@Api(value = "超级管理员相关接口说明")
@RestController
@Validated
@RequestMapping("/api/super")
@AllArgsConstructor
public class SuperAdministratorController {
    private final SuperAdministratorService superAdministratorService;
    private final ImageStorageService storageService;

    /**
     * 超级管理员登录系统
     */
    @ApiOperation(value = "超级管理员登录", notes = "通过输入内置的超级管理员用户名和密码进行登录", httpMethod = "GET")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 40011, message = "用户名或密码错误")
    })
    @NoToken
    public CommonResult<String> loginHandler(@NotNull(message = "用户名不能为空") String userName,
                                             @NotNull(message = "密码不能为空") String password)
    {
        String token = superAdministratorService.checkInfo(userName, password);
        if(token != null)
            return new CommonResult<>(200, "登录成功", token);
        return new CommonResult<>(40011, "用户名或密码错误", "");
    }

    /**
     * 超级管理员修改密码
     */
    @ApiOperation(value = "修改超级管理员密码", notes = "比对输入的原始密码后，将新密码转换为MD5格式后储存至数据库", httpMethod = "PUT")
    @RequestMapping(value = "/change", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功"),
            @ApiResponse(code = 40011, message = "原始密码错误")
    })
    @SUAccess
    public CommonResult<String> changePassword(@RequestHeader @NotNull(message = "原始密码不能为空") String oldPassword,
                                               @RequestHeader @NotNull(message = "新密码不能为空") String newPassword)
    {
        boolean success = superAdministratorService.changePassword(oldPassword, newPassword);
        if(success)
            return new CommonResult<>(200, "修改成功", "Change password successfully!");
        return new CommonResult<>(40011, "原始密码错误", "Wrong old password!");
    }

    /**
     * 超管获取缓存图片数量
     */
    @ApiOperation(value = "获取缓存图片数量", notes = "超管专属", httpMethod = "GET")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    @SUAccess
    public CommonResult<Integer> getImageCacheNumber() {
        return new CommonResult<>(200,"请求成功",storageService.countCacheImages());
    }

    /**
     * 超管删除所有缓存图片
     */
    @ApiOperation(value = "删除指定日期前的所有缓存图片", notes = "超管专属", httpMethod = "DELETE")
    @RequestMapping(value = "/cache", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @SUAccess
    public CommonResult<String> handleImageCacheClear(@RequestParam("days") int days) {
        storageService.clearCacheByTime(days);
        return new CommonResult<>(200,"删除成功","deleted");
    }
}
