package com.sejong.sejongpeer.infra.config.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sejong.sejongpeer.infra.config.properties.S3Properties;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

	private final S3Properties s3Properties;

	@Bean
	@Primary
	public BasicAWSCredentials awsCredentialsProvider() {
		return new BasicAWSCredentials(s3Properties.accessKey(),
			s3Properties.secretKey());
	}

	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider()))
			.withRegion(s3Properties.region())
			.build();
	}

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
			.region(Region.of(s3Properties.region()))
			.credentialsProvider(ProfileCredentialsProvider.create())
			.build();
	}
}
