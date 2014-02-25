package com.easyhome.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

/**
 * @author yuankai
 * @version 1.0
 * @date 2011-6-7
 */
public class BitmapUtil
{
	private static final String TAG = "BitmapUtil";
	private static int sIconWidth = -1;
	private static int sIconHeight = -1;

	private static final Paint sPaint = new Paint();
	private static final Rect sBounds = new Rect();
	private static final Rect sOldBounds = new Rect();
	private static Canvas sCanvas = new Canvas();

	public static byte[] bitmap2Bytes(final Bitmap bm, int quality)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

	public static Bitmap resizeBitmap(Bitmap bm, int maxWidth)
	{
		Bitmap returnBm;
		int w = bm.getWidth();
		int h = bm.getHeight();
		float scaleWidth;
		float scaleHeight;
		if (w > h) {
			scaleWidth = ((float) maxWidth) / w;
			scaleHeight = scaleWidth;
		} else {
			scaleHeight = ((float) maxWidth) / h;
			scaleWidth = scaleHeight;
		}

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		returnBm = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
		return returnBm;
	}

	/**
	 * Returns a Bitmap representing the thumbnail of the specified Bitmap. The
	 * size of the thumbnail is defined by the dimension
	 * android.R.dimen.launcher_application_icon_size.
	 *
	 * This method is not thread-safe and should be invoked on the UI thread
	 * only.
	 *
	 * @param bitmap
	 *            The bitmap to get a thumbnail of.
	 * @param context
	 *            The application's context.
	 *
	 * @return A thumbnail for the specified bitmap or the bitmap itself if the
	 *         thumbnail could not be created.
	 */

