package com.thoughtworks.contraman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TalksFileReader {

    private static final Pattern PATTERN = Pattern.compile("(.*?)\\s(\\d+)?(min|lightning)$");

    public Talks read(Path path) throws IOException {
        Talk[] talks = Files.lines(path).map(this::talkFromLine).toArray(Talk[]::new);
        return new Talks(talks);
    }

    private Talk talkFromLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        boolean matches = matcher.matches();
        if (!matches) {
            throw new IllegalStateException("Line '" + line + "' does not conform to talk rules'");
        }

        String title = matcher.group(1);
        String duration = matcher.group(2);

        if (duration == null) {
            duration = "5";
        }

        return new Talk(title, Long.valueOf(duration));
    }
}
