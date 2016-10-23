package soselab.easylearn.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import soselab.easylearn.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends ZuulFilter {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${cerberus.token.header}")
    private String tokenHeader;


    @Value("${easylearn.testing}")
    private boolean isTesting;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String path = request.getRequestURI().substring(request.getContextPath().length());
        logger.info(path);

        if (isTesting){
            RequestContext.getCurrentContext().addZuulRequestHeader("user-id", "id");
            return false;
        }

        return true;
    }

    @Override
    public Object run() {
        logger.info(">auth");
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String token = request.getHeader(this.tokenHeader);
        String id = this.tokenUtils.getUserIdFromToken(token);
        if (id == null) {
            try {
                RequestContext.getCurrentContext().getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(id);

        RequestContext.getCurrentContext().addZuulRequestHeader("user-id", id);
        logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

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
