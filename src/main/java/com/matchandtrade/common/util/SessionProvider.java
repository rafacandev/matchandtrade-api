package com.matchandtrade.common.util;

import com.matchandtrade.authentication.UserAuthentication;

public class SessionProvider {

//	private MessageContext context;
//	
//	public SessionProvider(MessageContext context) {
//		this.context = context;
//	}
	
	public UserAuthentication getUserAuthentication() {
		UserAuthentication result = new UserAuthentication();
//    	HttpServletRequest request = context.getHttpServletRequest();
//    	HttpSession  session = request.getSession(true);
//		UserAuthentication user = (UserAuthentication) session.getAttribute("user");
        return result;
	}
	
}
