/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 验证码表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpMessageCaptchaPo extends AbstractBasePojo {

    private Long id;
    private EpMessageCaptchaCaptchaType captchaType;
    private Long sourceId;
    private String captchaCode;
    private String captchaContent;
    private EpMessageCaptchaCaptchaScene captchaScene;
    private String channelScene;
    private Timestamp expireTime;
    private String ip;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
