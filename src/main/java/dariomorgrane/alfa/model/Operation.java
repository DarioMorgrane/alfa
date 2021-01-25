package dariomorgrane.alfa.model;

import java.util.Objects;

public class Operation {

    private String baseCurrencyCode;
    private String operatingCurrencyCode;
    private Double todayOperatingCurrencyRate;
    private Double yesterdayOperatingCurrencyRate;
    private Boolean rateGoneUp;
    private String gifUrl;

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getOperatingCurrencyCode() {
        return operatingCurrencyCode;
    }

    public void setOperatingCurrencyCode(String operatingCurrencyCode) {
        this.operatingCurrencyCode = operatingCurrencyCode;
    }

    public Double getTodayOperatingCurrencyRate() {
        return todayOperatingCurrencyRate;
    }

    public void setTodayOperatingCurrencyRate(Double todayOperatingCurrencyRate) {
        this.todayOperatingCurrencyRate = todayOperatingCurrencyRate;
    }

    public Double getYesterdayOperatingCurrencyRate() {
        return yesterdayOperatingCurrencyRate;
    }

    public void setYesterdayOperatingCurrencyRate(Double yesterdayOperatingCurrencyRate) {
        this.yesterdayOperatingCurrencyRate = yesterdayOperatingCurrencyRate;
    }

    public Boolean getRateGoneUp() {
        return rateGoneUp;
    }

    public void setRateGoneUp(Boolean rateGoneUp) {
        this.rateGoneUp = rateGoneUp;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "baseCurrencyCode='" + baseCurrencyCode + '\'' +
                ", operatingCurrencyCode='" + operatingCurrencyCode + '\'' +
                ", todayOperatingCurrencyRate=" + todayOperatingCurrencyRate +
                ", yesterdayOperatingCurrencyRate=" + yesterdayOperatingCurrencyRate +
                ", rateGoneUp=" + rateGoneUp +
                ", gifUrl='" + gifUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(baseCurrencyCode, operation.baseCurrencyCode) &&
                Objects.equals(operatingCurrencyCode, operation.operatingCurrencyCode) &&
                Objects.equals(todayOperatingCurrencyRate, operation.todayOperatingCurrencyRate) &&
                Objects.equals(yesterdayOperatingCurrencyRate, operation.yesterdayOperatingCurrencyRate) &&
                Objects.equals(rateGoneUp, operation.rateGoneUp) &&
                Objects.equals(gifUrl, operation.gifUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyCode,
                operatingCurrencyCode,
                todayOperatingCurrencyRate,
                yesterdayOperatingCurrencyRate,
                rateGoneUp,
                gifUrl);
    }
}
