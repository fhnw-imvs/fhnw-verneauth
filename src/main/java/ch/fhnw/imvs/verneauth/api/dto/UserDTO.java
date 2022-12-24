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
package ch.fhnw.imvs.verneauth.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author mluppi
 * @author paradoxxl
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private String username;
    private String password;
    private List<String> aclSubscribe;
    private List<String> aclPublish;

    public UserDTO() {
        // used by Jackson
    }

    public UserDTO(final String username, final String password, final List<String> aclSubscribe, final List<String> aclPublish) {
        this.username = username;
        this.password = password;
        this.aclSubscribe = aclSubscribe;
        this.aclPublish = aclPublish;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<String> getAclSubscribe() {
        return aclSubscribe;
    }

    public void setAclSubscribe(final List<String> aclSubscribe) {
        this.aclSubscribe = aclSubscribe;
    }

    public List<String> getAclPublish() {
        return aclPublish;
    }

    public void setAclPublish(final List<String> aclPublish) {
        this.aclPublish = aclPublish;
    }
}
