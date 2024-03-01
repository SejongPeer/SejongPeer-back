package com.sejong.sejongpeer.global.config.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
	private static final String SERVER_NAME = "Sejong Peer Server";
	private static final String API_TITLE = "Sejong Peer 서버 API 문서";
	private static final String API_DESCRIPTION = "Sejong Peer 서버 API 문서입니다.";
	private static final String GITHUB_URL = "https://github.com/SejongPeer/SejongPeer-back";

	@Value("${swagger.version}")
	private String version;

	@Bean
	public OpenAPI openAPI(ServletContext servletContext) {
		Server server =
			new Server().url(servletContext.getContextPath()).description(API_DESCRIPTION);
		return new OpenAPI()
			.servers(List.of(server))
			.addSecurityItem(securityRequirement())
			.components(authSetting())
			.info(swaggerInfo());
	}

	private Components authSetting() {
		return new Components()
			.addSecuritySchemes(
				"accessToken",
				new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization"));
	}

	private Info swaggerInfo() {
		License license = new License();
		license.setUrl(GITHUB_URL);
		license.setName(SERVER_NAME);

		return new Info()
			.version("v" + version)
			.title(API_TITLE)
			.description(API_DESCRIPTION)
			.license(license);
	}

	private SecurityRequirement securityRequirement() {
		SecurityRequirement securityRequirement = new SecurityRequirement();
		securityRequirement.addList("accessToken");
		return securityRequirement;
	}

	@Bean
	public ModelResolver modelResolver(ObjectMapper objectMapper) {
		// 객체 직렬화
		return new ModelResolver(objectMapper);
	}
}
