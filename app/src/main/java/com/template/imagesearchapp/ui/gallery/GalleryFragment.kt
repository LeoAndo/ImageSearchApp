package com.template.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.template.imagesearchapp.R
import com.template.imagesearchapp.databinding.FragmentGalleyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_galley) {
    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: FragmentGalleyBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleyBinding.bind(view)

        val adapter = UnsplashPhotoAdapter {
            val action = GalleryFragmentDirections.actionGalleryFragment2ToDetailsFragment(it)
            findNavController().navigate(action)
        }
        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading && it.source.append.endOfPaginationReached && adapter.itemCount < 1) {
                Toast.makeText(requireContext(), "empty list.", Toast.LENGTH_SHORT).show()
            }
        }



        binding.list.apply {
            layoutManager =
                FlexboxLayoutManager(requireContext()) // default: FlexDirection.ROW, FlexWrap.WRAP, AlignItems.STRETCH
            setHasFixedSize(true) // adapterの内容によってrecyclerViewのサイズに変動がない場合は trueにする.
            this.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() },
            )
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.photos.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }

        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)

        val clearItem = menu.findItem(R.id.action_clear)
        clearItem.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                viewModel.clearList()
                return true
            }
        })

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query ?: return true

                binding.list.scrollToPosition(0)
                viewModel.searchPhotos(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }
}