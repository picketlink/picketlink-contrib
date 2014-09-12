/**
 * 
 */
package org.picketlink.contrib.loginModules;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.logging.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;


/**
 * @author mike cirioli  mikecirioli@gmail.com
 *
 * Jboss loginModules authenticator that does not perform authn, but will provide a statically mapped role
 * configured via module options
 * 4.22.2013
 *
 */

public class StaticRoleLoginModule extends UsernamePasswordLoginModule {
	public static final String AUTH_ROLE_NAME = "authRoleName";
    private Logger logger = Logger.getLogger(StaticRoleLoginModule.class);
	private static final String[] ALL_VALID_OPTIONS = {AUTH_ROLE_NAME};

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String,?> sharedState, Map<String,?> options) {
    	logger.debug("StaticRoleLoginModule initialize!");
		addValidOptions(ALL_VALID_OPTIONS);
		super.initialize(subject, callbackHandler, sharedState, options);
	}

	/* (non-Javadoc)
	 * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
        logger.debug("statocRoleLoginModule getRoleSets()");
		String roleName = (String)options.get(AUTH_ROLE_NAME);

		SimpleGroup userRoles = new SimpleGroup("Roles");
        Principal p = null;
		try {
            p = super.createIdentity(roleName);
			logger.debug("Assign principal [" + p.getName() + "] to role [" + roleName + "]");
			userRoles.addMember(p);
		} catch (Exception e) {
			logger.info("Failed to assign principal [" + p.getName() + "] to role [" + roleName + "]", e);
		}

		Group[] roleSets = {userRoles};
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