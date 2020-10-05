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
package ch.fhnw.imvs.verneauth.webhooks;

import ch.fhnw.imvs.verneauth.auth.AuthService;
import ch.fhnw.imvs.verneauth.webhooks.dto.AuthOnPublishDTO;
import ch.fhnw.imvs.verneauth.webhooks.dto.AuthOnRegisterDTO;
import ch.fhnw.imvs.verneauth.webhooks.dto.AuthOnSubscribeDTO;
import ch.fhnw.imvs.verneauth.webhooks.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author mluppi
 * @author paradoxxl
 */
@RestController
@RequestMapping("/webhooks")
public class WebhooksController {

    private static final String RESPONSE_OK = "{\"result\":\"ok\"}";
    private static final String RESPONSE_NOT_AUTHORIZED = "{\"result\":{\"error\":\"not_authorized\"}}";

    private static final CacheControl CACHE_CONTROL = CacheControl.maxAge(10, TimeUnit.SECONDS);

    private final AuthService authService;

    @Autowired
    public WebhooksController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> authOnRegister(@RequestBody final AuthOnRegisterDTO dto) {
        return getResponse(authService.isLoginAuthorized(dto.getUsername(), dto.getPassword()));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> authOnSubscribe(@RequestBody final AuthOnSubscribeDTO dto) {
        final List<String> topics = dto.getTopics().stream().map(TopicDTO::getTopic).collect(Collectors.toList());
        return getResponse(authService.isTopicSubscribeAuthorized(dto.getUsername(), topics));
    }

    @PostMapping("/publish")
    public ResponseEntity<String> authOnPublish(@RequestBody final AuthOnPublishDTO dto) {
        return getResponse(authService.isTopicPublishAuthorized(dto.getUsername(), dto.getTopic()));
    }

    private ResponseEntity<String> getResponse(final boolean isAuthorized) {
        return ResponseEntity.ok()
                .cacheControl(CACHE_CONTROL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(isAuthorized ? RESPONSE_OK : RESPONSE_NOT_AUTHORIZED);
    }
}
