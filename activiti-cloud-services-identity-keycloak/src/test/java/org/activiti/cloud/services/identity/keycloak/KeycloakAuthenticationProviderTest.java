package org.activiti.cloud.services.identity.keycloak;

import org.junit.Before;
import org.junit.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeycloakAuthenticationProviderTest {

    private KeycloakActivitiAuthenticationProvider keycloakActivitiAuthenticationProvider = new KeycloakActivitiAuthenticationProvider();
    private KeycloakAuthenticationToken token;
    private KeycloakAccount keycloakAccount;
    private KeycloakPrincipal principal;
    private RefreshableKeycloakSecurityContext keycloakSecurityContext;

    @Before
    public void setUp(){
        keycloakSecurityContext = mock(RefreshableKeycloakSecurityContext.class);
        principal = new KeycloakPrincipal("bob",keycloakSecurityContext);
        keycloakAccount = new SimpleKeycloakAccount(principal, new HashSet<>(Arrays.asList("role1","role2")),keycloakSecurityContext);
        token = new KeycloakAuthenticationToken(keycloakAccount,false);

    }

    @Test
    public void testAuthenticate(){

        assertThat(keycloakActivitiAuthenticationProvider.authenticate(token)).isNotNull();

        AccessToken accessToken = mock(AccessToken.class);
        when(keycloakSecurityContext.getToken()).thenReturn(accessToken);
        when(accessToken.getPreferredUsername()).thenReturn("bob");

        assertThat(keycloakActivitiAuthenticationProvider.authenticate(token)).isNotNull();

    }

    @Test
    public void testAuthenticateWithPreferredUsername(){

        AccessToken accessToken = mock(AccessToken.class);
        when(keycloakSecurityContext.getToken()).thenReturn(accessToken);
        when(accessToken.getPreferredUsername()).thenReturn("bob");

        assertThat(keycloakActivitiAuthenticationProvider.authenticate(token)).isNotNull();

    }
}
