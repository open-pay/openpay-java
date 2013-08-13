package mx.openpay.client.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * @author Heber Lazcano
 */
public class Client {

    private static final String AGENT = "openpay-java/";

    private static final int CONNECTION_TIMEOUT = 60000;

    private String root;

    private String key;

    private HttpClient httpClient;

    public Client(String location, String key) {
        this.key = key;
        this.root = location;

        PoolingClientConnectionManager connMgr = new PoolingClientConnectionManager();
        this.httpClient = new DefaultHttpClient(connMgr);

        this.httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        this.httpClient.getParams().setParameter("http.socket.timeout", new Integer(CONNECTION_TIMEOUT));
        this.httpClient.getParams().setParameter("http.connection.timeout", new Integer(CONNECTION_TIMEOUT));
        this.httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
    }

    public Client() {
        this(null, null);
    }

    public <T> T get(String path, Map<String, String> params, final Class<T> clazz) throws HttpError,
            ServiceUnavailable {
        URI uri = buildUri(path, params);
        HttpGet request = new HttpGet(uri);
        this.addHeaders(request);
        return executeOperation(request, clazz, null);
    }

    public <T> T get(String path, final Class<T> clazz) throws HttpError, ServiceUnavailable {
        return get(path, new HashMap<String, String>(), clazz);
    }

    public <T> T getList(String path, final Type type) throws HttpError, ServiceUnavailable {
        return this.getList(path, new HashMap<String, String>(), type);
    }

    public <T> T getList(String path, Map<String, String> params, final Type type) throws HttpError, ServiceUnavailable {
        URI uri = this.buildUri(path, params);
        HttpGet request = new HttpGet(uri);
        this.addHeaders(request);
        return executeOperation(request, null, type);
    }

    public void delete(String path, Map<String, String> params) throws HttpError, ServiceUnavailable {
        URI uri = buildUri(path, params);
        HttpDelete request = new HttpDelete(uri);
        this.addHeaders(request);
        this.executeOperation(request, null, null);
    }

    public void delete(String path) throws HttpError, ServiceUnavailable {
        this.delete(path, new HashMap<String, String>());
    }

    public <T> T put(String path, Object payload, Class<T> returnClazz) throws HttpError, ServiceUnavailable {
        URI uri = this.buildUri(path);
        HttpPut request = new HttpPut(uri);
        this.addHeaders(request);
        request.setEntity(new StringEntity(serialize(payload), ContentType.APPLICATION_JSON));

        return this.executeOperation(request, returnClazz, null);
    }

    public <T> T post(String path, Object payload, Class<T> clazz) throws HttpError, ServiceUnavailable {
        URI uri = this.buildUri(path);
        HttpPost request = new HttpPost(uri);
        this.addHeaders(request);
        request.setEntity(new StringEntity(this.serialize(payload), ContentType.APPLICATION_JSON));

        return this.executeOperation(request, clazz, null);
    }

    private String buildQueryString(Map<String, String> params) {
        ArrayList<NameValuePair> nvs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            NameValuePair nv = new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue());
            nvs.add(nv);
        }
        String queryString = URLEncodedUtils.format(nvs, "UTF-8");
        return queryString;
    }

    private URI buildUri(String path, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.root);
        sb.append(path);
        if ((params != null) && (params.size() > 0)) {
            sb.append("?");
            sb.append(buildQueryString(params));
        }
        try {
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private URI buildUri(String path) {
        return buildUri(path, null);
    }

    private void addHeaders(HttpUriRequest request) {
        String version = super.getClass().getPackage().getImplementationVersion();
        request.addHeader(new BasicHeader("User-Agent", AGENT + version));
        request.addHeader(new BasicHeader("Accept", "application/json"));
    }

    private <T> T executeOperation(HttpUriRequest request, Class<T> clazz, Type type) throws HttpError,
            ServiceUnavailable {
        if (this.key != null) {
            byte[] auth;
            try {
                auth = (this.key + ":").getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            String authEncoding = Base64.encodeBase64String(auth);
            request.setHeader("Authorization", "Basic " + authEncoding);
        }
        long init = System.currentTimeMillis();
        HttpResponse response;
        try {
            response = this.httpClient.execute(request);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ServiceUnavailable(e);
        }

        String body = null;
        T payload = null;
        String contentType = null;

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                contentType = entity.getContentType().getValue();
                body = EntityUtils.toString(entity);
            } catch (IOException e) {
                throw new ServiceUnavailable(e);
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - init));

        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= 299) {
            if (contentType != null && contentType.startsWith(ContentType.APPLICATION_JSON.getMimeType())) {
                HttpError error = this.deserialize(body, HttpError.class, null);
                throw error;
            } else {
                throw new HttpError("[" + status.getStatusCode() + "] Internal server error");
            }
        } else {
            if (contentType != null && contentType.startsWith(ContentType.APPLICATION_JSON.getMimeType())) {
                payload = this.deserialize(body, clazz, type);
            }
        }
        return payload;
    }

    private String serialize(final Object payload) {
        Gson gson = new Gson();
        return gson.toJson(payload);
        // ObjectMapper objectMapper = new ObjectMapper();
        // try {
        // return objectMapper.writeValueAsString(payload);
        // } catch (JsonProcessingException e) {
        // return "";
        // }
    }

    private <T> T deserialize(String body, Class<T> clazz, Type type) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter( Date.class, new JsonDeserializer<Date>() {

            private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            
            public Date deserialize(JsonElement json, Type paramType,
                    JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
                try {
                    return this.dateFormat.parse(json.getAsJsonPrimitive().getAsString());
                } catch (ParseException e) {
                   return null;
                }
            }
        });
        Gson gson = gsonBuilder.create();
        if (type == null) {
            return (T) gson.fromJson(body, clazz);
        } else {
            return gson.fromJson(body, type);
        }
        // ObjectMapper objectMapper = new ObjectMapper();
        // try {
        // return objectMapper.readValue(body, new TypeReference<List<Card>>() {
        // });
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
    }
}
