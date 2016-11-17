package com.boarbeard.bggauctions.util;

import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

/**
 * Loading images? This is your huckleberry.
 */
public class ImageUtils {
	private static final String IMAGE_URL_PREFIX = "https://cf.geekdo-images.com/images/pic";
	//public static final String SUFFIX_SMALL_THUMBNAIL = "_mt";
	public static final String SUFFIX_THUMBNAIL = "_t";
	//private static final String SUFFIX_SQUARE = "_sq";
	public static final String SUFFIX_SMALL = "_t";
	public static final String SUFFIX_MEDIUM = "_md";
	//private static final String SUFFIX_LARGE = "_lg";
	private static final float IMAGE_ASPECT_RATIO = 1.6777777f;

	private ImageUtils() {
	}

	/**
	 * Create a URL for a thumbnail image as a JPG.
	 */
	public static String createThumbnailJpgUrl(int imageId) {
		return IMAGE_URL_PREFIX + imageId + SUFFIX_SMALL + ".jpg";
	}

	/**
	 * Create a URL for a thumbnail image as a PNG.
	 */
	public static String createThumbnailPngUrl(int imageId) {
		return IMAGE_URL_PREFIX + imageId + SUFFIX_SMALL + ".png";
	}



	/**
	 * Append a suffix to an image URL. Assumes the URL has no suffix (but may have an extension or not.
	 */
	public static String appendImageUrl(String imageUrl, String suffix) {
		if (TextUtils.isEmpty(imageUrl)) {
			return "";
		}
		if (TextUtils.isEmpty(suffix)) {
			return imageUrl;
		}
		int dot = imageUrl.lastIndexOf('.');
		if (dot == -1) {
			return imageUrl + suffix;
		} else {
			return imageUrl.substring(0, dot) + suffix + imageUrl.substring(dot, imageUrl.length());
		}
	}

	/**
	 * Resize the resizableView based on a standard aspect ratio, up to a maximum height
	 */
	public static void resizeImagePerAspectRatio(View image, int maxHeight, View resizableView) {
		if (image == null) return;
		if (resizableView == null) return;

		int height = (int) (image.getWidth() / IMAGE_ASPECT_RATIO);
		height = Math.min(height, maxHeight);

		ViewGroup.LayoutParams lp;
		lp = resizableView.getLayoutParams();
		if (lp.height != height) {
			lp.height = height;
			resizableView.setLayoutParams(lp);
		}
	}

	/**
	 * Call back from loading an image.
	 */
	public interface Callback {
		void onSuccessfulImageLoad(Palette palette);

		void onFailedImageLoad();
	}


}