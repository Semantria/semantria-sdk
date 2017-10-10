package com.semantria.test;

import com.semantria.interfaces.ICallbackHandler;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CallbackHandler implements ICallbackHandler {

    private static Logger log = LoggerFactory.getLogger(CallbackHandler.class);

    List<String> errors = new ArrayList<String>();

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public void onResponse(Object sender, ResponseArgs responseArgs) {
        // The response can be quite verbose so this is commented out by default.
        // Uncomment this to see the raw response data.
        //log.info("rawResponse = {}", responseArgs.getMessage());
    }

    @Override
    public void onRequest(Object sender, RequestArgs requestArgs) {
        log.info("requestUrl = {} {}", requestArgs.getMethod(), requestArgs.getUrl());
        // The request can be quite verbose so this is commented out by default.
        // Uncomment this to see the raw request data.
        //log.info("rawRequest = {}", requestArgs.getMessage());
    }

    @Override
    public void onError(Object sender, ResponseArgs errorArgs) {
        log.error("status: {}, error message: {}", errorArgs.getStatus(), errorArgs.getMessage());
        errors.add(String.format("status: %d, error message: %s",
                errorArgs.getStatus(), errorArgs.getMessage()));
    }

    @Override
    public void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData) {
        for (DocAnalyticData aData : processedData) {
            log.info("{} = {}", aData.getId(), aData.getStatus());
        }

    }

    @Override
    public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
        for (CollAnalyticData aData : processedData) {
            log.info("{} = {}", aData.getId(), aData.getStatus());
        }

    }
}
