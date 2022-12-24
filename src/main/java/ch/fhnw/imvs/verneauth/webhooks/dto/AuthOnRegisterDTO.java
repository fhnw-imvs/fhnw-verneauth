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
 * Data Transfer Object matching the {@code auth_on_register} payload for VerneMQ webhooks.
 *
 * @author mluppi
 * @author paradoxxl
 * @see <a href="https://docs.vernemq.com/plugindevelopment/webhookplugins#auth_on_register">auth_on_register - VerneMQ Webhook specs</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthOnRegisterDTO {

    @JsonProperty("peer_addr")
    private String peerAddr;

    @JsonProperty("peer_port")
    private int peerPort;

    private String username;

    private String password;

    private String mountpoint;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("clean_session")
    private boolean cleanSession;

    public String getPeerAddr() {
        return peerAddr;
    }

    public void setPeerAddr(String peerAddr) {
        this.peerAddr = peerAddr;
    }

    public int getPeerPort() {
        return peerPort;
    }

    public void setPeerPort(int peerPort) {
        this.peerPort = peerPort;
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

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }
}
