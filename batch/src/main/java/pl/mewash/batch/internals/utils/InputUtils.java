package pl.mewash.batch.internals.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InputUtils {

    private static final String SPLIT_REGEX = "[,;\\s\\n]+";

    private static final String YT_PLAYLIST_PREFIX = "&list=";


    public static List<String> toUrlList(String input, boolean ignorePlaylist) {
        return Arrays.stream(input.split(SPLIT_REGEX))
            .map(String::trim)
            .filter(token -> token.startsWith("http"))
            .map(url -> ignorePlaylist ? extractSingleIfPlaylist(url) : url)
            .toList();
    }

    public static int getDetectedDuplicatesCount(List<String> urls) {
        return urls.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values().stream()
            .filter(count -> count > 1)
            .mapToInt(count -> count.intValue() - 1)
            .sum();
    }

    public static List<String> removeDuplicates(List<String> urls) {
        return urls.stream()
            .distinct()
            .toList();
    }

    private static String extractSingleIfPlaylist(String url) {
        return url.contains(YT_PLAYLIST_PREFIX)
            ? url.substring(0, url.indexOf(YT_PLAYLIST_PREFIX))
            : url;
    }
}
