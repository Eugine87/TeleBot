/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.star.telebot.service.impl;

import com.star.telebot.service.CurrencyConversionService;
import com.star.telebot.Currency;

public class HardcodedCurrencyConversionService implements CurrencyConversionService {
  @Override
  public double getConversionRatio(Currency from, Currency to) {
    switch (from) {
      case BYN:
        switch (to) {
          case USD:
            return 1 / 2.5;
          case EUR:
            return 1 / 3.1;
          case BYN:
            return 1;
        }
      case EUR:
        switch (to) {
          case USD:
            return 1.1;
          case EUR:
            return 1;
          case BYN:
            return 3.1;
        }
      case USD:
        switch (to) {
          case USD:
            return 1;
          case EUR:
            return 1 / 1.1;
          case BYN:
            return 2.5;
        }
    }
    return 1;
  }
}