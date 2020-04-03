package com.xxx.pattern.chain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Alarm {
    //告警id
    private Integer id;

    //告警事件总数：这条告警是有几条事件合并而成的
    private Integer eventNumber;

    //告警名称
    private String alarmName;

    //告警发生位置
    private String alarmAddress;

    //是否确认告警 0:确认 1：未确认
    private Integer alarmAck;

    //告警等级 1：可疑 2：高危 3：严重 4：紧急
    private Integer alarmLevel;

    //告警类型 1:停电 2：硬件 3：软件
    private Integer alarmType;

    //告警发送时间
    private Date date;

    private String desc;
}
