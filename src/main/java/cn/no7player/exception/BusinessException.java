/*
 * Copyright 2014 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.no7player.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1146019055114385175L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }


    public BusinessException(List<FieldError> fieldErrors) {
        super((null != fieldErrors && fieldErrors.size() > 0) ? fieldErrors.get(0).getDefaultMessage() : "校验失败");
    }
}
