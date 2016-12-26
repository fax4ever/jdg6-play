package it.redhat.demo.config;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * Created by fabio.ercoli@redhat.com on 26/12/16.
 */

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Documented
public @interface CiaoCache {
}
