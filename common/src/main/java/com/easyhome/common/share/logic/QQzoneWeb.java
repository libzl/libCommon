package com.easyhome.common.share.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easyhome.common.R;
import com.easyhome.common.share.ShareConfiguration;
import com.easyhome.common.share.model.IShareObject;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * 通过Web或者QQ空间客户端进行分享
 *
 * @author zhoulu
 * @date 13-12-23
 */
public class QQzoneWeb extends QQConnect{
    public QQzoneWeb(Context context) {
        super(context);
    }

	@Override
	public boolean isSupportWeb() {
		return true;
	}

    @Override
    public int getIcon() {
        return ShareConfiguration.QQCONNECT.QQZONE_ICON_ID;
    }

    @Override
    public String getName() {
        return getString(ShareConfiguration.QQCONNECT.QQZONE_NAME_ID);
    }


	@Override
	public boolean onEvent(Context context, Intent intent) {
		String action = intent.getAction();
		boolean success = intent.getBooleanExtra(EXTREA_RESULT, false);
		if (ACTION_LOGIN.equals(action) && success) {
			doShare();
		}
		return super.onEvent(context, intent);
	}

	@Override
    public void onShare(IShareObject... shareObject) {
		//微博需要先进行登录验证
		if (!mTencent.isSessionValid()) {
			doLogin();
			return;
		}

		IShareObject object = shareObject[0];
		String imageUrls = "";
		if (object.getThumbnailUrl() != null) {
			for (String imageUrl : object.getThumbnailUrl()) {
				imageUrls += imageUrl + "|";
			}
		}

		Bundle parameters = new Bundle();
		parameters.putString("title", object.getTitle());
		parameters.putString("summary", object.getSecondTitle());
		parameters.putString("comment", object.getMessage());

		parameters.putString("url", object.getRedirectUrl());
		//所分享的网页资源的代表性图片链接"，对应上文接口说明的4。
		//请以http://开头，长度限制255字符。
		//多张图片以竖线（|）分隔，目前只有第一张图片有效，图片规格100*100为佳。
		parameters.putString("images", imageUrls);

		//分享内容的类型。4表示网页；5表示视频（type=5时，必须传入playurl）。
		parameters.putString("type", "4");

		parameters.putString("site", "百度音乐");
		// 分享的来源网站对应的网站地址url，
		// 对应上文接口说明中5的超链接。请以http://开头
		parameters.putString("fromurl", "http://music.baidu.com");

		//值为1时，表示分享不默认同步到微博，其他值或者不传此参数表示默认同步到微博。
		parameters.putString("nswb", "1");

		mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, parameters,
				Constants.HTTP_POST, new BlogApiListener(), null);
    }

    @Override
    public boolean validateCheck(IShareObject... shareObjects) {
        if (shareObjects == null || shareObjects.length == 0) {
            logE("数据为NULL或者空");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        for (IShareObject shareObject : shareObjects) {
            if (shareObject == null) {
                notifyEvent(getString(R.string.share_invalidate_datas));
                return false;
            }

            if (TextUtil.isEmpty(shareObject.getTitle())) {
                notifyEvent(getString(R.string.share_image_title_empty));
                return false;
            } else if(TextUtil.isEmpty(shareObject.getRedirectUrl())){
                notifyEvent(getString(R.string.share_image_invalid_detail_url));
                return false;
            } else {
                if (shareObject.getThumbnailUrl() != null) {
                    for (String imageUrl : shareObject.getThumbnailUrl()) {
                        if (TextUtil.isEmpty(imageUrl) || !URIUtil.isValidHttpUri(imageUrl)) {
                            notifyEvent(getString(R.string.share_image_empty));
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

	private class BlogApiListener implements IRequestListener {

		@Override
		public void onComplete(final JSONObject response, Object state) {
			try {
				int ret = response.getInt("ret");
				if (ret == 0) {
					performShare(true, getString(R.string.share_status_success));
				} else if (ret == 100030) {
					performShare(false, getString(R.string.share_errcode_auth_timeout));
				} else if (ret == 100031) {
					performShare(false, getString(R.string.share_errcode_api_unsupport));
				}
			} catch (JSONException e) {
			}
		}

		@Override
		public void onIOException(final IOException e, Object state) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onMalformedURLException(final MalformedURLException e,
											Object state) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onJSONException(final JSONException e, Object state) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException e,
											  Object arg1) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException e,
											 Object arg1) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onUnknowException(Exception e, Object arg1) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException e, Object arg1) {
			performShare(false, getString(R.string.share_status_fail));
		}

		@Override
		public void onHttpStatusException(HttpStatusException e, Object o) {
			performShare(false, getString(R.string.share_status_fail));
		}
	}
}
