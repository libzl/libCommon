// Copy right Baidu Inc.
package com.easyhome.common.utils;

import android.annotation.SuppressLint;
import android.net.Uri;

/**
 * Uri工具类
 * 
 * @author kevin
 * @since 2013-May 10, 2013
 * @version v1.0
 */
public class URIUtil {

	@SuppressLint("DefaultLocale")
	public static boolean isValidHttpUri(Uri uri) {
		String uriString = uri.toString();
		return isValidHttpUri(uriString);
	}

	public static boolean isValidHttpUri(String uriString) {
		if (TextUtil.isEmpty(uriString)) {
			return false;
		}
		String loweCaseString = uriString.toLowerCase();
		if (loweCaseString.startsWith("http") || loweCaseString.startsWith("https")) {
			return true;
		}

		return false;
	}

	public static boolean isContentUri(String uriString) {
		if (TextUtil.isEmpty(uriString)) {
			return false;
		}
		String loweCaseString = uriString.toLowerCase();
		if (loweCaseString.startsWith("content")) {
			return true;
		}
		return false;
	}

	public static boolean isContentUri(Uri uri) {
		String uriString = uri.toString();
		return isContentUri(uriString);
	}

	public static boolean isFileUri(String uriString) {
		if (TextUtil.isEmpty(uriString)) {
			return false;
		}
		String loweCaseString = uriString.toLowerCase();
		if (loweCaseString.startsWith("file")) {
			return true;
		}
		return false;
	}

	public static boolean isFileUri(Uri uri) {
		String uriString = uri.toString();
		return isFileUri(uriString);
	}

    public static String getPath(String path) {
        if (isContentUri(path) || isFileUri(path)) {
            Uri uri = Uri.parse(path);
            return uri.getPath();
        }
        return path;
    }
}
