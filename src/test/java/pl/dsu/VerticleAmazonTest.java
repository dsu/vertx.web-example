package pl.dsu;

import org.junit.runner.RunWith;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class VerticleAmazonTest extends VerticleTest {
	@Override
	protected JsonObject getConf() {
		return new JsonObject().put("http.port", port).put("service", "amazon");
	}

}