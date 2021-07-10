package pl.janowicz.githubapidemo.features.userlist.ui

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pl.janowicz.githubapidemo.databinding.ItemUserBinding
import pl.janowicz.githubapidemo.features.userlist.repository.User
import pl.janowicz.githubapidemo.utils.extensions.getLayoutInflater

class UsersAdapter(
    private val onClickListener: (User) -> Unit
) : ListAdapter<User, UsersAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UsersAdapter.ViewHolder(
        ItemUserBinding.inflate(parent.getLayoutInflater(), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        with(holder) {
            avatarImageView.load(user.avatarUrl)
            nameTextView.text = user.login
            itemView.setOnClickListener {
                onClickListener(user)
            }
        }
    }

    class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val avatarImageView: ImageView = binding.avatarImageView
        val nameTextView: TextView = binding.nameTextView
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem

    }
}