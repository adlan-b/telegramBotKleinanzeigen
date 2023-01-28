package parser;

import modul.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final Logger log = LoggerFactory.getLogger(Parser.class);

    public static Connection.Response getConnect(String url) {
        log.info("getConnect in");
        Connection.Response soup = null;
        try {
            soup = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .followRedirects(false)
                    .execute();

            log.debug(soup.statusCode() + " : " + url);
            Thread.sleep(1000);

        } catch (IOException | InterruptedException e) {
            log.error("Error {}", e.getMessage());
            e.printStackTrace();
        }

        log.info("getConnect out");
        return soup;
    }

    public static List<Product> parseProductsFromDocument(Document document) {
        log.info("parseProductFromDocument in");
        List<Product> listProducts = new ArrayList<>();
        Elements elm = document.select("article");
        log.warn("Total path {}", document.location());
        for (Element article : elm) {

            Elements ids = article.select("a[name]");
            if (ids.size() == 0) {
                continue;
            }
            Long id = Long.valueOf(ids.attr("name"));

            Elements names = article.select("a");
            String name = names.text().replaceAll("[/n|\\\\]", "");


            Elements prices = article.select(".aditem-main--middle--price-shipping--price");
            String price = prices.text().strip().trim();
            if (price.isEmpty()) {
                price = "Kostenfrei";
            }


            Elements links = article.select("a[href]");
            String link = links.attr("href");

            Elements img = article.select("div.aditem-image > a > div");
            String imgSrc = img.attr("data-imgsrc");
            listProducts.add(new Product(id, name, price, link, imgSrc));
        }

        log.info("parseProductFromDocument out");
        return listProducts;
    }

}
