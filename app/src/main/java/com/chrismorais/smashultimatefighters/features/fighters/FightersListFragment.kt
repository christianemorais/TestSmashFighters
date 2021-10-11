package com.chrismorais.smashultimatefighters.features.fighters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chrismorais.smashultimatefighters.R
import com.chrismorais.smashultimatefighters.features.fighters.filter.SortedBy
import com.chrismorais.smashultimatefighters.data.repository.model.Fighter
import com.chrismorais.smashultimatefighters.data.repository.model.Universe
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FightersListFragment : Fragment() {

    private val fightersViewModel: FightersListViewModel by sharedViewModel()

    private val adapter: FightersAdapter =
        FightersAdapter(mutableListOf(), object : FightersAdapter.OnClickListener {
            override fun itemClick(fighter: Fighter) {
                fightersViewModel.selectFighter(fighter)

                findNavController().navigate(R.id.action_fightersListFragment_to_fighterDetailFragment)
            }
        })

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var chipUniverses: ChipGroup
    private lateinit var tvFightersCount: TextView
    private lateinit var pbLoading: LinearProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fighters_list_fragment, container, false)
        setHasOptionsMenu(true)

        setupObservers()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initToolbar()

        if (fightersViewModel.listFighters.value.isNullOrEmpty()) {
            fightersViewModel.getUniverses()
            fightersViewModel.getAllFighters()
        }

        setupSwipeToRefresh()
    }

    override fun onResume() {
        super.onResume()

        setupFightersCount()
        selectUniverse()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                fightersViewModel.setShouldFilters(false)
                findNavController().navigate(R.id.action_fightersListFragment_to_filterFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupObservers() {
        fightersViewModel.listUniverse.observe(viewLifecycleOwner, { result ->
            result?.let {
                loadChipUniverses(it)
            }
        })

        fightersViewModel.listFighters.observe(viewLifecycleOwner, { result ->
            result?.let {
                loadFighters(it)
            }
        })

        fightersViewModel.shouldFilter.observe(viewLifecycleOwner, {
            if (it == true) {
                fightersViewModel.filterPriceRange()
                fightersViewModel.filterRatingStars()
                fightersViewModel.orderBy()
            }
        })

    }

    private fun initViews() {
        toolbar = requireView().findViewById(R.id.toolbar) as Toolbar

        pbLoading = requireView().findViewById(R.id.pb_loading)

        chipUniverses = requireView().findViewById(R.id.cg_universes)

        recyclerView = requireView().findViewById(R.id.rv_fighters)
        recyclerView.adapter = adapter

        swipe = requireView().findViewById(R.id.srl_update)

        tvFightersCount = requireView().findViewById(R.id.tv_fighters_count)
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupSwipeToRefresh() {
        swipe.setOnRefreshListener {

            clearFilters()

            fightersViewModel.getAllFighters()
            fightersViewModel.getUniverses()

            swipe.isRefreshing = false
        }
    }

    private fun clearFilters() {
        fightersViewModel.universeName = null
        fightersViewModel.sortedBy = SortedBy.ASCENDING
        fightersViewModel.stars = null
        fightersViewModel.priceRange = null
    }

    private fun setupFightersCount() {
        tvFightersCount.text = getString(R.string.fighters_count, adapter.itemCount)
    }

    private fun loadFighters(fighters: List<Fighter>) {
        adapter.updateList(fighters)

        setupFightersCount()

        swipe.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
    }

    private fun loadChipUniverses(universes: List<Universe>) {
        chipUniverses.removeAllViews()

        addDefaultChip()
        addUniversesChips(universes)

        selectUniverse()
    }

    private fun addUniversesChips(universes: List<Universe>) {
        for (universe in universes) {
            val chip: Chip = (layoutInflater.inflate(
                R.layout.layout_item_universe,
                chipUniverses,
                false
            ) as Chip).apply {
                text = universe.name
                setOnClickListener {
                    fightersViewModel.universeName = (it as Chip).text.toString()
                    fightersViewModel.getAllFighters()
                }
            }

            chipUniverses.addView(chip)
        }
    }

    private fun addDefaultChip() {
        val chipAll: Chip = (layoutInflater.inflate(
            R.layout.layout_item_universe,
            chipUniverses,
            false
        ) as Chip).apply {
            text = getString(R.string.chip_default_all)
            setOnClickListener {
                fightersViewModel.universeName = null
                fightersViewModel.getAllFighters()
            }
        }

        chipUniverses.addView(chipAll)
    }

    private fun selectUniverse() {
        val chip = fightersViewModel.universeName?.let { universe ->
            chipUniverses.children.first { (it as Chip).text == universe }
        } ?: chipUniverses.children.firstOrNull()

        chip?.let {
            chipUniverses.check(it.id)

            scrollToUniversePosition(it)
        }

    }

    private fun scrollToUniversePosition(it: View) {
        requireView().findViewById<HorizontalScrollView>(R.id.hsv_chips)
            .smoothScrollTo(it.left - it.paddingLeft, it.top)
    }
}