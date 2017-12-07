package cq_server.http;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class MainConfiguration {
	@Bean(name = "allowedIps")
	public List<String> allowedIps() {
		return new ArrayList<>();
	}

	@Bean(name = "bannedIps")
	public List<String> bannedIps() {
		return new ArrayList<>();
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpFilter getIpFilter() {
		return new IpFilter(this.bannedIps(), this.allowedIps());
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public FilterRegistrationBean ipFilterRegistrationBean(final IpFilter ipFilter) {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(ipFilter);
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpLimitFilter ipLimitFilter(final IpTimeWindowManager ipTimeWindowManager) {
		return new IpLimitFilter(ipTimeWindowManager);
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public FilterRegistrationBean ipLimitFilterRegistrationBean(final IpLimitFilter ipLimitFilter) {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(ipLimitFilter);
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registrationBean;
	}

	@Bean
	@Scope(value = "singleton")
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpTimeWindowManager ipTimeWindowManager() {
		return new IpTimeWindowManager();
	}
}
