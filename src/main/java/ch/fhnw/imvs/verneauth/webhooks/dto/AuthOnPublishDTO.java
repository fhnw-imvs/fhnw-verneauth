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
package ch.fhnw.imvs.verneauth.webhooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object matching the {@code auth_on_publish} payload for VerneMQ webhooks.
 *
 * @author mluppi
 * @author paradoxxl
 * @see <a href="https://docs.vernemq.com/plugindevelopment/webhookplugins#auth_on_publish">auth_on_publish - VerneMQ Webhook specs</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthOnPublishDTO {

    private String username;

    @JsonProperty("client_id")
    private String clientId;

    private String mountpoint;

    private int qos;

    private String topic;

    private String payload;

    private boolean retain;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }
}
