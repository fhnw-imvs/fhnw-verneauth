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

/**
 * Data Transfer Object representing a topic in the {@code auth_on_subscribe} payload for VerneMQ webhooks.
 *
 * @author mluppi
 * @author paradoxxl
 * @see <a href="https://docs.vernemq.com/plugindevelopment/webhookplugins#auth_on_subscribe">auth_on_subscribe - VerneMQ Webhook specs</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicDTO {

    private String topic;
    private int qos;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }
}
