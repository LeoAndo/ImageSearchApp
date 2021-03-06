package com.template.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.template.imagesearchapp.R
import com.template.imagesearchapp.data.UnSplashPhoto
import com.template.imagesearchapp.databinding.ItemUnsplashPhotoBinding

class UnsplashPhotoAdapter(private val onItemClick: (UnSplashPhoto) -> Unit) :
    PagingDataAdapter<UnSplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    inner class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position) ?: return@setOnClickListener
                    onItemClick(item)
                }
            }
        }

        fun bind(photo: UnSplashPhoto) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(imageView)

                textViewId.text = """id: ${photo.id}"""
                textViewUsername.text = """name: ${photo.user.username}"""
                textLikes.text = """likes: ${photo.likes}"""
                Glide.with(itemView)
                    .load(photo.user.profileImage.small)
                    .placeholder(R.drawable.ic_user)
                    .into(imageAvatar)

                // FlexboxLayoutManager では xml ではなく、
                // FlexboxLayoutManager.LayoutParams を使用して RecyclerView の各要素の属性を設定する.
                val lp = itemView.layoutParams
                if (lp is FlexboxLayoutManager.LayoutParams) {
                    lp.flexGrow = 1.0f
                }
            }
        }
    }


    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnSplashPhoto>() {
            // oldItemとnewItemの値が同じ場合にtrue (同値比較)
            override fun areItemsTheSame(oldItem: UnSplashPhoto, newItem: UnSplashPhoto): Boolean =
                oldItem.id == newItem.id

            // oldItemとnewItemが同じ要素を参照している場合にtrue (同一比較)
            override fun areContentsTheSame(
                oldItem: UnSplashPhoto,
                newItem: UnSplashPhoto
            ) = oldItem == newItem
        }
    }
}