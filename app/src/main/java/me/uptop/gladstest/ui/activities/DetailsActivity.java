package me.uptop.gladstest.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.uptop.gladstest.R;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.second_textview_title)
    TextView titleView;
//    @BindView(R.id.second_textview_pubdate)
//    TextView dateView;
//    @BindView(R.id.second_btn_guid)
//    Button guidBtn;
    @BindView(R.id.second_imageview)
    ImageView pictureView;
    @BindView(R.id.second_textview_articletext)
    TextView articleView;
    @BindView(R.id.details_textview_votescount)
    TextView votesView;
    @BindView(R.id.webView)
    WebView webView;

    String itemTitle, itemLink, itemDesc, itemPicture;
    int itemVotesCount;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        initToolbar();

        itemTitle = getIntent().getExtras().getString("title");
        itemLink = getIntent().getExtras().getString("redirectUrl");
        itemDesc = getIntent().getExtras().getString("desc");
        itemPicture = getIntent().getExtras().getString("screenshot");
        itemVotesCount = getIntent().getExtras().getInt("votesCount");


        if(itemPicture == null) {
            pictureView.setImageDrawable(getDrawable(R.drawable.cap));
        } else {
            Picasso.with(getApplicationContext()).load(itemPicture).into(pictureView);
        }
        titleView.setText(itemTitle);
        articleView.setText(itemDesc);
        votesView.setText("Votes count: "+itemVotesCount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startMainActivity(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.second_btn_guid)
    public void clickToUrl() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(itemLink);
    }
}
