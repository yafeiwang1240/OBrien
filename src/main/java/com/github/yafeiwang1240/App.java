package com.github.yafeiwang1240;

import com.github.yafeiwang1240.obrien.bean.Convert;
import com.github.yafeiwang1240.obrien.fastreflect.FastReflectUtils;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectClassException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectFieldException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectMethodException;
import com.github.yafeiwang1240.obrien.pool.BeanPool;
import com.github.yafeiwang1240.obrien.pool.BeanPoolFactory;
import com.github.yafeiwang1240.obrien.pool.execption.BeanPoolSizeArgumentException;
import com.github.yafeiwang1240.obrien.pool.model.BaseBean;
import com.github.yafeiwang1240.obrien.pool.model.BeanFactory;
import com.github.yafeiwang1240.obrien.stacktrace.Verification;
import com.github.yafeiwang1240.obrien.stacktrace.VerificationResult;
import com.github.yafeiwang1240.obrien.stacktrace.annotation.BeanRequest;
import com.github.yafeiwang1240.obrien.stacktrace.annotation.MethodRequest;
import com.github.yafeiwang1240.obrien.template.InterfaceTemplateClass;
import com.github.yafeiwang1240.obrien.validation.IValidator;
import com.github.yafeiwang1240.obrien.validation.ValidationUtils;
import com.github.yafeiwang1240.obrien.validation.annotation.Length;
import com.github.yafeiwang1240.obrien.validation.annotation.NotNull;
import com.github.yafeiwang1240.obrien.validation.annotation.UserDefined;
import com.github.yafeiwang1240.obrien.validation.exception.ValidateInitialException;
import com.github.yafeiwang1240.obrien.validation.model.ValidateResult;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ReflectClassException, ReflectFieldException, ReflectMethodException, BeanPoolSizeArgumentException {
        test3();
    }

    public static void test3() throws BeanPoolSizeArgumentException {
        BeanPool<Session> beanPool = BeanPoolFactory.newBeanPool(2, 5, 2, TimeUnit.MINUTES, new SessionFactory());
        List<Session> lll = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Session session = beanPool.getBean();
            System.out.println(session);
            if (i % 2 == 0) {
                if (session != null) session.release();
            } else {
                if (session != null) lll.add(session);
            }
        }
    for (int i = 0; i < 4; i++) {
        lll.get(i).release();
    }
        System.out.println("/////////////////////");
    }

    public static class SessionFactory implements BeanFactory {

        @Override
        public BaseBean newBean() {
            return new Session();
        }
    }

    public static class Session extends BaseBean {
        public void print(String v) {
            System.out.println("hello " + v);
        }
    }

    public static void test2() throws Exception {
        Method method = App.class.getMethod("test", Integer.class);
        long t = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            method.invoke(null, i);
        }
        System.out.println();
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            App.test(i);
        }
        System.out.println();
        System.out.println(System.currentTimeMillis() - t);

        t = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            FastReflectUtils.methodInvoke(App.class, "test", new Object[]{i});
        }
        System.out.println();
        System.out.println(System.currentTimeMillis() - t);
    }

    String ls;
    public static void test(Integer value) {
        System.out.print(value);
    }

    public static class TT implements InterfaceTemplateClass<App> {

    }
    public static void test1() {
        DD dd = App.class.getDeclaredAnnotation(DD.class);
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface DD {
        String value() default "";
    }

    public static void test() {
        STACKTRANCE.call();
        FORM form = new FORM();
        CloneForm fff = null;
        try {
            fff = form.to(CloneForm.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ValidateResult result = null;
        try {
            result = ValidationUtils.validate(form);
        } catch (ValidateInitialException e) {
            System.err.println(e.getMessage());
        }
        for(String m : result.getMessages()) {
            System.out.print(m + "\t");
        }

    }

    public static class VV implements IValidator<FORM> {

        @Override
        public boolean isValid(FORM object) {
            if(object.v > 8) {
                return false;
            }
            return true;
        }

        @Override
        public String massage() {
            return "请传数值小于8";
        }
    }

    @UserDefined(validators = {VV.class})
    public static class FORM extends Convert {
        private int v = 10;

        @NotNull(message = "id 不允许为空")
        private Object id;

        @Length(max = 8, message = "名称长度不超过8")
        private String name = "0201enndaofknsdaifdasfds";
    }

    public static class CloneForm {
        private int v;
        private Object id;
        private String name;
    }

    @BeanRequest
    public static class STACKTRANCE {

        @MethodRequest
        public static void call() {
            valid(STACKTRANCE.class);
        }
    }


    public static void valid(Class clazz) {
        VerificationResult result = null;
        try {
            result = Verification.validStack(clazz, App.class, App.class.getDeclaredMethod("valid", Class.class));
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());
        }
        for(String m : result.getMessages()) {
            System.out.print(m + "\t");
        }
        System.out.print("\n");
    }
}
