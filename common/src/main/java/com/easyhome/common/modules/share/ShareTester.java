package com.easyhome.common.modules.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.easyhome.common.modules.share.logic.ShareManager;
import com.easyhome.common.modules.share.model.IShareObject;
import com.easyhome.common.modules.share.model.ShareAudio;
import com.easyhome.common.modules.share.model.ShareImage;
import com.easyhome.common.modules.share.model.ShareText;
import com.easyhome.common.modules.share.model.ShareVideo;
import com.easyhome.common.modules.share.model.ShareWebpage;
import com.easyhome.sample.R;


/**
 * 分享测试数据
 *
 * @author zhoulu
 * @date 13-12-15
 */
public class ShareTester {

    public static DataTest get(Context context, int option) {
        DataTest dataTest = null;
        switch (option) {
            case ShareManager.OPTION_WEICHAT_FRIENDS:
            case ShareManager.OPTION_WEICHAT:
                dataTest = new WeiChatTest(context);
                break;
            case ShareManager.OPTION_WEIBLOG:
                dataTest = new WeiBlogTest(context);
                break;
            case ShareManager.OPTION_QQWEIBO:
                dataTest = new QQWeiboTest(context);
                break;
            case ShareManager.OPTION_QQZONE:
                dataTest = new QQZoneTest(context);
                break;
            case ShareManager.OPTION_QQFIRENTS:
                dataTest = new QQFriendsTest(context);
                break;
        }
        return dataTest;
    }

    public static class QQFriendsTest implements DataTest {

        Context context;
        public QQFriendsTest(Context context) {
            this.context = context;
        }

        @Override
        public IShareObject getShareText() {
            ShareText text = new ShareText("Hello~ QQFriends!");
            text.setTitle("title");
            text.setSecondTitle("secondTitle");
            text.setRedirectUrl("http://www.baidu.com");
            return text;
        }

        @Override
        public IShareObject getShareImage() {
            ShareImage image = new ShareImage(new String[]{"/storage/emulated/0/tencent/ReaderZone/Adv/1228_m.png"});
            image.setTitle("title");
            image.setSecondTitle("secondTitle");
            image.setMessage("share a image");
            image.setRedirectUrl("http://www.baidu.com");
            return image;
        }

