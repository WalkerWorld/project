package com.walker.utils;

import android.os.Environment;

public class SDCardCheck {

	public static class Tools {
		/**
		 * 检查是否存在SDCard
		 * 
		 * @return
		 */
		public static boolean hasSdcard() {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
