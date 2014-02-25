package com.easyhome.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 系统环境工具 得到SD卡,屏幕等手机环境信息
 * 
 * @author fuliqiang
 * 
 */
public class EnvironmentUtilities {

	private static final double MIN_FREE_SPACE = 10;

	public static final String SYSTEM_SEPARATOR = System.getProperty("file.separator");

	public static final String DIR_HOME = "Baidu_music";
	public static final String DIR_LYRIC = "lyric";
	public static final String DIR_MUSIC = "music";
	public static final String DIR_PCSYNC = "pcsync";
	public static final String DIR_IMAGE = "image";
	public static final String DIR_DOWNLOAD = "download";
	public static final String DIR_DOWNLOAD_CACHE = "cache";
	public static final String DIR_ONLINE_CACHE = "online";
	public static final String DIR_ONLINE_TEMP = "temp";
	public static final String DIR_LOSSLESS = "lossless";
	public static final String DIR_OFFLINECACHING_CACHE = "offlinecache";
	public static final String DIR_GOODVOICE_CACHE = "goodvoice";
	public static final String DIR_PLUGIN = "plugin";
	public static final String DIR_SKIN = "skin";
	// 软件推荐, 3.3.0
	public static final String DIR_RECOMMEND = "recommend";
	public static final String DIR_RECOMMEND_ICON = "icon";
	public static final String DIR_RECOMMEND_APK = "apk";


