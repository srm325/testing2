package com.aliakberaakash.cutiehacksproject2020.ui.features.feed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aliakberaakash.cutiehacksproject2020.R

class PostViewHolder(item : View) : RecyclerView.ViewHolder(item){
    var postImage : ImageView = item.findViewById(R.id.post_image)
    var likesCount : TextView = item.findViewById(R.id.likes_count)
    var profileImage : ImageView = item.findViewById(R.id.profile_image)
    var userName : TextView = item.findViewById(R.id.user_name)
    var description : TextView = item.findViewById(R.id.description)
}