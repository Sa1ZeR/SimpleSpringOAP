package com.sa1zer.botcamp7.aspect;

import com.sa1zer.botcamp7.annotation.NotBlank;
import com.sa1zer.botcamp7.annotation.NotNull;
import com.sa1zer.botcamp7.annotation.Regex;
import com.sa1zer.botcamp7.annotation.Valid;
import com.sa1zer.botcamp7.exeption.ValidationException;
import com.sa1zer.botcamp7.payload.request.CreateUserRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Order(1)
public class ValidAspect {

    public static final Unsafe UNSAFE;
    public static final MethodHandles.Lookup LOOKUP;

    static {
        try {
            MethodHandles.publicLookup(); // Just to initialize class

            // Get unsafe to get trusted lookup
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafeField.get(null);

            // Get trusted lookup and other stuff
            Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(UNSAFE.staticFieldBase(implLookupField), UNSAFE.staticFieldOffset(implLookupField));
        } catch (Throwable exc) {
            throw new InternalError(exc);
        }
    }

    /**
     * Точка среза, которая определяет выполнение метода, у которого в параметрах используется аннотация @Valid
     */
    @Pointcut("execution(* *(.., @com.sa1zer.botcamp7.annotation.Valid (*), ..))")
    public void validMethodParams() {}

    /**
     * Срабатывает перед выполнением метода, проверяет валидность параметров
     */
    @Before("validMethodParams()")
    public void validateMethodParams(JoinPoint joinPoint) throws Throwable {
        //Получение метода
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //получение параметров
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        for(int i = 0; i < parameters.length; i++) {
            Parameter p = parameters[i];
            Object arg = args[i];

            //если параметр аннотирован @Valid
            if(p.isAnnotationPresent(Valid.class)) {
                //и содержит @NotNull
                if(p.isAnnotationPresent(NotNull.class)) {
                    if(arg == null)
                        throw new ValidationException(String.format("Field %s must not be null", p.getName()));
                }
                //и содержит @NotBlank
                if(p.isAnnotationPresent(NotBlank.class)) {
                    if(arg instanceof String) {
                        String value = (String) arg;
                        if(value.isEmpty()) throw new ValidationException(String.format("String %s must not be empty", p.getName()));
                    }
                }
                //и содержит @Pattern
                if(p.isAnnotationPresent(Regex.class)) {
                    if(arg instanceof String value) {
                        Regex annotation = p.getAnnotation(Regex.class);
                        if(!validateString(value, annotation)) throw new ValidationException(String.format("String %s is invalid", p.getName()));
                    }
                }
                //Проверим содержимое класса
                validateClassFields(p.getType(), arg);
            }
        }
    }

    /**
     * Проверяет поля класса
     * @param clazz класс, который необходимо проверить
     * @param instance - instance проверяемого объекта
     * @throws IllegalAccessException
     */
    private void validateClassFields(Class<?> clazz, Object instance) throws Throwable {
        Field[] declaredFields = clazz.getDeclaredFields();

        for(Field field : declaredFields) {
            if(field.isAnnotationPresent(NotNull.class)) {
                notNullValidation(field, instance);
            }
            if (field.isAnnotationPresent(NotBlank.class) && field.getType().isAssignableFrom(String.class)) {
                notBlankValidation(field, instance);
            }
            if (field.isAnnotationPresent(Regex.class) && field.getType().isAssignableFrom(String.class)) {
                patternValidation(field, instance);
            }
        }
    }

    /**
     * Проверка поля на null
     * @param field - поле, которое необходимо проверить
     * @throws IllegalAccessException
     */
    private void notNullValidation(Field field, Object instance) throws Throwable {
        Object o =  LOOKUP.unreflectGetter(field).invoke(instance);
        if(o == null)
            throw new ValidationException(String.format("Field %s must not be null", field.getName()));
    }

    /**
     * Проверка поля на пустоту
     * @param field - поле, которое необходимо проверить
     * @throws IllegalAccessException
     */
    private void notBlankValidation(Field field, Object instance) throws Throwable {
        String o = (String) LOOKUP.unreflectGetter(field).invoke(instance);
        if(o.isEmpty()) throw new ValidationException(String.format("String %s must not be empty", field.getName()));
    }

    /**
     * Проверка поля на соответствие с regex
     * @param field - поле, которое необходимо проверить
     * @throws IllegalAccessException
     */
    private void patternValidation(Field field, Object instance) throws Throwable {
        String o = (String) LOOKUP.unreflectGetter(field).invoke(instance);

        if(o == null) return;

        Regex annotation = field.getAnnotation(Regex.class);
        if(!validateString(o, annotation)) throw new ValidationException(String.format("String %s is invalid", field.getName()));
    }

    /**
     * Проверка сроки и regex из аннотации
     * @param value - строка, которую необходимо проверить
     * @param annotation - аннотация с настроенным regex
     * @return true - если строка соответствует regex
     */
    private boolean validateString(String value, Regex annotation) {
        java.util.regex.Pattern compile = java.util.regex.Pattern.compile(annotation.regex());
        return compile.matcher(value).matches();
    }
}
