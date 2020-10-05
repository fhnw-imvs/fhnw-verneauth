-- Copyright 2020 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- Authors: mluppi

CREATE TABLE mqtt_user (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(65535),
    username VARCHAR(65535)
        CONSTRAINT uk_mqtt_user_username UNIQUE
);

CREATE TABLE mqtt_user_acl_publish (
    mqtt_user_id BIGINT NOT NULL,
    acl_publish  VARCHAR(65535),
    CONSTRAINT fk_mqtt_user_acl_publish_mqtt_user_id FOREIGN KEY (mqtt_user_id) REFERENCES mqtt_user (id)
);

CREATE TABLE mqtt_user_acl_subscribe (
    mqtt_user_id  BIGINT NOT NULL,
    acl_subscribe VARCHAR(65535),
    CONSTRAINT fk_mqtt_user_acl_subscribe_mqtt_user_id FOREIGN KEY (mqtt_user_id) REFERENCES mqtt_user (id)
);
