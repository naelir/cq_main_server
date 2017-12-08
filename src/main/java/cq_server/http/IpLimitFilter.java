package cq_server.http;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpLimitFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(IpLimitFilter.class);

	private static final String REQUEST_CONTEXT_LISTENER_ATTRIBUTES = "org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES";

	private static final String[] LIMITED_PATHS = new String[] { "/" };

	private final IpTimeWindowManager ipTimeWindowManager;

	public IpLimitFilter(final IpTimeWindowManager ipTimeWindowManager) {
		this.ipTimeWindowManager = ipTimeWindowManager;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = this.getHttpServletRequest(servletRequest);
		final boolean isRestServicePostCall = this.isRestPublicUserServicePostCall(request);
		if (isRestServicePostCall) {
			final String ipAddress = request.getRemoteAddr();
			this.ipTimeWindowManager.addIpRequest(ipAddress);
			if (this.ipTimeWindowManager.ipAddressReachedLimit(ipAddress)) {
				LOG.error("The ip address filtered: " + ipAddress);
				return;
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private HttpServletRequest getHttpServletRequest(final ServletRequest servletRequest) {
		if (servletRequest instanceof HttpServletRequest)
			return (HttpServletRequest) servletRequest;
		else {
			final Object springRequest = servletRequest.getAttribute(REQUEST_CONTEXT_LISTENER_ATTRIBUTES);
			if (springRequest instanceof HttpServletRequest)
				return (HttpServletRequest) springRequest;
			else
				return null;
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	private boolean isRestPublicUserServicePostCall(final HttpServletRequest request) {
		if (request != null) {
			final String requestedUri = request.getRequestURI();
			return Arrays.stream(LIMITED_PATHS).anyMatch(limitedPath -> requestedUri.startsWith(limitedPath));
		} else
			return false;
	}
}