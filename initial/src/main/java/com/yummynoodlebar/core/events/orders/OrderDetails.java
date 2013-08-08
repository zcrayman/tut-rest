package com.yummynoodlebar.core.events.orders;

import java.util.Date;

public class OrderDetails {

    private final Date dateTimeOfSubmission;

    public OrderDetails(final Date dateTimeOfSubmission) {
        this.dateTimeOfSubmission = dateTimeOfSubmission;
    }
    public Date getDateTimeOfSubmission() {
        return this.dateTimeOfSubmission;
    }
}
