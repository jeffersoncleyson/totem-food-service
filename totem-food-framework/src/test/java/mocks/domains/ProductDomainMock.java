package mocks.domains;

import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.product.ProductDomain;

import java.time.ZonedDateTime;

public class ProductDomainMock {

    public static ProductDomain getMock() {
        var productDomain = new ProductDomain();
        productDomain.setId("1");
        productDomain.setName("Coca");
        productDomain.setPrice(4.99);
        productDomain.setCategory(new CategoryDomain());
        productDomain.setImage("URL");
        productDomain.setDescription("Sabor Cola");
        productDomain.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        productDomain.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return productDomain;
    }
}
