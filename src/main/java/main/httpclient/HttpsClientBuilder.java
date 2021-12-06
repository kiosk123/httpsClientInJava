package main.httpclient;

import java.util.Map;

public class HttpsClientBuilder {
    private HttpsClient httpsClient;


    private HttpsClientBuilder(String url) throws Exception {
        httpsClient = new HttpsClient(url);
    }

    /**
     * HttpsClientBuilder 시작
     * 
     * @param reqeuestUrl
     * @return
     * @throws Exception
     */
    public static HttpsClientBuilder create(String reqeuestUrl) throws Exception {
        return new HttpsClientBuilder(reqeuestUrl);
    }

    /**
     * 메서드 설정
     * 
     * @param method
     */
    public HttpsClientBuilder setMethod(String method) throws Exception {
        method = method.toUpperCase();
        httpsClient.setMethod(method);
        return this;
    }

    /**
     * 캐쉬 설정
     * 
     * @param isUse
     */
    public HttpsClientBuilder setUseCaches(boolean isUse) {
        httpsClient.setUseCaches(isUse);
        return this;
    }

    /**
     * 헤더 설정
     * 
     * @param headers
     * @return
     */
    public HttpsClientBuilder setHeader(Map<String, String> headers) {
        httpsClient.setHeader(headers);
        return this;
    }

    public HttpsClient build() {
        return httpsClient;
    }
}

