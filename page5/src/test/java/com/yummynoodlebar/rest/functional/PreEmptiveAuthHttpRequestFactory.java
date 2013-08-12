package com.yummynoodlebar.rest.functional;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import sun.net.www.protocol.http.AuthCache;

public class PreEmptiveAuthHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

  public PreEmptiveAuthHttpRequestFactory(DefaultHttpClient client) {
    super(client);
  }

  @Override
  protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
    AuthCache authCache = new BasicAuthCache();
    BasicScheme basicAuth = new BasicScheme();
    HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort());
    authCache.put(targetHost, basicAuth);
    BasicHttpContext localcontext = new BasicHttpContext();
    localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

    return localcontext;
  }