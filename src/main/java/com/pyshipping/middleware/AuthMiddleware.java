package com.pyshipping.middleware;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pyshipping.common.autosave.BaseContext;
import com.pyshipping.common.jwt.Jwt;
import com.pyshipping.common.jwt.JwtBody;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.model.Employee;
import com.pyshipping.service.EmployeeSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录权限验证
 */
@WebFilter(filterName = "authFilter",urlPatterns = "/*")
@Slf4j
public class AuthMiddleware implements Filter {
    @Autowired
    private Jwt jwt;
    // 路径比较方法 支持通配符
    @Autowired
    private EmployeeSrv employeeSrv;
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            filterChain.doFilter(request,response);
            return;
        }

        String uri = request.getRequestURI();

        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/static/**",
                "/upload/download",
                "/sms/**",
                "/goods/page",
                "/setmeal/page",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        // 判断是否放行
        if (isNext(uri,urls)) {
            // 放行
            log.info("放行请求: {}",uri);
            filterChain.doFilter(request,response);
            return;
        }
        // 判断是否有效token
        String bearToken;
        try {
            bearToken = request.getHeader("authorization");
            if (bearToken == null || bearToken.equals("") || bearToken.equals("null")) {
                // 返回验证失败控制器
                request.setAttribute("authException",new CustomException("header token required"));
                request.getRequestDispatcher("/unauthenticated").forward(request,response);
                return;
            }
        } catch (Exception ex) {
            // 返回异常处理控制器
            request.setAttribute("authException",new CustomException("header token required"));
            request.getRequestDispatcher("/unauthenticated").forward(request,response);
            return;
        }
        JwtBody jwtBody = null;
        try {
            jwtBody = jwt.verifyJwt(bearToken);
            if (jwtBody == null) {
                // 返回验证失败控制器
                request.setAttribute("authException",new CustomException("verify token return back null"));
                request.getRequestDispatcher("/unauthenticated").forward(request,response);
                return;
            } else {
                if (jwtBody.getStatus() == 6969) {
                    // 查询库验证管理员
                    Employee employee = employeeSrv.getEmployeeById(jwtBody.getUserID());
                    if (employee == null) {
                        request.setAttribute("authException",new CustomException("verify token return back null"));
                        request.getRequestDispatcher("/unauthenticated").forward(request,response);
                        return;
                    }
                    request.setAttribute("employee",jwtBody);
                    // 存储当前线程局部变量 在request 上下文之外获取当前登录用户数据
                    BaseContext.setEmployeeId(jwtBody.getUserID());
                } else {
                    request.setAttribute("user",jwtBody);
                    // 存储当前线程局部变量 在request 上下文之外获取当前登录用户数据
                    BaseContext.setUserId(jwtBody.getUserID());
                }

            }
            filterChain.doFilter(request,response);
        } catch (JWTDecodeException | TokenExpiredException ex) {
            // 返回异常处理控制器
            request.setAttribute("authException",ex);
            request.getRequestDispatcher("/unauthenticated").forward(request,response);
            return;
        }
    }

    // 放行方法
    public boolean isNext(String uri,String[] urls) {
        for (String url : urls) {
           boolean match = PATH_MATCHER.match(url,uri);
           if (match) {
               return true;
           }
        }
        return false;
    }
}
