package com.cyr1en.m1;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Roll {

  private final static LinkedHashMap<Integer, String> lines = new LinkedHashMap<>();
  final static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  static {
    lines.put(11, "Ooh Ooh Ooh Ooh");
    lines.put(19, "We're no strangers to love");
    lines.put(23, "You know the rules and so do I");
    lines.put(27, "A full commitment's what I'm thinking of");
    lines.put(31, "You wouldn't get this from any other guy");
    lines.put(36, "I just wanna tell you how I'm feeling");
    lines.put(41, "Gotta make you understand");
    lines.put(44, "Never gonna give you up");
    lines.put(46, "Never gonna let you down");
    lines.put(48, "Never gonna run around and desert you");
    lines.put(52, "Never gonna make you cry");
    lines.put(55, "Never gonna say goodbye");
    lines.put(57, "Never gonna tell a lie and hurt you");
    lines.put(62, "We've known each other for so long");
    lines.put(66, "Your heart's been aching, but");
    lines.put(68, "You're too shy to say it");
    lines.put(70, "Inside, we both know what's been going on");
    lines.put(75, "We know the game and we're gonna play it");
    lines.put(78, "And if you ask me how I'm feeling");
    lines.put(83, "Don't tell me you're too blind to see");
    lines.put(86, "Never gonna give you up");
    lines.put(89, "Never gonna let you down");
    lines.put(91, "Never gonna run around and desert you");
    lines.put(95, "Never gonna make you cry");
    lines.put(97, "Never gonna say goodbye");
    lines.put(100, "Never gonna tell a lie and hurt you");
  }

  public static void rollOut(int delay, boolean executable) {
    Instant start = Instant.now();
    scheduler.scheduleAtFixedRate(() -> {
      Integer elapsed = Math.toIntExact(Duration.between(start, Instant.now()).getSeconds());
      if (elapsed >= 213) scheduler.shutdownNow();
      if (lines.size() == 0 && executable) System.exit(0);
      if (lines.containsKey(elapsed)) {
        System.out.println("[FourOne] " + lines.get(elapsed));
        lines.remove(elapsed);
      }
    }, delay, 1, TimeUnit.SECONDS);
  }
}
