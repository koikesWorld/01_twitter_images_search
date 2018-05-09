package kadai01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class SearchImage {

	// 検索結果取得件数
	static final int TWEET_INCLUDE_IMAGES_NUM = 10;

	//出力先ディレクトリ定数
	static final String FOLDER_PATH_STRING = System.getProperty("user.dir") + File.separator + "images";

	// 検索クエリ
	// 今回は「JustinBieber」をキーワード検索する
	static final String MY_QUERY = "JustinBieber filter:images exclude:retweets";

	public static void main(String[] args) {

		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(MY_QUERY);
		query.setCount(TWEET_INCLUDE_IMAGES_NUM);
		QueryResult result;

		try {
			// 検索実行
			result = twitter.search(query);
			URL imageUrl = null;

			//保存先ディレクトリの作成
			File folder = new File(FOLDER_PATH_STRING);
			if(!folder.exists()){
				folder.mkdir();
			}
			
			// 画像のURLを抽出
			for (twitter4j.Status sts : result.getTweets()) {
				for (MediaEntity media : sts.getMediaEntities()) {
					imageUrl = new URL(media.getMediaURL());

					// 取得したURLを基に画像ファイル作成
					ReadableByteChannel rbc = Channels.newChannel(imageUrl.openStream());



					//ファイルの拡張子を取得する
					String imageUrlString = imageUrl.toString();
					String fileExtension=null;
					int lastDotIndex = imageUrlString.lastIndexOf(".");
					if(lastDotIndex != -1){
						fileExtension = imageUrlString.substring(lastDotIndex);
					}
					//ファイル名にStatusが持つ作成日時を付与
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					
					FileOutputStream fos =
							new FileOutputStream(FOLDER_PATH_STRING +File.separator +  "JustinBieber" + df.format(sts.getCreatedAt())+ fileExtension);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fos.close();
				}
			}
		} catch (TwitterException e) {
			System.out.println("Twitterの検索に失敗しました");
			return;
		} catch (MalformedURLException e) {
			System.out.println("URLが正しく取得できませんでした");
			return;
		} catch (IOException e) {
			System.out.println("ファイル作成に失敗しました");
			return;
		}
		System.out.println("ファイル作成が完了しました");
	}

}
