package org.picketlink.contrib.utils;

import org.apache.catalina.Session;
import org.jboss.logging.Logger;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by mcirioli on 12/17/13.
 */
public class SessionDumper {
    private static Logger log = Logger.getLogger(SessionDumper.class);

    public static String dump(HttpSession session) {
        log.debug("Begin processing session");
        StringBuffer buf = new StringBuffer("\n\n");

        buf.append("Session:\n--------\n");
        buf.append("Creation Time: [" + session.getCreationTime() + "]\n");
        buf.append("Last Access Time: [" + session.getLastAccessedTime() + "]\n");
        buf.append("Session ID: [" + session.getId() + "]\n");
        buf.append("isNew: [" + session.isNew() + "]\n");
        buf.append("maxInactiveInterval: [" + session.getMaxInactiveInterval() + "]\n");

        log.debug("querying session attributes...");
        Enumeration<String> e = session.getAttributeNames();
        buf.append("Session Attributes:\n");
        buf.append("-------------------\n");
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            try {
                String value = (String) session.getAttribute(name);
                buf.append("    Attribute: [" + name + "] == [" + value + "]\n");
            }
            catch (Exception ex) {
                log.debug("exception getting attribute: " + name);
                log.debug("Exception: " + ex.getMessage());
            }
        }

        log.debug("Done processing session");

        return (buf.toString());
    }

    public static String dump(Session session) {
        log.debug("Begin processing session");
        StringBuffer buf = new StringBuffer("\n\n");

        buf.append("Session:\n--------\n");
        buf.append("Creation Time: [" + session.getCreationTime() + "]\n");
        buf.append("Last Access Time: [" + session.getLastAccessedTime() + "]\n");
        buf.append("Session ID: [" + session.getId() + "]\n");
        buf.append("maxInactiveInterval: [" + session.getMaxInactiveInterval() + "]\n");

        log.debug("querying session attributes...");
        Iterator it = session.getNoteNames();
        buf.append("Session Notes:\n");
        buf.append("-------------------\n");
        while (it.hasNext()) {
            String name = (String) it.next();
            try {
                String value = (String) session.getNote(name);
                buf.append("    Attribute: [" + name + "] == [" + value + "]\n");
            }
            catch (Exception ex) {
                log.debug("exception getting attribute: " + name);
                log.debug("Exception: " + ex.getMessage());
            }
        }

        log.debug("Done processing session");

        return (buf.toString());
    }

}
