package tech.lapsa.javax.caching;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface NamedCache {
    
    String name() default "defaultCache";
    
    Class<?> keyType() default Object.class;

    Class<?> valueType() default Object.class;
}
