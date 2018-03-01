package com.ep.domain.pojo.event;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassCatalogCommentEventBo extends AbstractBasePojo {

    private Long classCatalogId;
    private Long childId;
    private List<Long> tagIds;
    private String comment;

}
