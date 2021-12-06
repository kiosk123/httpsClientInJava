package main.httpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/** 빌더랑 같은 패키지에 넣고 접근제한자는 default로 처리 */
public class HttpsClient {
    private URL url;
    HttpsURLConnection httpsConn;

    HttpsClient(String requestUrl) throws Exception {
        url = new URL(requestUrl);
        setSSLSocketFactory();
        httpsConn = (HttpsURLConnection) url.openConnection();
    }

    /**
     * http method 설정
     * 
     * @param method
     * @throws Exception
     */
    void setMethod(String method) throws Exception {
        method = method.toUpperCase();
        switch (method) {
        case "GET":
            httpsConn.setRequestMethod(method);
            break;
        case "POST":
            httpsConn.setRequestMethod(method);
            httpsConn.setDoOutput(true);
            break;
        default:
            throw new IllegalArgumentException("Not Supported Method : " + method);
        }
    }

    /**
     * 헤더 설정
     * 
     * @param headers
     */
    void setHeader(Map<String, String> headers) {
        headers.entrySet().stream().forEach(entry -> {
            httpsConn.setRequestProperty(entry.getKey(), entry.getValue());
        });
    }

    /**
     * 요청 바디부 전송 데이터
     * 
     * @param body
     * @throws Exception
     */
    public void sendRequestData(String body) throws Exception {
        try (DataOutputStream out = new DataOutputStream(httpsConn.getOutputStream());) {
            out.write(body.getBytes("UTF-8"));
            out.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 응답코드 확인
     * 
     * @return
     * @throws Exception
     */
    public int getStatusCode() throws Exception {
        return httpsConn.getResponseCode();
    }

    /**
     * 응답전문 확인
     * @return
     * @throws Exception
     */
    public String getResponseBody() throws Exception {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), "UTF-8"))) {
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 에러전문 확인
     * @return
     * @throws Exception
     */
    public String getErrResponseBody() throws Exception {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(httpsConn.getErrorStream(), "UTF-8"))) {
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 캐쉬 사용 여부
     * 
     * @param isUse
     */
    void setUseCaches(boolean isUse) {
        httpsConn.setUseCaches(isUse);
    }

    /**
     * SSL 설정
     * 
     * @throws Exception
     */
    private void setSSLSocketFactory() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}