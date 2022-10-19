package com.xiaobo.smartcalendar.Public;

import java.util.List;
import java.util.UUID;


public class VaribaleManager {
    private static VaribaleManager sVaribaleManager;

    private VaribaleManager() {

    }

    public static VaribaleManager getInstance() {
        if (sVaribaleManager == null) {
            sVaribaleManager = new VaribaleManager();
        }
        return sVaribaleManager;
    }

    
    List<UUID> arrayForUpload;
    public void addEventUUID(UUID eventUUID) {
        arrayForUpload.add(eventUUID);
    }
    public List<UUID> getArrayForUploadList() {
        return arrayForUpload;
    }
    public void removeEventUUID(UUID eventUUID) {
        arrayForUpload.remove(eventUUID);
    }
}
