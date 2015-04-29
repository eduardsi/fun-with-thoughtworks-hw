package com.thoughtworks.contraman;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class TalksReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Path testFileWithoutLightningTalks;
    Path testFileWithLightningTalks;
    Path testFileWithWrongFormat;

    TalksFileReader talksFileReader = new TalksFileReader();

    @Before
    public void readFile() throws URISyntaxException {
        testFileWithoutLightningTalks = get(getClass().getResource("/TalksReaderTestWithoutLightningTalk.data").toURI());
        testFileWithLightningTalks = get(getClass().getResource("/TalksReaderTestWithLightningTalk.data").toURI());
        testFileWithWrongFormat = get(getClass().getResource("/TalksReaderTestWithWrongFormat.data").toURI());
    }

    @Test
    public void readsTalksFromFile() throws IOException {
        Talks talks = talksFileReader.read(testFileWithoutLightningTalks);

        assertThat(talks.longestFirst(), contains(
                talk("Writing Fast Tests Against Enterprise Rails", 60),
                talk("Overdoing it in Python", 45),
                talk("Lua for the Masses", 30)
        ));

    }

    @Test
    public void treatsLightningTalkAsAFiveMinuteTalk() throws IOException {
        Talks talks = talksFileReader.read(testFileWithLightningTalks);

        assertThat(talks.longestFirst(), contains(talk("Rails for Python Developers", 5)));
    }

    @Test
    public void throwsExceptionIfLineDoesNotConformToTalkRules() throws IOException {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Line 'Rails for Python Developers ASDF' does not conform to talk rules");
        talksFileReader.read(testFileWithWrongFormat);
    }

    private static TalkMatcher talk(String title, long durationInMinutes) {
        return new TalkMatcher(title, durationInMinutes);
    }

    private static class TalkMatcher extends TypeSafeMatcher<Talk> {

        private final String title;
        private final long durationInMinutes;

        private TalkMatcher(String title, long durationInMinutes) {
            this.title = title;
            this.durationInMinutes = durationInMinutes;
        }

        @Override
        protected boolean matchesSafely(Talk item) {
            return item.title().equals(title) && item.duration().equals(new TimeCapacity(durationInMinutes));
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(title + "/" + durationInMinutes);

        }

        @Override
        protected void describeMismatchSafely(Talk item, Description mismatchDescription) {
            mismatchDescription.appendText(item.title() + "/" + item.duration());
        }
    }

}
