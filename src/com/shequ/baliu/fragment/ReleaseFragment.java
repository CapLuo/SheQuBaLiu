package com.shequ.baliu.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.dimen;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shequ.baliu.R;
import com.shequ.baliu.ShequApplication;
import com.shequ.baliu.adapter.AdapterInsertImageBrowsingSecondHand;
import com.shequ.baliu.dialog.ShequDialog;
import com.shequ.baliu.util.ShequTools;
import com.shequ.baliu.util.SqlHelper;
import com.shequ.baliu.util.StaticVariableSet;
import com.shequ.baliu.view.ImageBrowsingLayout;
import com.shequ.baliu.view.ImageBrowsingLayout.ShowInsertImg;

public class ReleaseFragment extends Fragment implements OnClickListener {

	private View mContentView;

	private EditText mTitleText;
	private TextView mCateText;
	private View mCateLayout;
	private EditText mContentText;
	private EditText mNewPrice;
	private EditText mOldPrice;
	private View mFinalRelease;

	private ImageBrowsingLayout mImageLayout;
	private AdapterInsertImageBrowsingSecondHand mAdapter;

	private ArrayList<String> mSecondGoods;
	private String mSendImage;
	private ArrayList<CateHolder> mCateList;

	private PopupWindow mCateWindow;
	private PopupWindow mMenuWindow;
	private ShequDialog mDialog;

