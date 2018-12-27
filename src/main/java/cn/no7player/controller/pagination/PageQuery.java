package cn.no7player.controller.pagination;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PageQuery {
    String pageNum() default "pageNum";//页号的参数名
    String pageSize() default "pageSize";//每页行数的参数名
}

