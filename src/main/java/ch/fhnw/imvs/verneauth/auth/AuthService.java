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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mluppi
 * @author paradoxxl
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final MqttUserRepository mqttUserRepository;
    private final AuthStatisticService authStatisticService;

    @Autowired
    public AuthService(final MqttUserRepository mqttUserRepository, final AuthStatisticService authStatisticService) {
        this.mqttUserRepository = mqttUserRepository;
        this.authStatisticService = authStatisticService;
    }

    public boolean isLoginAuthorized(final String username, final String password) {
        final MqttUser user = mqttUserRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            authStatisticService.logLoginTime(username, true);
            return true;
        }
        logger.info("Denied access for username='{}'", username);
        authStatisticService.logLoginTime(username, false);
        return false;
    }

    public boolean isTopicSubscribeAuthorized(final String username, final List<String> topics) {
        final MqttUser user = mqttUserRepository.findByUsername(username);
        if (user == null || topics == null || topics.isEmpty()) {
            return false;
        }
        boolean authorized = true;
        for (final String topic : topics) {
            if (user.getAclSubscribe().stream()
                    .noneMatch(aclTopic -> isTopicAuthorized(username, aclTopic, topic))) {
                logger.info("Denied subscribe for username='{}' topic='{}'", username, topic);
                authStatisticService.logSubscribeTopic(username, topic, false);
                authorized = false;
            } else {
                authStatisticService.logSubscribeTopic(username, topic, true);
            }
        }
        return authorized;
    }

    public boolean isTopicPublishAuthorized(final String username, final String topic) {
        final MqttUser user = mqttUserRepository.findByUsername(username);
        if (user != null && topic != null && user.getAclPublish().stream()
                .anyMatch(aclTopic -> isTopicAuthorized(username, aclTopic, topic))) {
            authStatisticService.logPublishTopic(username, topic, true);
            return true;
        }
        logger.info("Denied publish for username='{}' topic='{}'", username, topic);
        authStatisticService.logPublishTopic(username, topic, false);
        return false;
    }

    private boolean isTopicAuthorized(final String username, final String aclTopic, final String topic) {
        return MqttTopicMatcher.matchFilter(aclTopic.replace("%u", username), topic);
    }
}
