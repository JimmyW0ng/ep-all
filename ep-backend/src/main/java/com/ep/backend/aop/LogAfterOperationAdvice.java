package com.ep.backend.aop;


import com.ep.common.annotation.LogInfoAnnotation;
import com.ep.domain.pojo.po.EpSystemLogPo;
import com.ep.domain.service.SystemLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 上午9:30 2018/4/26
 */
@Aspect
@Component
@Slf4j
public class LogAfterOperationAdvice {

    @Autowired
    private SystemLogService systemLogService;


    @Pointcut("@annotation(com.ep.common.annotation.LogInfoAnnotation)")
    private void loginfoAnnotation() {
    }//定义一个切入点

    @Pointcut("execution(* com.ep.backend.controller.*.*(..))")
    private void controller() {
    }//定义一个切入点
    /**
     * 前置通知：在某连接点之前执行的通知，但这个通知不能阻止连接点前的执行
     *
     * @param jp
     *            连接点：程序执行过程中的某一行为，例如，AServiceImpl.barA()的调用或者抛出的异常行为
     */
//	 @Before(value = "loginfoAnnotation()" )
//	public void doBefore(JoinPoint jp) {
//		String strLog = " "
//				+ jp.getTarget().getClass().getName() + "."
//				+ jp.getSignature().getName();
//	    System.out.print(strLog);
//	}

    /**
     * 环绕通知：包围一个连接点的通知，可以在方法的调用前后完成自定义的行为，也可以选择不执行
     * 类似Web中Servlet规范中的Filter的doFilter方法。
     *
     * @param pjp 当前进程中的连接点
     * @return
     * @throws Throwable
     */
    @Around(value = "loginfoAnnotation()&&controller()&& @annotation(annotation) &&args(object,..) ")
    public Object doAround(ProceedingJoinPoint pjp, LogInfoAnnotation annotation, Object object) throws Throwable {
        EpSystemLogPo po = new EpSystemLogPo();
        po.setModuleName(annotation.moduleName());
        po.setModuleDesc(annotation.desc());
        //构造登陆用户的信息
//        buildSysLog(sysLog);
//        Object[] args = pjp.getArgs();
//        StringBuffer  param = new StringBuffer();
//        //获取参数详细信息
//        for (Object temp : args){
//            if(null == temp){
//                continue;
//            }
//            if(!temp.getClass().isArray()){
//                Class<?> tempClass = temp.getClass();
//                String packageName =	tempClass.getPackage().getName();
//                if (temp != null && isRecordPackage(packageName)) {
//                    param.append(temp.toString()).append("|");
//                }
//            }
//
//        }
//        sysLog.setParams(param.toString());
        po.setRequestUrl(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        Object retVal = pjp.proceed();
//        if (retVal != null){
//            String result = retVal.toString();
//            po.setResult(result);
//        }
//        try {
//            sysLogService.insert(sysLog);
//        }catch (Exception e ){
//            logger.error("插入日志异常",e);
//        }
        return retVal;
    }

    private boolean isRecordPackage(String p) {
        String[] pack = {"java.lang", "java.util", "com.changfu.pojo"};
        for (String s : pack) {
            if (p.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
    /***
     * 构造日志对象， 主要是登陆后用户
     * @param sysLog
     */
//    private  void  buildSysLog(EpSystemLogPo po){
//        Subject subject = SecurityUtils.getSubject();
//        sysLog.setRemoteAddr(subject.getSession().getHost());
//        SysUserPojo sysUser = null;
//        if(subject != null && subject.isAuthenticated() ){
//            sysUser =  (SysUserPojo) subject.getSession().getAttribute(Constant.CURRENT_USER);
//            sysLog.setOperateName(sysUser.getName());
//            sysLog.setOperateId(sysUser.getId());
//            sysLog.setOperateTime(DateTools.getCurrentDateTime());
//        }
//    }

}
