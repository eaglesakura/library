package com.eaglesakura.lib.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaglesakura.lib.android.test.activity.BaseActivity;
import com.eaglesakura.lib.android.ui.twitter.TwitterLoginActivity;

public class TwitterOAuthTestActivity extends BaseActivity {
    TextView screenName = null;
    TextView token = null;
    TextView tokenSecret = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        button.setText("ログイン");
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        token = new TextView(this);
        tokenSecret = new TextView(this);
        screenName = new TextView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(button);
        layout.addView(screenName);
        layout.addView(token);
        layout.addView(tokenSecret);

        setContentView(layout);
    }

    void startLogin() {
        final String eConsumerKey = "wb9jTlAQ0tkaUHghDZBsiw";
        final String eConsumerSecret = "KNFlLAPQ3gw4Xo7ug0W1ul02JAVpVlNMuS3K94";
        final String eCallbackURL = "http://eaglesakura.com/oauth";

        Intent intent = new Intent(this, TwitterLoginActivity.class);
        intent.putExtra(TwitterLoginActivity.INTENT_CONSUMER_KEY, eConsumerKey);
        intent.putExtra(TwitterLoginActivity.INTENT_CONSUMER_SECRET, eConsumerSecret);
        intent.putExtra(TwitterLoginActivity.INTENT_CALLBACK_URL, eCallbackURL);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == TwitterLoginActivity.RESULT_FAILED) {
            Toast.makeText(this, "ログインに失敗しました", Toast.LENGTH_SHORT).show();
        } else if (resultCode == TwitterLoginActivity.RESULT_OK) {
            Toast.makeText(this, "ログインに成功しました", Toast.LENGTH_SHORT).show();
            screenName.setText(data.getStringExtra(TwitterLoginActivity.INTENT_RESULT_TWITTER_USER_SCREEN_NAME));
            token.setText("token :: " + data.getStringExtra(TwitterLoginActivity.INTENT_RESULT_TWITTER_TOKEN));
            tokenSecret.setText("secret ::" + data.getStringExtra(TwitterLoginActivity.INTENT_RESULT_TWITTER_TOKEN_SECRET));
        } else if (resultCode == TwitterLoginActivity.RESULT_CANCELED) {
            Toast.makeText(this, "ログインをキャンセルしました", Toast.LENGTH_SHORT).show();
        }
    }
}
