package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.repository.domain.tables.records.EpFileRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_FILE;

/**
 * @Description:文件表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class FileRepository extends AbstractCRUDRepository<EpFileRecord, Long, EpFilePo> {

    @Autowired
    public FileRepository(DSLContext dslContext) {
        super(dslContext, EP_FILE, EP_FILE.ID, EpFilePo.class);
    }

    /**
     * 根据业务类型和来源获取文件列表，按排序字段正序和创建时间正序排列
     *
     * @return
     */
    public List<EpFilePo> getByBizTypeAndSourceId(Short bscFileBizType, Long sourceId) {
        return dslContext.selectFrom(EP_FILE)
                .where(EP_FILE.BIZ_TYPE_CODE.eq(bscFileBizType))
                .and(EP_FILE.SOURCE_ID.eq(sourceId))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .orderBy(EP_FILE.SORT.asc(), EP_FILE.CREATE_AT.asc())
                .fetchInto(EpFilePo.class);
    }

    /**
     * 根据业务类型和来源获取文件preCode，按排序字段正序和创建时间正序排列
     *
     * @return
     */
    public List<String> getPreCodeByBizTypeAndSourceId(Short bscFileBizType, Long sourceId) {
        return dslContext.select(EP_FILE.PRE_CODE).from(EP_FILE)
                .where(EP_FILE.BIZ_TYPE_CODE.eq(bscFileBizType))
                .and(EP_FILE.SOURCE_ID.eq(sourceId))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .orderBy(EP_FILE.SORT.asc(), EP_FILE.CREATE_AT.asc())
                .fetchInto(String.class);
    }

    /**
     * 根据业务类型和来源获取文件链接，按排序字段正序和创建时间正序排列
     *
     * @return
     */
    public List<String> getUrlByBizTypeAndSourceId(Short bscFileBizType, Long sourceId) {
        return dslContext.select(EP_FILE.FILE_URL).from(EP_FILE)
                .where(EP_FILE.BIZ_TYPE_CODE.eq(bscFileBizType))
                .and(EP_FILE.SOURCE_ID.eq(sourceId))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .orderBy(EP_FILE.SORT.asc(), EP_FILE.CREATE_AT.asc())
                .fetchInto(String.class);
    }

    /**
     * 获取带删除文件
     *
     * @return
     */
    public int logicDel(Timestamp endTime) {
        return dslContext.update(EP_FILE)
                .set(EP_FILE.DEL_FLAG, true)
                .where(EP_FILE.CREATE_AT.lessThan(endTime))
                .and(EP_FILE.SOURCE_ID.isNull())
                .and(EP_FILE.PRE_CODE.isNotNull())
                .and(EP_FILE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 获取带删除文件
     *
     * @return
     */
    public List<EpFilePo> getWaitDelete(Timestamp endTime) {
        return dslContext.selectFrom(EP_FILE)
                .where(EP_FILE.CREATE_AT.lessThan(endTime))
                .and(EP_FILE.DEL_FLAG.eq(true))
                .fetchInto(EpFilePo.class);
    }

    /**
     * 根据业务类型和来源获取文件,取创建时间最晚的一个
     *
     * @param bscFileBizType
     * @param sourceId
     * @return
     */
    public Optional<EpFilePo> getOneByBizTypeAndSourceId(Short bscFileBizType, Long sourceId) {
        EpFilePo data = dslContext.selectFrom(EP_FILE)
                .where(EP_FILE.BIZ_TYPE_CODE.eq(bscFileBizType))
                .and(EP_FILE.SOURCE_ID.eq(sourceId))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .orderBy(EP_FILE.CREATE_AT.desc())
                .limit(DSL.one())
                .fetchOneInto(EpFilePo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据业务类型和来源进行逻辑删除
     *
     * @param bizTypeCode
     * @param sourceId
     * @return
     */
    public int logicDelByBizTypeAndSourceId(Short bizTypeCode, Long sourceId) {
        return dslContext.update(EP_FILE).set(EP_FILE.DEL_FLAG, true)
                .where(EP_FILE.BIZ_TYPE_CODE.eq(bizTypeCode))
                .and(EP_FILE.SOURCE_ID.eq(sourceId))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据预授权码更新业务ID
     *
     * @param preCode
     * @param id
     * @return
     */
    public int updateSourceIdByPreCode(String preCode, Long id) {
        return dslContext.update(EP_FILE).set(EP_FILE.SOURCE_ID, id)
                .where(EP_FILE.PRE_CODE.eq(preCode))
                .and(EP_FILE.SOURCE_ID.isNull())
                .and(EP_FILE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据预授权码更新业务ID
     *
     * @param preCodes
     * @param id
     * @return
     */
    public int updateSourceIdInPreCodes(List<String> preCodes, Long id) {
        return dslContext.update(EP_FILE).set(EP_FILE.SOURCE_ID, id)
                .where(EP_FILE.PRE_CODE.in(preCodes))
                .and(EP_FILE.SOURCE_ID.isNull())
                .and(EP_FILE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据预授权码获取记录
     *
     * @param preCode
     * @return
     */
    public Optional<EpFilePo> getByPreCode(String preCode) {
        EpFilePo filePo = dslContext.selectFrom(EP_FILE)
                .where(EP_FILE.PRE_CODE.eq(preCode))
                .and(EP_FILE.SOURCE_ID.isNull())
                .and(EP_FILE.DEL_FLAG.eq(false))
                .fetchOneInto(EpFilePo.class);
        return Optional.ofNullable(filePo);
    }

    /**
     * 根据业务类型和来源id逻辑删除记录
     *
     * @param bizTypeCode
     * @param sourceId
     */
    public void deleteLogicByBizTypeAndSourceId(Short bizTypeCode, Long sourceId) {
        dslContext.update(EP_FILE)
                .set(EP_FILE.DEL_FLAG, true)
                .where(EP_FILE.BIZ_TYPE_CODE.eq(bizTypeCode))
                .and(EP_FILE.SOURCE_ID.eq(sourceId))
                .execute();
    }

    /**
     * 根据业务类型和来源id逻辑删除记录
     *
     * @param preCodes
     */
    public void deleteLogicByPreCodes(List<String> preCodes) {
        dslContext.update(EP_FILE)
                .set(EP_FILE.DEL_FLAG, true)
                .where(EP_FILE.PRE_CODE.in(preCodes))
                .execute();
    }
}

