package com.xiaobo.smartcalendar.Model.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyEventResponse {


    /**
     * msg
     */
    private String msg;
    /**
     * status
     */
    private Integer status;
    /**
     * data
     */
    private DataDTO data;

    /**
     * DataDTO
     */
    @NoArgsConstructor
    @Data
    public static class DataDTO {
        /**
         * uuid
         */
        private String uuid;
        /**
         * title
         */
        private String title;
        /**
         * date
         */
        private Long date;
        /**
         * calendar
         */
        private Integer calendar;
        /**
         * duration
         */
        private Integer duration;
        /**
         * etype
         */
        private String etype;
        /**
         * location
         */
        private String location;
        /**
         * longitude
         */
        private Double longitude;
        /**
         * latitude
         */
        private Double latitude;
        /**
         * host
         */
        private String host;
        /**
         * participant
         */
        private String participant;
        /**
         * periodicity
         */
        private String periodicity;
        /**
         * important
         */
        private Boolean important;
        /**
         * commuting
         */
        private Integer commuting;
        /**
         * minimumDuration
         */
        private Integer minimumDuration;
        /**
         * earliestStartTime
         */
        private Integer earliestStartTime;
        /**
         * latestStartTime
         */
        private Integer latestStartTime;
        /**
         * earliestEndTime
         */
        private Integer earliestEndTime;
        /**
         * latestEndTime
         */
        private Integer latestEndTime;
        /**
         * city
         */
        private Object city;
        /**
         * isDelete
         */
        private Boolean isDelete;
        /**
         * userId
         */
        private Integer userId;
    }
}
