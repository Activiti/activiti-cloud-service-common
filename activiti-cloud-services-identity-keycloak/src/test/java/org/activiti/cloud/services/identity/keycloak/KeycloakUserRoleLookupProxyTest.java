package org.activiti.cloud.services.identity.keycloak;

import org.activiti.engine.UserRoleLookupProxy;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class KeycloakUserRoleLookupProxyTest {

    private KeycloakUserRoleLookupProxy userRoleLookupProxy;

    @Mock
    private KeycloakInstanceWrapper keycloakInstanceWrapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userRoleLookupProxy = new KeycloakUserRoleLookupProxy(keycloakInstanceWrapper);
        userRoleLookupProxy.setAdminRoleName("admin");

        List<UserRepresentation> users = new ArrayList<>();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("id");
        users.add(userRepresentation);

        when(keycloakInstanceWrapper.getUser(anyString())).thenReturn(users);

    }

    @Test
    public void testGetRoles() {

        List<RoleRepresentation> roleRepresentations = new ArrayList<>();
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("testrole");
        roleRepresentations.add(roleRepresentation);
        when(keycloakInstanceWrapper.getRolesForUser(anyString())).thenReturn(roleRepresentations);


        assertThat(userRoleLookupProxy.getRolesForUser("bob")).contains("testrole");
        assertThat(userRoleLookupProxy.isAdmin("bob")).isFalse();
    }

    @Test
    public void testAdmin() {

        List<RoleRepresentation> roleRepresentations = new ArrayList<>();
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("admin");
        roleRepresentations.add(roleRepresentation);
        when(keycloakInstanceWrapper.getRolesForUser(anyString())).thenReturn(roleRepresentations);

        assertThat(userRoleLookupProxy.isAdmin("bob")).isTrue();
    }
}