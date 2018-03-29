package com.ep.domain.service;

import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.repository.MemberChildTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 孩子标签记录Service
 * @Author: CC.F
 * @Date: 15:11 2018/3/1
 */
@Service
public class MemberChildTagService {
    @Autowired
    private MemberChildTagRepository memberChildTagRepository;

    /**
     * 根据孩子childId和班次目录ClassCatalogId获取孩子的标签
     *
     * @param childId
     * @param classScheduleId
     * @return
     */
    public List<EpMemberChildTagPo> findByChildIdAndClassCatalogId(Long childId, Long classScheduleId) {
        return memberChildTagRepository.findByChildIdAndClassCatalogId(childId, classScheduleId);
    }

    /**
     * 根据id逻辑删除孩子标签记录
     * @param id
     */
    public void deleteChildTagById(Long id){
        memberChildTagRepository.deleteLogicChildTagById(id);
    }

//    public void updateChildTag(Long[] ids)
}
