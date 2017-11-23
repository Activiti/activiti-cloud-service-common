package org.activiti.cloud.services.identity.basic;

import org.activiti.engine.UserGroupLookupProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class BasicUserGroupLookupProxyTest {

    private UserGroupLookupProxy userGroupLookupProxy;

    @Mock
    private UserDetailsService userDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userGroupLookupProxy = new BasicUserGroupLookupProxy(userDetailsService);

    }


    @Test
    public void testGetGroups() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("testrole"));
        User user  = new User("test","pass",authorities);

        when(userDetailsService.loadUserByUsername("test"))
                .thenReturn(user);

        assertThat(userGroupLookupProxy.getGroupsForCandidateUser("test")).contains("testrole");
    }

}
