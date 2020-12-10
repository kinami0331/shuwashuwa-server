package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormRejectionDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.pojo.ServiceForm;

import java.util.List;

@Repository
public interface ServiceFormDao {
    /**
     * 插入
     *
     * @param serviceFormPO 构造的申请表信息
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("form") ServiceFormPO serviceFormPO);

    /**
     * 管理员更新维修单信息
     *
     * @param adminID              管理员id
     * @param serviceFormRejectionDTO 更新的内容
     */
    void updateAdvice(@Param("adminID") int adminID, @Param("updateInfo") ServiceFormRejectionDTO serviceFormRejectionDTO);

    /**
     * 根据form id寻找form
     *
     * @param id form id
     * @return 一个ServiceForm结构
     */
    ServiceForm getServiceFormByFormID(@Param("id") int id);

    /**
     * 根据event id寻找form
     *
     * @param eventID event id
     * @return 一个ServiceForm结构
     */
    List<ServiceForm> listServiceFormsByServiceEventID(@Param("eventID") int eventID);

    /**
     * 根据维修事件id获取最后一个维修单的id
     *
     * @param eventID 维修事件id
     * @return 最后一个维修单的id
     */
    Integer getLastFormIDByEventID(@Param("eventID") int eventID);
}
