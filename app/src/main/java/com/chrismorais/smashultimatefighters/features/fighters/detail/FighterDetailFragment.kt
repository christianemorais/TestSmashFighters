package com.chrismorais.smashultimatefighters.features.fighters.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.chrismorais.smashultimatefighters.R
import com.chrismorais.smashultimatefighters.features.fighters.FightersListViewModel
import com.google.android.material.appbar.AppBarLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FighterDetailFragment : Fragment() {

    private val fightersViewModel: FightersListViewModel by sharedViewModel()

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fighter_detail_fragment, container, false)

        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.title = ""

        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val appBar = view.findViewById<AppBarLayout>(R.id.appbar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset != 0) {
                view.findViewById<ConstraintLayout>(R.id.cl_header).visibility = View.GONE
            } else {
                view.findViewById<ConstraintLayout>(R.id.cl_header).visibility = View.VISIBLE
            }
        }
        )
        setupObservers()

        return view
    }

    private fun setupObservers() {
        fightersViewModel.selectedFighter.observe(viewLifecycleOwner) { fighter ->
            fighter?.let {
                view?.findViewById<TextView>(R.id.tv_fighter_name)?.text = it.name
                view?.findViewById<TextView>(R.id.tv_fighter_universe)?.text = it.universe
                view?.findViewById<RatingBar>(R.id.rb_stars)?.rating = it.rate.toFloat()
                view?.findViewById<TextView>(R.id.tv_price)?.text =
                    getString(R.string.price_format, it.price)
                view?.findViewById<TextView>(R.id.tv_fighter_downloads)?.text =
                    getString(R.string.downloads_count, it.downloads)

                view?.findViewById<TextView>(R.id.tv_description)?.text = it.description

                val ivFighter: ImageView = view?.findViewById(R.id.iv_header_fighter)!!
                Glide.with(ivFighter).load(it.imageURL).into(ivFighter)
            }
        }
    }
}