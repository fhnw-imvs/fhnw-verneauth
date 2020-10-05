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
package ch.fhnw.imvs.verneauth.auth;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link MqttTopicMatcher}.
 *
 * @author mluppi
 * @see <a href="https://docs.oasis-open.org/mqtt/mqtt/v5.0/mqtt-v5.0.html">MQTT Version 5.0 - OASIS Standard</a>
 */
@SuppressWarnings("squid:S1192")
public class MqttTopicMatcherTest {

    @Test
    public void testCompareWithoutWildcard() {
        assertTrue(MqttTopicMatcher.matchFilter("a", "a"));
        assertTrue(MqttTopicMatcher.matchFilter("aaa", "aaa"));
        assertTrue(MqttTopicMatcher.matchFilter("a/b/c", "a/b/c"));
        assertTrue(MqttTopicMatcher.matchFilter("aa/bbb/cccc", "aa/bbb/cccc"));
        assertTrue(MqttTopicMatcher.matchFilter("a/100-1", "a/100-1"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b/c", "a/b/x"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b/c", "a/b"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b", "a/b/c"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b", "a/b/"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b/", "a/b"));
    }

    @Test
    public void testCompareSingleLevelWildcard() {
        assertTrue(MqttTopicMatcher.matchFilter("a/+", "a/b"));
        assertTrue(MqttTopicMatcher.matchFilter("+/b/c", "a/b/c"));
        assertTrue(MqttTopicMatcher.matchFilter("+/b/+/d", "a/b/c/d"));
        assertTrue(MqttTopicMatcher.matchFilter("+", "a"));
        assertTrue(MqttTopicMatcher.matchFilter("+", "+"));
        assertTrue(MqttTopicMatcher.matchFilter("+/+", "a/b"));
        assertTrue(MqttTopicMatcher.matchFilter("a/+", "a/100-1"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b/c", "+/b/c")); // no match on first level
        assertFalse(MqttTopicMatcher.matchFilter("a/+/", "a/bbb")); // not same level
        assertFalse(MqttTopicMatcher.matchFilter("a/+/c", "a/b/+")); // no match on second level
        assertFalse(MqttTopicMatcher.matchFilter("a/b/c/+", "a/+/c/z")); // no match on second level
        assertFalse(MqttTopicMatcher.matchFilter("+", "/x")); // no match on second level
        assertFalse(MqttTopicMatcher.matchFilter("a/+/c", "a/b/x")); // no match on second level
        assertFalse(MqttTopicMatcher.matchFilter("a/+", "a")); // + does not match parent
        assertFalse(MqttTopicMatcher.matchFilter("a/b+/c", "a/b/c")); // + must occupy an entire level
        assertFalse(MqttTopicMatcher.matchFilter("+/abc", "$xyz/abc")); // topics beginning with $ should not match wildcard
    }

    @Test
    public void testCompareMultiLevelWildcard() {
        assertTrue(MqttTopicMatcher.matchFilter("#", "a/b/c"));
        assertTrue(MqttTopicMatcher.matchFilter("#", "#"));
        assertTrue(MqttTopicMatcher.matchFilter("#", "a/#"));
        assertTrue(MqttTopicMatcher.matchFilter("#", "a/100-1"));
        assertTrue(MqttTopicMatcher.matchFilter("a/#", "a")); // # matches parent
        assertFalse(MqttTopicMatcher.matchFilter("a/b/c/d/e", "a/b/#")); // # no match on third level
        assertFalse(MqttTopicMatcher.matchFilter("a/#/c", "a/xy")); // # must be the last character
        assertFalse(MqttTopicMatcher.matchFilter("a/b#", "a/b")); // # must occupy an entire level
        assertFalse(MqttTopicMatcher.matchFilter("#", "$SYS/abc")); // topics beginning with $ should not match wildcard
        assertFalse(MqttTopicMatcher.matchFilter("#", "$xyz")); // topics beginning with $ should not match wildcard
    }

    @Test
    public void testCompareCombinedWildcards() {
        assertTrue(MqttTopicMatcher.matchFilter("a/b/#", "a/b/+"));
        assertTrue(MqttTopicMatcher.matchFilter("a/b/#", "a/b/c/+"));
        assertTrue(MqttTopicMatcher.matchFilter("+/b/#", "a/b/c/d")); // no match on third level
        assertFalse(MqttTopicMatcher.matchFilter("+/b/#", "a/x")); // no match on second level
    }

    @Test
    public void testCompareTopicsBeginningWithDollar() {
        assertTrue(MqttTopicMatcher.matchFilter("$SYS/#", "$SYS/"));
        assertTrue(MqttTopicMatcher.matchFilter("$SYS/#", "$SYS/a"));
        assertTrue(MqttTopicMatcher.matchFilter("$SYS/#", "$SYS/a/+"));
        assertTrue(MqttTopicMatcher.matchFilter("$SYS/#", "$SYS/+/b"));
        assertTrue(MqttTopicMatcher.matchFilter("$SYS/#", "$SYS/#"));
        assertTrue(MqttTopicMatcher.matchFilter("$SYS/a/+", "$SYS/a/b"));
        assertFalse(MqttTopicMatcher.matchFilter("#", "$test"));
        assertFalse(MqttTopicMatcher.matchFilter("#", "$SYS/a/+"));
        assertFalse(MqttTopicMatcher.matchFilter("$test", "#"));
        assertFalse(MqttTopicMatcher.matchFilter("$SYS/a/+", "#"));
        assertFalse(MqttTopicMatcher.matchFilter("+/a/b", "$SYS/a/b"));
        assertFalse(MqttTopicMatcher.matchFilter("$SYS/a/b", "+/a/b"));
    }

    @Test
    public void testCompareCaseSensitivity() {
        assertTrue(MqttTopicMatcher.matchFilter("A/b/CCC", "A/b/CCC"));
        assertFalse(MqttTopicMatcher.matchFilter("A/b/CCC", "A/b/ccc"));
    }

    @Test
    public void testCompareEmptyLevels() {
        assertTrue(MqttTopicMatcher.matchFilter("a//c", "a//c"));
        assertTrue(MqttTopicMatcher.matchFilter("/b/c", "/b/c"));
        assertTrue(MqttTopicMatcher.matchFilter("+/b/c", "/b/c"));
        assertTrue(MqttTopicMatcher.matchFilter("#", "/b/c"));
        assertTrue(MqttTopicMatcher.matchFilter("a/b/#", "a/b/c//d"));
        assertFalse(MqttTopicMatcher.matchFilter("a/b/c", "a//c")); // no match on second level
        assertFalse(MqttTopicMatcher.matchFilter("a/b/", "a/b//")); // no match on third level
        assertFalse(MqttTopicMatcher.matchFilter("a//c", "a//c/d")); // no match on fourth level
        assertFalse(MqttTopicMatcher.matchFilter("a/+/c", "a//c/d")); // no match on fourth level
    }

    @Test
    public void testCompareUTF8() {
        assertTrue(MqttTopicMatcher.matchFilter("a/!*ç/öäé", "a/!*ç/öäé"));
        assertTrue(MqttTopicMatcher.matchFilter("a/!*ç/+", "a/!*ç/öäé"));
        assertTrue(MqttTopicMatcher.matchFilter("a/+/öäé", "a/!*ç/öäé"));
        assertTrue(MqttTopicMatcher.matchFilter("#", "a/!*ç/öäé"));
        assertTrue(MqttTopicMatcher.matchFilter("a/  /c/d", "a/  /c/d"));
        assertTrue(MqttTopicMatcher.matchFilter("a/+/c/d", "a/  /c/d"));
        assertTrue(MqttTopicMatcher.matchFilter("a/  /#", "a/  /c/d"));
        assertTrue(MqttTopicMatcher.matchFilter("\u04dc\u00f1\u00fc", "\u04dc\u00f1\u00fc"));
    }

}
