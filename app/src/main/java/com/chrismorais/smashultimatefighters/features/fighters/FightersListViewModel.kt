package com.chrismorais.smashultimatefighters.features.fighters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrismorais.smashultimatefighters.data.repository.FightersRepository
import com.chrismorais.smashultimatefighters.data.repository.UniverseRepository
import com.chrismorais.smashultimatefighters.data.repository.model.Fighter
import com.chrismorais.smashultimatefighters.data.repository.model.Universe
import com.chrismorais.smashultimatefighters.features.fighters.filter.SortedBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FightersListViewModel(
    private val universeRepository: UniverseRepository,
    private val fightersRepository: FightersRepository
) : ViewModel() {

    var sortedBy: SortedBy = SortedBy.ASCENDING
    var priceRange: List<Float>? = null
    var stars: Int? = null
    var universeName: String? = null

    private val _selectedFighter = MutableLiveData<Fighter>()
    val selectedFighter: LiveData<Fighter> = _selectedFighter

    private val _listFighters = MutableLiveData<List<Fighter>>()
    val listFighters: LiveData<List<Fighter>> = _listFighters

    private val _listUniverses = MutableLiveData<List<Universe>>()
    val listUniverse: LiveData<List<Universe>> = _listUniverses

    private val _shouldFilter = MutableLiveData<Boolean>()
    val shouldFilter: LiveData<Boolean> = _shouldFilter

    fun selectFighter(fighter: Fighter) {
        _selectedFighter.postValue(fighter)
    }

    fun setShouldFilters(toggle: Boolean) {
        _shouldFilter.postValue(toggle)
    }

    fun getUniverses() {
        viewModelScope.launch(Dispatchers.IO) {
            _listUniverses.postValue(universeRepository.getUniverses().body()?.sortedBy { it.name })
        }
    }

    fun getAllFighters() {
        viewModelScope.launch(Dispatchers.IO) {
            _listFighters.postValue(getFighters()?.sortedBy { it.name })
        }
    }

    private suspend fun getFighters(): List<Fighter>? {
        return if (universeName != null) {
            fightersRepository.getFightersByUniverse(universeName.toString()).body()
        } else {
            fightersRepository.getAllFighters().body()
        }
    }

    fun filterRatingStars() {
        stars?.let { rate ->
            val filteredList = _listFighters.value?.filter { it.rate == rate }

            _listFighters.value = (filteredList ?: emptyList())
        }

    }

    fun filterPriceRange() {
        priceRange?.let { range ->
            val filteredList =
                _listFighters.value?.filter { it.price.toFloat() >= range.first() && it.price.toFloat() <= range.last() }

            _listFighters.value = (filteredList ?: emptyList())
        }
    }

    fun orderBy() {
        _listFighters.postValue(
            when (sortedBy) {
                SortedBy.ASCENDING -> _listFighters.value?.sortedBy { it.name }
                SortedBy.DESCENDING -> _listFighters.value?.sortedByDescending { it.name }
                SortedBy.RATE -> _listFighters.value?.sortedBy { it.rate }
                SortedBy.DOWNLOADS -> _listFighters.value?.sortedBy { it.downloads.toLong() }
            }
        )
    }
}