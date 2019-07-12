package com.github.yafeiwang1240.obrien.validation;

import com.github.yafeiwang1240.obrien.lang.Maps;
import com.github.yafeiwang1240.obrien.validation.annotation.ValidateRule;
import com.github.yafeiwang1240.obrien.validation.exception.ValidateInitialException;
import com.github.yafeiwang1240.obrien.validation.impl.AbstractValidator;
import com.github.yafeiwang1240.obrien.validation.model.ValidatePack;
import com.github.yafeiwang1240.obrien.validation.model.ValidateResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidationUtils {

    protected static Map<Class, ValidatePack> validatorCache = Maps.create(ConcurrentHashMap::new);

    public static ValidateResult validate(Object o) throws ValidateInitialException {
        return validate("", o);
    }

    public static ValidateResult validate(String group, Object o) throws ValidateInitialException {
        ValidateResult result = new ValidateResult();
        result.setStatus(ValidateResult.Status.SUCCESS);
        if( null == o ) return result;
        ValidatePack validatePack = null;
        if(validatorCache.containsKey(o.getClass())) {
            validatePack = validatorCache.get(o.getClass());
        } else {
            try {
                validatePack = getAndCacheValidator(o.getClass());
            } catch (NoSuchMethodException e) {
                throw new ValidateInitialException("校验类没有提供带参数的构造方法！", e);
            } catch (InstantiationException e) {
                throw new ValidateInitialException("校验类构造方法访问失败！", e);
            } catch (InvocationTargetException e) {
                throw new ValidateInitialException("校验类访问异常！", e);
            } catch (IllegalAccessException e) {
                throw new ValidateInitialException("校验类访问异常！", e);
            }
        }
        return validatePack.validate(group, o);
    }

    private static synchronized ValidatePack getAndCacheValidator(Class<?> clazz) throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        if(validatorCache.containsKey(clazz)) return validatorCache.get(clazz);
        ValidatePack pack = new ValidatePack();
        Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
        for(Annotation annotation : classAnnotations) {
            if(annotation.annotationType().isAnnotationPresent(ValidateRule.class)) {
                ValidateRule validateRule = annotation.annotationType().getAnnotation(ValidateRule.class);
                AbstractValidator validator = validateRule.value().getConstructor(annotation.annotationType()).newInstance(annotation);
                pack.addClassValidator(getAnnotationGroup(annotation), validator);
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if(annotations != null && annotations.length > 0) {
                for(Annotation annotation : annotations) {
                    if(annotation.annotationType().isAnnotationPresent(ValidateRule.class)) {
                        ValidateRule validateRule = annotation.annotationType().getAnnotation(ValidateRule.class);
                        AbstractValidator validator = validateRule.value().getConstructor(annotation.annotationType()).newInstance(annotation);
                        pack.addFieldValidator(field, getAnnotationGroup(annotation), validator);
                    }
                }
            }
        }
        validatorCache.put(clazz, pack);
        return pack;
    }

    private static String getAnnotationGroup(Annotation annotation) {
        String group = "";
        try {
            Method groupMethod = annotation.annotationType().getDeclaredMethod("group");
            Object groupObject = groupMethod.invoke(annotation);
            if(groupObject != null) {
                group = groupObject.toString();
            }
        } catch (NoSuchMethodException e) {}
          catch (IllegalAccessException
                  | IllegalArgumentException
                  | InvocationTargetException e) {}

        return group;
    }
}
