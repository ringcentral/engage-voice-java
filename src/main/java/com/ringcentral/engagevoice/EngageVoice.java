package com.ringcentral.engagevoice;

import com.ringcentral.Utils;
import com.ringcentral.engagevoice.definitions.EngageVoiceToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class EngageVoice {
    private static final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType textMediaType = MediaType.parse("text/plain; charset=utf-8");

    public String server;
    public OkHttpClient httpClient;
    public EngageVoiceToken token;

    public EngageVoice(String server, OkHttpClient httpClient) {
        this.server = server;
        if (httpClient == null) {
            this.httpClient = new OkHttpClient();
        } else {
            this.httpClient = httpClient;
        }
    }

    public EngageVoice(String server) {
        this(server, null);
    }

    public EngageVoice() {
        this("https://engage.ringcentral.com", null);
    }

    public EngageVoiceToken authorize(String rcAccessToken) throws IOException, RestException {
        HttpUrl httpUrl = HttpUrl.parse(server).newBuilder("/api/auth/login/rc/accesstoken").build();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("rcAccessToken", rcAccessToken);
        formBodyBuilder.add("rcTokenType", "Bearer");
        Request request = new Request.Builder().url(httpUrl).post(formBodyBuilder.build()).build();
        Response response = httpClient.newCall(request).execute();
        int statusCode = response.code();
        if (statusCode < 200 || statusCode > 299) {
            throw new RestException(response, request);
        }
        this.token = Utils.gson.fromJson(response.body().string(), EngageVoiceToken.class);
        return this.token;
    }

    public ResponseBody get(String endpoint) throws IOException, RestException {
        return request(HttpMethod.GET, endpoint, null, null);
    }

    public ResponseBody get(String endpoint, Object queryParameters) throws IOException, RestException {
        return request(HttpMethod.GET, endpoint, queryParameters, null);
    }

    public ResponseBody delete(String endpoint) throws IOException, RestException {
        return request(HttpMethod.DELETE, endpoint, null, null);
    }

    public ResponseBody delete(String endpoint, Object queryParameters) throws IOException, RestException {
        return request(HttpMethod.DELETE, endpoint, queryParameters, null);
    }

    public ResponseBody post(String endpoint) throws IOException, RestException {
        return request(HttpMethod.POST, endpoint, null, null, ContentType.JSON);
    }

    public ResponseBody post(String endpoint, Object object) throws IOException, RestException {
        return request(HttpMethod.POST, endpoint, null, object, ContentType.JSON);
    }

    public ResponseBody post(String endpoint, Object object, Object queryParameters) throws IOException, RestException {
        return request(HttpMethod.POST, endpoint, queryParameters, object, ContentType.JSON);
    }

    public ResponseBody post(String endpoint, Object object, Object queryParameters, ContentType contentType) throws IOException, RestException {
        return request(HttpMethod.POST, endpoint, queryParameters, object, contentType);
    }

    public ResponseBody put(String endpoint) throws IOException, RestException {
        return request(HttpMethod.PUT, endpoint, null, null);
    }

    public ResponseBody put(String endpoint, Object object) throws IOException, RestException {
        return request(HttpMethod.PUT, endpoint, null, object);
    }

    public ResponseBody put(String endpoint, Object object, Object queryParameters) throws IOException, RestException {
        return request(HttpMethod.PUT, endpoint, queryParameters, object);
    }

    public ResponseBody put(String endpoint, Object object, Object queryParameters, ContentType contentType) throws IOException, RestException {
        return request(HttpMethod.PUT, endpoint, queryParameters, object, contentType);
    }

    public ResponseBody patch(String endpoint, Object object) throws IOException, RestException {
        return request(HttpMethod.PATCH, endpoint, null, object);
    }

    public ResponseBody patch(String endpoint, Object object, Object queryParameters) throws IOException, RestException {
        return request(HttpMethod.PATCH, endpoint, queryParameters, object);
    }

    public ResponseBody request(HttpMethod httpMethod, String endpoint, Object queryParameters, Object body) throws IOException, RestException {
        return request(httpMethod, endpoint, queryParameters, body, ContentType.JSON);
    }

    public ResponseBody request(HttpMethod httpMethod, String endpoint, Object queryParameters, Object body, ContentType contentType) throws IOException, RestException {
        RequestBody requestBody = null;
        switch (contentType) {
            case JSON:
                if (body != null && body.getClass().equals(String.class)) { // PUT text
                    requestBody = RequestBody.create(textMediaType, (String) body);
                } else {
                    requestBody = RequestBody.create(jsonMediaType, body == null ? "" : Utils.gson.toJson(body));
                }
                break;
            case FORM:
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Field field : body.getClass().getFields()) {
                    Object value = null;
                    try {
                        value = field.get(body);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (value != null) {
                        formBodyBuilder = formBodyBuilder.add(field.getName(), value.toString());
                    }
                }
                requestBody = formBodyBuilder.build();
                break;
//            case MULTIPART:
//                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
//                List<Attachment> attachments = new ArrayList<Attachment>();
//                Map<String, Object> fields = new HashMap<String, Object>();
//                String attachmentName = "attachment";
//                for (Field field : body.getClass().getFields()) {
//                    Object value = null;
//                    try {
//                        value = field.get(body);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    if (value != null) {
//                        if (field.getType() == Attachment.class) {
//                            attachmentName = field.getName();
//                            attachments.add((Attachment) value);
//                        } else if (field.getType() == Attachment[].class) {
//                            for (Attachment a : (Attachment[]) value) {
//                                attachments.add(a);
//                            }
//                        } else {
//                            fields.put(field.getName(), value);
//                        }
//                    }
//                }
//                if (fields.size() > 0) {
//                    multipartBodyBuilder.addPart(RequestBody.create(jsonMediaType, Utils.gson.toJson(fields)));
//                }
//                for (Attachment attachment : attachments) {
//                    multipartBodyBuilder.addFormDataPart(attachmentName, attachment.fileName, new RequestBody() {
//                        @Override
//                        public MediaType contentType() {
//                            if (attachment.contentType == null) {
//                                return null;
//                            }
//                            return MediaType.parse(attachment.contentType);
//                        }
//
//                        @Override
//                        public void writeTo(BufferedSink sink) throws IOException {
//                            sink.write(attachment.bytes);
//                        }
//                    });
//                }
//                requestBody = multipartBodyBuilder.setType(MultipartBody.FORM).build();
//                break;
            default:
                break;
        }
        return request(httpMethod, endpoint, queryParameters, requestBody);
    }

    // this method returns the raw response instead of just the body
    public Response requestRaw(HttpMethod httpMethod, String endpoint, Object queryParameters, RequestBody
            requestBody) throws IOException, RestException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server).newBuilder(endpoint);

        if (queryParameters != null) {
            for (Field field : queryParameters.getClass().getFields()) {
                Object value = null;
                try {
                    value = field.get(queryParameters);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value != null) {
                    if (value.getClass().isArray()) { // ?a=hello&a=world
                        for (int i = 0; i < Array.getLength(value); i++) {
                            urlBuilder = urlBuilder.addQueryParameter(field.getName(), Array.get(value, i).toString());
                        }
                    } else {
                        urlBuilder = urlBuilder.addQueryParameter(field.getName(), value.toString());
                    }
                }
            }
        }

        HttpUrl httpUrl = urlBuilder.build();

        Request.Builder builder = new Request.Builder().url(httpUrl);
        switch (httpMethod) {
            case GET:
                builder = builder.get();
                break;
            case POST:
                builder = builder.post(requestBody);
                break;
            case PUT:
                builder = builder.put(requestBody);
                break;
            case DELETE:
                builder = builder.delete();
                break;
            case PATCH:
                builder = builder.patch(requestBody);
                break;
            default:
                break;
        }

        String userAgentHeader = String.format("RC-ENGAGE-VOICE-JAVA-SDK Java %s %s", System.getProperty("java.version"), System.getProperty("os.name"));
        Request request = builder.addHeader("Authorization", "Bearer " + this.token.accessToken)
                .addHeader("X-User-Agent", userAgentHeader)
                .build();

        Response response = httpClient.newCall(request).execute();
        int statusCode = response.code();
        if (statusCode < 200 || statusCode > 299) {
            throw new RestException(response, request);
        }
        return response;
    }

    public ResponseBody request(HttpMethod httpMethod, String endpoint, Object queryParameters, RequestBody
            requestBody) throws IOException, RestException {
        Response response = requestRaw(httpMethod, endpoint, queryParameters, requestBody);
        return response.peekBody(Long.MAX_VALUE);
    }
}
