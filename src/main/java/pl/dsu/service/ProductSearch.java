package pl.dsu.service;

import java.util.List;
import java.util.function.Consumer;

import pl.dsu.model.Product;

@FunctionalInterface
public interface ProductSearch {

	void search(String q, Consumer<List<Product>> callback);

}