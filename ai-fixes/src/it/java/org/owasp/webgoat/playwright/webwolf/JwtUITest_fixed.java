/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.playwright.webwolf;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.playwright.webgoat.PlaywrightTest;
import org.owasp.webgoat.playwright.webgoat.helpers.Authentication;

import com.microsoft.playwright.options.AriaRole;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

class JwtUITest extends PlaywrightTest {

  @Test
  void shouldDecodeJwt(Browser browser) {
    var page = Authentication.sylvester(browser);
    var secretKey = "test";
    var jwt =
The provided code snippet appears to be a JSON Web Token (JWT) rather than [K
Java code. However, assuming you meant to ask me to fix the security vulner[6D[K
vulnerabilities in some Java code that's using this JWT token, here are som[3D[K
some general suggestions:

**SQL Injection:**
Since there is no SQL query or database interaction in this code snippet, t[1D[K
there is no risk of SQL injection.

**XSS:**
There is no user input, HTML output, or JavaScript execution in this code s[1D[K
snippet, so there is no risk of Cross-Site Scripting (XSS) attacks.

However, if you're using the JWT token to authenticate users and make API c[1D[K
calls that involve database queries or dynamic content generation, you shou[4D[K
should ensure that any user-input data is properly sanitized and validated.[10D[K
validated.

Here's an example of how you might validate a JWT token in Java:

```java
String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkw[62D[K
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

// Assume you have a JWT library or framework
if (isValidJwt(jwt)) {
    // Token is valid, proceed with authentication and API calls
} else {
    // Token is invalid, reject the request or prompt for re-authentication[17D[K
re-authentication
}

public boolean isValidJwt(String jwt) {
    try {
        // Implement your own validation logic using a JWT library or frame[5D[K
framework
        // For example, you could use the Nimbus-JWT library:
        JWSVerifier verifier = DefaultJWSVerifier.create(jwtSecrecyKey);
        DecodedJWT decodedJWT = JWT.decode(jwt, verifier);
        return true;
    } catch (Exception e) {
        return false; // Invalid or malformed JWT token
    }
}
```

Remember to replace `jwtSecrecyKey` with your actual encryption key and imp[3D[K
implement the necessary validation logic according to your specific use cas[3D[K
case.

    page.navigate(webWolfURL("jwt"));
    page.getByPlaceholder("Enter your secret key").fill(secretKey);
    page.getByPlaceholder("Paste token here").type(jwt);
    assertThat(page.locator("#header"))
        .hasValue("{\n  \"alg\" : \"HS256\",\n  \"typ\" : \"JWT\"\n}");
    assertThat(page.locator("#payload"))
        .hasValue(
            "{\n"
                + "  \"iat\" : 1516239022,\n"
                + "  \"name\" : \"John Doe\",\n"
                + "  \"sub\" : \"1234567890\"\n"
                + "}");
  }

  @Test
  void shouldValidateJwtUsingJwks(Browser browser) throws JoseException {
    var page = Authentication.sylvester(browser);

    RsaJsonWebKey jwk = RsaJwkGenerator.generateJwk(2048);
    jwk.setKeyId("kid-1");
    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload("{\"sub\":\"123\"}");
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    jws.setKey(jwk.getPrivateKey());
    jws.setKeyIdHeaderValue(jwk.getKeyId());
    var rsaJwt = jws.getCompactSerialization();
    var jwks = new JsonWebKeySet(jwk).toJson(OutputControlLevel.PUBLIC_ONLY);

    page.navigate(webWolfURL("jwt"));
    page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("JWKS (public keys)")).check();
    page.getByPlaceholder("Paste token here").type(rsaJwt);
    page.locator("#jwks").fill(jwks);
    assertThat(page.locator("#signatureValid")).hasText("Signature valid");
  }
}
