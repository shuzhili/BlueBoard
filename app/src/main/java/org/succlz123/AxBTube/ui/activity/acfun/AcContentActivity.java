package org.succlz123.AxBTube.ui.activity.acfun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import org.succlz123.AxBTube.R;
import org.succlz123.AxBTube.bean.acfun.AcContentInfo;
import org.succlz123.AxBTube.support.adapter.acfun.fragment.AcContentFmAdapter;
import org.succlz123.AxBTube.support.helper.acfun.AcString;
import org.succlz123.AxBTube.support.utils.ViewUtils;
import org.succlz123.AxBTube.ui.activity.BaseActivity;
import org.succlz123.AxBTube.ui.activity.VideoPlayActivity;
import org.succlz123.AxBTube.ui.fragment.acfun.other.AcContentInfoFragment;
import org.succlz123.AxBTube.ui.fragment.acfun.other.AcContentReplyFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by succlz123 on 15/8/1.
 */
public class AcContentActivity extends BaseActivity {

    public static void startActivity(Context activity, String contentId) {
        Intent intent = new Intent(activity, AcContentActivity.class);
        intent.putExtra(AcString.CONTENT_ID, contentId);
        activity.startActivity(intent);
    }

    private String mContentId;
    private AcContentFmAdapter mAdapter;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.collapsing_toolbar_content)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.img_content_title)
    SimpleDraweeView mTitleImg;

    @Bind(R.id.tab_layout_content)
    TabLayout mTabLayout;

    @Bind(R.id.viewpager_content_info)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_activity_content);
        ButterKnife.bind(this);
        mContentId = getIntent().getStringExtra(AcString.CONTENT_ID);

        ViewUtils.setToolbar(AcContentActivity.this, mToolbar, true);
        mCollapsingToolbarLayout.setTitle("AC" + mContentId);

        mAdapter = new AcContentFmAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

//        View contentView = findViewById(android.R.id.content);
//        contentView.getViewTreeObserver()
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        if (mContentId != null) {
//                            for (int i = 0; i < mAdapter.getCount(); i++) {
//                                String tag = mAdapter.getFragmentTag(i);
//                                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//
//                                if (fragment instanceof AcContentInfoFragment) {
//                                    ((AcContentInfoFragment) fragment)
//                                            .onAcContentResult(mContentId);
//                                } else if (fragment instanceof AcContentReplyFragment) {
//                                    ((AcContentReplyFragment) fragment)
//                                            .onAcContentResult(mContentId);
//                                }
//                            }
//                        }
//                    }
//                });

        if (mContentId != null) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                Fragment fragment = (Fragment) mAdapter.instantiateItem(mViewPager, i);

                if (fragment instanceof AcContentInfoFragment) {

                    ((AcContentInfoFragment) fragment).onAcContentResult(mContentId);

                } else if (fragment instanceof AcContentReplyFragment) {

                    ((AcContentReplyFragment) fragment).onAcContentResult(mContentId);
                }

            }
        }

    }

    public void onFragmentBack(final AcContentInfo.DataEntity.FullContentEntity fullContentEntity) {
        //加载标题图片并点击播放默认第一个视频
        mTitleImg.setImageURI(Uri.parse(fullContentEntity.getCover()));
        mTitleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcContentInfo.DataEntity.FullContentEntity.VideosEntity videosEntity
                        = fullContentEntity.getVideos().get(0);

                VideoPlayActivity.startActivity(AcContentActivity.this,
                        String.valueOf(videosEntity.getVideoId()),
                        String.valueOf(videosEntity.getDanmakuId()),
                        videosEntity.getSourceId(),
                        videosEntity.getType());
            }
        });
//        mCollapsingToolbarLayout.setTitle("AC" + fullContentEntity.getContentId());
//        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
