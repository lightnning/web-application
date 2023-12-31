package com.kmaebashi.henacat.servletimpl;
import java.util.*;
import java.io.*;
import java.nio.charset.*;

import com.kmaebashi.henacat.servlet.http.*;
import com.kmaebashi.henacat.util.*;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
    private String characterEncoding = "ISO-8859-1";
    // リクエストがmultipart/form-dataの時はbyteParameterMapを、
    // それ以外の場合はparameterMapを使用する。使わない方はnull。
    private Map<String, String[]> parameterMap;
    private Map<String, byte[][]> byteParameterMap;
    private Cookie[] cookies;
    private HttpSessionImpl session;
    private ArrayList<Part> partList;
    private HttpServletResponseImpl response;
    private WebApplication webApp;
    private final String SESSION_COOKIE_ID = "JSESSIONID";
    @Override
    public String getMethod() {
	return this.method;
    }

    @Override
    public String getParameter(String name) {
	String[] values = getParameterValues(name);
	if  (values == null) {
	    return null;
	}
	return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
	String[] decoded;
	if (this.parameterMap != null) {
    	    String[] values = this.parameterMap.get(name);
    	    if (values == null) {
    		return null;
    	    }
    	    decoded = new String[values.length];
    	    try {
        	for (int i = 0; i < values.length; i++) {
        	    decoded[i] = MyURLDecoder.decode(values[i],
        				    this.characterEncoding);
        	}
    	    } catch (UnsupportedEncodingException ex) {
    	        throw new AssertionError(ex);
    	    }
	} else {
    	    byte[][] data = this.byteParameterMap.get(name);
    	    if (data == null) {
    		return null;
    	    }
    	    decoded = new String[data.length];
    	    try {
        	for (int i = 0; i < data.length; i++) {
    	            decoded[i] = new String(data[i], this.characterEncoding);
        	}
    	    } catch (UnsupportedEncodingException ex) {
    	        throw new AssertionError(ex);
    	    }
	}
	return decoded;
    }
    @Override
    public void setCharacterEncoding(String env)
	throws UnsupportedEncodingException {
	if (!Charset.isSupported(env)) {
	    throw new UnsupportedEncodingException("encoding.." + env);
	}
	this.characterEncoding = env;
    }
    
    @Override
    public Cookie[] getCookies() {
	return this.cookies;
    }

    private static Cookie[] parseCookies(String cookieString) {
	if (cookieString == null) {
	    return null;
	}
	String[] cookiePairArray = cookieString.split(";");
	Cookie[] ret = new Cookie[cookiePairArray.length];
	int cookieCount = 0;

	for (String cookiePair : cookiePairArray) {
	    String[] pair = cookiePair.split("=", 2);

	    ret[cookieCount] = new Cookie(pair[0], pair[1]);
	    cookieCount++;
	}

	return ret;
    }

    public HttpSession getSession() {
	return getSession(true);
    }

    public HttpSession getSession(boolean create) {
	if (!create) {
	    return this.session;
	}
	if (this.session == null) {
	    SessionManager manager = this.webApp.getSessionManager();
	    this.session = manager.createSession();
	    addSessionCookie();
	}
	return this.session;
    }

    private HttpSessionImpl getSessionInternal() {
	if (this.cookies == null) {
	    return null;
	}
	Cookie cookie = null;
	for (Cookie tempCookie : this.cookies) {
	    if (tempCookie.getName().equals(SESSION_COOKIE_ID)) {
		cookie = tempCookie;
	    }
	}
	SessionManager manager = this.webApp.getSessionManager();
	HttpSessionImpl ret = null;
	if (cookie != null) {
	    ret = manager.getSession(cookie.getValue());
	}
	return ret;
    }

    private void addSessionCookie() {
	Cookie cookie = new Cookie(SESSION_COOKIE_ID,
		  		   this.session.getId());
	cookie.setPath("/" + webApp.directory + "/");
	cookie.setHttpOnly(true);
	this.response.addCookie(cookie);
    }

    public Part getPart(String name) {
	for (Part part: this.partList) {
	    if (part.getName().equals(name)) {
		return part;
	    }
	}
	return null;
    }
    
    public Collection<Part> getParts() {
	return this.partList;
    }
    
    private HttpServletRequestImpl(String method, Map<String, String> requestHeader,
		   HttpServletResponseImpl resp,
		   WebApplication webApp) {
        this.method = method;
        this.cookies = parseCookies(requestHeader.get("COOKIE"));
        this.response = resp;
        this.webApp = webApp;
        this.session = getSessionInternal();
        if (this.session != null) {
            addSessionCookie();
        }
    }

    HttpServletRequestImpl(String method, Map<String, String> requestHeader,
	    		   Map<String, String[]> parameterMap,
	    		   HttpServletResponseImpl resp,
	    		   WebApplication webApp) {
	this(method, requestHeader, resp, webApp);
	this.parameterMap = parameterMap;
    }

    HttpServletRequestImpl(Map<String, String> requestHeader,
		   Map<String, byte[][]> byteParameterMap,
		   ArrayList<Part> partList,
		   HttpServletResponseImpl resp,
		   WebApplication webApp) {
	this("POST", requestHeader, resp, webApp);
	this.partList = partList;
	this.byteParameterMap = byteParameterMap;
    }
}
