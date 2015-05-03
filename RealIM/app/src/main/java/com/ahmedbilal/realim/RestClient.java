package com.ahmedbilal.realim;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RestClient implements RestOperations{

	public Map postForObject(Object obj, String servicePath,final Map<String,String> headers)
			throws IOException {

        Gson gson = new Gson();
        String POST_JSON = "";
        if(obj != null) {
            POST_JSON = gson.toJson(obj); //JSONObject.fromObject(obj).toString();
        }
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),DEFAULT_CONN_TIME_OUT_5_MINUTES);
		HttpPost httpPost = new HttpPost(servicePath);
		StringEntity entity = new StringEntity(POST_JSON, "UTF-8");
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		if(headers!=null && headers.keySet().size()>0){
			for (String key : headers.keySet()) {
				httpPost.setHeader(key, headers.get(key));
			}
		}
		entity.setContentType(basicHeader);
		httpPost.setEntity(entity);
		HttpResponse predictResponse = httpClient.execute(httpPost);
        String response = read(predictResponse.getEntity().getContent());


        LinkedTreeMap<String,Object> responeMap = new LinkedTreeMap<>();
        responeMap = (LinkedTreeMap<String, Object>) gson.fromJson(response,LinkedTreeMap.class);

		return responeMap;
	}

	/*
	 * Make the HTTP GET request, marshaling the the response to a String
	 */
	public String get(final String servicePath,final int connTimeOut) throws IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpGet httpGet = new HttpGet(servicePath);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		httpGet.setHeader(basicHeader );
		HttpResponse predictResponse = httpClient.execute(httpGet);
		InputStream inStream = predictResponse.getEntity().getContent();
		return read(inStream);
	}


	public String get(String servicePath) throws IOException {
		return get(servicePath, new HashMap<String, String>());
	}
	
	public String get(String servicePath, Map<String, String> headers) throws IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), DEFAULT_CONN_TIME_OUT_5_MINUTES);
		HttpGet httpGet = new HttpGet(servicePath);
		Iterator<String> itrKeys = headers.keySet().iterator();
		while(itrKeys.hasNext()) {
			String key = itrKeys.next();
			String value = headers.get(key);
			httpGet.setHeader(key, value);
		}
		HttpResponse predictResponse = httpClient.execute(httpGet);
        InputStream inStream = predictResponse.getEntity().getContent();
        return read(inStream);
		//return predictResponse;
	}

	private String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(in), 1024);
		for (String line = bReader.readLine(); line != null; line = bReader.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
}
