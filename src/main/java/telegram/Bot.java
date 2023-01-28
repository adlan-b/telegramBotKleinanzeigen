package telegram;

import main.Loader;
import modul.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Bot extends TelegramLongPollingBot {
  private static final String PATH_TO_PROP_FILE = "src/main/resources/config.properties";
  private List<Product> products;
  private static String TOKEN_BOT;
  private static String USERNAME_BOT;
  private static final Logger log = LoggerFactory.getLogger(Bot.class);
  public Bot() {
    initBotAndUserName();
  }

  private static void initBotAndUserName() {
    log.info("Init variables, token and username");
    Properties prop = new Properties();
    try (InputStream input = new FileInputStream(PATH_TO_PROP_FILE)) {
      prop.load(input);
      TOKEN_BOT = prop.getProperty("bot_token");
      USERNAME_BOT = prop.getProperty("bot_username");

    } catch (IOException ex) {
      log.error(ex.getMessage());
      ex.printStackTrace();
    }

  }


  @Override
  public String getBotToken() {
    return TOKEN_BOT;
  }

  @Override
  public String getBotUsername() {
    return USERNAME_BOT;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      Message message = update.getMessage();
      String text = message.getText();
      if (text.equals("/start")) {
        sendTextMessage(message, "Please write article name");
      } else {
        try {
          Loader.start(text);
          run(message);
        } catch (IOException e) {
          log.error("Search article " + text);
          e.printStackTrace();
        }
      }
    }

  }


  public void sendPhotoMessage(Message msg, String text, String img) {
    if (img.isEmpty()) {
      return;
    }
    SendPhoto phMsg = new SendPhoto();
    phMsg.setChatId(msg.getChatId().toString());
    phMsg.setPhoto(new InputFile(img));
    phMsg.setCaption(text);
    phMsg.setParseMode("HTML");
    try {
      execute(phMsg);
    } catch (TelegramApiException e) {
      log.debug("Path to image " + img);
      e.printStackTrace();
    }
  }

  public void sendTextMessage(Message msg, String text) {
    SendMessage message = new SendMessage();
    message.setChatId(msg.getChatId().toString());
    message.setText(text);
    try {
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }


  private void run(Message msg) throws IOException {
    if (products.size() > 0) {
      for (Product product : products) {
        sendPhotoMessage(msg, "<b>" + product.getName() + "</b>\n<i>"
                + product.getPrice() + "</i>\n<a href = '"
                + Loader.URL + product.getLink() + "'>Follow the link</a>",
            product.getImg());
      }
    } else {
      log.warn("Bad request");
      sendTextMessage(msg, "Bad Request. Please write new article name");
    }
  }

  public List<Product> getProductClass() {
    return products;
  }

  public void setProductClass(List<Product> products) {
    if (products == null) {
      this.products = new ArrayList<>();
    }
    this.products = products;
  }

}
