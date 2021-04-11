package authorizationserver.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Data
public class YAMLConfig {
	private String name;
	private String enviroment;
	private boolean enabled;
	@Value("${server.address}")
	private String address;
	@Value("${server.port}")
	private String port;
	private String resoucesServer;
	private String createUserPath;
	private String secret;
	
	public String getBase64UrlSecret() {
		return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(this.secret.getBytes(StandardCharsets.UTF_8));
	}
}
