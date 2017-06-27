package com.osmanak.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();

        //Retrieves photo from intent
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null){
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);

            //Grabs resources from resource files
            Resources resources = getResources();

            //Substitutes placeholder text with the String resource
            photoTitle.setText(resources.getString(R.string.photo_title_text, photo.getTitle()));

            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this)
                    //Loads an image from a URL, and stores it in the image field of the Photo object
                    .load(photo.getLink())
                    //Sets placeholder image to be used if there is an error
                    .error(R.drawable.placeholder)
                    //Sets placeholder image to be used while image is downloading
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}
