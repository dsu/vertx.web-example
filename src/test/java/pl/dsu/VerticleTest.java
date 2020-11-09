package pl.dsu;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public abstract class VerticleTest {

	protected static Logger logger = LoggerFactory.getLogger(VerticleTest.class);

	protected Vertx vertx;
	protected int port = 8080;

	@Before
	public void setUp(TestContext context) throws IOException {

		ServerSocket socket = new ServerSocket(0); // random free port
		port = socket.getLocalPort();
		socket.close();

		DeploymentOptions options = new DeploymentOptions().setConfig(getConf());
		vertx = Vertx.vertx();
		vertx.deployVerticle(ProductsVerticle.class.getName(), options, context.asyncAssertSuccess());
	}

	protected abstract JsonObject getConf();

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
		logger.debug("Vertx closed {}...", port);
	}

	@Test(timeout = 10000)
	public void testApi(TestContext context) {
		logger.debug("testApi...");

		final Async async = context.async();

		// There is a difference between a handler and a bodyHandler. Handler will not
		// wait for a whole body, it will consume body in part - not good for JSON
		// parsing..

		vertx.createHttpClient().getNow(port, "localhost", "/api/search?q=xyz", response -> {
			response.bodyHandler(body -> {
				context.assertEquals(response.statusCode(), 200);
				JsonArray jsonArray = body.toJsonArray();
				context.assertTrue(jsonArray.size() > 0);
				JsonObject jsonObject = jsonArray.getJsonObject(0);
				context.assertTrue(jsonObject.containsKey("productname"));
				context.assertTrue(jsonObject.containsKey("description"));
				context.assertTrue(jsonObject.containsKey("imageurl"));
				context.assertTrue(jsonObject.containsKey("price"));
				logger.debug("API xyz search ok");
				async.complete();
			});
		});

	}

	@Test(timeout = 10000, expected = java.util.concurrent.TimeoutException.class)
	public void testWrongArguments(TestContext context) {
		logger.debug("testWrongArguments...");
		final Async async = context.async();
		vertx.createHttpClient().getNow(port, "localhost", "/api/search?", response -> {
			response.handler(body -> {
				context.assertEquals(response.statusCode(), 400);
				logger.debug("400 error ok");
				async.complete();
			});
		});
		async.await(500);
	}

	@Test(timeout = 10000)
	public void testInvalidAddress(TestContext context) {
		logger.debug("testInvalidAddress...");
		final Async async = context.async();
		vertx.createHttpClient().getNow(port, "localhost", "/api/search2?q=test", response -> {
			response.handler(body -> {
				context.assertEquals(response.statusCode(), 404);
				logger.debug("404 error ok");
				async.complete();
			});
		});

	}

}