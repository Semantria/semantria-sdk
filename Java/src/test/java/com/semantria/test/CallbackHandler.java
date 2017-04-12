package com.semantria.test;

import com.semantria.interfaces.ICallbackHandler;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;

import java.util.List;

public class CallbackHandler implements ICallbackHandler {

    @Override
    public void onResponse(Object sender, ResponseArgs responseArgs) {
        // The response can be quite verbose so this is commented out by default.
        // Uncomment this to see the raw response data.
        //System.out.format("rawResponse = %s\n", responseArgs.getMessage());
    }

    @Override
    public void onRequest(Object sender, RequestArgs requestArgs) {
        System.out.format("requestUrl = %s %s\n", requestArgs.getMethod(), requestArgs.getUrl());
        // The request can be quite verbose so this is commented out by default.
        // Uncomment this to see the raw request data.
        //System.out.format("rawRequest = %s\n", requestArgs.getMessage());
    }

    @Override
    public void onError(Object sender, ResponseArgs errorArgs) {
        System.out.format("HTTP status: %d, error message: %s", 
						  errorArgs.getStatus(), errorArgs.getMessage());
    }

    @Override
    public void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData) {
        for (DocAnalyticData aData : processedData) {
            System.out.format("%s = %s\n", aData.getId(), aData.getStatus());
        }

    }

    @Override
    public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
        for (CollAnalyticData aData : processedData) {
            System.out.format("%s = %s\n", aData.getId(), aData.getStatus());
        }

    }
}
