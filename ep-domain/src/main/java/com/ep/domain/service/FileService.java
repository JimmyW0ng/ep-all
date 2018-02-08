package com.ep.domain.service;

import com.ep.common.tool.FileTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.QiNiuComponent;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.FileDto;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 文件服务类
 * @Author: J.W
 * @Date: 下午2:19 2018/1/22
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private QiNiuComponent qiNiuComponent;

    /**
     * 根据业务类型预添加文件
     *
     * @param fileName
     * @param bytes
     * @param bizTypeCode
     * @param sort
     * @return
     */
    public ResultDo<FileDto> addFileByBizType(String fileName,
                                              byte[] bytes,
                                              Short bizTypeCode,
                                              Integer sort) {
        ResultDo<FileDto> resultDo = ResultDo.build();
        // 上传七牛云
        String saveName = StringTools.getUUID() + FileTools.POINT + FileTools.getFileExt(fileName);
        ResultDo<String> qiniuResult = qiNiuComponent.uploadPublicByByte(saveName, bytes);
        if (qiniuResult.isError()) {
            resultDo.setError(qiniuResult.getError()).setErrorDescription(qiniuResult.getErrorDescription());
            return resultDo;
        }
        // 创建预授权码
        String preCode = StringTools.getUUID();
        FileDto fileDto = new FileDto();
        fileDto.setPreCode(preCode);
        fileDto.setFileUrl(qiniuResult.getResult());
        resultDo.setResult(fileDto);
        // 保存到文件表
        EpFilePo filePo = new EpFilePo();
        filePo.setPreCode(preCode);
        filePo.setFileName(saveName);
        filePo.setFileUrl(qiniuResult.getResult());
        filePo.setBizTypeCode(bizTypeCode);
        filePo.setSort(sort);
        fileRepository.insert(filePo);
        return resultDo;
    }

    /**
     *
     * @param bizTypeCode
     * @param sourceId
     * @return
     */
    public List<EpFilePo> getByBizTypeAndSourceId(Short bizTypeCode, Long sourceId){
        return fileRepository.getByBizTypeAndSourceId(bizTypeCode,sourceId);
    }
}
