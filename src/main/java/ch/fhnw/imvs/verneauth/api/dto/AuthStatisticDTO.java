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
package ch.fhnw.imvs.verneauth.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Set;

/**
 * @author mluppi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthStatisticDTO {

    private Map<String, Long> grantedUserLogins;
    private Map<String, Set<String>> grantedSubscribeTopics;
    private Map<String, Set<String>> grantedPublishTopics;
    private Map<String, Long> deniedUserLogins;
    private Map<String, Set<String>> deniedSubscribeTopics;
    private Map<String, Set<String>> deniedPublishTopics;

    public Map<String, Long> getGrantedUserLogins() {
        return grantedUserLogins;
    }

    public void setGrantedUserLogins(final Map<String, Long> grantedUserLogins) {
        this.grantedUserLogins = grantedUserLogins;
    }

    public Map<String, Set<String>> getGrantedSubscribeTopics() {
        return grantedSubscribeTopics;
    }

    public void setGrantedSubscribeTopics(final Map<String, Set<String>> grantedSubscribeTopics) {
        this.grantedSubscribeTopics = grantedSubscribeTopics;
    }

    public Map<String, Set<String>> getGrantedPublishTopics() {
        return grantedPublishTopics;
    }

    public void setGrantedPublishTopics(final Map<String, Set<String>> grantedPublishTopics) {
        this.grantedPublishTopics = grantedPublishTopics;
    }

    public Map<String, Long> getDeniedUserLogins() {
        return deniedUserLogins;
    }

    public void setDeniedUserLogins(final Map<String, Long> deniedUserLogins) {
        this.deniedUserLogins = deniedUserLogins;
    }

    public Map<String, Set<String>> getDeniedSubscribeTopics() {
        return deniedSubscribeTopics;
    }

    public void setDeniedSubscribeTopics(final Map<String, Set<String>> deniedSubscribeTopics) {
        this.deniedSubscribeTopics = deniedSubscribeTopics;
    }

    public Map<String, Set<String>> getDeniedPublishTopics() {
        return deniedPublishTopics;
    }

    public void setDeniedPublishTopics(final Map<String, Set<String>> deniedPublishTopics) {
        this.deniedPublishTopics = deniedPublishTopics;
    }
}
