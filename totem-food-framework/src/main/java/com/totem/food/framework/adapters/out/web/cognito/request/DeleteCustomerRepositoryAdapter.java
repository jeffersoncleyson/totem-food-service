package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeleteUserRequest;

import java.util.Optional;

@AllArgsConstructor
@Component
public class DeleteCustomerRepositoryAdapter implements IRemoveRepositoryPort<CustomerModel> {

	private final CognitoClient cognitoClient;

	@Override
	public void removeItem(String accessToken) {

		String token = Optional.ofNullable(accessToken)
				.map(t -> t.replace(OAuth2AccessTokenInterceptor.BEARER.concat(" "), ""))
				.filter(StringUtils::isNotBlank)
				.orElseThrow(
						() -> new InvalidInput("Missing token ".concat(OAuth2AccessTokenInterceptor.BEARER))
				);

		try (CognitoIdentityProviderClient client = cognitoClient.connect()) {

			DeleteUserRequest deleteRequest = DeleteUserRequest.builder()
					.accessToken(token)
					.build();

			client.deleteUser(deleteRequest);

		} catch (CognitoIdentityProviderException e) {
			throw new ExternalCommunicationInvalid("Error to integrate with user service");
		}
	}
}
