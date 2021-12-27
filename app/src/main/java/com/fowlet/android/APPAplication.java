package com.fowlet.android;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory;
import xyz.doikki.videoplayer.player.AndroidMediaPlayerFactory;
import xyz.doikki.videoplayer.player.VideoViewConfig;
import xyz.doikki.videoplayer.player.VideoViewManager;

public class APPAplication extends Application {


	private String PlayerName,PlayerSearch,PlayerDetail;

	public static String CorePlayerSearch = "https://jialiapi.com/api.php/provide/vod/at/xml/?ac=list&wd=";
	public static String CorePlayerDetail = "https://jialiapi.com/api.php/provide/vod/at/xml/?ac=videolist&ids=";
	public static String CorePlayerName = "佳丽";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		VideoViewManager.setConfig(VideoViewConfig.newBuilder()
				//使用使用IjkPlayer解码
				//.setPlayerFactory(IjkPlayerFactory.create())
				//使用ExoPlayer解码
				.setPlayerFactory(ExoMediaPlayerFactory.create())
				//使用MediaPlayer解码
				.setPlayerFactory(AndroidMediaPlayerFactory.create())
				.build());

		setPlayerSearch(CorePlayerSearch);
		setPlayerDetail(CorePlayerDetail);
		setPlayerName(CorePlayerName);

		ZXingLibrary.initDisplayOpinion(this);
	}

	public String getPlayerSearch() {
		return PlayerSearch;
	}

	public void setPlayerSearch(String playerSearch) {
		PlayerSearch = playerSearch;
	}

	public String getPlayerDetail() {
		return PlayerDetail;
	}

	public void setPlayerDetail(String playerDetail) {
		PlayerDetail = playerDetail;
	}

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
}
