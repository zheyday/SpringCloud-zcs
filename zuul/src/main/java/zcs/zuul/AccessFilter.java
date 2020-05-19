package zcs.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

/**
 * 过滤器
 * 需要在应用主类为其创建Bean
 */
public class AccessFilter extends ZuulFilter {

    /**
     * 过滤器的类型
     * 决定过滤器在请求的哪个生命周期执行
     * @return pre代表在请求路由之前执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 存在多个过滤器时，根据该值决定执行顺序
     * @return 执行顺序
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断该过滤器是否需要被执行
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体逻辑
     * @return 不需要
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String accessToken = request.getParameter("name");
        if (accessToken == null) {
//            过滤该请求
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.addZuulResponseHeader("content-type","text/html;charset=utf-8");
            ctx.setResponseBody("z_error:无参数访问");
        }
        return null;
    }
}
