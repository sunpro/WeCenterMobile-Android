package cn.fanfan.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/*
 * edit at 2014/7/11
 * saveBitmap(String fileName, Bitmap bitmap);保存图片
 * getBitmap(String fileName)取图片
 * isFileExists()判断文件是否存在
 * deleteFile()清除所有图片文件缓存
 * deleteFile(String FileName)删除某个图片文件
 */

public class ImageFileUtils {
	private static String sdRootPath = Environment
			.getExternalStorageDirectory().getPath();
	private static String dataRootPath = null;
	private final static String FOLDER_NAME = "/fanfan";

	public ImageFileUtils(Context context) {
		dataRootPath = context.getCacheDir().getPath();
	}

	private String getStorageDirectory() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? sdRootPath + FOLDER_NAME
				: dataRootPath + FOLDER_NAME;
	}

	public void saveBitmap(String fileName, Bitmap bitmap) throws IOException {
		fileName = fileName.replaceAll("[^\\w]", "");
		if (bitmap == null) {
			return;
		}
		String path = getStorageDirectory();
		File foldFile = new File(path);
		if (!foldFile.exists()) {
			foldFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		file.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 80, outputStream);
		outputStream.flush();
		outputStream.close();
	}

	public Bitmap getBitmap(String fileName) {
		fileName = fileName.replaceAll("[^\\w]", "");
		return decodeSampledBitmapFromResource(getStorageDirectory()
				+ File.separator + fileName, 200, 150);
	}

	public Bitmap decodeSampledBitmapFromResource(String filepath,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public boolean isFileExists(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.exists();
	}

	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.length();
	}

	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		dirFile.delete();
	}

	public void deleteFile(String fileName) {
		fileName = fileName.replaceAll("[^\\w]", "");
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return;
		}
		File deleteFile = new File(getStorageDirectory() + File.separator
				+ fileName);
		if (!deleteFile.exists()) {
			return;
		} else {
			deleteFile.delete();
		}
	}
}