	/**
	 * Returns a Bitmap representing the thumbnail of the specified Bitmap. The
	 * size of the thumbnail is defined by the dimension
	 * android.R.dimen.launcher_application_icon_size.
	 *
	 * This method is not thread-safe and should be invoked on the UI thread
	 * only.
	 *
	 * @param bitmap
	 *            The bitmap to get a thumbnail of.
	 * @return A thumbnail for the specified bitmap or the bitmap itself if the
	 *         thumbnail could not be created.
	 */
	public synchronized static Bitmap createBitmapThumbnail(Bitmap bitmap,
															final int iconWidth, final int iconHeight)
	{
		if(bitmap == null)
		{
			return bitmap;
		}

		int width = iconWidth;
		int height = iconHeight;

		int srcWidth = iconWidth;
		int srcHeight = iconHeight;

		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		if (width > 0 && height > 0) {
			if (width < bitmapWidth || height < bitmapHeight) {
				final float ratio = (float) bitmapWidth / bitmapHeight;

				if (bitmapWidth > bitmapHeight) {
					height = (int) (width / ratio);
				} else if (bitmapHeight > bitmapWidth) {
					width = (int) (height * ratio);
				}

				Bitmap.Config c = (width == srcWidth && height == srcHeight) ?
						bitmap.getConfig() : Bitmap.Config.ARGB_8888;
				if (null == c){
					c = Bitmap.Config.ARGB_8888;
				}

				try
				{
					final Bitmap thumb = Bitmap.createBitmap(srcWidth, srcHeight, c);
					final Canvas canvas = sCanvas;
					final Paint paint = sPaint;
					canvas.setBitmap(thumb);
					paint.setDither(false);
					paint.setFilterBitmap(true);
					sBounds.set((srcWidth - width) / 2, (srcHeight - height) / 2, width, height);
					sOldBounds.set(0, 0, bitmapWidth, bitmapHeight);
					canvas.drawBitmap(bitmap, sOldBounds, sBounds, paint);
					return thumb;
				}
				catch (OutOfMemoryError e)
				{
					return null;
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}

			} else if (bitmapWidth < width || bitmapHeight < height) {
				try
				{
					final Bitmap.Config c = Bitmap.Config.ARGB_8888;
					final Bitmap thumb = Bitmap.createBitmap(srcWidth, srcHeight, c);
					final Canvas canvas = sCanvas;
					final Paint paint = sPaint;
					canvas.setBitmap(thumb);
					paint.setDither(false);
					paint.setFilterBitmap(true);
					sBounds.set(0, 0, width, height);
					sOldBounds.set(0, 0, bitmapWidth, bitmapHeight);
					canvas.drawBitmap(bitmap, sOldBounds, sBounds, paint);
					return thumb;
				}
				catch (OutOfMemoryError e)
				{
					return null;
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}


	/**
	 *
	 * 根据手机的分辨率�?dip 的单�?转成�?px(像素)
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将bitmap写入文件
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static String writeToFile(Bitmap bitmap, String path)
	{
		if (bitmap == null)
		{
			return null;
		}

		try
		{
			File dir = new File(path);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			File f = new File(path);

			if (f.exists())
			{
				f.delete();
			}

			if (f.createNewFile())
			{
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return path;
	}

	/**
	 * 将bitmap写入文件
	 * @param bitmap
	 * @param path
	 * @param filename
	 * @return
	 */
	public static String writeToFile(Bitmap bitmap, String path, String filename)
	{
		if (bitmap == null)
		{
			return null;
		}

		try
		{
			File dir = new File(path);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			File f = new File(path + filename);

			if (f.exists())
			{
				f.delete();
			}

			if (f.createNewFile())
			{
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return path + filename;
	}

	/**
	 * 从Uri中获取一张bitmap
	 *
	 * @param context
	 * @param uri
	 * @param max
	 * @return
	 */
	public static Bitmap createBitmap(Context context, Uri uri, int max)
	{
		ContentResolver res = context.getContentResolver();
		if (uri != null)
		{
			ParcelFileDescriptor fd = null;
			try
			{

				fd = res.openFileDescriptor(uri, "r");

				BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
				sBitmapOptionsCache.inPreferredConfig = Bitmap.Config.RGB_565;
				sBitmapOptionsCache.inDither = false;
				sBitmapOptionsCache.inJustDecodeBounds = true;

				BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(),
						null, sBitmapOptionsCache);

				if (sBitmapOptionsCache.outWidth > sBitmapOptionsCache.outHeight)
				{
					sBitmapOptionsCache.inSampleSize = sBitmapOptionsCache.outWidth
							/ max;
				}
				else
				{
					sBitmapOptionsCache.inSampleSize = sBitmapOptionsCache.outHeight
							/ max;
				}

				sBitmapOptionsCache.inJustDecodeBounds = false;

				Bitmap b = BitmapFactory.decodeFileDescriptor(
						fd.getFileDescriptor(), null, sBitmapOptionsCache);

				if (b != null)
				{

					Bitmap tmp = Bitmap.createScaledBitmap(b,
							sBitmapOptionsCache.outWidth,
							sBitmapOptionsCache.outHeight, true);

					if (tmp != b)
						b.recycle();

					b = tmp;

					return b;
				}
				return b;
			}
			catch (FileNotFoundException e)
			{

			} finally
			{
				try
				{
					if (fd != null)
						fd.close();
				}

				catch (IOException e)
				{
				}
			}
		}

		return null;
	}

	/**
	 * 释放bitmap
	 * @param bitmap
	 */
	public static void clean(Bitmap bitmap)
	{
		if(bitmap != null && !bitmap.isRecycled())
		{
			bitmap.recycle();
		}
	}

	/**
	 * 对上传的图片进行大小过滤
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static File filterFileImage(String filename, final int MAX_SIZE) throws IOException, FileNotFoundException
	{
		int fileSize = 0;//文件大小多少K
		File f = new File(filename);
		InputStream is = new FileInputStream(f);
		fileSize = is.available() / 1024;
		is.close();
		BitmapFactory.Options options = new BitmapFactory.Options();

		/**
		 * 计算方法说明
		 *
		 * options.inSampleSize = number;
		 * 其中number为压缩的倍数分之一，一般为2的倍数。
		 * 比如number为2，则图片的长和宽为原图片的2分之一，大小为原图片的4分之一
		 *
		 * 下面的计算方法是 先用图片的原始K数除以要压缩的K书 比如 原始为1000k 要要压缩成50k
		 * 就是1000/50=20;
		 * 然后在对20进行开方，等到一个比例是4 就相当于如果是1M的图片要压缩为原来的16分之1那么得到的图片大小为 1000/16 
		 * 62.5k  
		 */
		if(fileSize >= MAX_SIZE)
		{
			int sqr = (int)Math.ceil(Math.sqrt(((double)fileSize / MAX_SIZE)));
			options.inSampleSize = sqr;

			//LogUtil.d("mBitmap.oldSize=" + fileSize);
			//LogUtil.d("options.inSampleSize=" + options.inSampleSize);

			Bitmap bitmap = BitmapFactory.decodeFile(filename, options);

			//如果过50k 设置最大边长为600像素 压缩之后最大200多K
			if(fileSize >= MAX_SIZE)
			{
				bitmap = BitmapUtil.resizeBitmap(bitmap, 600);
			}

			//这里对bitmap进行判断是否合格		
			if(bitmap == null)
			{
				return f;
			}

			byte[] content = BitmapUtil.bitmap2Bytes(bitmap, 70);
			if(content != null && content.length > 0)
			{
				// 删除掉过去的图片
				f.delete();
				// 创建
				f.createNewFile();
				// 加入新的图片
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(content);
				fos.close();
			}
		}

		return f;
	}


	/**
	 * Stack Blur v1.0 from
	 * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
	 *  Java Author: Mario Klingemann
	 *
	 *  I called it Stack Blur because this describes best how this
	 filter works internally: it creates a kind of moving stack
	 of colors whilst scanning through the image. Thereby it
	 just has to add one new block of color to the right side
	 of the stack and remove the leftmost color. The remaining
	 colors on the topmost layer of the stack are either added on
	 or reduced by one, depending on if they are on the right or
	 on the left side of the stack.

	 Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
	 * */
	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

		if(sentBitmap==null){
			return null;
		}

//    	LogUtil.d(TAG, "fastblur, original size: " + sentBitmap.getByteCount());

		Bitmap bitmap = sentBitmap.copy(Bitmap.Config.RGB_565, true);

		if(bitmap == null){
			return null;
		}
//        LogUtil.d(TAG, "fastblur, copy size: " + bitmap.getByteCount());

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}


	/**
	 * 转换成圆角
	 *
	 * @param bmp
	 * @param roundPx
	 * @return
	 */
	public static Bitmap convertToRoundedCorner(Bitmap bmp, float roundPx) {

		Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				Config.ARGB_8888);
		// 得到画布
		Canvas canvas = new Canvas(newBmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// 第二个和第三个参数一样则画的是正圆的一角，否则是椭圆的一角
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bmp, rect, rect, paint);

		return newBmp;
	}


}
