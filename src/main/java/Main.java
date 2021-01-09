import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException{

        String url = "https://skillbox.ru";
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        new ForkJoinPool().invoke(new ParseUrl(url, "", list));

        Collections.sort(list);
        Collections.reverse(list);

        list.forEach(s -> printUrl(list, url));

    }
    public static void printUrl(CopyOnWriteArrayList<String> list, String url) {
        System.out.println(url);
        list.forEach(s -> {
            if (s.startsWith("\t" + url)) {
                printUrl(list, s);
            }

        });
    }


}
