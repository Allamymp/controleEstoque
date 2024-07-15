package org.linx.appck.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.linx.appck.enums.UrlEnum;
import org.linx.appck.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProductService {

    public List<Product> fetchProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new URL(UrlEnum.GET_ITEMS_URL.getUrl()), new TypeReference<List<Product>>() {});
    }
}
