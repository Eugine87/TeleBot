/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.star.telebot;

import com.star.telebot.service.CurrencyConversionService;
import com.star.telebot.service.impl.HashMapCurrencyModeService;
import com.star.telebot.service.CurrencyModeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TeleBot extends TelegramLongPollingBot{

    
    private final CurrencyModeService currencyModeService = CurrencyModeService.getInstance();
    private final CurrencyConversionService currencyConversionService = CurrencyConversionService.getInstance();
    /*
    public TeleBot(DefaultBotOptions options){
     super(options) ;  
    }
    */
    
    public static void main(String[] args) throws TelegramApiException {
       TeleBot bot = new TeleBot();
       //bot.execute(SendMessage.builder().chatId("1208934466").text("Go fuck yourself!!").build());
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    @Override
    public String getBotToken() {
        return "5044648950:AAH28aUN0aqQOz1-iXGbl_3b9yPX3eFA5RE";
    }

    
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            try {
                handleCallback(update.getCallbackQuery());
            } catch (TelegramApiException ex) {
                Logger.getLogger(TeleBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(update.hasMessage()){
           try {
               handleMessage(update.getMessage());
           } catch (TelegramApiException e) {
               e.printStackTrace();
           }
       }
    }
    private String getCurrencyButton(Currency saved,Currency current){
        return saved==current ? current + " ✅ " : current.name() ;
        
    }
    @Override
    public String getBotUsername() {
        return "@SexWithLukaBot";
    }

    
    private void handleMessage(Message message) throws TelegramApiException {
       if(message.hasText()&& message.hasEntities()){
        Optional <MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if(commandEntity.isPresent()){
        String command= message.getText().substring(commandEntity.get().getOffset(),commandEntity.get().getLength());
        switch(command){
            case "/set_currency": 
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                Currency originalCurrency= currencyModeService.getOriginalCurrency(message.getChatId());
                Currency targetCurrency= currencyModeService.getTargetCurrency(message.getChatId());
                for(Currency currency:Currency.values()){
                    buttons.add(Arrays.asList(InlineKeyboardButton.builder()
                            .text(getCurrencyButton(targetCurrency, currency))
                            .callbackData("ORIGINAL:" + currency)
                            .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
                }
                execute(SendMessage.builder()
                        .text("Please,choose Original and Target currencies")
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
                return;
        }
        }
       } 
       if(message.hasText()){
           String messageText = message.getText();
           Optional<Double> value =parseDouble(messageText);
           Currency originalCurrency = currencyModeService.getOriginalCurrency(message.getChatId());
            Currency targetCurrency = currencyModeService.getTargetCurrency(message.getChatId());
            double  ratio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
           if(value.isPresent()){
               execute(SendMessage.builder()
                       .chatId(message.getChatId().toString())
                       .text(String.format("%4.2f $s %s is %4.2f %s",value.get(),originalCurrency,(value.get()*ratio),targetCurrency))
                       .build());
           }
       }
    }
    
    
    private void handleCallback(CallbackQuery callbackQuery) throws TelegramApiException {
      Message message= callbackQuery.getMessage();
      String[] param =callbackQuery.getData().split(":");
      String action = param[0];
      Currency newCurrency =Currency.valueOf(param[1]);
      switch(action){
          case "ORIGINAL":
              currencyModeService.setOriginalCurrency(message.getChatId(), newCurrency);
              break;
          case "TARGET": 
              currencyModeService.setTargetCurrency(message.getChatId(), newCurrency);
              break;
      }
      List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                Currency originalCurrency= currencyModeService.getOriginalCurrency(message.getChatId());
                Currency targetCurrency= currencyModeService.getTargetCurrency(message.getChatId());
                for(Currency currency:Currency.values()){
                    buttons.add(Arrays.asList(InlineKeyboardButton.builder()
                            .text(getCurrencyButton(originalCurrency, currency))
                            .callbackData("ORIGINAL:" + currency)
                            .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
                }
      execute(EditMessageReplyMarkup.builder()
              .chatId(message.getChatId().toString())
              .messageId(message.getMessageId())
              .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
              .build());
    }

    private Optional<Double> parseDouble(String messageText) {
        try {
        return Optional.of(Double.parseDouble(messageText));
        
        } catch (Exception e) {
            return Optional.empty();
        }
  
    }
    
}
