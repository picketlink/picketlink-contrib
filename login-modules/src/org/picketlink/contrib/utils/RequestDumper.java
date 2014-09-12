package org.picketlink.contrib.utils;

import org.jboss.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by mcirioli on 12/17/13.
 */
public class RequestDumper {
    private static Logger log = Logger.getLogger(RequestDumper.class);
    public static String dump(HttpServletRequest request) {
        log.debug("Begin processing request");
        StringBuffer buf = new StringBuffer("\n\n");

        buf.append("REQUEST:\n--------\n");
        if (request.getUserPrincipal() != null) {
            buf.append("Principal name: [" + request.getUserPrincipal().getName() + "]\n");
        }
        else {
            buf.append("Principal is [null]\n");
        }

        buf.append("AuthType: [" + request.getAuthType() + "]\n");
        buf.append("request URI: [" + request.getRequestURI() + "]\n");
        buf.append("request URL: [" + request.getRequestURL().toString() + "]\n");
        buf.append("isRequestedSessionIdFromCookie: [" + request.isRequestedSessionIdFromCookie() + "]\n");
        buf.append("isRequestedSessionIdFromURL: [" + request.isRequestedSessionIdFromURL() + "]\n");
        buf.append("isRequestedSessionIdValid: [" + request.isRequestedSessionIdValid() + "]\n");
        buf.append("isSecure: [" + request.isSecure() + "]\n");

        buf.append("\n");
        buf.append("PARAMETERS: \n------\n");
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            String value = (String) request.getParameter(name);
            buf.append("    ");
            buf.append(name);
            buf.append("=");
            buf.append(value);
            buf.append("\n");
        }

        buf.append("\n");
        buf.append("HEADERS: \n------\n");

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            String value = request.getHeader(name);
            buf.append("    ");
            buf.append(name);
            buf.append("=");
            buf.append(value);
            buf.append("\n");
        }

        buf.append("COOKIES:\n-------\n");
        Cookie[] cookies = request.getCookies();

        for (int i=0;i<cookies.length;i++) {
            buf.append("Cookie: [" + cookies[i].getName() + "] Value: [" +cookies[i].getValue() + "]\n");
            buf.append("    comment: [" + cookies[i].getComment() + "]\n");
            buf.append("    domain: [" + cookies[i].getDomain() + "]\n");
            buf.append("    maxAge: [" + cookies[i].getMaxAge() + "]\n");
            buf.append("    path: [" + cookies[i].getPath() + "]\n");
            buf.append("    secure?: [" + cookies[i].getSecure() + "]\n");
            buf.append("    version: [" + cookies[i].getVersion() + "]\n");
        }


        log.debug("Done processing request");

        return (buf.toString());
    }
}
