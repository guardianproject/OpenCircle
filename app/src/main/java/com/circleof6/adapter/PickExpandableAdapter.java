package com.circleof6.adapter;


import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.model.CampusLang;
import com.circleof6.model.CollegeCountry;
import com.circleof6.util.Constants;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PickExpandableAdapter extends ExpandableRecyclerAdapter<PickExpandableAdapter.CollegeViewHolder, PickExpandableAdapter.CampusViewHolder> {

    Context context;
    OnChildItemsAdded onChildItemsAdded;
    OnItemClickListener onItemClickListener;
    List<ParentObject> parentObjectList;

    public PickExpandableAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        this.context = context;
        onChildItemsAdded = null;
        onItemClickListener = null;
        parentObjectList = parentItemList;

        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                onChildItemsAdded.onChildItemsAdded(positionStart, itemCount);
            }
        });
    }

    @Override
    public CollegeViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View collegeView = LayoutInflater.from(context)
                                         .inflate(R.layout.item_college, viewGroup, false);
        return new CollegeViewHolder(collegeView);
    }

    @Override
    public CampusViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View campusView = LayoutInflater.from(context)
                                        .inflate(R.layout.item_campus, viewGroup, false);
        return new CampusViewHolder(campusView);
    }

    @Override
    public void onBindParentViewHolder(CollegeViewHolder collegeViewHolder, int i, Object o) {
        CollegeCountry collegeCountry = (CollegeCountry)o;
        collegeViewHolder.setCollegeImage(collegeCountry.getLogoUrl());
        collegeViewHolder.setCollegeName(collegeCountry.getCollegeName());
        collegeViewHolder.showImageArrowExpandable(collegeCountry.hasCampuses());
        collegeViewHolder.setCollegeCountry(collegeCountry);
    }

    @Override
    public void onBindChildViewHolder(CampusViewHolder campusViewHolder, int i, Object o) {
        CampusLang campusLang = (CampusLang)o;
        campusViewHolder.setCampusName(campusLang.getCampusName());
        campusViewHolder.setCampusLang(campusLang);
    }

    public void setOnChildItemsAdded(OnChildItemsAdded onChildItemsAdded) {
        this.onChildItemsAdded = onChildItemsAdded;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnChildItemsAdded
    {
        void onChildItemsAdded(int position, int childListSize);
    }

    public interface OnItemClickListener
    {
        void onChildItemClicked(CampusLang campusLang);

        void onParentItemClicked(CollegeCountry collegeCountry);
    }

    public class CollegeViewHolder extends ParentViewHolder
    {
        CollegeCountry mCollegeCountry;
        NetworkImageView      collegeImage;
        TextView       collegeName;
        ImageView      imageArrowExpandable;
        private boolean isExpandable;

        public CollegeViewHolder(View itemView)
        {
            super(itemView);
            collegeImage = (NetworkImageView) itemView.findViewById(R.id.image_college);
            collegeName = (TextView) itemView.findViewById(R.id.text_college_name);
            imageArrowExpandable = (ImageView) itemView.findViewById(R.id.image_arrow_expandable);
            isExpandable = false;
        }

        @Override
        public void onClick(View v)
        {
            super.onClick(v);
            if(! mCollegeCountry.hasCampuses())
            {
                onItemClickListener.onParentItemClicked(mCollegeCountry);
            }
            else
            {
                isExpandable = ! isExpandable;
                if(isExpandable)
                {
                    rotateArrowExpandable(0.0f, 90.0f);
                }
                else
                {
                    rotateArrowExpandable(90.0f, 0.0f);
                }
            }
        }

        private void rotateArrowExpandable(float fromDegrees, float toDegrees)
        {
            RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees,
                                                                  Animation.RELATIVE_TO_SELF, 0.5f,
                                                                  Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(Constants.DEFAULT_DURATION_ANIMATION);
            imageArrowExpandable.startAnimation(rotateAnimation);
        }


        /*public void setCollegeImage(@DrawableRes String imageId)
        {
            Picasso.with(context).load(imageId).resizeDimen(R.dimen.size_ic_college_selection,
                                                            R.dimen.size_ic_college_selection)
                   .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE).centerInside()
                   .into(collegeImage);
        }*/
        public void setCollegeImage(String logoUrl){
            ImageLoader imageLoader = CircleOf6Application.getInstance().getImageLoader();

            // If you are using NetworkImageView
            collegeImage.setImageUrl(logoUrl, imageLoader);
        }

        public void setCollegeName(String name)
        {
            collegeName.setText(name);


        }

        public void showImageArrowExpandable(boolean hasCampus)
        {
            if(hasCampus)
            {
                imageArrowExpandable.setVisibility(View.VISIBLE);
            }
            else
            {
                imageArrowExpandable.setVisibility(View.GONE);
            }
        }

        public void setCollegeCountry(CollegeCountry collegeCountry)
        {
            this.mCollegeCountry = collegeCountry;
        }
    }

    public class CampusViewHolder extends ChildViewHolder
    {
        CampusLang mCampusLang;
        TextView   campusName;

        public CampusViewHolder(View itemView)
        {
            super(itemView);
            campusName = (TextView) itemView.findViewById(R.id.text_campus_name);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(onItemClickListener != null)
                    {
                        onItemClickListener.onChildItemClicked(mCampusLang);
                    }

                }
            });
        }

        public void setCampusLang(CampusLang campusLang)
        {
            this.mCampusLang = campusLang;
        }

        public void setCampusName(String name)
        {
            campusName.setText(name);
        }
    }
}
