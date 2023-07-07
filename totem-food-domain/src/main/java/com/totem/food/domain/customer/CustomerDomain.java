package com.totem.food.domain.customer;

import com.totem.food.domain.exceptions.InvalidDomainField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDomain {

    //########### Main Fields
    private String id;
    private String name;
    private String cpf;
    private String email;
    private String mobile;
    private String password;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

    //########### Constants
    public static final int MAX_CUSTOMER_LENGTH = 100;


    public void validateName() {

        if (StringUtils.isNotEmpty(name) && name.length() > MAX_CUSTOMER_LENGTH) {
            throw new InvalidDomainField(CustomerDomain.class, "name", "Max length accepted is ".concat(String.valueOf(MAX_CUSTOMER_LENGTH)));
        }

    }

    public void validEmailAddress() {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (StringUtils.isNotEmpty(email) && !email.matches(regex)) {
            throw new InvalidDomainField(CustomerDomain.class, "email", "Invalid e-mail address.");
        }
    }

    public void updateModifiedAt() {
        this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    public void fillDates() {
        if (StringUtils.isEmpty(this.id)) {
            this.createAt = ZonedDateTime.now(ZoneOffset.UTC);
            this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        }
    }

}
