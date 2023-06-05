/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.star.telebot.service;

import com.star.telebot.Currency;
import com.star.telebot.service.impl.NbrbCurrencyConversionService;

public interface CurrencyConversionService {

  static CurrencyConversionService getInstance() {
    return new NbrbCurrencyConversionService();
  }

  double getConversionRatio(Currency original, Currency target);
}