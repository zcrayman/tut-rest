## Step 5: Securing your Service with Spring Security


Add spring security to the build.gradle.  This includes adding the spring milestone repository.
Also added is a gradle plugin that adds a 'provided' dependency scope, used for the servlet api.

Add SecurityConfig in the Config domain and include in the WebAppInitializer root context.

 This config create an authentication with an in memory database of users, containing a single user, 'letsnosh' with the USER role.

 It protects the /aggregators/** urls to ensure that only users with the USER role can access them.
 It enables only HTTP BASIC authentication, and does not implement any challenge/ login system.
   So, all accesses to the page must include an HTTP BASIC Authorization Header. Any request without this header will be responded to with a 403 Forbidden status.

Add ContextLoaderListener in the WebAppInitializer, using the root context

Add a new Filter in WebAppInitializer.configureSpringSecurity.


Update rest.funcitonal.OrderTests to include the necessary authentication header.

Done by updating the HttpHeaders to include the correct header

static HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    String authorisation = "letsnosh" + ":" + "noshing";
    byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
    headers.add("Authorization", "Basic " + new String(encodedAuthorisation));

    return headers;
  }

[Next… Make your Service Discoverable using Spring HATEOAS](../6/)
