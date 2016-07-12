package ntou.soselab.easylearn.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import ntou.soselab.easylearn.domain.entity.User;
import ntou.soselab.easylearn.repository.UserRepository;
import ntou.soselab.easylearn.security.TokenUtils;

public class AuthFilter extends ZuulFilter {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Value("${cerberus.token.header}")
	private String tokenHeader;

	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean shouldFilter() {
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String path = request.getRequestURI().substring(request.getContextPath().length());
		logger.info(path);
				
		return true;
	}

	@Override
	public Object run() {
		logger.info(">auth");
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String token = request.getHeader(this.tokenHeader);
		String id = this.tokenUtils.getUserIdFromToken(token);
		User user = userRepository.findById(id);
		
		if(user==null){
			try {
				RequestContext.getCurrentContext().getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		logger.info(user.toString());
		
		logger.info("<auth");

		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}
