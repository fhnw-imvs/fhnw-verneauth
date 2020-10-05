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

import ch.fhnw.imvs.verneauth.api.dto.AuthStatisticDTO;
import ch.fhnw.imvs.verneauth.api.dto.UserDTO;
import ch.fhnw.imvs.verneauth.auth.AuthStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author mluppi
 * @author paradoxxl
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private static final String USER_ENDPOINT = "/user";
    private static final String STATS_ENDPOINT = "/stats";

    private final ApiUserService apiUserService;
    private final AuthStatisticService authStatisticService;

    @Autowired
    public ApiController(final ApiUserService apiUserService, final AuthStatisticService authStatisticService) {
        this.apiUserService = apiUserService;
        this.authStatisticService = authStatisticService;
    }

    @GetMapping(USER_ENDPOINT)
    public List<UserDTO> getUsers() {
        return apiUserService.getAllUsers();
    }

    @GetMapping(USER_ENDPOINT + "/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") final String username) {
        return ResponseEntity.of(Optional.ofNullable(apiUserService.getUser(username)));
    }

    @PostMapping(USER_ENDPOINT)
    public ResponseEntity<UserDTO> createUser(@RequestBody final UserDTO dto) {
        try {
            return ResponseEntity.ok().body(apiUserService.createUser(dto));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping(USER_ENDPOINT + "/{username}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("username") final String username, @RequestBody final UserDTO dto) {
        try {
            return ResponseEntity.of(Optional.ofNullable(apiUserService.updateUser(username, dto)));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping(USER_ENDPOINT + "/{username}")
    public void removeUser(@PathVariable("username") final String username) {
        apiUserService.removeUser(username);
    }

    @GetMapping(STATS_ENDPOINT + "/auth")
    public AuthStatisticDTO getAuthStatistic() {
        final AuthStatisticDTO authStatistic = new AuthStatisticDTO();
        authStatistic.setGrantedUserLogins(authStatisticService.getGrantedLoginTimeByUsername());
        authStatistic.setGrantedSubscribeTopics(authStatisticService.getGrantedUsernameBySubscribeTopic());
        authStatistic.setGrantedPublishTopics(authStatisticService.getGrantedUsernameByPublishTopic());
        authStatistic.setDeniedUserLogins(authStatisticService.getDeniedLoginTimeByUsername());
        authStatistic.setDeniedSubscribeTopics(authStatisticService.getDeniedUsernameBySubscribeTopic());
        authStatistic.setDeniedPublishTopics(authStatisticService.getDeniedUsernameByPublishTopic());
        return authStatistic;
    }

    @DeleteMapping(STATS_ENDPOINT + "/auth")
    public void clearAuthStatistic() {
       authStatisticService.clearStatistic();
    }
}
