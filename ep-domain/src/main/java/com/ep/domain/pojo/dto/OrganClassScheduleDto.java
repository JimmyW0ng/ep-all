package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 22:46 2018/3/28
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrganClassScheduleDto extends EpOrganClassSchedulePo {
    private String childNickName;
    private String childTrueName;
    private Long launchId;
    private String contentLaunch;
    private EpMemberChildCommentType type;
    private Long replyId;
    private String contentReply;
    private Long courseId;

}
