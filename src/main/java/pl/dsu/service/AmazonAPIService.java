package pl.dsu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import pl.dsu.model.Product;

/**
 * TOOD Waiting for valid API keys from Amazon
 * 
 * 
 * @author dsu
 *
 */
public class AmazonAPIService implements ProductSearch {

	private static Logger logger = LoggerFactory.getLogger(AmazonAPIService.class);

	private Vertx vertx;

	public AmazonAPIService(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void search(String q, Consumer<List<Product>> callback) {
		throw new RuntimeException("Not implemented");
	}

	/*
	 * Your Access Key ID, as taken from the Your Account page.
	 */
	private static final String ACCESS_KEY_ID = "AKIAJU2IKLHRU4FLP2DA";

	/*
	 * Your Secret Key corresponding to the above ID, as taken from the Your Account
	 * page.
	 */
	private static final String SECRET_KEY = "WXpMC7ix1cs8q9SFHMCxDxQ0ISS2PD+KvV8Fl/2W";

	/*
	 * Use the end-point according to the region you are interested in.
	 */
	private static final String ENDPOINT = "webservices.amazon.com";

	public static void main(String[] args) {

		/*
		 * Set up the signed requests helper.
		 */
		SignedRequestsHelper helper;

		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		String requestUrl = null;

		Map<String, String> params = new HashMap<String, String>();

		params.put("Service", "AWSECommerceService");
		params.put("Operation", "ItemSearch");
		params.put("AWSAccessKeyId", "AKIAJU2IKLHRU4FLP2DA");
		params.put("AssociateTag", "");
		params.put("SearchIndex", "All");
		params.put("Keywords", "harry_potter");
		params.put("ResponseGroup", "Images,ItemAttributes,Offers");

		requestUrl = helper.sign(params);

		System.out.println("Signed URL: \"" + requestUrl + "\"");
	}

}
