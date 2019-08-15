package com.github.yafeiwang1240;

import com.github.yafeiwang1240.obrien.algorithm.DirectedGraph;
import com.github.yafeiwang1240.obrien.algorithm.node.ListNode;
import com.github.yafeiwang1240.obrien.bean.Convert;
import com.github.yafeiwang1240.obrien.bean.EnhanceBeanUtils;
import com.github.yafeiwang1240.obrien.fastreflect.BeanReflectUtils;
import com.github.yafeiwang1240.obrien.fastreflect.FastReflectUtils;
import com.github.yafeiwang1240.obrien.fastreflect.pack.BeanReflectPack;
import com.github.yafeiwang1240.obrien.initialization.InitializationUtils;
import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.Initializer;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializerGroup;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.lang.Lists;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        test10();
    }

    public static void test10() {
        ListNode node1 = new ListNode("1");
        ListNode node2 = new ListNode("3");
        ListNode node3 = new ListNode("4");
        ListNode node4 = new ListNode("4");
        ListNode node5 = new ListNode("5");
        ListNode node6 = new ListNode("6");
        ListNode node7 = new ListNode("7");
        node1.addNext(node2);
        node1.addNext(node3);
        node2.addNext(node3);
        node3.addNext(node4);
        node4.addNext(node3);
        node5.addNext(node7);
        node6.addNext(node1);
        boolean r = DirectedGraph.ring(node1);
        System.out.println(r);
    }

    public static void test9() throws Exception {
        MethodAndField mf = new MethodAndField();
        mf.setInsert(true);
        mf.setName("我是王亚飞");
        mf.setList(Lists.asList(new InitForm()));
        mf.setNoList(Lists.asList(1, 2, "5"));
        Map<String, Object> map = EnhanceBeanUtils.toMapNotNull(mf);
        MethodAndField t = EnhanceBeanUtils.toObject(map, MethodAndField.class);
        System.out.println(t.getName());
    }

    public static void test8() throws Exception {
        Field field = MethodAndField.class.getDeclaredField("noList");
        Type type = field.getGenericType();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        System.out.println(types[0]);
    }

    public static void test7() throws Exception {
        Field field = MethodAndField.class.getDeclaredField("list");
        Type type = field.getGenericType();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        System.out.println(types[0]);
    }

    public static void test6() {
        BeanReflectPack pack = BeanReflectUtils.getBeanReflectPack(MethodAndField.class);
        System.out.println(pack);
    }

    public static void test5() throws Exception {
        MethodAndField obj = new MethodAndField();
        Field field = obj.getClass().getDeclaredField("name");
        field.setAccessible(true);
        Method set = obj.getClass().getDeclaredMethod("setName", field.getType());
        set.setAccessible(true);
        Method get = obj.getClass().getDeclaredMethod("getName");
        get.setAccessible(true);
        int time = Integer.MAX_VALUE;
        long now = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            field.set(obj, "hello world");
        }
        System.out.println("field.set(obj, value): " + (System.currentTimeMillis() - now));
        now = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            set.invoke(obj, "set hello world!");
        }
        System.out.println("setName(value): " + (System.currentTimeMillis() - now));
        now = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            field.get(obj);
        }
        System.out.println("field.get(obj): " + (System.currentTimeMillis() - now));
        now = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            get.invoke(obj);
        }
        System.out.println("getName(obj): " + (System.currentTimeMillis() - now));
    }

    public static class MethodAndField {
        protected String name;
        protected Boolean exit;
        protected boolean insert;
        protected boolean _insert;
        protected List<InitForm> list;
        protected List noList;
        protected Integer val = 3;
        protected double value = 4;

        public List getNoList() {
            return noList;
        }

        public void setNoList(List noList) {
            this.noList = noList;
        }

        public List<InitForm> getList() {
            return list;
        }

        public void setList(List<InitForm> list) {
            this.list = list;
        }

        public boolean is_insert() {
            return _insert;
        }

        public void set_insert(boolean _insert) {
            this._insert = _insert;
        }

        public boolean isInsert() {
            return insert;
        }

        public void setInsert(boolean insert) {
            this.insert = insert;
        }

        public Boolean getExit() {
            return exit;
        }

        public void setExit(Boolean exit) {
            this.exit = exit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void test4() {
        InitForm form = new InitForm();
        try {
            InitializationUtils.initialize(form);
            System.out.println(form.name);
        } catch (InitializedFailedException e) {
            e.printStackTrace();
        }
    }

    public static class InitForm {
        @Initializer(initializations = Init.class)
        String name = "hh";
        String value;
    }

    @InitializerGroup
    public class Init implements Initialized<String> {

        @Override
        public String execute(String data) throws InitializedFailedException {
            return "hello world";
        }
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
