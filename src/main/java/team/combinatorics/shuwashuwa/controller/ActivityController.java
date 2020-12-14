package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.UserParam;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.ActivityService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.sql.Timestamp;
import java.util.List;

@Api("活动信息接口说明")
@RestController
@RequestMapping("api/activity")
@AllArgsConstructor
public class ActivityController {
    ActivityService activityService;

    @ApiOperation("根据条件筛选活动列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ActivityResponseDTO>> handleListRequest(
            @RequestParam(value = "startLower",required = false)
            @ApiParam(value = "开始时间下界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String startTimeLowerBound,
            @RequestParam(value = "startUpper",required = false)
            @ApiParam(value = "开始时间上界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String startTimeUpperBound,
            @RequestParam(value = "endLower",required = false)
            @ApiParam(value = "结束时间下界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String endTimeLowerBound,
            @RequestParam(value = "endUpper",required = false)
            @ApiParam(value = "结束时间上界，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
                    String endTimeUpperBound
    ) {
        System.out.println("请求活动列表");
        SelectActivityCO co=new SelectActivityCO();
        if(startTimeLowerBound!=null) co.setStartTimeLowerBound(Timestamp.valueOf(startTimeLowerBound));
        if(startTimeUpperBound!=null) co.setStartTimeUpperBound(Timestamp.valueOf(startTimeUpperBound));
        if(endTimeLowerBound!=null) co.setEndTimeLowerBound(Timestamp.valueOf(endTimeLowerBound));
        if(endTimeUpperBound!=null) co.setEndTimeUpperBound(Timestamp.valueOf(endTimeUpperBound));
        return new CommonResult<>(200, "请求成功",activityService.listActivityByConditions(co));
    }

    @ApiOperation("用户活动现场签到")
    @RequestMapping(value = "/attend", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handlePresence(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam(value = "activity") @ApiParam(value = "活动Id，从二维码参数获取",required = true) Integer activityId
    ) {
        int userid = TokenUtil.extractUserid(token);
        activityService.setActive(userid,activityId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("查看一个活动的时间段列表")
    @RequestMapping(value = "/slot", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ActivityTimeSlotDTO>> handleTimeSlotRequest(
            @RequestParam("activity") @ApiParam(value = "活动ID", required = true) Integer activityId) {
        System.out.println("请求活动" + activityId + "时间段");
        return new CommonResult<>(200, "请求成功", activityService.listTimeSlots(activityId));
    }

    @ApiOperation("查询当前用户在某活动中是否进行有效签到")
    @RequestMapping(value = "/attend", method = RequestMethod.GET)
    @AllAccess
    @UserParam("user")
    public CommonResult<Boolean> attendingActivity(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam("activity") @ApiParam(value = "活动id", required = true) Integer activityId
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200, "请求成功", activityService.haveAttended(userid, activityId));
    }
}
