package com.totem.food.domain.combo;

import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboDomain {

    //########### Main Fields
    private String id;
    private String name;
    private double price;
    private List<ProductDomain> products;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

    //########### Constants
    public static final int MAX_CATEGORY_LENGTH = 50;


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
