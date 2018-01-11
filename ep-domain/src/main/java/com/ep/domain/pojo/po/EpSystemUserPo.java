/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.repository.domain.enums.EpSystemUserStatus;

import javax.annotation.Generated;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 平台用户表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpSystemUserPo implements Serializable {

    private static final long serialVersionUID = 993375720;

    private Long id;
    private Long mobile;
    private String userName;
    private String salt;
    private String password;
    private String role;
    private String email;
    private EpSystemUserStatus status;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

    public EpSystemUserPo() {
    }

    public EpSystemUserPo(EpSystemUserPo value) {
        this.id = value.id;
        this.mobile = value.mobile;
        this.userName = value.userName;
        this.salt = value.salt;
        this.password = value.password;
        this.role = value.role;
        this.email = value.email;
        this.status = value.status;
        this.createAt = value.createAt;
        this.updateAt = value.updateAt;
        this.remark = value.remark;
        this.delFlag = value.delFlag;
        this.version = value.version;
    }

    public EpSystemUserPo(
            Long id,
            Long mobile,
            String userName,
            String salt,
            String password,
            String role,
            String email,
            EpSystemUserStatus status,
            Timestamp createAt,
            Timestamp updateAt,
            String remark,
            Boolean delFlag,
            Long version
    ) {
        this.id = id;
        this.mobile = mobile;
        this.userName = userName;
        this.salt = salt;
        this.password = password;
        this.role = role;
        this.email = email;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.remark = remark;
        this.delFlag = delFlag;
        this.version = version;
    }

    public Long getId() {
        return this.id;
    }

    public EpSystemUserPo setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getMobile() {
        return this.mobile;
    }

    public EpSystemUserPo setMobile(Long mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getUserName() {
        return this.userName;
    }

    public EpSystemUserPo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getSalt() {
        return this.salt;
    }

    public EpSystemUserPo setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public EpSystemUserPo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRole() {
        return this.role;
    }

    public EpSystemUserPo setRole(String role) {
        this.role = role;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public EpSystemUserPo setEmail(String email) {
        this.email = email;
        return this;
    }

    public EpSystemUserStatus getStatus() {
        return this.status;
    }

    public EpSystemUserPo setStatus(EpSystemUserStatus status) {
        this.status = status;
        return this;
    }

    public Timestamp getCreateAt() {
        return this.createAt;
    }

    public EpSystemUserPo setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
        return this;
    }

    public Timestamp getUpdateAt() {
        return this.updateAt;
    }

    public EpSystemUserPo setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public String getRemark() {
        return this.remark;
    }

    public EpSystemUserPo setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public EpSystemUserPo setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public Long getVersion() {
        return this.version;
    }

    public EpSystemUserPo setVersion(Long version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EpSystemUserPo (");

        sb.append(id);
        sb.append(", ").append(mobile);
        sb.append(", ").append(userName);
        sb.append(", ").append(salt);
        sb.append(", ").append(password);
        sb.append(", ").append(role);
        sb.append(", ").append(email);
        sb.append(", ").append(status);
        sb.append(", ").append(createAt);
        sb.append(", ").append(updateAt);
        sb.append(", ").append(remark);
        sb.append(", ").append(delFlag);
        sb.append(", ").append(version);

        sb.append(")");
        return sb.toString();
    }
}