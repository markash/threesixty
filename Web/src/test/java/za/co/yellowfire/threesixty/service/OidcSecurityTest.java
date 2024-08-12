package za.co.yellowfire.threesixty.service;


import com.github.markash.threesixty.web.security.AzureGrantedAuthoritiesMapper;
import com.github.markash.threesixty.web.security.CurrentUserProvider;
import com.github.markash.threesixty.web.service.UserInfo;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class OidcSecurityTest {

    private DefaultCurrentUserProvider userProvider;

    @BeforeEach
    void before() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("preferred_username", "c.brown@gmail.com");
        claims.put("given_name", "Charlie");
        claims.put("family_name", "Brown");
        claims.put("sub", "1234567890");

        this.userProvider = new DefaultCurrentUserProvider(claims);
    }

    @Test
    void tesCurrentUserProvide() {

        Optional<UserInfo> result = this.userProvider.getCurrentUser();

        if (result.isEmpty())
            Assert.fail("User info should not be empty");

        UserInfo userInfo = result.get();
        Assert.assertEquals("c.brown@gmail.com", userInfo.getUsername());
        Assert.assertEquals("Charlie", userInfo.getFirstName());
        Assert.assertEquals("Brown", userInfo.getLastName());
        Assert.assertEquals("Charlie Brown", userInfo.getFullName());
    }

    @Test
    void testAzureGrantedAuthority() {

        Optional<OidcUser> result = this.userProvider.getOauth2User();

        if (result.isEmpty())
            Assert.fail("Result should not be empty");

        Collection<? extends GrantedAuthority> grantedAuthorities =
                new AzureGrantedAuthoritiesMapper().mapAuthorities(result.get().getAuthorities());

        Assert.assertEquals(2, grantedAuthorities.size());
        Assert.assertTrue(grantedAuthorities.stream().anyMatch(grantedAuthority -> "User.Read".equals(grantedAuthority.getAuthority())));
        Assert.assertTrue(grantedAuthorities.stream().anyMatch(grantedAuthority -> "Assessment.Read".equals(grantedAuthority.getAuthority())));
    }

    private static class DefaultCurrentUserProvider extends CurrentUserProvider {
        private final Map<String, Object> claims;
        public DefaultCurrentUserProvider(Map<String, Object> claims) {
            this.claims = claims;
        }

        @Override
        protected Optional<OidcUser> getOauth2User() {

            final OidcIdToken token =new OidcIdToken(
                    "TestIdToken",
                    LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant(),
                    LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant(),
                    claims);

            final Map<String, Object> claims = new HashMap<>();
            claims.put("roles", List.of("User.Read", "Assessment.Read"));
            claims.put("name", "Charlie Brown");

            final OidcUserAuthority authority = new OidcUserAuthority(token, new OidcUserInfo(claims));

            final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(authority);


            return Optional.of(new DefaultOidcUser(grantedAuthorities, token));
        }
    }
}
