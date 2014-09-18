/**
 * 
 */
package org.picketlink.contrib.loginModules;


import java.security.acl.Group;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;


/**
 * @author mike cirioli  mikecirioli@gmail.com
 *
 * Jboss loginModules authenticator that validates username/password against staticly configured values
 * 4.22.2013
 *
 */

public class StaticLoginModule extends UsernamePasswordLoginModule {
	public static final String AUTH_USER_NAME = "authUserName";
	public static final String AUTH_USER_PASS = "authUserPassword";
	public static final String AUTH_ANY_USER = "authAnyUser";
	public static final String AUTH_ANY_PASS = "authAnyPass";

    private Logger logger = Logger.getLogger(StaticLoginModule.class);	
	private static final String[] ALL_VALID_OPTIONS = {AUTH_USER_NAME,AUTH_USER_PASS,AUTH_ANY_USER,AUTH_ANY_PASS};

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String,?> sharedState, Map<String,?> options) {
    	logger.debug("StaticLoginModule initialize!");
		addValidOptions(ALL_VALID_OPTIONS);
		super.initialize(subject, callbackHandler, sharedState, options);
	}

	/* (non-Javadoc)
	 * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
        logger.debug("returning empty Group[] rolesets");
		Group[] roleSets = {};
		return roleSets;
	}

	@Override
	protected String getUsersPassword() throws LoginException {
        logger.debug("getUsersPassword() retuns [" + options.get(AUTH_USER_PASS) + "]");
        return  (String) options.get(AUTH_USER_PASS);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.security.auth.spi.UsernamePasswordLoginModule#validatePassword(java.lang.String, java.lang.String)
	 * 
	 */

	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword){
		boolean user_ok = false;
		boolean password_ok = false;

        logger.debug("AUTH_ANY_USER == " + options.get(AUTH_ANY_USER));
        logger.debug("AUTH_USER_NAME == " + options.get(AUTH_USER_NAME));
        logger.debug("AUTH_ANY_PASS == " + options.get(AUTH_ANY_PASS));
        logger.debug("AUTH_USER_PASS == " + options.get(AUTH_ANY_PASS));
        logger.debug("inputPassword == " + inputPassword);
        logger.debug("getUserName() --> " + getUsername());

        if (((String) options.get(AUTH_ANY_USER)).equalsIgnoreCase("true")) {
				user_ok = true;
		}
		else {
            if (getUsername() != null) {
				if (getUsername().equals((String) options.get(AUTH_USER_NAME))) {
					user_ok = true;
				}
			}
		}

        if (((String) options.get(AUTH_ANY_PASS)).equalsIgnoreCase("true")) {
            password_ok = true;
		}
		else {
			if (inputPassword != null) {
				if ((inputPassword.equals((String)options.get(AUTH_USER_PASS)))) {
					password_ok = true;
				}
			}	
		}
        logger.debug("user_ok == " + user_ok);
        logger.debug("password_ok == " + password_ok);
		return (user_ok && password_ok);
    }
}
