package de.alexanderwodarz.code.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.representation.Form;
import org.json.JSONObject;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class ClientThread extends Thread {

    private final Client client;
    private WebResource.Builder resource;
    private RequestMethod method;
    private String response;
    private JSONObject body;
    private ClientResponse clientResponse;
    private HashMap<String, String> headers = new HashMap<>();
    private int status;
    private String type = MediaType.APPLICATION_JSON;
    private String rawBody;
    private boolean isRaw = false;
    private boolean isForm = false;
    private Form form;

    public ClientThread(String url, RequestMethod method) {
        this(url, method, null);
    }

    public ClientThread(String url, RequestMethod method, ClientConfig config) {
        this(url, method, config, 5000);
    }

    public ClientThread(String url, RequestMethod method, ClientConfig config, int readTimeOut) {
        this.method = method;
        if (config != null)
            client = Client.create(config);
        else
            client = Client.create();
        client.setConnectTimeout(1500);
        client.setReadTimeout(readTimeOut);
        resource = client.resource(url).getRequestBuilder();
    }

    public static SSLContext getKeyStore(String path, String password) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(path), password.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, password.toCharArray());

            TrustManager[] managers = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), managers, null);
            return sc;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addCookie(String name, String value, String path, String domain) {
        Cookie cookie = new Cookie(name, value, path, domain);
        resource = resource.cookie(cookie);
    }

    public void addCookie(String name, String value) {
        addCookie(name, value, null, null);
    }

    public void rerun(String url, RequestMethod method) {
        resource = client.resource(url).getRequestBuilder();
        this.method = method;
        run();
    }

    public ClientResponse getClientResponse() {
        return clientResponse;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setBodyRaw(String body) {
        isRaw = true;
        this.rawBody = body;
    }

    public void setMediaType(String type) {
        this.type = type;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public void addForm(String key, String value) {
        isForm = true;
        if (form == null)
            form = new Form();
        setMediaType("application/x-www-form-urlencoded");
        form.putSingle(key, value);
    }

    public void run() {
        switch (method) {
            case GET: {
                WebResource.Builder builder = resource.type(type);
                for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
                    builder = builder.header(stringStringEntry.getKey(), stringStringEntry.getValue());
                }
                ClientResponse response = builder.get(ClientResponse.class);
                this.clientResponse = response;
                setStatus(response.getStatus());
                if (getStatus() == 204)
                    setResponse("{}");
                else
                    setResponse(response.getEntity(String.class));
                break;
            }
            case POST: {
                WebResource.Builder builder = resource.type(type);
                for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
                    builder = builder.header(stringStringEntry.getKey(), stringStringEntry.getValue());
                }
                ClientResponse response;
                if (isForm)
                    response = builder.post(ClientResponse.class, (form != null ? form : ""));
                else if (!isRaw)
                    response = builder.post(ClientResponse.class, (body != null ? body.toString() : ""));
                else
                    response = builder.post(ClientResponse.class, (rawBody != null ? rawBody.toString() : ""));
                this.clientResponse = response;
                setStatus(response.getStatus());
                if (getStatus() == 204)
                    setResponse("{}");
                else
                    setResponse(response.getEntity(String.class));

                break;
            }
            case PUT: {
                WebResource.Builder builder = resource.type(type);
                for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
                    builder = builder.header(stringStringEntry.getKey(), stringStringEntry.getValue());
                }

                ClientResponse response;
                if (isForm)
                    response = builder.put(ClientResponse.class, (form != null ? form : ""));
                else if (!isRaw)
                    response = builder.put(ClientResponse.class, (body != null ? body.toString() : ""));
                else
                    response = builder.put(ClientResponse.class, (rawBody != null ? rawBody.toString() : ""));

                setResponse(response.getEntity(String.class));
                setStatus(response.getStatus());
                break;
            }
            case DELETE: {
                ClientResponse response = resource
                        .delete(ClientResponse.class);
                setResponse(response.getEntity(String.class));
                setStatus(response.getStatus());
                break;
            }
        }
    }

    public enum RequestMethod {
        DELETE, GET, POST, PUT
    }
}
