package infixsoft.imrankst1221.android.starter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.databinding.ItemUserBinding
import okhttp3.internal.notify
import android.graphics.ColorMatrixColorFilter

import android.graphics.ColorMatrix
import com.bumptech.glide.request.RequestOptions
import infixsoft.imrankst1221.android.starter.utilities.Constants


/**
 * @author imran.choudhury
 * 6/10/21
 *
 * UserAdapter recycler adapter for user list
 */

class UserAdapter: RecyclerView.Adapter<UserAdapter.AdapterViewHolder>(), Filterable {
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

    lateinit var mFullList: List<User>
    val differ = AsyncListDiffer(this, differCallback)

    fun submitList(users: List<User>) {
        differ.submitList(users)
        mFullList = users
        notifyItemChanged(users.size)
    }

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
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivUserProfile)

            // profile inverted logic
            if ((position + 1) % 4 == 0) {
                binding.ivUserProfile.colorFilter = ColorMatrixColorFilter(Constants.NEGATIVE)
            }else{
                binding.ivUserProfile.clearColorFilter()
            }

            // show note icon
            if(user.userNote.isNullOrEmpty()){
                binding.ivNote.visibility = View.GONE
            }else{
                binding.ivNote.visibility = View.VISIBLE
            }

            binding.tvUserId.text = user.login
            //TODO set user details
            //binding.tvUserDetails.text = user.login
        }

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let {
                    it(user)
                }
            }
        }
    }

    // filter for search data
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList: MutableList<User> = ArrayList()
                if (constraint == null || constraint.length === 0) {
                    filteredList.addAll(mFullList)
                } else {
                    val filterPattern: String = constraint.toString().trim()
                    for (store in mFullList) {
                        if (store.login.contains(filterPattern) ||
                            (store.userNote != null && store.userNote.toLowerCase().contains(filterPattern.toLowerCase()))) {
                            filteredList.add(store)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                differ.submitList(filterResults.values as MutableList<User>?)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // item click listener
    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }
}