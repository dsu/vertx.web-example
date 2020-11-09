package pl.dsu;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Fat jar is the preferred way to deploy a verticle. This is a class to run a
 * server within an IDE.
 *
 * @see readme.md
 * @author dsu
 *
 */
public class RunDev {

	private static Vertx vertx;

	public static void main(String[] args) {

		JsonObject conf = new JsonObject();
		conf.put("http.port", 8080);
		conf.put("service", "amazon");
		DeploymentOptions options = new DeploymentOptions().setConfig(conf);
		vertx = Vertx.vertx();
		vertx.deployVerticle(ProductsVerticle.class.getName(), options);
	}
}
