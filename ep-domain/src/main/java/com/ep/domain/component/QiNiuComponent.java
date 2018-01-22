package com.ep.domain.component;

import com.ep.common.tool.JsonTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:七牛文件云组件
 * @Author: J.W
 * @Date: 下午2:34 17/11/16
 */
@Slf4j
@Component
public class QiNiuComponent {

    @Value("${qiniu.domain.public}")
    public String publicDomain;
    @Value("${qiniu.bucket.public}")
    public String publicBucket;
    @Value("${qiniu.token.expires}")
    public Long tokenExpires;
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;

    /**
     * 上传七牛公共空间
     *
     * @param fileName
     * @param data
     * @return
     */
    public ResultDo<String> uploadPublicByByte(String fileName, byte[] data) {
        ResultDo<String> resultDo = ResultDo.build();
        String key = fileName;
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(data, key, this.getToken(key));
            DefaultPutRet putRet = JsonTools.decode(response.bodyString(), DefaultPutRet.class);
            String url = this.publicDomain + StringTools.SLASH + putRet.key;
            return resultDo.setResult(url);
        } catch (QiniuException e) {
            log.error("上传七牛公共空间异常,response={}", e.response, e);
            return resultDo.setError(String.valueOf(e.code())).setErrorDescription(e.error());
        }
    }

    /**
     * 删除七牛公共空间文件
     *
     * @param fileName 文件名
     * @return
     */
    public ResultDo<Integer> deletePublic(String fileName) {
        ResultDo<Integer> resultDo = ResultDo.build();
        Auth auth = Auth.create(accessKey, secretKey);
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(this.publicBucket, fileName);
            return resultDo.setResult(BizConstant.DB_NUM_ONE);
        } catch (QiniuException e) {
            //如果遇到异常，说明删除失败
            log.error("删除七牛公共空间文件异常,response={}", e.response, e);
            return resultDo.setError(String.valueOf(e.code())).setErrorDescription(e.error());
        }
    }

    /**
     * 获取上传token
     *
     * @param fileName
     * @return
     */
    private String getToken(String fileName) {
        StringMap policy = new StringMap();
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        return auth.uploadToken(this.publicBucket, fileName, this.tokenExpires, policy);
    }


}
