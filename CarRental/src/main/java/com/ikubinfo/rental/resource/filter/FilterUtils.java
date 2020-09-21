package com.ikubinfo.rental.resource.filter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class FilterUtils {

    private FilterUtils() {
    }

    public static HashMap<String, LocalDateTime> getFilterData(String startDateString, String endDateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HashMap<String, LocalDateTime> dataMap = new HashMap<>();
        LocalDateTime startLocalDateTime = LocalDateTime.parse(startDateString, dateFormatter);
        LocalDateTime endLocalDateTime = LocalDateTime.parse(endDateString, dateFormatter);
        dataMap.put("startDate", startLocalDateTime);
        dataMap.put("endDate", endLocalDateTime);
        return dataMap;
    }
}
