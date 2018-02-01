package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.SystemMenuRepository;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 菜单服务类
 * @Author: J.W
 * @Date: 上午9:30 2018/1/14
 */
@Slf4j
@Service
public class SystemMenuService {
    @Autowired
    private SystemMenuRepository systemMenuRepository;
    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;

    public EpSystemMenuPo getById(Long id) {
        return systemMenuRepository.findById(id);

    }

    public List<EpSystemMenuPo> getAllByUserType(EpSystemUserType type) {
        return systemMenuRepository.getAllByUserType(type);
    }

    public List<SystemMenuBo> getLeftMenuByUserType(EpSystemUserType type) {
        List<SystemMenuBo> list = null;
        if(type.equals(EpSystemUserType.platform)){
            list = systemMenuRepository.getAllMenu(BizConstant.ADMIN_MENU_PARENT_ID);
        }else{
            list = systemMenuRepository.getAllMenu(BizConstant.BACKEND_MENU_PARENT_ID);
        }

        list.forEach(p -> {
            p.setChildList(systemMenuRepository.getAllMenu(p.getId()));
        });
        return list;
    }

    public EpSystemMenuPo insert(EpSystemMenuPo po) {
        return systemMenuRepository.create(po);
    }

    public int update(EpSystemMenuPo po) {
        po.setUpdateAt(DateTools.getCurrentDateTime());
        return systemMenuRepository.updateReturnEffRowsCount(po);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        systemRoleAuthorityRepository.deleteByMenuId(id);
        return systemMenuRepository.deleteLogical(id);
//        ResultDo resultDo=ResultDo.build();
//        if(systemRoleAuthorityRepository.isMenuUseByMenu(id)){
//            List<EpSystemRolePo> systemRolePos=systemRoleAuthorityRepository.getRoleByUseMenu(id);
//            StringBuffer sb=new StringBuffer("存在以下角色：");
//            systemRolePos.forEach(p->{
//                sb.append(p.getRoleName());
//            });
//            sb.append("正在使用此菜单");
//            resultDo.setSuccess(false);
//            resultDo.setErrorDescription(sb.toString());
//        }else{
//            systemMenuRepository.deleteLogical(id);
//        }
//        return resultDo;
    }

}
