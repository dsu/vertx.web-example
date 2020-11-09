package pl.dsu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import pl.dsu.model.Product;

/**
 * Makes a request to https://www.amazon.com/s?field-keywords=KEYWORD and parses
 * the response.
 * 
 * Disadvantages:
 * 
 * This solution has many disadvantages. A request has no user-agent header. It
 * is easy to block such a request by amazon.com. If Amazon will receive too
 * many requests the microservice IP will be blocked as it can be recognized as
 * a DDOS attack and this can have legal consequences. Moreover a web page
 * structure can change any time and the code will stop working. Amazon can
 * change technology the page is generated to some JavaScript SPA framework so
 * this approach will be close to impossible.
 * 
 * @author dsu
 *
 */
public class AmazonService implements ProductSearch {

	private static Logger logger = LoggerFactory.getLogger(AmazonService.class);

	private Vertx vertx;

	public AmazonService(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void search(String q, Consumer<List<Product>> callback) {

		List<Product> results = new ArrayList<>();
		WebClientOptions options = new WebClientOptions();
		options.setKeepAlive(false);
		WebClient client = WebClient.create(vertx, options);

		client.get(80, "www.amazon.com", "/s").addQueryParam("field-keywords", q).send(request -> {
			if (request.succeeded()) {
				HttpResponse<Buffer> result = request.result();
				String html = result.body().toString();
				Document document = Jsoup.parse(html);

				Elements items = document.select("#atfResults .s-result-item");
				logger.debug("Parsing items {}", items.size());
				for (Element li : items) {
					try {

						Product product = new Product();

						product.setPrice(getTextSafe(li, ".sx-price-currency") + getTextSafe(li, ".sx-price-whole")
								+ "." + getTextSafe(li, ".sx-price-fractional"));

						product.setProductname(getTextSafe(li, "h2.s-access-title"));
						product.setImageurl(getSrcSafe(li, "img.s-access-image"));
						product.setDescription(getTextSafeFromAll(li,
								"div.a-column.a-span5.a-span-last span.a-size-small.a-color-secondary"));
						results.add(product);
					} catch (Exception e) {
						logger.warn("Jsoup exception", e);
					}
				}
				callback.accept(results);
			} else {
				logger.warn("Vertx client error", request.cause());
				callback.accept(Collections.emptyList());
			}
		});

	}

	private String getSrcSafe(Element el, String selector) {
		Element selectFirst = el.selectFirst(selector);
		if (selectFirst != null) {
			String attr = selectFirst.attr("src");
			if (attr != null) {
				return attr.toString();
			}
		}
		return "";
	}

	private String getTextSafe(Element el, String selector) {
		Element selectFirst = el.selectFirst(selector);
		if (selectFirst != null) {
			return selectFirst.text();
		} else
			return "";
	}

	private String getTextSafeFromAll(Element el, String selector) {
		Elements list = el.select(selector);
		if (list == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		list.forEach(e -> sb.append(e.text()));
		return sb.toString();
	}
}
