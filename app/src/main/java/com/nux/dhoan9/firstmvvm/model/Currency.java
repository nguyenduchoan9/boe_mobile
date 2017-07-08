package com.nux.dhoan9.firstmvvm.model;

/**
 * Created by hoang on 29/06/2017.
 */

public class Currency {
    private boolean success;
    private String term;
    private String privacy;
    private long timestamp;
    private String source;
    private Quote quotes;

    public boolean isSuccess() {
        return success;
    }

    public String getTerm() {
        return term;
    }

    public String getPrivacy() {
        return privacy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public Quote getQuotes() {
        return quotes;
    }

    public float convertVNDtoUSD(float VND){
        int rate = Integer.valueOf(quotes.getUSDVND());
        return VND/rate;
    }
}
