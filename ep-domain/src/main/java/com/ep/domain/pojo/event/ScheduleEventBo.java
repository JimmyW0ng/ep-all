package com.ep.domain.pojo.event;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEventBo extends AbstractBasePojo {

    private EpOrganClassSchedulePo classSchedulePo;
}
