/*
Copyright 2020 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

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

/**
 * Matcher for MQTT Topic Names against a Topic Filter.
 *
 * @author mluppi
 * @see <a href="https://docs.oasis-open.org/mqtt/mqtt/v5.0/mqtt-v5.0.html">MQTT Version 5.0 - OASIS Standard</a>
 */
public class MqttTopicMatcher {

    private static final Logger logger = LoggerFactory.getLogger(MqttTopicMatcher.class);

    public static final String TOPIC_SEPARATOR = "/";
    public static final String SINGLE_LEVEL_WILDCARD = "+";
    public static final String MULTI_LEVEL_WILDCARD = "#";


    /**
     * Determines whether a given Topic Name matches a given Topic Filter.
     *
     * @param topicFilter the Topic Filter
     * @param topicName   the Topic Name
     * @return {@code true} when the Topic Name matches the Topic Filter, {@code false} otherwise
     */
    @SuppressWarnings("squid:S3776")
    public static boolean matchFilter(final String topicFilter, final String topicName) {
        logger.debug("Matching topicFilter='{}' with topicName='{}'", topicFilter, topicName);

        // [SPEC] The Server MUST NOT match Topic Filters starting with a wildcard character (# or +) with Topic Names beginning with a $ character [MQTT-4.7.2-1].
        if (topicName.startsWith("$") && !topicFilter.startsWith("$")) {
            return false;
        }

        if (!topicFilter.contains(MULTI_LEVEL_WILDCARD)) {
            // When no wildcards are present, we need an exact match between topic filter and topic name
            // [SPEC] Each non-wildcarded level in the Topic Filter has to match the corresponding level in the Topic Name character for character for the match to succeed.
            if (!topicFilter.contains(SINGLE_LEVEL_WILDCARD)) {
                return topicFilter.equals(topicName);
            }
        } else {
            // [SPEC] The multi-level wildcard character MUST be specified either on its own or following a topic level separator.
            // [SPEC] In either case it MUST be the last character specified in the Topic Filter [MQTT-4.7.1-1].
            if (!topicFilter.endsWith(TOPIC_SEPARATOR + MULTI_LEVEL_WILDCARD) && topicFilter.length() > 1) {
                return false;
            }
        }

        // Looping through all common topic levels and apply matching logic
        final String[] filterLevels = topicFilter.split(TOPIC_SEPARATOR, -1); // Preserve all levels while splitting
        final String[] nameLevels = topicName.split(TOPIC_SEPARATOR, -1); // [SPEC] Adjacent Topic level separators indicate a zero-length topic level.
        int i = 0;
        while ((i < filterLevels.length) && (i < nameLevels.length)) {
            if (filterLevels[i].equals(nameLevels[i])) {
                // [SPEC] Each non-wildcarded level in the Topic Filter has to match the corresponding level in the Topic Name character for character for the match to succeed.
                // We need to keep looping but this level matches directly
            } else {
                if (filterLevels[i].equals(SINGLE_LEVEL_WILDCARD)) {
                    // [SPEC] The plus sign (+) is a wildcard character that matches only one topic level.
                    // We need to keep looping but this level matches because of the wildcard
                } else {
                    // [SPEC] The multi-level wildcard represents [...] and any number of child levels.
                    // If there is a multi-level wildcard at this level in the topic filter, the topics match. Otherwise we need to abort.
                    return filterLevels[i].equals(MULTI_LEVEL_WILDCARD);
                }
            }
            i++;
        }

        // Special case if the topic name does not have more levels and the topic filter has a multi-level wildcard in next level
        // [SPEC] The multi-level wildcard represents the parent and [...].
        if ((nameLevels.length == i) && (filterLevels.length > i) && filterLevels[i].equals(MULTI_LEVEL_WILDCARD)) {
            return true;
        }

        // If there are the same amount of levels, the topic name matches the topic filter
        return (filterLevels.length == nameLevels.length);
    }

    private MqttTopicMatcher() {
    }
}
