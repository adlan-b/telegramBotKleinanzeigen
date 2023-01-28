package main;

import modul.Product;
import org.jsoup.Connection;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import parser.Parser;
import telegram.Bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Loader {
  public static final String URL = "https://www.ebay-kleinanzeigen.de/";
  private static Bot bot;


  public static void main(String[] args) throws TelegramApiException {
    bot = new Bot();
    TelegramBotsApi tm = new TelegramBotsApi(DefaultBotSession.class);
    tm.registerBot(bot);
  }

  public static void start(String article) {
    String end = "/k0";
    Connection.Response response = Parser.getConnect(URL + article + end);
    try {
      List<Product> loadList = new ArrayList<>(Parser.parseProductsFromDocument(response.parse()));
      bot.setProductClass(loadList);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}
