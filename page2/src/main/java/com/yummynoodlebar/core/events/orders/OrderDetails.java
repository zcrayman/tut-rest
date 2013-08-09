package com.yummynoodlebar.core.events.orders;

import java.util.Date;

public class OrderDetails {

    private Date dateTimeOfSubmission;

    public Date getDateTimeOfSubmission() {
        return this.dateTimeOfSubmission;
    }

    void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
      this.dateTimeOfSubmission = dateTimeOfSubmission;
    }
}
