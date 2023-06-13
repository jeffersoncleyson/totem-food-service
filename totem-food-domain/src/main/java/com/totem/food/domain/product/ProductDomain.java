package com.totem.food.domain.product;

import com.totem.food.domain.category.CategoryDomain;
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
public class ProductDomain {

    //########### Main Fields
    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private String category;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;


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
