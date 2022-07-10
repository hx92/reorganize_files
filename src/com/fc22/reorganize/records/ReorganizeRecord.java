package com.fc22.reorganize.records;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReorganizeRecord {

  private static final String dir = URLDecoder.decode(
      new File(ReorganizeRecord.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent(), Charset.defaultCharset());

  public static void main(String[] args) {
    System.out.println(dir);
    final File file = new File(dir);
    final File[] files = file.listFiles();
    assert files != null;
    System.out.println(files.length);
    final String regex = "(.+)_(20\\d{12}.m4a)";
    final Pattern compile = Pattern.compile(regex);
    Map<String, List<String>> multimap = new HashMap<>();
    Arrays.stream(files).filter(File::isFile).map(File::getName).filter(s -> s.endsWith(".m4a")).filter(s -> s.contains("_"))
        .filter(s -> s.matches(regex)).forEach(s -> {
          final Matcher matcher = compile.matcher(s);
          if (matcher.find()) {
            multimap.computeIfAbsent(matcher.group(1), k -> new ArrayList<>()).add(s);
          }
        });
    System.out.println(multimap);
    multimap.forEach((s, strings) -> {
      new File(dir + File.separator + s).mkdirs();
      strings.forEach(s1 -> new File(dir + File.separator + s1).renameTo(new File(dir + File.separator + s + File.separator + s1)));
    });
  }
}
