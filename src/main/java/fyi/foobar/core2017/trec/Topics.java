package fyi.foobar.core2017.trec;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TREC topics.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class Topics implements Iterable<Topic> {

    private static final Logger logger = LogManager.getLogger(Topics.class);

    private List<Topic> topics;

    public Topics(Configuration config) {

        try {

            topics = new ArrayList<Topic>();

            org.jsoup.nodes.Document jsoup = Jsoup.parse(FileUtils.readFileToString(
                    new File(config.getString("core2017.topics")), StandardCharsets.UTF_8));

            Elements elements = jsoup.body().select("*");

            Topic topic = null;

            for (Element element : elements) {

                String tag = element.tagName();
                String txt = element.ownText();

                switch (tag) {
                    case "top":
                        topic = new Topic();
                        break;
                    case "num": {
                        txt = txt.replace("Number: ", "");
                        topic.num = txt;
                        break;
                    }
                    case "title": {
                        topic.title = txt;
                        break;
                    }
                    case "desc": {
                        topic.desc = txt;
                        break;
                    }
                    case "narr": {
                        topic.narr = txt;
                        topics.add(topic);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
    }

    @Override
    public Iterator<Topic> iterator() {
        return topics.iterator();
    }

}
