package cn.no7player.controller.pagination;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class PageAop {
      @Around("@annotation(pageQuery)")
      public Object pagingQuery(ProceedingJoinPoint joinPoint, PageQuery pageQuery) throws Throwable {

              String pageNumParameterName = pageQuery.pageNum();
              String pageSizeParameterName = pageQuery.pageSize();
              //获取request，从中获取分页参数
              ServletRequestAttributes currentRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
              HttpServletRequest request = currentRequestAttributes.getRequest();
              String pageNum = request.getParameter(pageNumParameterName);
              String pageSize = request.getParameter(pageSizeParameterName);
              if (StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
                  try {
                      PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
                      return joinPoint.proceed();
                  } finally {//保证线程变量被清除
                      if (PageHelper.getLocalPage() != null)
                          PageHelper.clearPage();
                  }
              }

          return joinPoint.proceed();
      }
}
