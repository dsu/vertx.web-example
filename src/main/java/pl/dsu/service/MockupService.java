package pl.dsu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.dsu.model.Product;

/**
 * 
 * @author dsu
 *
 */
public class MockupService implements ProductSearch {

	private static Logger logger = LoggerFactory.getLogger(MockupService.class);

	@Override
	public void search(String q, Consumer<List<Product>> callback) {
		logger.debug("API search for {}", q);
		ArrayList<Product> arrayList = new ArrayList<>();
		arrayList.add(new Product("test", "desc", "http://image.png", "2.00"));
		arrayList.add(new Product());
		callback.accept(arrayList);

	}

}
