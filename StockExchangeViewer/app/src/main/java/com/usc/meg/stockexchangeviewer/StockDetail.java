package com.usc.meg.stockexchangeviewer;

/**
 * Created by sbmeg on 30/04/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StockDetail implements Serializable{

    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Symbol")
    @Expose
    private String Symbol;
    @SerializedName("LastPrice")
    @Expose
    private Double LastPrice;
    @SerializedName("Change")
    @Expose
    private Double Change;
    @SerializedName("ChangeType")
    @Expose
    private Integer ChangeType;
    @SerializedName("ChangePercent")
    @Expose
    private Double ChangePercent;
    @SerializedName("Timestamp")
    @Expose
    private String Timestamp;
    @SerializedName("MarketCap")
    @Expose
    private Double MarketCap;
    @SerializedName("MarketType")
    @Expose
    private String MarketType;
    @SerializedName("Volume")
    @Expose
    private Integer Volume;
    @SerializedName("ChangeYTD")
    @Expose
    private Double ChangeYTD;
    @SerializedName("ChangePercentYTD")
    @Expose
    private Double ChangePercentYTD;
    @SerializedName("High")
    @Expose
    private Double High;
    @SerializedName("Low")
    @Expose
    private Double Low;
    @SerializedName("Open")
    @Expose
    private Double Open;

    /**
     * No args constructor for use in serialization
     *
     */
    public StockDetail() {
    }

    /**
     *
     * @param Open
     * @param Low
     * @param Change
     * @param MarketType
     * @param Volume
     * @param ChangeType
     * @param Timestamp
     * @param Name
     * @param ChangePercent
     * @param Symbol
     * @param LastPrice
     * @param ChangeYTD
     * @param MarketCap
     * @param ChangePercentYTD
     * @param High
     */
    public StockDetail(String Name, String Symbol, Double LastPrice, Double Change, Integer ChangeType, Double ChangePercent, String Timestamp, Double MarketCap, String MarketType, Integer Volume, Double ChangeYTD, Double ChangePercentYTD, Double High, Double Low, Double Open) {
        this.Name = Name;
        this.Symbol = Symbol;
        this.LastPrice = LastPrice;
        this.Change = Change;
        this.ChangeType = ChangeType;
        this.ChangePercent = ChangePercent;
        this.Timestamp = Timestamp;
        this.MarketCap = MarketCap;
        this.MarketType = MarketType;
        this.Volume = Volume;
        this.ChangeYTD = ChangeYTD;
        this.ChangePercentYTD = ChangePercentYTD;
        this.High = High;
        this.Low = Low;
        this.Open = Open;
    }

    /**
     *
     * @return
     * The Name
     */
    public String getName() {
        return Name;
    }

    /**
     *
     * @param Name
     * The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     *
     * @return
     * The Symbol
     */
    public String getSymbol() {
        return Symbol;
    }

    /**
     *
     * @param Symbol
     * The Symbol
     */
    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    /**
     *
     * @return
     * The LastPrice
     */
    public Double getLastPrice() {
        return LastPrice;
    }

    /**
     *
     * @param LastPrice
     * The LastPrice
     */
    public void setLastPrice(Double LastPrice) {
        this.LastPrice = LastPrice;
    }

    /**
     *
     * @return
     * The Change
     */
    public Double getChange() {
        return Change;
    }

    /**
     *
     * @param Change
     * The Change
     */
    public void setChange(Double Change) {
        this.Change = Change;
    }

    /**
     *
     * @return
     * The ChangeType
     */
    public Integer getChangeType() {
        return ChangeType;
    }

    /**
     *
     * @param ChangeType
     * The ChangeType
     */
    public void setChangeType(Integer ChangeType) {
        this.ChangeType = ChangeType;
    }

    /**
     *
     * @return
     * The ChangePercent
     */
    public Double getChangePercent() {
        return ChangePercent;
    }

    /**
     *
     * @param ChangePercent
     * The ChangePercent
     */
    public void setChangePercent(Double ChangePercent) {
        this.ChangePercent = ChangePercent;
    }

    /**
     *
     * @return
     * The Timestamp
     */
    public String getTimestamp() {
        return Timestamp;
    }

    /**
     *
     * @param Timestamp
     * The Timestamp
     */
    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }

    /**
     *
     * @return
     * The MarketCap
     */
    public Double getMarketCap() {
        return MarketCap;
    }

    /**
     *
     * @param MarketCap
     * The MarketCap
     */
    public void setMarketCap(Double MarketCap) {
        this.MarketCap = MarketCap;
    }

    /**
     *
     * @return
     * The MarketType
     */
    public String getMarketType() {
        return MarketType;
    }

    /**
     *
     * @param MarketType
     * The MarketType
     */
    public void setMarketType(String MarketType) {
        this.MarketType = MarketType;
    }

    /**
     *
     * @return
     * The Volume
     */
    public Integer getVolume() {
        return Volume;
    }

    /**
     *
     * @param Volume
     * The Volume
     */
    public void setVolume(Integer Volume) {
        this.Volume = Volume;
    }

    /**
     *
     * @return
     * The ChangeYTD
     */
    public Double getChangeYTD() {
        return ChangeYTD;
    }

    /**
     *
     * @param ChangeYTD
     * The ChangeYTD
     */
    public void setChangeYTD(Double ChangeYTD) {
        this.ChangeYTD = ChangeYTD;
    }

    /**
     *
     * @return
     * The ChangePercentYTD
     */
    public Double getChangePercentYTD() {
        return ChangePercentYTD;
    }

    /**
     *
     * @param ChangePercentYTD
     * The ChangePercentYTD
     */
    public void setChangePercentYTD(Double ChangePercentYTD) {
        this.ChangePercentYTD = ChangePercentYTD;
    }

    /**
     *
     * @return
     * The High
     */
    public Double getHigh() {
        return High;
    }

    /**
     *
     * @param High
     * The High
     */
    public void setHigh(Double High) {
        this.High = High;
    }

    /**
     *
     * @return
     * The Low
     */
    public Double getLow() {
        return Low;
    }

    /**
     *
     * @param Low
     * The Low
     */
    public void setLow(Double Low) {
        this.Low = Low;
    }

    /**
     *
     * @return
     * The Open
     */
    public Double getOpen() {
        return Open;
    }

    /**
     *
     * @param Open
     * The Open
     */
    public void setOpen(Double Open) {
        this.Open = Open;
    }

}
