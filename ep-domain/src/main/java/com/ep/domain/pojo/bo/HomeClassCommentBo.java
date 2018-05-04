package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Description: 后台管理首页评价bo
 * @Author: CC.F
 * @Date: 14:44 2018/5/4/004
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeClassCommentBo extends AbstractBasePojo {
    private Long id;
    private String childNickName;
    private Timestamp createAt;
    private String courseName;
    private String content;
    private String className;
    private Byte score;
}

