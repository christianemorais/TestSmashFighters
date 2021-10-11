package com.chrismorais.smashultimatefighters.features.fighters.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chrismorais.smashultimatefighters.R
import com.chrismorais.smashultimatefighters.features.fighters.FightersListViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterFragment : Fragment() {

    private val fightersViewModel: FightersListViewModel by sharedViewModel()

    private lateinit var toolbar: Toolbar
    private lateinit var rgbSortedBy: RadioGroup
    private lateinit var rsPrice: RangeSlider
    private lateinit var rbStars: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initView()
    }

    override fun onResume() {
        super.onResume()

        initValues()
    }

    private fun initToolbar() {
        toolbar = requireView().findViewById(R.id.toolbar) as Toolbar

        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initView() {
        rgbSortedBy = requireView().findViewById(R.id.rg_sort)
        rgbSortedBy.setOnCheckedChangeListener { _, checkedId ->
            fightersViewModel.sortedBy = when (checkedId) {
                R.id.rb_descending -> SortedBy.DESCENDING
                R.id.rb_rate -> SortedBy.RATE
                R.id.rb_downloads -> SortedBy.DOWNLOADS
                else -> SortedBy.ASCENDING
            }
        }

        rsPrice = requireView().findViewById(R.id.rs_price)
        rsPrice.addOnChangeListener { _, _, _ ->
            fightersViewModel.priceRange = rsPrice.values
        }

        rbStars = requireView().findViewById(R.id.rb_stars)
        rbStars.setOnRatingBarChangeListener { _, rating, _ ->
            fightersViewModel.stars = rating.toInt()
        }

        requireView().findViewById<MaterialButton>(R.id.bt_apply_filters).setOnClickListener {
            fightersViewModel.setShouldFilters(true)

            findNavController().popBackStack()
        }
    }

    private fun initValues() {
        when (fightersViewModel.sortedBy) {
            SortedBy.ASCENDING -> rgbSortedBy.check(R.id.rb_ascending)
            SortedBy.DESCENDING -> rgbSortedBy.check(R.id.rb_descending)
            SortedBy.RATE -> rgbSortedBy.check(R.id.rb_rate)
            SortedBy.DOWNLOADS -> rgbSortedBy.check(R.id.rb_downloads)
        }

        rsPrice.values = fightersViewModel.priceRange ?: listOf(0.0f, 999.9f)
        rbStars.rating = fightersViewModel.stars?.toFloat() ?: 3f
    }
}