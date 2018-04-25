package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpMessageCaptchaPo;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import com.ep.domain.repository.domain.tables.records.EpMessageCaptchaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

import static com.ep.domain.repository.domain.Tables.EP_MESSAGE_CAPTCHA;

/**
 * @Description:验证码Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class MessageCaptchaRepository extends AbstractCRUDRepository<EpMessageCaptchaRecord, Long, EpMessageCaptchaPo> {

    @Autowired
    public MessageCaptchaRepository(DSLContext dslContext) {
        super(dslContext, EP_MESSAGE_CAPTCHA, EP_MESSAGE_CAPTCHA.ID, EpMessageCaptchaPo.class);
    }

    /**
     * 统计当天请求短信验证码次数
     *
     * @param sourceId
     * @return
     */
    public int countBySourceId(Long sourceId) {
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp startTime = DateTools.zerolizedTime(now);
        Timestamp endTime = DateTools.getEndTime(now);
        return dslContext.selectCount().from(EP_MESSAGE_CAPTCHA)
                .where(EP_MESSAGE_CAPTCHA.SOURCE_ID.eq(sourceId))
                .and(EP_MESSAGE_CAPTCHA.DEL_FLAG.eq(false))
                .and(EP_MESSAGE_CAPTCHA.CREATE_AT.between(startTime, endTime))
                .fetchOneInto(Integer.class);
    }

    /**
     * 统计一个IP当天请求短信验证码次数
     *
     * @param ip
     * @return
     */
    public int countByIP(String ip) {
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp startTime = DateTools.zerolizedTime(now);
        Timestamp endTime = DateTools.getEndTime(now);
        return dslContext.selectCount().from(EP_MESSAGE_CAPTCHA)
                         .where(EP_MESSAGE_CAPTCHA.IP.eq(ip))
                         .and(EP_MESSAGE_CAPTCHA.DEL_FLAG.eq(false))
                         .and(EP_MESSAGE_CAPTCHA.CREATE_AT.between(startTime, endTime))
                         .fetchOneInto(Integer.class);
    }

    /**
     * 根据业务ID、类型、场景、业务编码获取验证码信息
     *
     * @param sourceId
     * @param captchaCode
     * @return
     */
    public EpMessageCaptchaPo getBySourceIdAndCaptchaCode(Long sourceId,
                                                          EpMessageCaptchaCaptchaType type,
                                                          EpMessageCaptchaCaptchaScene scene,
                                                          String captchaCode) {
        return dslContext.selectFrom(EP_MESSAGE_CAPTCHA)
                .where(EP_MESSAGE_CAPTCHA.SOURCE_ID.eq(sourceId))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_CODE.eq(captchaCode))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_TYPE.eq(type))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_SCENE.eq(scene))
                .and(EP_MESSAGE_CAPTCHA.DEL_FLAG.eq(false))
                .fetchOneInto(EpMessageCaptchaPo.class);
    }

    /**
     * 根据业务ID、类型、场景、验证码内容获取验证码信息
     *
     * @param sourceId
     * @param captchaContent
     * @return
     */
    public EpMessageCaptchaPo getBySourceIdAndCaptchaContent(Long sourceId,
                                                             EpMessageCaptchaCaptchaType type,
                                                             EpMessageCaptchaCaptchaScene scene,
                                                             String captchaContent) {
        return dslContext.selectFrom(EP_MESSAGE_CAPTCHA)
                .where(EP_MESSAGE_CAPTCHA.SOURCE_ID.eq(sourceId))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_CONTENT.eq(captchaContent))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_TYPE.eq(type))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_SCENE.eq(scene))
                .and(EP_MESSAGE_CAPTCHA.DEL_FLAG.eq(false))
                .fetchOneInto(EpMessageCaptchaPo.class);
    }

    /**
     * 按类型和场景删除业务id对应的验证码
     *
     * @param sourceId
     * @param type
     * @param scene
     */
    public int delBySourceIdAndTypeAndSence(Long sourceId, EpMessageCaptchaCaptchaType type, EpMessageCaptchaCaptchaScene scene) {
        return dslContext.update(EP_MESSAGE_CAPTCHA)
                .set(EP_MESSAGE_CAPTCHA.DEL_FLAG, true)
                .where(EP_MESSAGE_CAPTCHA.DEL_FLAG.eq(false))
                .and(EP_MESSAGE_CAPTCHA.SOURCE_ID.eq(sourceId))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_TYPE.eq(type))
                .and(EP_MESSAGE_CAPTCHA.CAPTCHA_SCENE.eq(scene))
                .execute();
    }

    /**
     * 删除过期的验证码记录
     *
     * @param handleTime
     */
    public int delCaptcha(Timestamp handleTime) {
        // 删除长期失效的验证码
        return dslContext.delete(EP_MESSAGE_CAPTCHA)
                .where(EP_MESSAGE_CAPTCHA.EXPIRE_TIME.lessThan(handleTime))
                .execute();
    }

}
