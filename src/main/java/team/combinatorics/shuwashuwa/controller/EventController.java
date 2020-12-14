package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.UserParam;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUniversalDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.sql.Timestamp;
import java.util.List;

@Api("维修事件接口说明")
@RestController
@RequestMapping("api/service")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @ApiOperation(value = "创建维修事件", notes = "返回一个空的维修事件")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @AllAccess
    public CommonResult<ServiceEventDetailDTO> handleServiceEventCreation(
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",eventService.createNewEvent(userid));
    }

    @ApiOperation(value = "处理维修单提交", notes = "需要预先创建维修事件")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @AllAccess
    public CommonResult<String> handleFormCommit(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件的id，以及维修单的信息",required = true)
                    ServiceFormSubmitDTO serviceFormSubmitDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.submitForm(userid, serviceFormSubmitDTO,false);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "处理维修单草稿保存", notes = "需要预先创建维修事件")
    @RequestMapping(value = "/draft", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFormSaving(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件的id，以及维修单的信息",required = true)
                    ServiceFormSubmitDTO serviceFormSubmitDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.submitForm(userid, serviceFormSubmitDTO,true);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "[管理员]维修单审核通过")
    @RequestMapping(value = "/audit", method = RequestMethod.DELETE)
    @AdminAccess
    public CommonResult<String> handleFormAcceptance(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件Id和留言（可不留言）",required = true) ServiceEventUniversalDTO messageDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.acceptForm(userid,messageDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[管理员]维修单审核不通过")
    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    @AdminAccess
    public CommonResult<String> handleFormRejection(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件Id和拒绝理由",required = true) ServiceEventUniversalDTO reasonDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.rejectForm(userid,reasonDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[志愿者]接单")
    @RequestMapping(value = "/work", method = RequestMethod.DELETE)
    @VolunteerAccess
    public CommonResult<String> handleOrderTaking(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件Id",required = true) Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.takeOrder(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[志愿者]退回已接维修单")
    @RequestMapping(value = "/work", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleOrderGiveUp(
          @RequestHeader("token") @ApiParam(hidden = true) String token,
          @RequestBody @ApiParam(value = "维修事件Id",required = true) Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.giveUpOrder(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation("[志愿者]完成维修单并填写结果")
    @RequestMapping(value = "/complete", method = RequestMethod.PUT)
    @VolunteerAccess
    public CommonResult<String> handleServiceCompletion(
          @RequestHeader("token") @ApiParam(hidden = true) String token,
          @RequestBody @ApiParam(value = "维修事件Id和维修结果",required = true) ServiceEventUniversalDTO resultDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.completeOrder(userid,resultDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "用户对维修进行反馈",notes = "会覆盖之前的反馈")
    @RequestMapping(value = "/feedback", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handleFeedback(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件Id和反馈内容",required = true) ServiceEventUniversalDTO feedbackDTO
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.updateFeedback(userid,feedbackDTO);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "中止维修事件，取消预订", notes = "会将维修事件的closed位置1")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @AllAccess
    public CommonResult<String> handleShutdown(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestBody @ApiParam(value = "维修事件Id",required = true) Integer serviceEventId
    ) {
        int userid = TokenUtil.extractUserid(token);
        eventService.shutdownService(userid,serviceEventId);
        return new CommonResult<>(200,"请求成功","success");
    }

    @ApiOperation(value = "列出满足指定筛选条件的维修事件")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @AllAccess
    @UserParam("client")
    public CommonResult<List<ServiceAbstractDTO>> getServiceEventList(
            @RequestParam("client") @ApiParam("创建维修事件的用户id") Integer clientId,
            @RequestParam("volunteer") @ApiParam("接单的志愿者id") Integer volunteerId,
            @RequestParam("activity") @ApiParam("报名的活动id") Integer activityId,
            @RequestParam("status") @ApiParam(value = "该次维修处于的状态,可能状态如下:\n" +
                    "0:等待用户编辑\n" +
                    "1:等待管理员审核\n" +
                    "2:审核通过（待签到）\n" +
                    "3:等待志愿者接单\n" +
                    "4:维修中\n" +
                    "5:维修完成\n",
                    allowableValues = "0,1,2,3,4,5") Integer status,
            @RequestParam("draft") @ApiParam("是否有云端保存的草稿") Boolean draftSaved,
            @RequestParam("closed") @ApiParam("维修事件是否关闭") Boolean closed,
            @RequestParam("createLower") @ApiParam("创建时间下界") String createTimeLowerBound,
            @RequestParam("createUpper") @ApiParam("创建时间上界") String createTimeUpperBound
    ) {
        SelectServiceEventCO serviceEventCO = SelectServiceEventCO
                .builder()
                .userId(clientId)
                .volunteerId(volunteerId)
                .activityId(activityId)
                .closed(closed)
                .draft(draftSaved)
                .status(status)
                .beginTime(Timestamp.valueOf(createTimeLowerBound))
                .endTime(Timestamp.valueOf(createTimeUpperBound))
                .build();
        return new CommonResult<>(200,"请求成功",eventService.listServiceEvents(serviceEventCO));
    }

    @ApiOperation("获取一次维修事件的详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<ServiceEventDetailDTO> getServiceDetail(
            @RequestBody @ApiParam(value = "要查询的维修事件",required = true) Integer eventId
    ) {
        return new CommonResult<>(200,"请求成功",eventService.getServiceDetail(eventId));
    }

}
