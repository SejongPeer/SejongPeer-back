package com.sejong.sejongpeer.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

import com.sejong.sejongpeer.global.common.constants.EnvironmentConstants;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional({OnProfileCondition.class})
public @interface ConditionalOnProfile {
	EnvironmentConstants[] value() default {EnvironmentConstants.LOCAL};
}
