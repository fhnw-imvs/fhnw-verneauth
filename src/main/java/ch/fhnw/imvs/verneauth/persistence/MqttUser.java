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
package ch.fhnw.imvs.verneauth.persistence;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mluppi
 * @author paradoxxl
 */
@Entity
@Table(name = "mqtt_user")
public class MqttUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @ElementCollection
    private List<String> aclSubscribe;

    @ElementCollection
    private List<String> aclPublish;

    public MqttUser() {
        this.aclSubscribe = new ArrayList<>();
        this.aclPublish = new ArrayList<>();
    }

    public MqttUser(final String username, final String password, final List<String> aclSubscribe, final List<String> aclPublish) {
        this.username = username;
        this.password = password;
        this.aclSubscribe = aclSubscribe != null ? aclSubscribe : new ArrayList<>();
        this.aclPublish = aclPublish != null ? aclPublish : new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAclSubscribe() {
        return aclSubscribe;
    }

    public void setAclSubscribe(List<String> aclSubscribe) {
        this.aclSubscribe = aclSubscribe;
    }

    public List<String> getAclPublish() {
        return aclPublish;
    }

    public void setAclPublish(List<String> aclPublish) {
        this.aclPublish = aclPublish;
    }

}
