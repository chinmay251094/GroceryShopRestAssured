package com.grocery.supplier;

import com.grocery.pojo.CartIdentifier;
import com.grocery.pojo.Products;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.JsonReader;
import one.util.streamex.StreamEx;

import static io.github.sskorol.data.TestDataReader.use;

public class DataReader {
    private DataReader() {
    }

    @DataSupplier
    public static StreamEx<CartIdentifier> getCartIdentifier() {
        return use(JsonReader.class)
                .withTarget(CartIdentifier.class)
                .withSource("CartIdentifier.json")
                .read();
    }

    @DataSupplier
    public static StreamEx<Products> getProductData() {
        return use(JsonReader.class)
                .withTarget(Products.class)
                .withSource("Products.json")
                .read();
    }

    @DataSupplier
    public static StreamEx<Products> getProductId() {
        return use(JsonReader.class)
                .withTarget(Products.class)
                .withSource("ItemsForCart.json")
                .read();
    }
}
