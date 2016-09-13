package com.squalala.dz6android.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Back Packer
 * Date : 07/10/15
 */
public class HtmlUtils {

    private HtmlUtils() {}

    public static String addAttributesToImages(String html) {

        Document doc = Jsoup.parse(html);

        // Get all img tags
        Elements img = doc.getElementsByTag("img");

        int id = 0;

        for (Element el : img) {

            if (el.toString().contains("www.dz-android.com") && !el.toString().contains("resize=120") &&
                    !el.toString().contains("Telecharger-sur-Google-play-Small1.png")) {
                el.attr("id", String.valueOf(id++));
                el.attr("onclick", "Image.onClickImage(this.id);");
            }
        }

        // Pour supprimer la séction articles similaires
        doc.select("div.yarpp-related").remove();
        // Supprimer la partie du partage
        doc.select("div.sd-sharing-enabled").remove();
        // Supprimer le gif
        doc.select("p.pvc_stats").remove();

        doc = Jsoup.parse(doc.html());



        Element head = doc.head();

        head.append("<style>img{max-width: 100%; width:auto; height: auto;} iframe {max-width: 100%; width:auto; height: auto;}</style>");
        head.append("<script>function onClickImage(value) { Image.onClickImage(value); }</script>");

        return doc.html();
    }

    public static String addHtml(String content, String htmlToAdd )
    {
        Document document = Jsoup.parse(content);

        Element body = document.body();

        body.prepend(htmlToAdd);

       return document.html();
    }


    public static ArrayList<String> getUrlImages(String html) {

        ArrayList<String> urlImages = new ArrayList<>();

        Document doc = Jsoup.parse(html);

        // Get all img tags
        Elements img = doc.getElementsByTag("img");

        for (Element el : img) {

            if (el.toString().contains("www.dz-android.com") && !el.toString().contains("resize=120") &&
                    !el.toString().contains("Telecharger-sur-Google-play-Small1.png"))
                urlImages.add(el.attr("src"));
        }

       /* for(String image : urlImages)
            System.out.println("#" + image);*/

        return urlImages;
    }



}
