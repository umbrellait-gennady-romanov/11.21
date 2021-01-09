import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ParseUrl extends RecursiveAction {

    String url, shift;
    CopyOnWriteArrayList<String> list;

    public ParseUrl(String url, String shift, CopyOnWriteArrayList<String> list) {
        this.url = url;
        this.list = list;
        this.shift = shift;

    }

    @Override
    protected void compute() {
        list.add(shift + url);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            return;
        }
        List<String> listHref = doc.select("a").eachAttr("href");
        System.out.println("очередной цикл");
        System.out.println(url);
        listHref.forEach(s -> System.out.println("\t" + s));
        String startUrl;
        if (url.substring(url.indexOf(".")).contains("/")) {
            startUrl = url.substring(0, url.indexOf(".") + url.substring(url.indexOf(".")).indexOf("/"));
        } else startUrl = url;

        List<ParseUrl> parseUrls = listHref.stream()
                .filter(s -> !s.equals("/") && !s.startsWith("//") && !s.endsWith("#"))
                .map(s -> s.startsWith("/") ? startUrl + s : s)
                .filter(s -> s.startsWith(url) && !s.equals(url) && !s.equals(url+ "/")  )
                .distinct()
                .sorted()
                .map(s -> new ParseUrl(s, shift + "\t", list))
                .collect(Collectors.toList());
        ForkJoinTask.invokeAll(parseUrls);
    }

}
