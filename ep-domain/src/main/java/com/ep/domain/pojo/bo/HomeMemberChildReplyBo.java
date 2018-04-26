package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Description: 后台管理首页回复bo
 * @Author: CC.F
 * @Date: 10:41 2018/4/19/019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeMemberChildReplyBo extends AbstractBasePojo {
    private Long id;
    private String childNickName;
    private String replyContent;
    private Timestamp replyCreateAt;
    public String replyFromNow;
    private String launchName;
    private String launchContent;
    private Timestamp launchCreateAt;
    public String launchFromNow;
}
