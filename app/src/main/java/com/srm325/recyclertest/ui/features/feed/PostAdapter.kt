package com.aliakberaakash.cutiehacksproject2020.ui.features.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aliakberaakash.cutiehacksproject2020.R
import com.aliakberaakash.cutiehacksproject2020.data.model.Post

class PostAdapter(var postList: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.single_post_item, parent, false)
        return PostViewHolder(view)

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.postImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cutie))
        holder.userName.text = postList[position].user.userName
        holder.description.text = postList[position].description
        holder.likesCount.text = postList[position].likes.size.toString()
    }

    override fun getItemCount() = postList.size
}