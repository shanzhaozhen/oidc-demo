package sample.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: shanzhaozhen
 * @Date: 2022-01-29
 * @Description: 初始化oauth2客户端信息
 */
//@Component
public class InitRegisteredClient implements CommandLineRunner {

    private final RegisteredClientRepository registeredClientRepository;

	public InitRegisteredClient(RegisteredClientRepository registeredClientRepository) {
		this.registeredClientRepository = registeredClientRepository;
	}

	/**
     * clientSettings.tokenEndpointAuthenticationSigningAlgorithm 为 SignatureAlgorithm.RS256
     * clientSettings.jwkSetUrl 必填，授权服务器在这种模式下会校验
     * tokenSettings.idTokenSignatureAlgorithm 为 SignatureAlgorithm.RS256 ，指生成的 token 也加密
     * tokenSettings.accessTokenFormat 必须为 OAuth2TokenFormat.SELF_CONTAINED
     * clientAuthenticationMethod 为 ClientAuthenticationMethod.PRIVATE_KEY_JWT
     */

    @Override
    public void run(String... args) {
		RegisteredClient confidentialClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("auth")
				.clientSecret("{noop}secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/auth")
				.redirectUri("http://127.0.0.1:8080/authorized")
				.scope(OidcScopes.OPENID)
				.scope("message.read")
				.scope("message.write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				.build();
//		RegisteredClient publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("auth")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.redirectUri("http://127.0.0.1:4200")
//				.redirectUri("http://127.0.0.1:4200/silent-renew.html")
//				.redirectUri("http://127.0.0.1:8000/oidc")
//				.scope(OidcScopes.OPENID)
//				.scope("message.read")
//				.scope("message.write")
//				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).requireProofKey(true).build())
//				.build();

		// Save registered client in db as if in-memory
		registeredClientRepository.save(confidentialClient);
//		registeredClientRepository.save(publicClient);
    }
}
