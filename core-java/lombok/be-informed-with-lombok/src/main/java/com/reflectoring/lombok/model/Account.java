package com.reflectoring.lombok.model;

import lombok.Builder;

@Builder
public class Account {
    private String acctNo;

    private String acctName;

    private String dateOfJoin;

    private String acctStatus;
}
