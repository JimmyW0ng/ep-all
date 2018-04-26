
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpSystemLogPo extends AbstractBasePojo {

    private Long id;
    private String moduleName;
    private String moduleDesc;
    private String remoteAddr;
    private Long operateId;
    private String operateName;
    private String operateMethod;
    private String requestUrl;
    private String params;
    private String result;
    private String exception;
    private Timestamp operateTime;
}
