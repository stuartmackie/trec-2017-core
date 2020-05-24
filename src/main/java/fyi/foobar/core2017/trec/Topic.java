package fyi.foobar.core2017.trec;

/**
 * TREC topic.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class Topic {

    public String num;
    public String title;
    public String desc;
    public String narr;

    public Topic() {
        this.num = "";
        this.title = "";
        this.desc = "";
        this.narr = "";
    }

    public Topic(String num, String title, String desc, String narr) {
        this.num = num;
        this.title = title;
        this.desc = desc;
        this.narr = narr;
    }

    public String toString() {
        return "[" + num + "]" + " " + title;
    }

}
