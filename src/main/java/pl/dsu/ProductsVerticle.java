package pl.dsu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import pl.dsu.service.AmazonService;
import pl.dsu.service.MockupService;
import pl.dsu.service.ProductSearch;

/**
 * Implements a REST API Endpoint /api/search?q=KEYWORD which delegates to the
 * amazon.com.
 * 
 * @author dsu
 *
 */
public class ProductsVerticle extends AbstractVerticle {

	private static Logger logger = LoggerFactory.getLogger(ProductsVerticle.class);
	private ProductSearch service;

	@Override
	public void start(Future<Void> fut) {

		String serviceType = config().getString("service");
		this.service = getService(serviceType);

		logger.debug("vertx is starting on port: {}", config().getInteger("http.port"));
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		router.get("/api/search").handler(this::search);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 8080),
				result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});
	}

	private ProductSearch getService(String serviceType) {
		if (StringUtil.isNullOrEmpty(serviceType)) {
			throw new RuntimeException("Invalid service type");
		}
		if (serviceType.equals("mock")) {
			return new MockupService();
		} else if (serviceType.equals("amazon")) {
			return new AmazonService(getVertx());
		} else {
			throw new RuntimeException("No matching service");
		}
	}

	/**
	 * Perform a product search there and transform the results into a JSON. The
	 * JSON structure should be an array with JSONObjects.
	 * 
	 * @param routingContext
	 */
	private void search(RoutingContext routingContext) {

		String q = routingContext.request().getParam("q");

		if (StringUtil.isNullOrEmpty(q)) {
			routingContext.response().setStatusCode(400).end();
			logger.warn("Wrong request arguments");
			return;
		}

		// when encodePrettily vertx cannot parse it again properly ... You can run
		// tests to check that..
		service.search(q, list -> routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(list)));
	}

}