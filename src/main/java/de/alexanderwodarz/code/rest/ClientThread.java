package de.alexanderwodarz.code.rest;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ClientThread extends Thread {

    private final Client client;
    private WebTarget webTarget;
    private Invocation.Builder invocationBuilder;
    private RequestMethod method;
    private String response;
    private JSONObject body;
    private JSONArray bodyArray;
    private Response clientResponse;
    private HashMap<String, String> headers = new HashMap<>();
    private Map<String, NewCookie> cookies = new HashMap<>(); // Stores cookies
    private int status;
    private String type = MediaType.APPLICATION_JSON;
    private String rawBody;
    private boolean isRaw = false;
    private boolean isForm = false;
    private boolean isMultiForm = false;
    private Form form = new Form();  // Single Form object to accumulate all form data
    private FormDataMultiPart multiPart;
    private MultiPart part;

    public ClientThread(String url, RequestMethod method) {
        this.method = method;
        ClientConfig config = new ClientConfig();
        config.register(MultiPartFeature.class);
        config.register(new org.glassfish.jersey.jackson.JacksonFeature());

        this.client = ClientBuilder.newBuilder()
                .sslContext(createTrustAllSSLContext())
                .hostnameVerifier((s, sslSession) -> true)
                .withConfig(config)
                .build();

        this.webTarget = client.target(url);
    }

    public static SSLContext createTrustAllSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            sslContext.init(null, trustAllCertificates, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Erstellen des SSL-Kontexts", e);
        }
    }

    public void addCookie(String name, String value) {
        NewCookie cookie = new NewCookie(name, value);
        cookies.put(name, cookie);
    }

    public void run() {
        invocationBuilder = webTarget.request(type);
        headers.forEach((key, value) -> invocationBuilder.header(key, value));
        cookies.values().forEach(cookie -> invocationBuilder.cookie(cookie));

        switch (method) {
            case GET:
                clientResponse = invocationBuilder.get();
                break;
            case PATCH:
            case POST:
            case PUT:
                Entity<?> entity = createEntity();
                clientResponse = invocationBuilder.method(method.name(), entity);
                break;
            case DELETE:
                clientResponse = invocationBuilder.delete();
                break;
        }

        setStatus(clientResponse.getStatus());
        if (getStatus() == 204)
            setResponse("{}");
        else
            setResponse(clientResponse.readEntity(String.class));
    }

    public void addForm(String key, String value) {
        isForm = true;
        form.param(key, value);
    }

    private Entity<?> createEntity() {
        if (isMultiForm) {
            return Entity.entity(part, MediaType.MULTIPART_FORM_DATA);
        } else if (isForm) {
            return Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
        } else if (isRaw) {
            return Entity.text(rawBody);
        } else {
            return Entity.json(body != null ? body.toString() : bodyArray != null ? bodyArray.toString() :  "{}");
        }
    }

    public enum RequestMethod {
        DELETE, GET, POST, PUT, PATCH
    }
}
