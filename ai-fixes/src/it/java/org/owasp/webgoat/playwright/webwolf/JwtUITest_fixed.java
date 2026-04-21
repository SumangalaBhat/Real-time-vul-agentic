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
The provided code is not a Java code snippet and does not contain any SQL i[1D[K
injection or XSS vulnerabilities. It appears to be a JSON Web Token (JWT) t[1D[K
that contains a JSON payload.

However, I can provide you with some general guidelines on how to handle JW[2D[K
JWTs securely in your Java application:

1. Validate the token signature using a trusted public key.
2. Verify the token's expiration time and prevent expired tokens from being[5D[K
being used.
3. Ensure that the token is only valid for the intended user or audience.

Here's an example of how you might validate a JWT in Java using the JJWT li[2D[K
library:
```java
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

public class MyService {
    public boolean validateToken(String token) {
        try {
            // Validate the token signature and expiration time
            JwsValidator validator = new JwsValidator("path/to/private/key"[34D[K
JwsValidator("path/to/private/key");
            if (!validator.validate(token)) {
                return false; // Token is invalid or expired
            }

            // Get the user's identity from the token
            String userId = Jwts.parser().setSigningKey("path/to/public/key[47D[K
Jwts.parser().setSigningKey("path/to/public/key").parseClaimsJws(token).getJwts.parser().setSigningKey("path/to/public/key).parseClaimsJws(token).getBody().getSubject();
            // Use the user's identity to authenticate or authorize

        } catch (Exception e) {
            return false; // Token is invalid or expired
        }
    }
}
```
Note that this code snippet assumes you have a trusted private key and a pu[2D[K
public key used for token signature validation. You should keep your privat[6D[K
private key secure and only share it with trusted parties.

Additionally, consider using a robust JWT library like JJWT, which provides[8D[K
provides features such as token signing, verification, and expiration time [K
management.

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
