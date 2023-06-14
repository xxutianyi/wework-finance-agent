package market.known.api.finance.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import market.known.api.finance.Utils.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

public class RequestInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        UUID traceId = UUID.randomUUID();
        MDC.put("TRACE_ID", traceId.toString());
        RequestHelper requestHelper = new RequestHelper(request);

        final String loggerName = "Request:%s";

        final Logger logger = LoggerFactory.getLogger(String.format(loggerName, traceId));

        final String loggerContent = "RemoteIP:%s, Method:%s, Path:%s, Query:%s, Body:%s";


        logger.info(String.format(loggerContent,
                requestHelper.getRemoteAddr(),
                requestHelper.getMethod(),
                requestHelper.getRequestURI(),
                requestHelper.getQueryString(),
                requestHelper.getBody()
        ));

        request.setAttribute("body", requestHelper.getBody());

        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) {

        MDC.clear();

    }
}
