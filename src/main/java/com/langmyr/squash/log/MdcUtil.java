package com.langmyr.squash.log;

import org.slf4j.MDC;

public class MdcUtil
{    
    public static final void clear() {
        MDC.clear();
    }
    
    public static final void setRequestId(String requestId) {
        if(requestId != null)
            MDC.put("REQUEST_ID", requestId);
    }
    
    public static final String getRequestId() {
        return MDC.get("REQUEST_ID");
    }
    
    public static final void setUserId(String userId) {
        if(userId != null)
            MDC.put("USER_ID", userId);
    }
    
    public static final void removeUserId() {
        MDC.remove("USER_ID");
    }
    

}