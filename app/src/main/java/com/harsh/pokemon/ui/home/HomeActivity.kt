package com.harsh.pokemon.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.harsh.pokemon.Injection
import com.harsh.pokemon.R
import com.harsh.pokemon.data.RecylcerViewItemClickListener
import com.harsh.pokemon.model.Pokemon
import com.harsh.pokemon.ui.details.DetailsActivity
import com.harsh.pokemon.util.EndlessRecyclerOnScrollListener
import com.harsh.pokemon.util.GridItemDecoration
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity(), RecylcerViewItemClickListener {
    override fun onItemClick(pokemon: Pokemon, shareImageView: ImageView) {

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("url", pokemon.url)
        intent.putExtra("imageUrl", pokemon.imageUrl)
        intent.putExtra("image_transition_name", "profile")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                shareImageView,
                "profile")

        startActivity(intent, options.toBundle())

    }

    private lateinit var viewModel: PokemonRepositoriesViewModel
    private val adapter = PokemonAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(PokemonRepositoriesViewModel::class.java)

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.isAutoMeasureEnabled = true

        val spacing = resources.getDimensionPixelSize(R.dimen.show_grid_spacing)

        list.apply {
            addItemDecoration(GridItemDecoration(spacing, 2))
            this.layoutManager = layoutManager
            setHasFixedSize(true)
        }

        list.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore() {
                viewModel.listScrolled()
                bottomLoading.visibility = View.VISIBLE
            }
        })

        showEmptyList(false)

        contentLoading.visibility = View.VISIBLE
        bottomLoading.visibility = View.GONE

        initAdapter()

        viewModel.getPokemon()
    }

    private fun initAdapter() {
        list.adapter = adapter
        viewModel.repos.observe(this, Observer<List<Pokemon>> {
            Log.d("Activity", "list: ${it?.size}")
            it?.let { list ->
                if (list.isNotEmpty()) {
                    contentLoading.visibility = View.GONE
                    bottomLoading.visibility = View.GONE
                }
            }
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
    }


    private fun updateList() {
        list.scrollToPosition(0)
        viewModel.getPokemon()
        adapter.submitList(null)
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }
}
