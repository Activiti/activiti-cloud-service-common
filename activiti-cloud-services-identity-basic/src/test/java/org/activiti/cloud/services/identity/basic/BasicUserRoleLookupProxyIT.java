package org.activiti.cloud.services.identity.basic;

import org.activiti.engine.UserRoleLookupProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BasicUserRoleLookupProxyIT.Configuration.class)
public class BasicUserRoleLookupProxyIT {

    @org.springframework.context.annotation.Configuration
    @ComponentScan("org.activiti.cloud.services.identity.basic")
    public static class Configuration{

    }

    @Autowired
    private UserRoleLookupProxy userRoleLookupProxy;

    @Test
    public void testAdminRole() throws Exception {
        assertThat(userRoleLookupProxy.isAdmin("client")).isTrue();
        assertThat(userRoleLookupProxy.isAdmin("testuser")).isFalse();
    }
}
