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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mluppi
 */
@Service
public class AuthStatisticService {

    private static final Logger logger = LoggerFactory.getLogger(AuthStatisticService.class);

    private final Map<String, Long> grantedLoginTimeByUsername = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> grantedUsernameBySubscribeTopic = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> grantedUsernameByPublishTopic = new ConcurrentHashMap<>();

    private final Map<String, Long> deniedLoginTimeByUsername = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> deniedUsernameBySubscribeTopic = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> deniedUsernameByPublishTopic = new ConcurrentHashMap<>();

    public void logLoginTime(final String username, final boolean granted) {
        if (granted) {
            grantedLoginTimeByUsername.put(username, System.currentTimeMillis());
        } else {
            deniedLoginTimeByUsername.put(username, System.currentTimeMillis());
        }
    }

    public void logSubscribeTopic(final String username, final String topic, final boolean granted) {
        if (topic != null) {
            if (granted) {
                addToUsernameByTopicMap(grantedUsernameBySubscribeTopic, topic, username);
            } else {
                addToUsernameByTopicMap(deniedUsernameBySubscribeTopic, topic, username);
            }
        }
    }

    public void logPublishTopic(final String username, final String topic, final boolean granted) {
        if (topic != null) {
            if (granted) {
                addToUsernameByTopicMap(grantedUsernameByPublishTopic, topic, username);
            } else {
                addToUsernameByTopicMap(deniedUsernameByPublishTopic, topic, username);
            }
        }
    }

    public Map<String, Long> getGrantedLoginTimeByUsername() {
        return Collections.unmodifiableMap(grantedLoginTimeByUsername);
    }

    public Map<String, Set<String>> getGrantedUsernameBySubscribeTopic() {
        return Collections.unmodifiableMap(grantedUsernameBySubscribeTopic);
    }

    public Map<String, Set<String>> getDeniedUsernameBySubscribeTopic() {
        return Collections.unmodifiableMap(deniedUsernameBySubscribeTopic);
    }

    public Map<String, Long> getDeniedLoginTimeByUsername() {
        return Collections.unmodifiableMap(deniedLoginTimeByUsername);
    }

    public Map<String, Set<String>> getGrantedUsernameByPublishTopic() {
        return Collections.unmodifiableMap(grantedUsernameByPublishTopic);
    }

    public Map<String, Set<String>> getDeniedUsernameByPublishTopic() {
        return Collections.unmodifiableMap(deniedUsernameByPublishTopic);
    }

    public void clearStatistic() {
        grantedLoginTimeByUsername.clear();
        grantedUsernameBySubscribeTopic.clear();
        grantedUsernameByPublishTopic.clear();
        deniedLoginTimeByUsername.clear();
        deniedUsernameBySubscribeTopic.clear();
        deniedUsernameByPublishTopic.clear();
        logger.info("Cleared auth statistic");
    }

    private void addToUsernameByTopicMap(final Map<String, Set<String>> map, final String topic, final String username) {
        map.computeIfAbsent(topic, k -> Collections.synchronizedSortedSet(new TreeSet<>())).add(username);
    }
}
