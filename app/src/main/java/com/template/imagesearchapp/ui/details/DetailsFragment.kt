package com.template.imagesearchapp.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.template.imagesearchapp.R
import com.template.imagesearchapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val args by navArgs<DetailsFragmentArgs>()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        binding?.apply {
            val photo = args.photo
            this.photo = photo
            Glide.with(this@DetailsFragment)
                .load(photo.urls.full)
                .error(R.drawable.ic_error)
                .into(imageView)
            textViewShareSingleImage.apply {
                setOnClickListener {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, photo.urls.full)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "share image to...")
                    startActivity(shareIntent)
                }
                paint.isUnderlineText = true
            }
        }
    }
}