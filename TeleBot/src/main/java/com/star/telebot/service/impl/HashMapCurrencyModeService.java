/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.star.telebot.service.impl;

import com.star.telebot.service.CurrencyModeService;
import com.star.telebot.Currency;
import java.util.HashMap;
import java.util.Map;

public class HashMapCurrencyModeService implements CurrencyModeService {
  private final Map<Long, Currency> originalCurrency = new HashMap<>();
  private final Map<Long, Currency> targetCurrency = new HashMap<>();

  public HashMapCurrencyModeService() {
    System.out.println("HASHMAP MODE is created");
  }

  @Override
  public Currency getOriginalCurrency(long chatId) {
    return originalCurrency.getOrDefault(chatId, Currency.USD);
  }

  @Override
  public Currency getTargetCurrency(long chatId) {
    return targetCurrency.getOrDefault(chatId, Currency.USD);
  }

  @Override
  public void setOriginalCurrency(long chatId, Currency currency) {
    originalCurrency.put(chatId, currency);
  }

  @Override
  public void setTargetCurrency(long chatId, Currency currency) {
    targetCurrency.put(chatId, currency);
  }
}