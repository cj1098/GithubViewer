package com.example.chris.githubviewer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.chris.githubviewer.model.GithubRepository
import com.squareup.picasso.Picasso

class RepositoryListAdapter(private val context: Context?, private val listener: ListFragment.OnRepositorySelected): RecyclerView.Adapter<RepositoryListAdapter.RepositoryListViewHolder>() {

    private var items: List<GithubRepository> = emptyList()

    fun setItems(items: List<GithubRepository>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RepositoryListViewHolder, position: Int) {
        holder.userName.text = items[position].owner.login
        holder.repositoryName.text = items[position].name
        holder.itemView.setOnClickListener{
            listener.onRepositoryItemSelected(items[position])
        }
        Picasso.get().load(items[position].owner.avatar_url).into(holder.userImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolder {
        return RepositoryListViewHolder(LayoutInflater.from(context).inflate(R.layout.github_list_item, parent, false))
    }

    class RepositoryListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val repositoryName: TextView = itemView.findViewById(R.id.user_repository_name)
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
    }
}