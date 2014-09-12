package org.picketlink.contrib.utils;

/**
 * Created by mcirioli on 12/17/13.
 */

import javax.servlet.http.HttpServletResponse;

public class ResponseDumper {

    public static String dump(HttpServletResponse response) {
        StringBuffer buf = new StringBuffer("RESPONSE: \n--------\n");


        /*
        buf.append("isAppCommited: [" + response.isAppCommitted() + "]");
        buf.append("isError: [" + response.isError() + "]");
        buf.append("isSuspended: [" + response.isSuspended() + "]");
        buf.append("getContentCount: [" + response.getContentCount() + "]");
        buf.append("getContentLength: [" + response.getContentLength() + "]");
        buf.append("getIncluded: [" + response.getIncluded() + "]");
        buf.append("getInfo: [" + response.getInfo() + "]");
        buf.append("getMessage: [" + response.getMessage() + "]");
        */

        buf.append("isCommitted: [" + response.isCommitted() + "]");
        buf.append("getCharacterEncoding: [" + response.getCharacterEncoding() + "]");
        buf.append("getLocale: [" + response.getLocale() + "]");
        buf.append("getContentType: [" + response.getContentType() + "]");
        //buf.append("getStatus: [" + response.getStatus() + "]");


        buf.append("\n");


        return buf.toString();
    }
}
