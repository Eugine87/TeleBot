/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.star.telebot.service;

import com.star.telebot.Currency;
import com.star.telebot.service.impl.HashMapCurrencyModeService;

public interface CurrencyModeService {

  static CurrencyModeService getInstance() {
    return new HashMapCurrencyModeService();
  }

  Currency getOriginalCurrency(long chatId);

  Currency getTargetCurrency(long chatId);

  void setOriginalCurrency(long chatId, Currency currency);

  void setTargetCurrency(long chatId, Currency currency);
}
