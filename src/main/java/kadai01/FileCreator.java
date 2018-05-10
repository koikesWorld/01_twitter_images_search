package kadai01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 画像ファイル作成のためのクラス.
 * @author Koike
 *
 */
public class FileCreator {

	/**
	 * 出力先フォルダの作成を行う.
	 * @return file
	 */
	public static File folderCreate(String folderName) {
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folder;
	}

	/**
	 * URLからファイル拡張子を取得する.
	 * @param url
	 * @return fileExtension
	 */
	public static String getExtension(URL url){
		String fileExtension=null;
		String targetString  =url.toString();
		int lastDotIndex = targetString.lastIndexOf(".");

		if(lastDotIndex != -1){
			fileExtension = targetString.substring(lastDotIndex);
		}
		return fileExtension;
	}

	/**
	 * 画像URLからファイルを作成する.
	 * @param imageUrl
	 * @param screenName
	 * @throws IOException
	 */
	public static void fileCreate( URL imageUrl,String folderName, String screenName) throws IOException {
		ReadableByteChannel rbc = Channels.newChannel(imageUrl.openStream());
		String fileExtension = getExtension(imageUrl);
		FileOutputStream fos =
				new FileOutputStream(folderName +File.separator + screenName + fileExtension);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	}
}
