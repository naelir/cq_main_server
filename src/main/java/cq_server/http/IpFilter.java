package cq_server.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(IpFilter.class);

	private final List<String> bannedIps;

	@SuppressWarnings("unused")
	private final List<String> allowedIps;

	public IpFilter(final List<String> bannedIps, final List<String> allowedIps) {
		this.bannedIps = bannedIps;
		this.allowedIps = allowedIps;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		final String ip = servletRequest.getRemoteAddr();
		if (servletResponse instanceof HttpServletResponse) {
			final HttpServletResponse httpservletResponse = (HttpServletResponse) servletResponse;
			final boolean isIPAllowed = !this.bannedIps.contains(ip);
			if (!isIPAllowed) {
				httpservletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
				LOG.info("ip {} filtered", ip);
			} else
				filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}
}
