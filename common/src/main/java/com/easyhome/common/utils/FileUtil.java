package com.easyhome.common.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video;
import android.text.TextUtils;

import com.sina.weibo.sdk.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FileUtil {
	private static final String TAG = "FileUtil";
	/**
	 * KB
	 */
	public static final long ONE_KB = 1024;

	/**
	 * MB
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * GB
	 */
	public static final long ONE_GB = ONE_KB * ONE_MB;

	/**
	 * Unix路径分隔符
	 */
	private static final char UNIX_SEPARATOR = '/';

	/**
	 * Windows路径分隔符
	 */
	private static final char WINDOWS_SEPARATOR = '\\';
	/**
	 * 后缀名分隔符
	 */
	public static final char EXTENSION_SEPARATOR = '.';

	private static final String SDCARD_DIR = "/sdcard";

	private static final int BUF_SIZE = 1024;

	/**
	 * 根据文件URI判断是否为媒体文件
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isMediaUri(String uri) {
		if (uri.startsWith(Audio.Media.INTERNAL_CONTENT_URI.toString())
				|| uri.startsWith(Audio.Media.EXTERNAL_CONTENT_URI.toString())
				|| uri.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
				|| uri.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean copyfile(File src, File desc) throws Exception {
		if (src == null || desc == null) {
			return false;
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			if (!src.isFile() || !src.exists()) {
				return false;
			}

			if (!checkFile(desc)) {
				return false;
			}
			is = new FileInputStream(src);
			os = new FileOutputStream(desc);

			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null)
				is.close();
			if (os != null)
				os.close();
		}
		return true;
	}

	public static String getFileName(String path) {
		if (path == null) {
			return null;
		}
		String retStr = "";
		if (path.indexOf(File.separator) > 0) {
			retStr = path.substring(path.lastIndexOf(File.separator) + 1);
		} else {
			retStr = path;
		}
		return retStr;
	}

	public static String getFileNameNoPostfix(String path) {
		if (path == null) {
			return null;
		}
		return path.substring(path.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 根据文件URI得到文件扩展名
	 * 
	 * @param uri
	 *            文件路径标识
	 * @return
	 */
	public static String getExtension(String uri) {
		if (uri == null)
			return null;

		int extensionIndex = uri.lastIndexOf(EXTENSION_SEPARATOR);
		int lastUnixIndex = uri.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsIndex = uri.lastIndexOf(WINDOWS_SEPARATOR);
		int index = Math.max(lastUnixIndex, lastWindowsIndex);
		if (index > extensionIndex || extensionIndex < 0)
			return null;
		return uri.substring(extensionIndex);
	}

	/**
	 * 判断是否为本地文件
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isLocal(String uri) {
		if (uri != null && !uri.startsWith("http://")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断文件是否为视频文件
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean isVideo(String filename) {
		String mimeType = getMimeType(filename);
		if (mimeType != null && mimeType.startsWith("video/")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断文件是否为音频文件
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean isAudio(String filename) {
		String mimeType = getMimeType(filename);
		if (mimeType != null && mimeType.startsWith("audio/")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据文件名得到文件的mimetype 简单判断,考虑改为xml文件配置关联
	 * 
	 * @param filename
	 * @return
	 */
	public static String getMimeType(String filename) {
		String mimeType = null;

		if (filename == null) {
			return mimeType;
		}
		if (filename.endsWith(".3gp")) {
			mimeType = "video/3gpp";
		} else if (filename.endsWith(".mid")) {
			mimeType = "audio/mid";
		} else if (filename.endsWith(".mp3")) {
			mimeType = "audio/mpeg";
		} else if (filename.endsWith(".xml")) {
			mimeType = "text/xml";
		} else {
			mimeType = "";
		}
		return mimeType;
	}

	/**
	 * 将文件大小的long值转换为可读的文字
	 * 
	 * @param size
	 * @return 10KB或10MB或1GB
	 */
	public static String byteCountToDisplaySize(long size) {
		String displaySize;

		if (size / ONE_GB > 0) {
			displaySize = String.valueOf(size / ONE_GB) + " GB";
		} else if (size / ONE_MB > 0) {
			displaySize = String.valueOf(size / ONE_MB) + " MB";
		} else if (size / ONE_KB > 0) {
			displaySize = String.valueOf(size / ONE_KB) + " KB";
		} else {
			displaySize = String.valueOf(size) + " bytes";
		}
		return displaySize;
	}

	public static boolean isDirectory(File file) {
		return file.exists() && file.isDirectory();
	}

	public static boolean isFile(File file) {
		return file.exists() && file.isFile();
	}

	public static boolean createNewDirectory(File file) {
		if (file.exists() && file.isDirectory()) {
			return false;
		}
		return file.mkdirs();
	}

	public static boolean deleteFile(String filePath) {
		if (filePath == null || filePath.length() < 1)
			return true;
		File file = new File(filePath);
		if (!file.exists())
			return true;
		boolean flag = false;
		if (file.isFile())
			flag = file.delete();
		return flag;
	}

	public static void delDirectory(String directoryPath) {
		try {
			delAllFile(directoryPath); // 删除完里面所有内容
			String filePath = directoryPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delDirectory(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// -------------------- 获得文件的md5等hash值---------------------//
	public final static String HASH_TYPE_MD5 = "MD5";
	public final static String HASH_TYPE_SHA1 = "SHA1";
	public final static String HASH_TYPE_SHA1_256 = "SHA-256";
	public final static String HASH_TYPE_SHA1_384 = "SHA-384";
	public final static String HASH_TYPE_SHA1_512 = "SHA-512";
	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getHash(String fileName, String hashType)
			throws Exception {
		InputStream fis;
		fis = new FileInputStream(fileName);
		byte[] buffer = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			md5.update(buffer, 0, numRead);
		}
		fis.close();
		return toHexString(md5.digest());
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	// 处理lossless音频格式的文件。
	// zhouhenlei add .2010-10-14
	// 音频格式
	public static final String[] AUDIO_EXTS = { ".flac", ".FLAC", ".ape",
			".APE", ".wv", ".WV", ".mpc", ".MPC", "m4a", "M4A", ".wav", ".WAV",
			".mp3", ".MP3", ".wma", ".WMA", ".ogg", ".OGG", ".3gpp", ".3GPP",
			".aac", ".AAC" };
	// 无损音频格式
	public static final String[] LOSSLESS_EXTS = { ".flac", ".FLAC", ".ape",
			".APE", ".wv", ".WV", ".mpc", ".MPC", "m4a", "M4A", ".wav", ".WAV" };
	// 有损音频格式
	public static final String[] LOSS_EXTS = { ".mp3", ".MP3", ".wma", ".WMA",
			".ogg", ".OGG", ".3gpp", ".3GPP", ".aac", ".AAC" };

	// 用在ape,flac的cue后缀
	public static final String CUE_EXT = ".cue";
	// 自定义的播放列表后缀，暂时不用。
	public static final String CUSTOM_PLAYLIST_EXT = ".playlist";
	// 列表格式
	public static final String[] PLAYLIST_EXTS = { CUSTOM_PLAYLIST_EXT, ".m3u",
			".M3U", ".pls", ".PLS" };
	// 自定义书签到后缀
	public static final String BOOKMARK_EXT = ".bmark";

	// true :lossless
	// false:loss
	public static boolean isLosslessSupported(File f) {
		String s = f.toString();
		if (s.endsWith(".flac") || s.endsWith(".FLAC"))
			return true;
		else if (s.endsWith(".ape") || s.endsWith(".APE"))
			return true;
		else if (s.endsWith(".wav") || s.endsWith(".WAV"))
			return true;
		else if (s.endsWith(".wv") || s.endsWith(".WV"))
			return true;
		else if (s.endsWith(".mpc") || s.endsWith(".MPC"))
			return true;
		else if (s.endsWith(".m4a") || s.endsWith(".M4A"))
			return true;
		else
			return false;
	}

	public static boolean isLosslessSupported(String s) {
		// if ((s == null) || (s.length() == 0))
		// return false;
		// if (s.endsWith(".flac") || s.endsWith(".FLAC"))
		// return true;
		// else if (s.endsWith(".ape") || s.endsWith(".APE"))
		// return true;
		// else if (s.endsWith(".wav") || s.endsWith(".WAV"))
		// return true;
		// else if (s.endsWith(".wv") || s.endsWith(".WV"))
		// return true;
		// else if (s.endsWith(".mpc") || s.endsWith(".MPC"))
		// return true;
		// else if (s.endsWith(".m4a") || s.endsWith(".M4A"))
		// return true;
		// else
		return false;
	}

	/**
	 * 清除目录dirPath下后缀名为suffix的文件
	 *
	 * @param dirPath
	 * @param suffix
	 */
	public static void clearFiles(String dirPath, String suffix) {
		if (TextUtil.isEmpty(dirPath) || TextUtil.isEmpty(suffix))
			return;
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory())
			return;
		String filename = null;
		for (File file : dir.listFiles()) {
			filename = file.getName();
			if (filename.endsWith(suffix))
				file.delete();
		}
	}

	/**
	 * 清理路径列表list中后缀名为suffix的文件
	 *
	 * @param list
	 * @param suffix
	 */
	public static void clearFiles(ArrayList<String> list, String suffix) {
		if (list == null || list.isEmpty())
			return;
		File file = null;
		for (String path : list) {
			if (TextUtil.isEmpty(path))
				continue;
			String tmp = FileUtil.getExtension(path);
			if (TextUtil.isEmpty(tmp) || !tmp.equals(suffix))
				continue;
			file = new File(path);
			if (file.exists() && file.isFile())
				file.delete();
		}
	}

	/**
	 * 将is输入流指定的数据写入到context对应的/data/data/<package name>/files目录下，filename作为文件名
	 *
	 * @param context
	 * @param is
	 * @param filename
	 * @return
	 */
	public static String writeToFile(Context context, InputStream is,
			String filename) {
		if (is == null || context == null || TextUtil.isEmpty(filename))
			return null;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(is);
			out = new BufferedOutputStream(context.openFileOutput(filename,
					Context.MODE_PRIVATE));
			byte[] buffer = new byte[BUF_SIZE];
			int l;
			while ((l = in.read(buffer)) != -1)
				out.write(buffer, 0, l);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				is.close();
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return context.getFilesDir() + File.separator + filename;
	}

	/**
	 * 将输入流is指定的数据写入filepath指定的文件中
	 *
	 * @param is
	 * @param filepath
	 * @return
	 * @throws java.io.IOException
	 */
	public static String writeToFile(InputStream is, String filepath)
			throws IOException {
		if (is == null || TextUtil.isEmpty(filepath))
			return null;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			LogUtil.d(TAG, "write to file : " + filepath);

			File file = new File(filepath);
			checkDir(file.getParent());
			in = new BufferedInputStream(is);
			out = new BufferedOutputStream(new FileOutputStream(filepath));
			byte[] buffer = new byte[BUF_SIZE];
			int l;
			while ((l = in.read(buffer)) != -1)
				out.write(buffer, 0, l);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (in != null)
					in.close();
				is.close();
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return filepath;
	}

	/**
	 * 将输入流is指定数据同步写入filepath指定位置
	 *
	 * @param is
	 * @param filepath
	 * @return
	 * @throws java.io.IOException
	 */
	synchronized public static String writeToFileSync(InputStream is,
			String filepath) throws IOException {
		return FileUtil.writeToFile(is, filepath);
	}

	/**
	 * write a string To a File
	 *
	 * @param file
	 * @param string
	 * @param isAppend
	 * @return
	 */
	public static boolean writeStringToFile(File file, String string,
			boolean isAppend) {
		boolean isWriteOk = false;

		if (null == file || null == string) {
			return isWriteOk;
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(file, isAppend);

			fw.write(string, 0, string.length());
			fw.flush();
			isWriteOk = true;
		} catch (Exception e) {
			isWriteOk = false;
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					isWriteOk = false;
					e.printStackTrace();
				}
			}
		}
		return isWriteOk;
	}

	/**
	 * read file to a string
	 *
	 * @param file
	 * @return
	 */
	public static String readFileToString(File file) {
		if (null == file)
			return "";
		FileInputStream fileInput = null;
		StringBuffer strBuf = new StringBuffer();

		try {
			fileInput = new FileInputStream(file);
			byte[] buf = new byte[BUF_SIZE];
			while (fileInput.read(buf) != -1) {
				strBuf.append(new String(buf));
				buf = new byte[BUF_SIZE];
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return strBuf.toString();
	}

	/**
	 * list all files from current path
	 *
	 * @param path
	 * @return
	 */
	public static File[] listFiles(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			return dir.listFiles();
		} catch (SecurityException ex) {
			return null;
		}
	}

	/**
	 * get a file lines
	 *
	 * @return
	 */
	public static int getFileLines(File file) {
		if (null == file)
			return 0;
		BufferedReader bufReader = null;
		int count = 0;
		try {
			bufReader = new BufferedReader(new FileReader(file));

			while ((bufReader.readLine()) != null)
				count++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			count = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}

	/**
	 * 检查目录是否存在，如果不存在创建之
	 *
	 * @return 目录是否存在
	 */
	public static boolean checkDir(String dirPath) {
		if (TextUtil.isEmpty(dirPath))
			return false;
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory())
			return true;
		if (dir.exists())
			dir.delete();
		return dir.mkdirs();
	}

	/**
	 * 获取目录所占空间大小(包括子目录文件)
	 *
	 * @param dirPath
	 *            目录路径
	 * @return
	 */
	public static long getDirLength(String dirPath) {
		if (TextUtil.isEmpty(dirPath))
			return 0;
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory())
			return 0;
		long length = 0;
		for (File file : dir.listFiles()) {
			if (file.isFile())
				length += file.length();
			else if (file.isDirectory())
				length += getDirLength(file.getAbsolutePath());
		}
		return length;
	}

	/**
	 * 从指定目录清除最久未使用的文件，被清除的文件大小总和需要大于length
	 *
	 * @param dirPath
	 *            目录路径
	 * @param length
	 *            要清除掉的文件大小总和
	 * @return 被清除的文件数
	 */
	public static int removeOldFiles(String dirPath, long length) {
		if (TextUtil.isEmpty(dirPath) || length <= 0)
			return 0;
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory())
			return 0;
		File[] files = FileUtil.getFilesByLastModified(dir);
		long l = 0;
		int count = 0;
		for (File file : files) {
			l = file.length();
			if (file.delete()) {
				count++;
				length -= l;
				if (length <= 0)
					break;
			}
		}
		return count;
	}

	/**
	 * 按照最后修改时间排序，获取目录dir下文件列表
	 *
	 * @param dir
	 * @return
	 */
	public static File[] getFilesByLastModified(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return null;
		File[] files = dir.listFiles();

		try{
			//jdk7 此算法有修改，有可能报错，设置属性，使其使用旧版本排序
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File lhs, File rhs) {
					return (int) (lhs.lastModified() - rhs.lastModified());
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}

		return files;
	}

	/**
	 * SDCard是否可用
	 */
	public static boolean isSDCardAvailable() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 清除目录下的所以文件
	 *
	 * @param dirPath
	 *            目录路径
	 * @return 是否清除成功
	 */
	public static boolean clearDir(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory())
			return false;
		for (File file : dir.listFiles()) {
			if (!file.exists())
				continue;
			if (file.isFile())
				file.delete();
			if (file.isDirectory()) {
				clearDir(file.getAbsolutePath());
			}
		}
		File[] files = dir.listFiles();
		return files == null || files.length == 0;
	}

	/**
	 * 检查文件是否存在，不存在时创建相应文件及所在目录
	 *
	 * @param file
	 * @return
	 * @throws java.io.IOException
	 */
	public static boolean checkFile(File file) throws IOException {
		if (file == null)
			return false;
		if (file.isFile() && file.exists())
			return true;
		if (file.exists() && !file.isFile())
			file.delete();
		file.getParentFile().mkdirs();
		return file.createNewFile();
	}

	/**
	 * 过滤文件名，保证过滤后的文件名为合法文件名<br/>
	 * 非法字符将被替换成下划线_
	 * 
	 * @param filename
	 *            需要过滤的文件名(不包括父目录路径)
	 * @return 过滤后合法的文件名
	 */
	public static String filterFileName(String filename) {
		if (TextUtils.isEmpty(filename))
			return filename;
		filename = filename.replace(' ', '_');
		filename = filename.replace('"', '_');
		filename = filename.replace('\'', '_');
		filename = filename.replace('\\', '_');
		filename = filename.replace('/', '_');
		filename = filename.replace('<', '_');
		filename = filename.replace('>', '_');
		filename = filename.replace('|', '_');
		filename = filename.replace('?', '_');
		filename = filename.replace(':', '_');
		filename = filename.replace(',', '_');
		filename = filename.replace('*', '_');
		return filename;
	}

	/**
	 * 获取SD卡可用大小
	 * 
	 * @return
	 */
	public static long getSDCardAvailableSpace() {
		File file = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(file.getPath());

		long blockSize = stat.getBlockSize();

		long availableBlocks = stat.getAvailableBlocks();

		return availableBlocks * blockSize;
	}

	/**
	 * 检查SD卡可用空间是否满足需要
	 * 
	 * @return
	 */
	public static boolean checkSDCardHasEnoughSpace(long size) {
		return getSDCardAvailableSpace() > size;
	}

	/**
	 * 检测path是否为SD卡中的目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkPathInSDcard(String path) {
		if (TextUtil.isEmpty(path))
			return false;
		String sdcardPrefix = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return path.startsWith(SDCARD_DIR) || path.startsWith(sdcardPrefix);
	}
}
