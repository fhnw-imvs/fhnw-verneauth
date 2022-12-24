/*
Copyright 2020-2022 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ch.fhnw.imvs.verneauth.auth;

import ch.fhnw.imvs.verneauth.persistence.MqttUser;
import ch.fhnw.imvs.verneauth.persistence.MqttUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the {@link AuthService}.
 *
 * @author mluppi
 */
@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    public static final String VALID_USER = "c100";
    public static final String INVALID_USER = "invalid-user";
    public static final String EMPTY_ACL_USER = "empty-acl-user";
    public static final String VALID_PASSWORD = "c100";
    public static final String INVALID_PASSWORD = "invalid-pass";
    public static final String VALID_TOPIC = "dt/valid/topic";
    public static final String INVALID_TOPIC = "dt/invalid/topic";

    @Autowired
    private AuthService authService;

    @MockBean
    private MqttUserRepository mqttUserRepository;

    @BeforeEach
    public void setUp() {
        final List<String> aclSubscribe = new ArrayList<>();
        aclSubscribe.add(VALID_TOPIC + "SUB1");
        aclSubscribe.add(VALID_TOPIC + "SUB2");
        final List<String> aclPublish = new ArrayList<>();
        aclPublish.add(VALID_TOPIC + "PUB1");
        aclPublish.add(VALID_TOPIC + "PUB2");
        final MqttUser validUser = new MqttUser(VALID_USER, VALID_PASSWORD, aclSubscribe, aclPublish);
        Mockito.when(mqttUserRepository.findByUsername(VALID_USER)).thenReturn(validUser);
        final MqttUser emptyAclUser = new MqttUser(EMPTY_ACL_USER, VALID_PASSWORD, new ArrayList<>(), new ArrayList<>());
        Mockito.when(mqttUserRepository.findByUsername(EMPTY_ACL_USER)).thenReturn(emptyAclUser);
    }

    @Test
    void isAuthorized() {
        assertTrue(authService.isLoginAuthorized(VALID_USER, VALID_PASSWORD));
        assertFalse(authService.isLoginAuthorized(VALID_USER, INVALID_PASSWORD));
        assertFalse(authService.isLoginAuthorized(INVALID_USER, VALID_PASSWORD));
        assertFalse(authService.isLoginAuthorized(INVALID_USER, INVALID_PASSWORD));
        assertFalse(authService.isLoginAuthorized(VALID_USER, ""));
        assertFalse(authService.isLoginAuthorized("", ""));
    }

    @Test
    void authorizeSubTopic() {
        final List<String> validTopics = new ArrayList<>();
        validTopics.add(VALID_TOPIC + "SUB1");

        final List<String> invalidTopic1 = new ArrayList<>();
        invalidTopic1.add(INVALID_TOPIC + "SUB1");

        final List<String> invalidTopic2 = new ArrayList<>();
        invalidTopic2.add(INVALID_TOPIC + "SUB2");

        final List<String> mixedTopicsValidFirst = new ArrayList<>();
        mixedTopicsValidFirst.add(VALID_TOPIC + "SUB1");
        mixedTopicsValidFirst.add(INVALID_TOPIC + "SUB1");

        final List<String> mixedTopicsInvalidFirst = new ArrayList<>();
        mixedTopicsInvalidFirst.add(INVALID_TOPIC + "SUB1");
        mixedTopicsInvalidFirst.add(VALID_TOPIC + "SUB1");

        assertFalse(authService.isTopicSubscribeAuthorized(VALID_USER, null));
        assertFalse(authService.isTopicSubscribeAuthorized(VALID_USER, new ArrayList<>()));
        assertTrue(authService.isTopicSubscribeAuthorized(VALID_USER, validTopics));
        assertFalse(authService.isTopicSubscribeAuthorized(VALID_USER, invalidTopic1));
        assertFalse(authService.isTopicSubscribeAuthorized(VALID_USER, invalidTopic2));
        assertFalse(authService.isTopicSubscribeAuthorized(VALID_USER, mixedTopicsValidFirst));
        assertFalse(authService.isTopicSubscribeAuthorized(VALID_USER, mixedTopicsInvalidFirst));

        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, null));
        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, new ArrayList<>()));
        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, validTopics));
        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, invalidTopic1));
        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, invalidTopic2));
        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, mixedTopicsValidFirst));
        assertFalse(authService.isTopicSubscribeAuthorized(EMPTY_ACL_USER, mixedTopicsInvalidFirst));
    }

    @Test
    void authorizePubTopic() {
        assertTrue(authService.isTopicPublishAuthorized(VALID_USER, VALID_TOPIC + "PUB1"));
        assertTrue(authService.isTopicPublishAuthorized(VALID_USER, VALID_TOPIC + "PUB2"));
        assertFalse(authService.isTopicPublishAuthorized(VALID_USER, INVALID_TOPIC));
        assertFalse(authService.isTopicPublishAuthorized(VALID_USER, null));
        assertFalse(authService.isTopicPublishAuthorized(VALID_USER, ""));

        assertFalse(authService.isTopicPublishAuthorized(EMPTY_ACL_USER, VALID_TOPIC + "PUB1"));
        assertFalse(authService.isTopicPublishAuthorized(EMPTY_ACL_USER, VALID_TOPIC + "PUB2"));
        assertFalse(authService.isTopicPublishAuthorized(EMPTY_ACL_USER, INVALID_TOPIC));
        assertFalse(authService.isTopicPublishAuthorized(EMPTY_ACL_USER, null));
        assertFalse(authService.isTopicPublishAuthorized(EMPTY_ACL_USER, ""));
    }

}