	private String upload_url;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_release,
					container, false);
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}

		initView();
		initData();

		return mContentView;
	}

	private void initView() {
		mFinalRelease = mContentView.findViewById(R.id.release_goods_button);
		mFinalRelease.setOnClickListener(this);

		mTitleText = (EditText) mContentView
				.findViewById(R.id.release_goods_title);
		mCateText = (TextView) mContentView
				.findViewById(R.id.release_cate_text);
		mCateLayout = mContentView.findViewById(R.id.release_cate_layout);
		mCateLayout.setOnClickListener(this);
		android.view.ViewGroup.LayoutParams params = mCateLayout
				.getLayoutParams();
		params.width = countTextWidth();
		mCateLayout.setLayoutParams(params);
		mContentText = (EditText) mContentView
				.findViewById(R.id.release_description_content);
		mNewPrice = (EditText) mContentView
				.findViewById(R.id.release_goods_new_price);
		mOldPrice = (EditText) mContentView
				.findViewById(R.id.release_goods_old_price);
		mImageLayout = (ImageBrowsingLayout) mContentView
				.findViewById(R.id.release_image_browsing);

		mDialog = new ShequDialog(getActivity()) {
			@Override
			public void onBackPressed() {
				// 屏蔽换回建
			}
		};
		View dialogContent = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_login_layout, null);
		TextView mDialogText = (TextView) dialogContent
				.findViewById(R.id.dialog_content);
		mDialogText.setText(R.string.release_dialog_text);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(dialogContent);
	}

	private void initData() {
		mSecondGoods = new ArrayList<String>();
		mCateList = new ArrayList<CateHolder>();
		mAdapter = new AdapterInsertImageBrowsingSecondHand(getActivity());
		mImageLayout.setAdapter(mAdapter);
		mImageLayout.setShowImgInterface(new ShowInsertImg() {

			@Override
			public void showImg(String uri) {
				if (TextUtils.isEmpty(uri)) {
					showPopuWindow(mImageLayout);
				}
			}
		});

		getSencondHandCate();
	}

	private void getSencondHandCate() {
		SqlHelper.getSencondCate(new JsonHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject object = response.getJSONObject(i);
						CateHolder cate = new CateHolder();
						String id = object.getString("cateid");
						String name = object.getString("catename");
						cate.id = id;
						cate.name = name;
						mCateList.add(cate);
					}
					mCateText.setText(mCateList.get(0).name);
					mCateText.setTag(mCateList.get(0).id);
				} catch (JSONException e) {
					Log.e(StaticVariableSet.TAG, e.getMessage());
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.release_goods_button:
			try {
				PicRelease();
			} catch (IOException e) {
				Log.e(StaticVariableSet.TAG, e.getMessage());
			} catch (JSONException e) {
				Log.e(StaticVariableSet.TAG, e.getMessage());
			}
			break;
		case R.id.release_cate_layout:
			showPopuWindow();
			break;
		case R.id.release_menu_camera:
			mMenuWindow.dismiss();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			mSendImage = ShequTools.getAppFilePath() + "/"
					+ new Date().getTime() + ".jpg";
			intent.putExtra("output", Uri.fromFile(new File(mSendImage)));
			intent.putExtra("outputFormat", "JPEG");
			startActivityForResult(intent, 2);
			break;
		case R.id.release_menu_albums:
			mMenuWindow.dismiss();
			Intent albums = new Intent(Intent.ACTION_GET_CONTENT);
			albums.addCategory(Intent.CATEGORY_OPENABLE);
			albums.setType("image/*");
			startActivityForResult(Intent.createChooser(albums, "选择图片"), 1);
			break;
		default:
			break;
		}
	}

	private void showPopuWindow() {
		if (mCateWindow == null) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View popuView = inflater.inflate(R.layout.popup_cate_background,
					null);
			mCateWindow = new PopupWindow(getActivity());
			mCateWindow.setHeight(LayoutParams.WRAP_CONTENT);
			mCateWindow.setWidth(countTextWidth());
			mCateWindow.setFocusable(true);
			mCateWindow.setOutsideTouchable(true);
			mCateWindow.setBackgroundDrawable(getActivity().getResources()
					.getDrawable(R.drawable.pop_second_background));

			LinearLayout layout = (LinearLayout) popuView
					.findViewById(R.id.pop_view_parent);
			int length = mCateList.size();
			for (int i = 0; i < length; i++) {
				TextView text = new TextView(getActivity());
				final String name = mCateList.get(i).name;
				final String id = mCateList.get(i).id;
				text.setText(name);
				text.setPadding(5, 2, 0, 2);
				layout.addView(text);
				text.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						mCateText.setText(name);
						mCateText.setTag(id);
						if (mCateWindow.isShowing()) {
							mCateWindow.dismiss();
						}
					}
				});
			}

			mCateWindow.setContentView(popuView);
		}
		mCateWindow.showAsDropDown(mCateLayout);
	}

	private void showPopuWindow(View view) {
		if (mMenuWindow == null) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View popuView = inflater.inflate(R.layout.popup_menu_release, null);
			mMenuWindow = new PopupWindow(popuView,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			View camera = popuView.findViewById(R.id.release_menu_camera);
			camera.setOnClickListener(this);
			View albums = popuView.findViewById(R.id.release_menu_albums);
			albums.setOnClickListener(this);
			mMenuWindow.setBackgroundDrawable(new BitmapDrawable());
			mMenuWindow.setFocusable(true);
			mMenuWindow.setOutsideTouchable(true);
		}
		mMenuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private void PicRelease() throws JSONException, IOException {
		if (!verificationInput()) {
			return;
		}
		if (mDialog != null) {
			mDialog.show();
		}
		String file_path = mSecondGoods.get(0);
		if (file_path.startsWith("content")) {
			file_path = ShequTools.getRealFilePath(getActivity(),
					Uri.parse(file_path));
		}
		File file = new File(file_path);
		FileInputStream fileInput = new FileInputStream(file);
		byte[] b = new byte[fileInput.available()];
		fileInput.read(b);
		final String ends = file_path.substring(file_path.lastIndexOf(".") + 1,
				file_path.length());
		SqlHelper.uploadPic(getActivity(), b, ends,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if (arg2 != null) {
							String str = new String(arg2);
							if (str.endsWith(ends)) {
								upload_url = str;
								finalRelease();
								return;
							}
							String[] array = str.split("\n");
							if (array.length > 1) {
								upload_url = array[0];
								finalRelease();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e(StaticVariableSet.TAG, arg3.getMessage());
						dismiss();
					}
				});

		fileInput.close();
	}

	private void finalRelease() {
		ShequApplication application = (ShequApplication) getActivity()
				.getApplication();
		String userid = application.getInfo().getUserId();
		if (TextUtils.isEmpty(userid)) {
			Toast.makeText(getActivity(), R.string.user_login_first,
					Toast.LENGTH_SHORT).show();
		}
		String cateValue = (String) mCateText.getTag();
		String title = mTitleText.getText().toString();
		String price = mNewPrice.getText().toString();
		String content = mContentText.getText().toString();
		Date date = new Date();
		long addtime = (long) date.getTime() / 1000;

		SqlHelper
				.add(StaticVariableSet.SECOND_MARKET,
						"(`userid`, `cateid`, `title`, `price`, `content`, `photo`, `isrecommend`, `addtime`, `updatetime`) VALUES ('"
								+ userid
								+ "', '"
								+ cateValue
								+ "', '"
								+ title
								+ "', '"
								+ price
								+ "', '"
								+ content
								+ "', '"
								+ upload_url
								+ "', '1', '"
								+ addtime
								+ "', '"
								+ addtime + "')",
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int status, Header[] head,
									byte[] bytes) {
								dismiss();
								getActivity().finish();
							}

							@Override
							public void onFailure(int status, Header[] head,
									byte[] bytes, Throwable throwable) {
								if (throwable != null) {
									Log.e(StaticVariableSet.TAG,
											throwable.getMessage());
								}
								dismiss();
							}
						});
	}

	private boolean verificationInput() {
		String title = mTitleText.getText().toString();
		if (TextUtils.isEmpty(title)) {
			showToast("亲,标题不能空着！");
			return false;
		}
		String content = mContentText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			showToast("亲,给点物品描述吧！");
			return false;
		}
		String price = mNewPrice.getText().toString();
		if (TextUtils.isEmpty(price)) {
			showToast("亲,别忘了价格.");
			return false;
		}
		int length = mSecondGoods.size();
		if (length <= 0) {
			showToast("亲,让您家宝贝露个脸吧.");
			return false;
		}
		return true;
	}

	private void showToast(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	private class CateHolder {
		String id;
		String name;
	}

	private int countTextWidth() {
		Rect bounds = new Rect();
		String text = "电动车/自行车/摩托车";
		TextPaint paint;

		paint = mCateText.getPaint();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.width();
		return width;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			addPhotoTolayout(uri.toString());
		}
		if (requestCode == 2) {
			addPhotoTolayout(mSendImage);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addPhotoTolayout(String photo_url) {
		if (photo_url.startsWith("content")) {
		} else {
			File imageFile;
			imageFile = new File(photo_url);
			if (imageFile.length() == 0) {
				imageFile.delete();
				return;
			}
		}

		mSecondGoods.add(photo_url);
		mAdapter.notifyDataList(mSecondGoods);
		mImageLayout.setAdapter(mAdapter);
	}

	private void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

}