package com.sejong.sejongpeer.global.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UrlConstants {
	DEV_SERVER_URL("https://api-sejongpeer.shop"),
	LOCAL_SERVER_URL("http://localhost:8080"),

	DEV_DOMAIN_URL("https://www.api-sejongpeer.shop"),
	LOCAL_DOMAIN_URL("http://localhost:3000"),
	LOCAL_SECURE_DOMAIN_URL("https://localhost:3000"),

	DEV_URL("https://sejongpeer.com"),
	DEV_NONE_SECURE_URL("http://sejongpeer.com"),
	WWW_DEV_NONE_SECURE_URL("http://www.sejongpeer.com"),
	WWW_DEV_URL("https://www.sejongpeer.com"),

	CLOUDFRONT_URL("https://d3tbacvbyfqs43.cloudfront.net");

	private String value;
}
