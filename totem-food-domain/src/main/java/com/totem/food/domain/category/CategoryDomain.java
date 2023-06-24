package com.totem.food.domain.category;

import com.totem.food.domain.exceptions.InvalidDomainField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDomain {

	//########### Main Fields
	private String id;
	private String name;
	private ZonedDateTime modifiedAt;
	private ZonedDateTime createAt;

	//########### Constants
	public static final int MAX_CATEGORY_LENGTH = 50;


	public void validateCategory(){

		if(StringUtils.isNotEmpty(name) && name.length() > MAX_CATEGORY_LENGTH){
			throw new InvalidDomainField(CategoryDomain.class, "name", "Max length accepted is ".concat(String.valueOf(MAX_CATEGORY_LENGTH)));
		}

	}

	public void updateModifiedAt(){
		this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
	}

	public void fillDates(){
		if(StringUtils.isEmpty(this.id)){
			this.createAt = ZonedDateTime.now(ZoneOffset.UTC);
			this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
		}
	}

}
