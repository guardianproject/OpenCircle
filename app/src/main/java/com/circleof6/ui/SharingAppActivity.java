package com.circleof6.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.circleof6.R;


public class SharingAppActivity extends AppCompatActivity implements View.OnClickListener
{

    public static final  String LOG_TAG                = SharingAppActivity.class.getSimpleName();
    public static final  int    REQUEST_CODE           = 1001;
    public static final  String PACKAGE_NAME_TWITTER   = "com.twitter.android";
    public static final  String PACKAGE_NAME_INSTAGRAM = "com.instagram.android";
    private static final String PACKAGE_NAME_FACEBOOK  = "com.facebook.katana";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_app);
        setupButtons();

    }

    private void setupButtons()
    {
        findViewById(R.id.fabshareFacebook).setOnClickListener(this);
        findViewById(R.id.fabshareTwitter).setOnClickListener(this);
        findViewById(R.id.fabshareInstagram).setOnClickListener(this);
        findViewById(R.id.fabshareDefault).setOnClickListener(this);
        findViewById(R.id.buttonNoThanks).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.fabshareFacebook:
                sharingByFacebook();
                break;
            case R.id.fabshareTwitter:
                sharingByTwitter();
                break;
            case R.id.fabshareInstagram:
                sharingByInstagram();
                break;
            case R.id.fabshareDefault:
                sharingByDefault();
                break;
            case R.id.buttonNoThanks:
                finish();
                break;
        }
    }


    private void sharingByFacebook()
    {

        if(isPackageInstalled(PACKAGE_NAME_FACEBOOK))
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage(PACKAGE_NAME_FACEBOOK);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_app));
            startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            Toast.makeText(this, R.string.error_share_facebook, Toast.LENGTH_LONG).show();
        }
    }

    private void sharingByTwitter()
    {

        if(isPackageInstalled(PACKAGE_NAME_TWITTER))
        {
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");
            intent.setPackage(PACKAGE_NAME_TWITTER);
            intent.putExtra(Intent.EXTRA_TEXT, createTexShare());
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, createUri());

            startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            Toast.makeText(this, R.string.error_share_twitter, Toast.LENGTH_LONG).show();
        }

    }

    private void sharingByInstagram()
    {

        if(isPackageInstalled(PACKAGE_NAME_INSTAGRAM))
        {
            Intent intent = new Intent();
            intent.setPackage(PACKAGE_NAME_INSTAGRAM);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, createUri());

            startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            Toast.makeText(this, R.string.error_share_instagram, Toast.LENGTH_LONG).show();
        }
    }

    private void sharingByDefault()
    {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_TEXT, createTexShare());
        startActivityForResult(Intent.createChooser(sharingIntent, getString(R.string.shared_app)),
                               REQUEST_CODE);


    }

    private Uri createUri()
    {
        return Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image_sharing);
    }


    private String createTexShare()
    {
        return getString(R.string.text_share_app) + "\n" + getString(R.string.link_share_app);
    }


    private boolean isPackageInstalled(String packageName)
    {
        try
        {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, FinishSharingActivity.class);
        startActivity(intent);
        finish();
    }
}
