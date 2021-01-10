package com.ikubinfo.rental.service.filter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class FilterUtils {

    private FilterUtils() {
    }

    public static HashMap<String, LocalDateTime> getFormattedLocalDateTimes(String startDate, String endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HashMap<String, LocalDateTime> dataMap = new HashMap<>();
        LocalDateTime startLocalDateTime = LocalDateTime.parse(startDate, dateFormatter);
        LocalDateTime endLocalDateTime = LocalDateTime.parse(endDate, dateFormatter);
        dataMap.put("startDate", startLocalDateTime);
        dataMap.put("endDate", endLocalDateTime);
        return dataMap;
    }
}
