package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto extends AbstractBasePojo {

    private String preCode;
    private String fileUrl;

}
