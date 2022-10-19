package com.xiaobo.smartcalendar.Model.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyTemporalInconsistencyResponse {


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
         * sysActionType1
         */
        private String sysActionType1;
        /**
         * sysActionType2
         */
        private String sysActionType2;
        /**
         * sysActionType3
         */
        private String sysActionType3;
        /**
         * sysActionType4
         */
        private String sysActionType4;
        /**
         * sysTimeAction1
         */
        private Integer sysTimeAction1;
        /**
         * sysTimeAction2
         */
        private Integer sysTimeAction2;
        /**
         * sysTimeAction3
         */
        private Integer sysTimeAction3;
        /**
         * sysTimeAction4
         */
        private Integer sysTimeAction4;
    }
}
