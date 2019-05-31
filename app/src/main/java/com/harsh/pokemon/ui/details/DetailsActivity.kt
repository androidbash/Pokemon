package com.harsh.pokemon.ui.details

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.harsh.pokemon.R
import com.harsh.pokemon.api.WebService
import com.harsh.pokemon.model.PokemonDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.contentLoading


class DetailsActivity : AppCompatActivity() {

    private val webService: WebService by lazy {
        WebService.create()
    }
    var compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportPostponeEnterTransition()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val imageTransitionName = intent.getStringExtra("image_transition_name")
            pokeImage.transitionName = imageTransitionName
        }

        contentLoading.visibility = View.VISIBLE

        toolbar.setNavigationOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }

        val url = intent.getStringExtra("url") ?: ""

        supportStartPostponedEnterTransition()
        Glide.with(this).load(intent.getStringExtra("imageUrl") ?: "").into(pokeImage)

        compositeDisposable.add(
                webService.getPokemonDetails(url)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<PokemonDetails>() {
                            override fun onSuccess(details: PokemonDetails) {
                                contentLoading.visibility = View.GONE

                                tvName.text = details.name
                                tvHeight.text = details.height.toString()
                                tvOrder.text = details.order.toString()
                                tvWeight.text = details.weight.toString()
                            }

                            override fun onError(e: Throwable) {

                                Toast.makeText(this@DetailsActivity, e.message, Toast.LENGTH_SHORT).show()
                            }

                        })
        )

    }

}
