package com.usc.meg.stockexchangeviewer;

/**
 * Created by sbmeg on 01/05/16.
 */
public class NewsClass {
    private String title;
    private String content;
    private String publisher;
    private String publishedDate;
    private String unescapedUrl;


    public NewsClass(String title, String content, String publisher, String publishedDate, String unescapedUrl) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.unescapedUrl = unescapedUrl;
    }

    public NewsClass() {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.unescapedUrl = unescapedUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getUnescapedUrl() {
        return unescapedUrl;
    }

    public void setUnescapedUrl(String unescapedUrl) {
        this.unescapedUrl = unescapedUrl;
    }
}
