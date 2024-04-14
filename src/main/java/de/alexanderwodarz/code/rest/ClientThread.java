package de.alexanderwodarz.code.rest;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.json.JSONObject;

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
        config.connectorProvider(new ApacheConnectorProvider());  // Use Apache connector
        this.client = ClientBuilder.newClient(config);
        this.webTarget = client.target(url);
    }

    public void addCookie(String name, String value) {
        NewCookie cookie = new NewCookie(name, value);
        cookies.put(name, cookie);
    }

    public void run() {
        invocationBuilder = webTarget.request(type);
        headers.forEach((key, value) -> invocationBuilder.header(key, value));
        cookies.values().forEach(cookie -> invocationBuilder.cookie(cookie));  // Add cookies to the request

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
            return Entity.json(body != null ? body.toString() : "{}");
        }
    }

    public enum RequestMethod {
        DELETE, GET, POST, PUT, PATCH
    }
}
