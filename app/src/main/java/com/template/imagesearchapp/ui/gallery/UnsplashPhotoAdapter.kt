package com.template.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .error(R.drawable.ic_error)
                    .into(imageView)

                textViewUserName.text = photo.user.username

                Glide.with(itemView)
                    .load(photo.user.profileImage.small)
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .error(R.drawable.ic_error)
                    .into(imageAvatar)
            }
        }
    }


    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnSplashPhoto>() {
            // oldItemとnewItemの値が同じ場合にtrue
            override fun areItemsTheSame(oldItem: UnSplashPhoto, newItem: UnSplashPhoto): Boolean =
                oldItem.id == newItem.id

            // oldItemとnewItemが同じ要素を参照している場合にtrue
            override fun areContentsTheSame(
                oldItem: UnSplashPhoto,
                newItem: UnSplashPhoto
            ) = oldItem == newItem
        }
    }
}