package com.nux.dhoan9.firstmvvm.model;

/**
 * Author: hoang
 * Created by: ModelGenerator on 03/07/2017
 */
public class PaypalInvoiceResp {
    private Client client;
    private Response response;
    private String responseType;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}