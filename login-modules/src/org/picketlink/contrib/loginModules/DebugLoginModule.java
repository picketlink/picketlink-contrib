/**
 * 
 */
package org.picketlink.contrib.loginModules;

import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 *  @author mike cirioli mikecirioli@gmail.com
 *
 * Jboss loginModules authenticator that only outputs debug info (does not do authn or provide roles)
 * 4.22.2013
 *
 */

public class DebugLoginModule extends UsernamePasswordLoginModule {

    private Logger logger = Logger.getLogger(DebugLoginModule.class);
	private static final String[] ALL_VALID_OPTIONS = {};

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String,?> sharedState, Map<String,?> options) {
    	logger.debug("DebugLoginModule initialize!");
		addValidOptions(ALL_VALID_OPTIONS);

        logger.debug("dumping shareState");
        Set keys = sharedState.keySet();
        for (Iterator i = keys.iterator();i.hasNext();) {
            String key = (String) i.next();
            Object value = (Object) sharedState.get(key);
            logger.debug("key: [" + key + "]   value: [" + value.toString() + "]");
        }
        logger.debug("done dumping sharedState");

        logger.debug("getting subject prinicpals");
        Set principals = subject.getPrincipals();
        for (Iterator i = principals.iterator();i.hasNext();) {
            Principal p = (Principal) i.next();
            logger.debug("principal.getName(): [" + p.getName()+ "]");
        }
        logger.debug("done dumping subject prinicpals");

        super.initialize(subject, callbackHandler, sharedState, options);


	}

	/* (non-Javadoc)
	 * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
        logger.debug("debugLoginModule.getRoleSets()");
		Group[] roleSets = {};
		return roleSets;
	}

	/** Overridden to return an empty password string as typically one cannot
     obtain a user's password. We also override the validatePassword so
     this is ok.
     @return an empty password String
	 */
	@Override
	protected String getUsersPassword() throws LoginException {
	    logger.debug("getUsersPassword() [returning empty string always]");
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.security.auth.spi.UsernamePasswordLoginModule#validatePassword(java.lang.String, java.lang.String)
	 * 
	 * Takes users password and validates it using the redhat REST user service
	 * this method should not typically be called, it is used to stuff a fake role into the users 
	 */
	 
	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword){
        logger.debug("validatePassword() [returning false always]");
        return false;
    }
}
