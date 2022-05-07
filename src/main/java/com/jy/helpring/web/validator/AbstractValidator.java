package com.jy.helpring.web.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** 중복 검사를 위한 Validator 구현 추상 클래스 **/
@Slf4j
public abstract class AbstractValidator<T> implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @SuppressWarnings("unchekced")
    @Override
    public void validate(Object target, Errors errors) {
        try{
            doValidate((T) target, errors); // 유효성 검증 로직
        } catch (IllegalStateException e){
            log.error("중복 검증 에러", e);
            throw e;
        }
    }

    /**
     * 유효성 검증 로직
     **/
    protected abstract void doValidate(final T dto, final Errors errors);
}
