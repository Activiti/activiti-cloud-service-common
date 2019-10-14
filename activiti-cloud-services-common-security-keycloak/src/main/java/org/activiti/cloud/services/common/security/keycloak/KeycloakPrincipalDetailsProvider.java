/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.common.security.keycloak;

import org.activiti.api.runtime.shared.security.PrincipalDetailsProvider;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.lang.NonNull;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KeycloakPrincipalDetailsProvider implements PrincipalDetailsProvider {

    private final KeycloakAccessTokenProvider keycloakAccessTokenProvider;
    private final KeycloakAccessTokenValidator keycloakAccessTokenValidator;
    
    public KeycloakPrincipalDetailsProvider(@NonNull KeycloakAccessTokenProvider keycloakSecurityContextProvider,
                                            @NonNull KeycloakAccessTokenValidator keycloakAccessTokenValidator) {
        this.keycloakAccessTokenProvider = keycloakSecurityContextProvider;
        this.keycloakAccessTokenValidator = keycloakAccessTokenValidator;
    }
    
    @Override
    public List<String> getGroups(@NonNull Principal principal) {
        return keycloakAccessTokenProvider.accessToken(principal)
                                          .filter(keycloakAccessTokenValidator::isValid)
                                          .map(AccessToken::getOtherClaims)
                                          .map(it -> it.get("groups"))
                                          .filter(Collection.class::isInstance)
                                          .<Collection<String>>map(Collection.class::cast)
                                          .map(groups -> Collections.unmodifiableList(new ArrayList<>(groups)))
                                          .orElseThrow(() -> new SecurityException("Invalid groups claim in access token"));
    }

    @Override
    public List<String> getRoles(@NonNull Principal principal) {
        return keycloakAccessTokenProvider.accessToken(principal)
                                          .filter(keycloakAccessTokenValidator::isValid)
                                          .map(AccessToken::getRealmAccess)
                                          .map(Access::getRoles)
                                          .map(roles -> Collections.unmodifiableList(new ArrayList<String>(roles)))
                                          .orElseThrow(() -> new SecurityException("Invalid realm access roles in access token"));
    }
}
