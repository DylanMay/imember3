package com.yundong.imember.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HTTPRequestHelper {
	//获取类的名字
   private static final String CLASSTAG = HTTPRequestHelper.class.getSimpleName();

   private static final int POST_TYPE = 1;
   private static final int GET_TYPE = 2;
   private static final String CONTENT_TYPE = "Content-Type";
   private static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
   private static final String MIME_TEXT_PLAIN = "text/plain";

   // establish client as static
   // (best practice in 
   public static final DefaultHttpClient client;
   private static final int REQUEST_TIMEOUT = 40*1000;//设置请求超时10秒钟  
   private static final int SO_TIMEOUT = 40*1000;  //设置等待数据超时时间10秒钟   
   
   static {
      HttpParams params = new BasicHttpParams();      
      params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
      params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
      ///params.setParameter(CoreProtocolPNames.USER_AGENT, "Android-x");
      
      params.setParameter("networkaddress.cache.negative.ttl", "5");
//      params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);
//      params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
      params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
      
      //如果要使用HTTPs访问资源，首先需要能够对访问资源和端口号进行判别。
      //这样的判别将影响使用什么程序以及访问什么端口。
      //用到一个EasySSLSocketFactory的类（原理是自己签名认证自己）
      //注册SchemeRegistry的https schemes来创建一个httpclient
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(
               new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      schemeRegistry.register(
               new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

      ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
      
      client = new DefaultHttpClient(cm, params);
      
   }

   private final ResponseHandler<String> responseHandler;

   // ctor that takes in a ResponseHandler
   public HTTPRequestHelper(final ResponseHandler<String> responseHandler) {
      this.responseHandler = responseHandler;
   }

   // ctor that automatically uses String based ResponseHandler
   public HTTPRequestHelper(final Handler handler, Context context) {
      this(HTTPRequestHelper.getResponseHandlerInstance(handler, context));
   }
   
   /**
    * Perform a simple HTTP GET operation.
    * 
    */
   public void performGet(final String url) throws Exception{
      performRequest(null, url, null, null, null, null, HTTPRequestHelper.GET_TYPE);
   }

   public void performPost(final String url) throws Exception{
	      performRequest(null, url, null, null, null, null, HTTPRequestHelper.POST_TYPE);
	   }
   /**
    * Perform an HTTP GET operation with user/pass and headers.
    * 
    */
   public void performGet(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders) throws Exception{
      performRequest(null, url, user, pass, additionalHeaders, null, HTTPRequestHelper.GET_TYPE);
   }

   /**
    * Perform an HTTP POST operation with specified content type.
    * 
    */
   public void performPost(final String contentType, final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders, final Map<String, String> params) throws Exception{
      performRequest(contentType, url, user, pass, additionalHeaders, params, HTTPRequestHelper.POST_TYPE);
   }

   /**
    * Perform an HTTP POST operation with a default conent-type of
    * "application/x-www-form-urlencoded."
    * 
    */
   public void performPost(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders, final Map<String, String> params) throws Exception{
      performRequest(HTTPRequestHelper.MIME_FORM_ENCODED, url, user, pass, additionalHeaders, params,
               HTTPRequestHelper.POST_TYPE);
   }

   /**
    * Private heavy lifting method that performs GET or POST with supplied url, user, pass, data,
    * and headers.
    * 
    * @param contentType
    * @param url
    * @param user
    * @param pass
    * @param headers
    * @param params
    * @param requestType
    */
   private void performRequest(final String contentType, final String url, final String user, final String pass,
            final Map<String, String> headers, final Map<String, String> params, final int requestType) throws Exception{

      Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " making HTTP request to url - " + url);

      // add user and pass to client credentials if present
      if ((user != null) && (pass != null)) {
         Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " user and pass present, adding credentials to request");
         client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
      }

      // process headers using request interceptor
      final Map<String, String> sendHeaders = new HashMap<String, String>();
      if ((headers != null) && (headers.size() > 0)) {
         sendHeaders.putAll(headers);
      }
      if (requestType == HTTPRequestHelper.POST_TYPE) {
         sendHeaders.put(HTTPRequestHelper.CONTENT_TYPE, contentType);
      }
      if (sendHeaders.size() > 0) {
         client.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
               for (String key : sendHeaders.keySet()) {
                  if (!request.containsHeader(key)) {
                     Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " adding header: " + key + " | "
                              + sendHeaders.get(key));
                     request.addHeader(key, sendHeaders.get(key));
                  }
               }
            }
         });
      }

      // handle POST or GET request respectively
      if (requestType == HTTPRequestHelper.POST_TYPE) {
         Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest POST");
         HttpPost method = new HttpPost(url);

         // data - name/value params
         List<NameValuePair> nvps = null;
         if ((params != null) && (params.size() > 0)) {
            nvps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
               Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " adding param: " + key + " | " + params.get(key));
               nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
         }
         if (nvps != null) {
            try {
               method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
               Log.e(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG, e);
            }
         }
         execute(client, method);
      } else if (requestType == HTTPRequestHelper.GET_TYPE) {
         Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest GET");
         HttpGet method = new HttpGet(url);
         execute(client, method);
      }
   }

   /**
    * Once the client and method are established, execute the request. 
    * 
    * @param client
    * @param method
    */
   private void execute(HttpClient client, HttpRequestBase method) {
      Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " execute invoked");

      // create a response specifically for errors (in case)
      BasicHttpResponse errorResponse = new BasicHttpResponse(new ProtocolVersion("HTTP_ERROR", 1, 1), 500, "ERROR");

      try {
         client.execute(method, this.responseHandler);
         Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " request completed");
      } catch (Exception e) {
         Log.e(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG, e);
         errorResponse.setReasonPhrase(e.getMessage());
         try {
            this.responseHandler.handleResponse(errorResponse);
         } catch (Exception ex) {
            Log.e(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG, ex);
         }
      }
   }

   /**
    * Static utility method to create a default ResponseHandler that sends a Message to the passed
    * in Handler with the response as a String, after the request completes.
    * 
    * @param handler
    * @return
    */
   public static ResponseHandler<String> getResponseHandlerInstance(final Handler handler, final Context context) {
      final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

         public String handleResponse(final HttpResponse response) {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            StatusLine status = response.getStatusLine();
            Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " statusCode - " + status.getStatusCode());
            Log.d(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " statusReasonPhrase - " + status.getReasonPhrase());
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
               try {
                  result = HTTPRequestHelper.inputStreamToString(entity.getContent());
                  bundle.putString("RESPONSE", result);
                  bundle.putBoolean("SUCCESS", true);
                  //自定义的
//                  bundle.putSerializable("RESPONSE_IS", (Serializable) entity.getContent());
                  message.setData(bundle);
                  handler.sendMessage(message);
               } catch (IOException e) {
                  Log.e(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                  bundle.putString("RESPONSE", "Error - " + e.getMessage());
                  bundle.putBoolean("SUCCESS", false);
                  e.printStackTrace();
                  message.setData(bundle);
                  handler.sendMessage(message);
                  
//                  createDialog(context);
               }
            } else {
               Log.w(CLASSTAG, " " + HTTPRequestHelper.CLASSTAG + " empty response entity, HTTP error occurred");
               bundle.putString("RESPONSE", "Error - " + response.getStatusLine().getReasonPhrase());
               bundle.putBoolean("SUCCESS", false);
               message.setData(bundle);
               handler.sendMessage(message);
               
//               createDialog(context);
            }
            return result;
         }
      };
      return responseHandler;
   }
   

   private static String inputStreamToString(final InputStream stream) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = br.readLine()) != null) {
         sb.append(line + "\n");
      }
      br.close();
      return sb.toString();
   }
}
