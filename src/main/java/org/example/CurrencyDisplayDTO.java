package org.example;

import java.math.BigDecimal;

public class CurrencyDisplayDTO {
    String currencyId;
    BigDecimal btcPrice;
    BigDecimal btcMarketCap;
    BigDecimal btcVolume;
    BigDecimal btcPriceChange;

    public CurrencyDisplayDTO() {
    }

    public CurrencyDisplayDTO(String currencyId, BigDecimal btcPrice, BigDecimal btcMarketCap, BigDecimal btcVolume, BigDecimal btcPriceChange) {
        this.currencyId = currencyId;
        this.btcPrice = btcPrice;
        this.btcMarketCap = btcMarketCap;
        this.btcVolume = btcVolume;
        this.btcPriceChange = btcPriceChange;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(BigDecimal btcPrice) {
        this.btcPrice = btcPrice;
    }

    public BigDecimal getBtcMarketCap() {
        return btcMarketCap;
    }

    public void setBtcMarketCap(BigDecimal btcMarketCap) {
        this.btcMarketCap = btcMarketCap;
    }

    public BigDecimal getBtcVolume() {
        return btcVolume;
    }

    public void setBtcVolume(BigDecimal btcVolume) {
        this.btcVolume = btcVolume;
    }

    public BigDecimal getBtcPriceChange() {
        return btcPriceChange;
    }

    public void setBtcPriceChange(BigDecimal btcPriceChange) {
        this.btcPriceChange = btcPriceChange;
    }
}
