package com.shequ.baliu;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shequ.baliu.holder.PersonInfo;

import android.app.Application;

public class ShequApplication extends Application {

	private boolean isLogin;
	private PersonInfo mInfo;

	@Override
	public void onCreate() {
		super.onCreate();

		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);

		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(5).memoryCache(new WeakMemoryCache())
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		isLogin = false;
		mInfo = new PersonInfo();
	}

	public boolean getLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLoginSucess) {
		isLogin = isLoginSucess;
	}

	public PersonInfo getInfo() {
		return mInfo;
	}

	public void setInfo(PersonInfo mInfo) {
		this.mInfo = mInfo;
	}
}
