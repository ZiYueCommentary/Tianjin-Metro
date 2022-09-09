package ziyue.tjmetro;

import java.lang.annotation.*;

/**
 * This annotation means this method/type was copy & paste from super class, without any modify.
 * If you met bugs with this thing, please check is that same with super.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface PrivateInSuper
{
}
