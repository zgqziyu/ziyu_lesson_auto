package com.ziyu.admin.core.aspect;

import com.ziyu.admin.core.utils.JsonUtil;
import com.ziyu.admin.core.utils.PageResultSet;
import com.ziyu.admin.core.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.ziyu.admin.core.annotation.SystemLog;
import com.ziyu.admin.core.utils.WebUtil;
import com.ziyu.admin.modules.system.po.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * \* User: ziyu
 * \* Description: 系统日志切面
 */
@Aspect
@Component
public class SystemLogAspect {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Pointcut("@annotation(systemLog)")
    public void systemLogPointcut(SystemLog systemLog) {
    }

    @Around("systemLogPointcut(systemLog)")
    public Object aroundMethod(ProceedingJoinPoint point, SystemLog systemLog) throws Throwable {
        long time = System.currentTimeMillis();
        try {
            Object returns = point.proceed();
            save(point, returns, systemLog, System.currentTimeMillis() - time);
            return returns;
        } catch (Throwable e) {
            save(point, e, systemLog, System.currentTimeMillis() - time);
            throw e;
        }
    }

    private void save(ProceedingJoinPoint point, Object returns, SystemLog systemLog, Long time) {
        String sign = point.getSignature().toString();

        // 获取相关参数
        WebUtil wu = WebUtil.getInstance();
        // 请求对象
        HttpServletRequest req = wu.getRequest();
        // 当前用户
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        // 查询参数
        String qs = req.getQueryString();
        // url
        String url = req.getRequestURL().append(qs == null ? "" : "?" + qs).toString();
        // IP地址
        String ip = wu.getIpAddress();
        // 方法
        String method = req.getMethod();
        // 协议
        String protocol = req.getProtocol();
        String status = null;
        String msg = null;
        String desc = !StringUtils.isEmpty(systemLog.desc()) ? systemLog.desc() : systemLog.value();
        if (returns != null && returns instanceof Result) {
            Result result = (Result) returns;
            status = result.getCode();
            msg = result.getMsg();
        }

        if (returns != null && returns instanceof PageResultSet) {
            msg = "查询成功";
            status = HttpStatus.OK.toString();
        }

        String text = null;
        if (returns != null) {
            String tmp;
            if (returns instanceof CharSequence) {
                tmp = returns.toString();
            } else if (returns instanceof Throwable) {
                String m = ((Throwable) returns).getMessage();
                String n = returns.getClass().getSimpleName();
                tmp = m == null ? n : n + ": " + m;
            } else {
                tmp = JsonUtil.getInstance().obj2json(returns);
            }
            text = tmp;
        }

        // 构造入库参数
        Log log = new Log();
        // 用户信息
        log.setUsername(username);
        // 请求信息
        log.setIp(ip);
        log.setReqMethod(method + " " + protocol);
        log.setReqUri(url);
        // 执行信息
        log.setExecMethod(sign);
        log.setExecTime(time);
        // 请求参数
        if ("POST".equals(method)) {
            log.setArgs(JsonUtil.getInstance().obj2json(point.getArgs()[0]));
        }
        log.setStatus(msg);
        log.setExecDesc(desc);
        // 响应信息
        log.setReturnVal(text);
        log.setCreateTime(new Date());
        try {
            // 入库
            applicationEventPublisher.publishEvent(log);
        } catch (Exception e) {

        }
    }

}
