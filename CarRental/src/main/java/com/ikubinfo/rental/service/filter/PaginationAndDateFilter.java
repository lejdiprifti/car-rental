package com.ikubinfo.rental.service.filter;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
public abstract class PaginationAndDateFilter {
    private int startIndex;
    private int pageSize;
    private LocalDateTime startLocalDateTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    private LocalDateTime endLocalDateTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);

    public void setBothLocalDateTimes(HashMap<String, LocalDateTime> dateTimeHashMap) {
        this.startLocalDateTime = dateTimeHashMap.get("startDate");
        this.endLocalDateTime = dateTimeHashMap.get("endDate");
    }
}
