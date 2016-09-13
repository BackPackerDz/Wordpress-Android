package com.squalala.dz6android.utils;

import java.util.List;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : UrlToSplit.java
 * Date : 31 juil. 2014
 * 
 */
public class UrlToSplit {
	
	public static String strWithComma(List<?> url_images) {
		String urlToSplit = "";
		
		for (int i = 0; i < url_images.size(); i++) {
			urlToSplit += url_images.get(i).toString() + ",";
		}
		
		if (urlToSplit.endsWith(",")) {
			urlToSplit = removeLastChar(urlToSplit);
		}
		
		return urlToSplit;
	}

    public static String strWithComma(String [] url_images) {
        String urlToSplit = "";

        for (int i = 0; i < url_images.length; i++) {
            urlToSplit += url_images[i] + ",";
        }

        if (urlToSplit.endsWith(",")) {
            urlToSplit = removeLastChar(urlToSplit);
        }

        return urlToSplit;
    }
	
	 public static String removeLastChar(String str) {
	        return str.substring(0,str.length()-1);
	 }

}
