package asmb.dms.ticket;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class TicketWebScript extends DeclarativeWebScript {

	private static Log log = LogFactory.getLog(TicketWebScript.class);

	private AuthenticationService authenticationService;

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {

		final Map<String, Object> model = new HashMap<String, Object>();
		final String username = req.getParameter("u");

		log.debug("ticket request for " + username);

		AuthenticationUtil.pushAuthentication();

		try {
			AuthenticationUtil.setFullyAuthenticatedUser(username);
			model.put("key", "ticket");
			model.put("value", authenticationService.getCurrentTicket());
		} catch (Exception e) {
			model.put("key", "error");
			model.put("value", e.getMessage());
			log.error(e.getMessage());
		} finally {
			AuthenticationUtil.popAuthentication();
		}

		return model;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

}
