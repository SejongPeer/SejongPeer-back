package com.sejong.sejongpeer.global.annotation;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.sejong.sejongpeer.global.common.constants.EnvironmentConstants;

public class OnProfileCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String[] activeProfiles = context.getEnvironment().getActiveProfiles();
		EnvironmentConstants[] targetProfiles = getTargetProfiles(metadata);

		return Arrays.stream(targetProfiles)
			.anyMatch(
				targetProfile ->
					Arrays.asList(activeProfiles).contains(targetProfile.getValue()));
	}

	private EnvironmentConstants[] getTargetProfiles(AnnotatedTypeMetadata metadata) {
		return (EnvironmentConstants[])
			requireNonNull(
				metadata.getAnnotationAttributes(
					ConditionalOnProfile.class.getName()))
				.get("value");
	}
}
