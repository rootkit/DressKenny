package com.fcdream.dress.kenny.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcdream.dress.kenny.App;
import com.fcdream.dress.kenny.BaseFragment;
import com.fcdream.dress.kenny.R;
import com.fcdream.dress.kenny.bus.TestBus;
import com.fcdream.dress.kenny.ioc.BindLayout;
import com.fcdream.dress.kenny.ioc.BindView;
import com.fcdream.dress.kenny.player.XulMediaPlayer;
import com.fcdream.dress.kenny.speech.BaseSpeech;
import com.fcdream.dress.kenny.utils.MessageUtils;

import java.lang.ref.WeakReference;

/**
 * Created by shmdu on 2017/8/31.
 */

@BindLayout(layout = R.layout.fragment_robot)
public class RobotFragment extends BaseMainPageFragment {

    private static final String TAG = RobotFragment.class.getSimpleName();

    @BindView(id = R.id.robot_tip)
    private TextView robotTipText;

    @BindView(id = R.id.robot_mouth)
    private ImageView robotMouthImage;

    @BindView(id = R.id.robot_mic, click = true, clickEvent = "onRobotMicClick")
    private ImageView robotMicImage;

    private AnimationDrawable robotMouthAnim;

    private String vodTipPath = "android.resource://" + App.getAppInstance().getApplicationContext().getPackageName() + "/" + R.raw.robot_tip_voice;

    @Override
    protected void initView(Activity activity, View sourceView) {
        robotMouthAnim = (AnimationDrawable) robotMouthImage.getDrawable();
        robotTipText.setEllipsize(null);
    }

    @Override
    protected void initData(Activity activity) {
        App.postDelayToMainLooper(new Runnable() {
            @Override
            public void run() {
                //TestBus.testSearch();
                //dealStartTips();
            }
        }, 5 * 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ifaceReference != null && ifaceReference.get() != null) {
            ifaceReference.get().getSpeech().setSpeechListener(this);
            ifaceReference.get().getMediaPlayer().setEventListener(this);
            ifaceReference.get().getSpeech().setSpeechListener(this);
        }
    }

    public void onRobotMicClick(View view) {

    }

    @Override
    public String getFragmentType() {
        return BaseMainFragmentIface.TYPE_ROBOT;
    }

    @Override
    public void onListenError(String errorInfo) {
        super.onListenError(errorInfo);
        MessageUtils.showToast(getActivity(), "抱歉，我没听懂你在说什么！！！");
    }

    @Override
    public void onListenEnd(String info) {
        super.onListenEnd(info);
        if (isFragmentIfaceValid()) {
            ifaceReference.get().handleSpeech(info);
        }
    }

    private void dealStartTips() {
        robotMouthAnim.start();
        robotTipText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        if (isFragmentIfaceValid()) {
            XulMediaPlayer mediaPlayer = ifaceReference.get().getMediaPlayer();
            mediaPlayer.stop();
            mediaPlayer.open(vodTipPath);
            mediaPlayer.play();
        }
    }

    private void dealEndTips() {
        robotMouthAnim.stop();
        robotTipText.setEllipsize(null);
    }

    private void dealStartSpeech() {
        if (isFragmentIfaceValid()) {
            ifaceReference.get().getSpeech().start();
        }
    }

    private void dealEndSpeech() {
        if (isFragmentIfaceValid()) {
            ifaceReference.get().getSpeech().stop();
        }
    }

    @Override
    public boolean onError(XulMediaPlayer xmp, int code, String msg) {
        dealStartSpeech();
        return true;
    }

    @Override
    public boolean onComplete(XulMediaPlayer xmp) {
        dealStartSpeech();
        if (xmp != null) {
            xmp.stop();
        }
        return true;
    }
}
