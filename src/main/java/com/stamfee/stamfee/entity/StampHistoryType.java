package com.stamfee.stamfee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StampHistoryType {

    STAMP_ADDED("적립"),
    STAMP_USED("사용");

    private final String type;

}
