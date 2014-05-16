package com.easyhome.sample.animator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.easyhome.common.uikit.app.BaseActionBarActivity;
import com.easyhome.common.uikit.app.BaseFragment;
import com.easyhome.sample.R;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

/**
 * 动画
 *
 * @author by kevin on 14-5-16.
 */
public class AnimatorActivity extends BaseActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AnimatorFragment())
                    .commit();
        }
    }

    public static class AnimatorFragment extends BaseFragment {

        public static AnimatorFragment newInstance() {
            AnimatorFragment animatorFragment = new AnimatorFragment();
            return animatorFragment;
        }

        private Spring mButtonSpring;
        private boolean mBtnAnimatorMark;
        @Override
        public CharSequence getTitle() {
            return getString(R.string.title_section_animator);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mButtonSpring = getSprint();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_animator, container, false);
            return view;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            final Button button = (Button) view.findViewById(R.id.button_click);
            button.setText("Sprint Button");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBtnAnimatorMark) {
                        mButtonSpring.setEndValue(0);
                    } else {
                        mButtonSpring.setEndValue(1);
                    }
                    mBtnAnimatorMark = !mBtnAnimatorMark;
                }
            });
            double friction = mButtonSpring.getSpringConfig().friction;
            double tension = mButtonSpring.getSpringConfig().tension;
            Log.d("zl", "friction = " + friction);
            Log.d("zl", "tension = " + tension);
            SpringConfig config = new SpringConfig(200, 5);
            mButtonSpring.setSpringConfig(config);
            mButtonSpring.addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    super.onSpringUpdate(spring);
                    double value = spring.getCurrentValue();
                    float scale = (float) (1f - (value * 0.5f));
                    button.setScaleX(scale);
                    button.setScaleY(scale);
                }
            });
        }

        public Spring getSprint() {
            SpringSystem springSystem = SpringSystem.create();
            Spring spring = springSystem.createSpring();
            spring.setAtRest();
            return spring;
        }
    }

}
