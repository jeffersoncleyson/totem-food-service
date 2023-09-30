package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import com.totem.food.framework.adapters.out.web.cognito.utils.CognitoUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeleteUserRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Component
public class DeleteCustomerRepositoryAdapter implements IRemoveRepositoryPort<CustomerModel> {

	private final CognitoClient cognitoClient;
	private static final String BEARER_ = "Bearer ";

	@Override
	public void removeItem(String accessToken) {

		String token = Optional.ofNullable(accessToken)
				.map(t -> t.replace(BEARER_, ""))
				.orElseThrow();

		try (CognitoIdentityProviderClient client = cognitoClient.connect()) {

			DeleteUserRequest deleteRequest = DeleteUserRequest.builder()
					.accessToken(token)
					.build();

			client.deleteUser(deleteRequest);

		} catch (CognitoIdentityProviderException e) {
			throw new RuntimeException(e);
		}
	}
}
