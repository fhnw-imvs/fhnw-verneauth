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
package ch.fhnw.imvs.verneauth.api;

import ch.fhnw.imvs.verneauth.api.dto.UserDTO;
import ch.fhnw.imvs.verneauth.persistence.MqttUser;
import ch.fhnw.imvs.verneauth.persistence.MqttUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mluppi
 */
@Service
@Transactional
public class ApiUserService {

    private final MqttUserRepository mqttUserRepository;

    @Autowired
    public ApiUserService(final MqttUserRepository mqttUserRepository) {
        this.mqttUserRepository = mqttUserRepository;
    }

    public List<UserDTO> getAllUsers() {
        return mqttUserRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(final String username) {
        return mapToDto(mqttUserRepository.findByUsername(username));
    }

    public UserDTO createUser(final UserDTO dto) {
        if (mqttUserRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException();
        }
        MqttUser user = new MqttUser(dto.getUsername(), dto.getPassword(), dto.getAclSubscribe(), dto.getAclPublish());
        user = mqttUserRepository.save(user);
        return mapToDto(user);
    }

    public UserDTO updateUser(final String username, final UserDTO dto) {
        final MqttUser user = mqttUserRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (!username.equals(dto.getUsername()) && mqttUserRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException();
        }
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getAclPublish() != null) {
            user.setAclPublish(dto.getAclPublish());
        }
        if (dto.getAclSubscribe() != null) {
            user.setAclSubscribe(dto.getAclSubscribe());
        }
        return mapToDto(mqttUserRepository.save(user));
    }

    public void removeUser(String username) {
        mqttUserRepository.deleteByUsername(username);
    }

    private UserDTO mapToDto(final MqttUser user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getUsername(), user.getPassword(), user.getAclSubscribe(), user.getAclPublish());
    }
}
