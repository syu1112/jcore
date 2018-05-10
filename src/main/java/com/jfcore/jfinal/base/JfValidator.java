package com.jfcore.jfinal.base;

import com.jfcore.web.vo.JsonRtn;
import com.jfcore.web.vo.RtnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public abstract class JfValidator extends Validator {
    private static Logger logger = LoggerFactory.getLogger(JfValidator.class);

    private JsonRtn<?> errorRtn;

    protected boolean hasError() {
        return invalid;
    }

    protected void addErrorRtn(JsonRtn<?> rtn) {
        this.errorRtn = rtn;
        super.addError("errorRtn", "errorRtn");
    }

    @Override
    protected void addError(String errorKey, String errorMessage) {
        logger.error("err para,{}:{}", errorKey, errorMessage);
        super.addError(errorKey, errorMessage);
    }

    @Override
    protected void handleError(Controller c) {
        if (errorRtn != null) {
            JfUtil.renderRtn(c, errorRtn);
            return;
        }
        JfUtil.renderRtn(c, RtnFactory.invalid);
    }

    protected void checkRegex(String field, String regExpression) {
        validateRegex(field, regExpression, true, field, controller.getPara(field));
    }

    protected void checkString(String field, int minLen, int maxLen) {
        validateString(field, minLen, maxLen, field, controller.getPara(field));
    }
    
    protected void checkNotEmpty(String field) {
        validateRequiredString(field, field, controller.getPara(field));
    }

    protected void checkInteger(String field, int min, int max) {
        validateInteger(field, min, max, field, controller.getPara(field));
    }

    protected void checkDouble(String field, int min, int max) {
        validateDouble(field, min, max, field, controller.getPara(field));
    }

    protected void checkLong(String field, int min, int max) {
        validateLong(field, min, max, field, controller.getPara(field));
    }

    public JsonRtn<?> getErrorRtn() {
        return errorRtn;
    }
}
