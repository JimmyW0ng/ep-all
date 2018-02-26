package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构课程班次评分服务接口
 * @Author: J.W
 * @Date: 下午5:47 2018/2/8
 */
@Service
public class OrganClassCommentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private OrganClassCommentRepository organClassCommentRepository;

    /**
     * 分页查询课程全部评论
     *
     * @param pageable
     * @param courseId
     * @return
     */
    public Page<OrganClassCommentBo> findCourseCommentForPage(Pageable pageable, Long courseId) {
        Page<OrganClassCommentBo> page = organClassCommentRepository.findCourseCommentForPage(pageable, courseId);
        List<OrganClassCommentBo> data = page.getContent();
        if (CollectionsTools.isEmpty(data)) {
            return page;
        }
        for (OrganClassCommentBo classCommentBo : data) {
            Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, classCommentBo.getChildId());
            String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
            classCommentBo.setChildAvatar(avatar);
        }
        return page;
    }

    /**
     * 班次评价
     *
     * @param memberId
     * @param orderId
     * @param score
     * @param content
     * @param picList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo addClassComment(Long memberId, Long orderId, Byte score, String content, List<String> picList) {
        // 校验订单
        EpOrderPo orderPo = orderRepository.getById(orderId);
        if (orderPo == null || orderPo.getDelFlag()) {
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        if (!orderPo.getStatus().equals(EpOrderStatus.end)) {
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_END);
        }
        EpMemberChildPo childPo = memberChildRepository.getById(orderPo.getChildId());
        if (childPo == null || !childPo.getMemberId().equals(memberId)) {
            return ResultDo.build(MessageCode.ERROR_CHILD_NOT_EXISTS);
        }
        // 校验重复评论课程
        Optional<EpOrganClassCommentPo> optional = organClassCommentRepository.getByOrderId(orderId);
        if (optional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_COURSE_COMMENT_DUPLICATE);
        }
        // 保存评论
        EpOrganClassCommentPo addPo = new EpOrganClassCommentPo();
        addPo.setOgnId(orderPo.getOgnId());
        addPo.setCourseId(orderPo.getCourseId());
        addPo.setClassId(orderPo.getClassId());
        addPo.setScore(score);
        addPo.setChildId(orderPo.getChildId());
        addPo.setContent(content);
        addPo.setOrderId(orderId);
        organClassCommentRepository.insert(addPo);
        // 更新评论幅图业务id
        if (CollectionsTools.isNotEmpty(picList)) {
            fileRepository.updateSourceIdInPreCodes(picList, addPo.getId());
        }
        // 更新评论标志
        organClassChildRepository.updateCourseCommentFlagByOrderId(orderId);
        // 更新课程平均分
        Byte avgScore = organClassCommentRepository.getAvgScoreByOgnId(orderPo.getOgnId());
        int avg = avgScore.intValue() - (avgScore.byteValue() % BizConstant.SCORE_UNIT);
        organRepository.updateTogetherById(orderPo.getOgnId(), new Integer(avg).byteValue());
        return ResultDo.build();
    }
}