        @Override
        public IShareObject getShareAudio() {
            ShareAudio audio = new ShareAudio(null,"music-title", "music-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 0);
            audio.setMessage("share a audio");
            audio.setRedirectUrl("http://www.baidu.com");
            audio.setThumbnailUrls(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            return audio;
        }

        @Override
        public IShareObject getShareVidio() {
            ShareVideo video = new ShareVideo(null,"mv-title", "mv-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 0);
            video.setMessage("share a video");
            video.setRedirectUrl("http://www.baidu.com");
            video.setThumbnailUrls(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            return video;
        }

        @Override
        public IShareObject getShareWebpage() {
            ShareWebpage webpage = new ShareWebpage("http://music.baidu.com");
            webpage.setTitle("title");
            webpage.setSecondTitle("secondTitle");
            webpage.setMessage("share a webpage");
            webpage.setRedirectUrl("http://www.baidu.com");
            webpage.setThumbnailUrls(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            return webpage;
        }
    }

    public static class QQWeiboTest implements DataTest {

        Context context;
        public QQWeiboTest(Context context) {
            this.context = context;
        }

        @Override
        public IShareObject getShareText() {
            ShareText text = new ShareText("Hello~ QQWeibo!");
            text.setTitle("title");
            text.setSecondTitle("secondTitle");
            return text;
        }

        @Override
        public IShareObject getShareImage() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareImage image = new ShareImage(bitmap);
            image.setTitle("title");
            image.setSecondTitle("secondTitle");
            image.setMessage("share a image");
            image.setRedirectUrl("http://www.baidu.com");
            return image;
        }

        @Override
        public IShareObject getShareAudio() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareAudio audio = new ShareAudio(bitmap,"music-title", "music-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 0);
            audio.setMessage("share a audio");
            return audio;
        }

        @Override
        public IShareObject getShareVidio() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareVideo video = new ShareVideo(bitmap,"mv-title", "mv-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 0);
            video.setMessage("share a video");
            return video;
        }

        @Override
        public IShareObject getShareWebpage() {
            ShareWebpage webpage = new ShareWebpage("http://music.baidu.com");
            webpage.setTitle("title");
            webpage.setSecondTitle("secondTitle");
            webpage.setMessage("share a webpage");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            webpage.setThumbnail(bitmap);
            return webpage;
        }
    }

    public static class QQZoneTest implements DataTest {

        Context context;
        public QQZoneTest(Context context) {
            this.context = context;
        }

        @Override
        public IShareObject getShareText() {
            ShareText text = new ShareText("Hello~ QQZone!");
            text.setTitle("title");
            text.setSecondTitle("secondTitle");
            text.setRedirectUrl("http://music.baidu.com");
            return text;
        }

        @Override
        public IShareObject getShareImage() {
            ShareImage image = new ShareImage(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            image.setTitle("title");
            image.setSecondTitle("secondTitle");
            image.setMessage("share a image");
            image.setRedirectUrl("http://music.baidu.com");
            return image;
        }

        @Override
        public IShareObject getShareAudio() {
            ShareAudio audio = new ShareAudio(null,"music-title", "music-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 10);
            audio.setMessage("share a audio");
            audio.setRedirectUrl("http://music.baidu.com");
            audio.setThumbnailUrls(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            return audio;
        }

        @Override
        public IShareObject getShareVidio() {
            ShareVideo video = new ShareVideo(null,"mv-title", "mv-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 10);
            video.setMessage("share a video");
            video.setRedirectUrl("http://music.baidu.com");
            video.setThumbnailUrls(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            return video;
        }

        @Override
        public IShareObject getShareWebpage() {
            ShareWebpage webpage = new ShareWebpage("http://music.baidu.com");
            webpage.setTitle("title");
            webpage.setSecondTitle("secondTitle");
            webpage.setMessage("share a webpage");
            webpage.setRedirectUrl("http://music.baidu.com");
            webpage.setThumbnailUrls(new String[]{"http://img3.douban.com/lpic/s3635685.jpg"});
            return webpage;
        }
    }

    public static class WeiBlogTest implements DataTest {

        Context context;
        public WeiBlogTest(Context context) {
            this.context = context;
        }

        @Override
        public IShareObject getShareText() {
            ShareText text = new ShareText("Hello~ WeiBlog!");
            text.setTitle("title");
            text.setSecondTitle("secondTitle");
            return text;
        }

        @Override
        public IShareObject getShareImage() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareImage image = new ShareImage(bitmap);
            image.setTitle("title");
            image.setSecondTitle("secondTitle");
            image.setMessage("share a image");
            return image;
        }

        @Override
        public IShareObject getShareAudio() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareAudio audio = new ShareAudio(bitmap,"music-title", "music-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 10);
            audio.setMessage("share a audio");
            return audio;
        }

        @Override
        public IShareObject getShareVidio() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareVideo video = new ShareVideo(bitmap,"mv-title", "mv-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 10);
            video.setMessage("share a video");
            return video;
        }

        @Override
        public IShareObject getShareWebpage() {
            ShareWebpage webpage = new ShareWebpage("http://music.baidu.com");
            webpage.setTitle("title");
            webpage.setSecondTitle("secondTitle");
            webpage.setMessage("share a webpage");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            webpage.setThumbnail(bitmap);
            return webpage;
        }
    }

    public static class WeiChatTest implements DataTest {

        Context context;
        public WeiChatTest(Context context) {
            this.context = context;
        }

        @Override
        public IShareObject getShareText() {
            ShareText text = new ShareText("Hello~ WeiChat!");
            text.setTitle("title");
            text.setSecondTitle("secondTitle");
            return text;
        }

        @Override
        public IShareObject getShareImage() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareImage image = new ShareImage(bitmap);
            image.setTitle("title");
            image.setSecondTitle("secondTitle");
            return image;
        }

        @Override
        public IShareObject getShareAudio() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareAudio audio = new ShareAudio(bitmap,"music-title", "music-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 0);
            return audio;
        }

        @Override
        public IShareObject getShareVidio() {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            ShareVideo video = new ShareVideo(bitmap,"mv-title", "mv-description", "http://mediaurl", "http://lowmediaurl", "http://redirecturl", 0);
            return video;
        }

        @Override
        public IShareObject getShareWebpage() {
            ShareWebpage webpage = new ShareWebpage("http://music.baidu.com");
            webpage.setTitle("title");
            webpage.setSecondTitle("secondTitle");
            return webpage;
        }
    }

    public interface DataTest {

        IShareObject getShareText();
        IShareObject getShareImage();
        IShareObject getShareAudio();
        IShareObject getShareVidio();
        IShareObject getShareWebpage();
    }


}
