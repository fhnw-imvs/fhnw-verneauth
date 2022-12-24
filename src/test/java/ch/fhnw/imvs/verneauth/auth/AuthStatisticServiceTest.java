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
package ch.fhnw.imvs.verneauth.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link AuthStatisticService}.
 *
 * @author mluppi
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class AuthStatisticServiceTest {

    @Autowired
    private AuthStatisticService authStatisticService;

    @Before
    public void clearStatistic() {
        authStatisticService.clearStatistic();
    }

    @Test
    public void testGrantedLoginTimeByUsername() {
        authStatisticService.logLoginTime("A", true);
        authStatisticService.logLoginTime("B", true);
        final Map<String, Long> loginTimes = authStatisticService.getGrantedLoginTimeByUsername();
        assertNotNull(loginTimes.get("A"));
        assertNotNull(loginTimes.get("B"));
    }

    @Test
    public void testDeniedLoginTimeByUsername() {
        authStatisticService.logLoginTime("A", false);
        authStatisticService.logLoginTime("B", false);
        final Map<String, Long> loginTimes = authStatisticService.getDeniedLoginTimeByUsername();
        assertNotNull(loginTimes.get("A"));
        assertNotNull(loginTimes.get("B"));
    }

    @Test
    public void testGrantedUsernameBySubscribeTopic() {
        authStatisticService.logSubscribeTopic("A", "a/t1", true);
        authStatisticService.logSubscribeTopic("A", "a/t2", true);
        authStatisticService.logSubscribeTopic("B", "b/t3", true);
        authStatisticService.logSubscribeTopic("X", "a/t1", true);
        final Map<String, Set<String>> usernameByTopic = authStatisticService.getGrantedUsernameBySubscribeTopic();

        final Set<String> at1 = usernameByTopic.get("a/t1");
        assertNotNull(at1);
        assertEquals(2, at1.size());
        assertTrue(at1.contains("A"));
        assertTrue(at1.contains("X"));

        final Set<String> at2 = usernameByTopic.get("a/t2");
        assertNotNull(at2);
        assertEquals(1, at2.size());
        assertTrue(at2.contains("A"));

        final Set<String> bt3 = usernameByTopic.get("b/t3");
        assertNotNull(bt3);
        assertEquals(1, bt3.size());
        assertTrue(bt3.contains("B"));
    }

    @Test
    public void testGrantedUsernameByPublishTopic() {
        authStatisticService.logPublishTopic("A", "a/t1", true);
        authStatisticService.logPublishTopic("A", "a/t2", true);
        authStatisticService.logPublishTopic("B", "b/t3", true);
        authStatisticService.logPublishTopic("X", "a/t1", true);
        final Map<String, Set<String>> usernameByTopic = authStatisticService.getGrantedUsernameByPublishTopic();

        final Set<String> at1 = usernameByTopic.get("a/t1");
        assertNotNull(at1);
        assertEquals(2, at1.size());
        assertTrue(at1.contains("A"));
        assertTrue(at1.contains("X"));

        final Set<String> at2 = usernameByTopic.get("a/t2");
        assertNotNull(at2);
        assertEquals(1, at2.size());
        assertTrue(at2.contains("A"));

        final Set<String> bt3 = usernameByTopic.get("b/t3");
        assertNotNull(bt3);
        assertEquals(1, bt3.size());
        assertTrue(bt3.contains("B"));
    }


    @Test
    public void testDeniedUsernameBySubscribeTopic() {
        authStatisticService.logSubscribeTopic("A", "a/t1", false);
        authStatisticService.logSubscribeTopic("A", "a/t2", false);
        authStatisticService.logSubscribeTopic("B", "b/t3", false);
        authStatisticService.logSubscribeTopic("X", "a/t1", false);
        final Map<String, Set<String>> usernameByTopic = authStatisticService.getDeniedUsernameBySubscribeTopic();

        final Set<String> at1 = usernameByTopic.get("a/t1");
        assertNotNull(at1);
        assertEquals(2, at1.size());
        assertTrue(at1.contains("A"));
        assertTrue(at1.contains("X"));

        final Set<String> at2 = usernameByTopic.get("a/t2");
        assertNotNull(at2);
        assertEquals(1, at2.size());
        assertTrue(at2.contains("A"));

        final Set<String> bt3 = usernameByTopic.get("b/t3");
        assertNotNull(bt3);
        assertEquals(1, bt3.size());
        assertTrue(bt3.contains("B"));
    }

    @Test
    public void testDeniedUsernameByPublishTopic() {
        authStatisticService.logPublishTopic("A", "a/t1", false);
        authStatisticService.logPublishTopic("A", "a/t2", false);
        authStatisticService.logPublishTopic("B", "b/t3", false);
        authStatisticService.logPublishTopic("X", "a/t1", false);
        final Map<String, Set<String>> usernameByTopic = authStatisticService.getDeniedUsernameByPublishTopic();

        final Set<String> at1 = usernameByTopic.get("a/t1");
        assertNotNull(at1);
        assertEquals(2, at1.size());
        assertTrue(at1.contains("A"));
        assertTrue(at1.contains("X"));

        final Set<String> at2 = usernameByTopic.get("a/t2");
        assertNotNull(at2);
        assertEquals(1, at2.size());
        assertTrue(at2.contains("A"));

        final Set<String> bt3 = usernameByTopic.get("b/t3");
        assertNotNull(bt3);
        assertEquals(1, bt3.size());
        assertTrue(bt3.contains("B"));
    }

    @Test
    public void testClearStatistic() {
        authStatisticService.logLoginTime("A", true);
        authStatisticService.clearStatistic();
        final Map<String, Long> loginTimes = authStatisticService.getGrantedLoginTimeByUsername();
        assertNull(loginTimes.get("A"));
    }
}
