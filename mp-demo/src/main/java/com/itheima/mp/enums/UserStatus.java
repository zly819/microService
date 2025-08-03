package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL(1, "正常"),
    FREEZE(2, "冻结");

    @EnumValue //保存到数据库的值-----枚举中的哪个字段的值作为数据库值
    private Integer code;
    @JsonValue //标记JSON序列化时展示的字段
    private String message;

    UserStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