	/**
	 * 是否mount外置存储
	 * 
	 * @return
	 */
	public static boolean isMount() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * SD卡剩余空间的百分比
	 * 
	 * @return
	 */
	public static double freePercentage() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long availableBlocks = stat.getAvailableBlocks();
		long totalBlocks = stat.getBlockCount();
		return (double) availableBlocks / (double) totalBlocks * 100;
	}

	/**
	 * 判断SD卡访问状态,是否已经映射并且有足够的剩余空间
	 * 
	 * @return
	 */
	public static String assertSdCard() {
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return "Your SD card is not mounted. You'll need it for caching thumbs.";
		}
		return null;
	}

	public static String checkFreeSpaceSdCard() {
		if (freePercentage() < MIN_FREE_SPACE) {
			return "You need to have more than " + MIN_FREE_SPACE + "% of free space on your SD card.";
		}
		return null;
	}

	/**
	 * 得到SD卡全部存储空间
	 * 
	 * @return
	 */
	public static long totalSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 检查SD卡剩余空间
	 * 
	 * @return
	 */
	public static long freeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 得到软件所需要的全部文件夹列表
	 * 
	 * @return
	 */
	public static ArrayList<File> getTingMp3AllDirectory() {
		ArrayList<File> dirList = new ArrayList<File>();
		dirList.add(getTingHomeDirectory());
		dirList.add(getTingMusicDirectory());
		dirList.add(getTingPcsyncDirectory());
		dirList.add(getTingLosslessMusicDirectory());
		dirList.add(getTingImageDirectory());
		dirList.add(getTingLyricDirectory());
		dirList.add(getTingDownloadDirectory());
		dirList.add(getTingDownloadCacheDirectory());
		dirList.add(getTingOfflineCachingDirectory()); // Added from v3.0
		dirList.add(getTingOnlineDirectory());
		return dirList;
	}

	/**
	 * SDCARD的绝对路径。
	 */
	public static String getExternalStoragePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 得到本软件默认的主文件夹,将来如果可以通过配置修改默认文件夹,也要从这里读取
	 */
	public static File getTingHomeDirectory() {
		return new File(getExternalStoragePath(), DIR_HOME);
	}

	public static String getTingHomePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getExternalStoragePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_HOME);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的歌曲文件目录
	 */
	public static File getTingMusicDirectory() {
		return new File(getTingHomeDirectory(), DIR_MUSIC);
	}

	/**
	 * 得到PC同步歌曲的文件夹
	 * 
	 * @return
	 */
	public static File getTingPcsyncDirectory() {
		File dir = new File(getTingHomeDirectory(), DIR_PCSYNC);
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	public static String getTingMusicPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_MUSIC);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的歌曲缓存文件目录
	 */
	public static File getTingMusicCacheDirectory() {
		return new File(getTingMusicDirectory(), DIR_DOWNLOAD_CACHE);
	}

	public static String getTingMusicCachePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingMusicPath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_DOWNLOAD_CACHE);
		return sb.toString();
	}

	public static String getTingMusicOnlineCachePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_ONLINE_CACHE);
		return sb.toString();
	}

	public static String getTingMusicEngineTempPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_ONLINE_CACHE);
		return sb.toString();
	}
	
	
	
	/**
	 * 得到本软件默认的无损歌曲文件目录
	 */
	public static File getTingLosslessMusicDirectory() {
		return new File(getTingHomeDirectory(), DIR_LOSSLESS);
	}

	public static String getTingLosslessMusicPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_LOSSLESS);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的图片文件目录
	 */
	public static File getTingImageDirectory() {
		return new File(getTingHomeDirectory(), DIR_IMAGE);
	}

	public static String getImageMusicPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_IMAGE);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的歌词文件目录
	 */
	public static File getTingLyricDirectory() {
		return new File(getTingHomeDirectory(), DIR_LYRIC);
	}

	public static String getTingLyricPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_LYRIC);
		return sb.toString();
	}

	public static String getTingDefaultDownloadPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_DOWNLOAD);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的下载文件目录
	 */
	public static File getTingDownloadDirectory() {
		return new File(getTingHomeDirectory(), DIR_DOWNLOAD);
	}

	/**
	 * 得到本软件默认的离线缓存下载缓存文件目录。 v3.0 引入。
	 */
	public static File getTingOfflineCachingDirectory() {
		return new File(getTingHomeDirectory(), DIR_OFFLINECACHING_CACHE);
	}

	public static File getTingOnlineDirectory() {
		return new File(getTingHomeDirectory(), DIR_ONLINE_CACHE);
	}

	public static String getTingDownloadPath() {
		return getTingDefaultDownloadPath();

	}

	/**
	 * 获取 插件目录
	 * 
	 * @return
	 */
	public static String getTingPluginPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_PLUGIN);
		String result = sb.toString();
		File dir = new File(result);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return result;
	}

	public static String getSkinPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_SKIN);
		String result = sb.toString();
		File dir = new File(result);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return result;
	}

	/**
	 * 得到本软件默认的下载缓存文件目录
	 */
	public static File getTingDownloadCacheDirectory() {
		return new File(getTingDownloadDirectory(), DIR_DOWNLOAD_CACHE);
	}

	public static String getTingDownloadCachePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingDownloadPath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_DOWNLOAD_CACHE);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的离线缓存下载缓存文件path。 v3.0 引入。
	 */
	public static String getTingDefaultOfflineCachingPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_OFFLINECACHING_CACHE);
		return sb.toString();
	}

	/**
	 * 得到本软件默认的离线缓存下载缓存文件path。 v3.0 引入。
	 */
	public static String getGoodVoiceCachingPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_GOODVOICE_CACHE);
		return sb.toString();
	}

	/**
	 * 得到软件版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 得到当前系统的API level
	 * 
	 * @return
	 */
	public static int getSystemVersion() {
		return Integer.parseInt(Build.VERSION.SDK);
	}

	/**
	 * 判断SDK是否为 1.5
	 * 
	 * @return
	 */
	public static boolean isVersion1_5() {
		return getSystemVersion() == Build.VERSION_CODES.CUPCAKE;
	}

	/**
	 * 判断SDK是否为 1.6
	 * 
	 * @return
	 */
	public static boolean isVersion1_6() {
		return getSystemVersion() == Build.VERSION_CODES.DONUT;
	}

	/**
	 * 判断SDK是否为 2.0
	 * 
	 * @return
	 */
	public static boolean isVersion2_0() {
		return getSystemVersion() == Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * 判断SDK是否为 2.0.1
	 * 
	 * @return
	 */
	public static boolean isVersion2_0_1() {
		return getSystemVersion() == Build.VERSION_CODES.ECLAIR_0_1;
	}

	/**
	 * 判断SDK是否为 2.1
	 * 
	 * @return
	 */
	public static boolean isVersion2_1() {
		return getSystemVersion() == Build.VERSION_CODES.ECLAIR_MR1;
	}

	/**
	 * 判断SDK是否为 2.2
	 * 
	 * @return
	 */
	public static boolean isVersion2_2() {
		return false;
		// return getSystemVersion() == Build.VERSION_CODES.FROYO;
	}

	/**
	 * 判断是否模拟器。如果返回TRUE，则当前是模拟器
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isEmulator(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.equals("000000000000000")) {
			return true;
		}
		return false;
	}

	/**
	 * 得到当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean networkAvailable(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetInfo == null ? false : activeNetInfo.isAvailable();
	}

	public static boolean isSdcardWritable() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED) || status.equals(Environment.MEDIA_CHECKING)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSdcardMounted() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED) || status.equals(Environment.MEDIA_CHECKING) || status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 软件推荐icon下载路径, 如/mnt/sdcard/Baidu_music/recommend/icon/, 最后有一个斜杠
	 * 
	 * @return
	 */
	public static String getSoftwareRecommendIconPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_RECOMMEND);
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_RECOMMEND_ICON);
		sb.append(SYSTEM_SEPARATOR);
		return sb.toString();
	}

	/**
	 * 软件推荐apk下载路径, 如/mnt/sdcard/Baidu_music/recommend/apk/, 最后有一个斜杠
	 * 
	 * @return
	 */
	public static String getSoftwareRecommendApkPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTingHomePath());
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_RECOMMEND);
		sb.append(SYSTEM_SEPARATOR);
		sb.append(DIR_RECOMMEND_APK);
		sb.append(SYSTEM_SEPARATOR);
		return sb.toString();
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	// 创新目录.
	public static void checkDownloadDir() {
		File file = new File(EnvironmentUtilities.getGoodVoiceCachingPath());
		if (file.exists() && file.isDirectory()) {
			return;
		} else {
			file.mkdirs();
			File f = new File(EnvironmentUtilities.getGoodVoiceCachingPath() + File.separator + ".nomedia");
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
