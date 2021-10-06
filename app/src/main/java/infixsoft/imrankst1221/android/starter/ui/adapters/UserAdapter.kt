package infixsoft.imrankst1221.android.starter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.databinding.ItemUserBinding

/**
 * @author imran.choudhury
 * 6/10/21
 */

class UserAdapter: RecyclerView.Adapter<UserAdapter.AdapterViewHolder>() {
    inner class AdapterViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((User) -> Unit)? = null

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val user = differ.currentList[position]
        with(holder) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivUserProfile)

            binding.tvUserId.text = user.login
            //TODO set user details
            binding.tvUserDetails.text = user.login
        }

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let {
                    it(user)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}