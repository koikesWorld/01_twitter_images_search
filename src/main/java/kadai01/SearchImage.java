package kadai01;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class SearchImage {

	// 出力先ディレクトリ定数
	static final String FOLDER_PATH_STRING = System.getProperty("user.dir") + File.separator + "images";

	// 検索結果取得件数
	static final int TWEET_NUM = 100;

	// 検索クエリ
	// 今回は「JustinBieber」をキーワード検索する
	static final String MY_QUERY = " JustinBieber exclude:retweets";

	public static void main(String[] args) {

		//出力先フォルダを作成
		File folder = FileCreator.folderCreate(FOLDER_PATH_STRING);

		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(MY_QUERY);
		query.setCount(TWEET_NUM);
		QueryResult result;

		try {
			// 検索実行
			result = twitter.search(query);
			URL imageUrl = null;

			// 画像のURLを抽出
			for (twitter4j.Status sts : result.getTweets()) {
				for (MediaEntity media : sts.getMediaEntities()) {
					imageUrl = new URL(media.getMediaURL());

					// 取得したURLを基に画像ファイル作成
					FileCreator.fileCreate(imageUrl, folder.getPath(), sts.getUser().getScreenName());

					// ファイルが10件作成されたら終了
					if(folder.listFiles().length >= 10 ) {
						System.out.println("ファイル作成が完了しました");
						return;
					}
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
	}

}
