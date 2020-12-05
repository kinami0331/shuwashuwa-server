package team.combinatorics.shuwashuwa.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerApplicationPO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private Integer userId;
    private String reasonForApplication;
    private String replyByAdmin;
    private Integer adminId;
    private Integer status;
}