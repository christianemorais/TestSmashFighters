package com.chrismorais.smashultimatefighters.features.fighters

import com.chrismorais.smashultimatefighters.data.repository.FightersRepository
import com.chrismorais.smashultimatefighters.data.repository.UniverseRepository
import com.chrismorais.smashultimatefighters.data.repository.model.Fighter
import com.chrismorais.smashultimatefighters.features.fighters.filter.SortedBy
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class FightersListViewModelTest {

    private val fightersRepository = mockk<FightersRepository>()
    private val universeRepository = mockk<UniverseRepository>()

    private lateinit var viewModel: FightersListViewModel

    @BeforeEach
    fun setupMocks() {
        clearAllMocks()
        viewModel = FightersListViewModel(universeRepository, fightersRepository)

        coEvery { fightersRepository.getAllFighters() } returns Response.success(getFightersList())

        viewModel.getAllFighters()
        viewModel.listFighters.getOrAwaitValue()
    }

    @Test
    fun `when changed sort option to Ascending should reorder list`() {
        viewModel.sortedBy = SortedBy.ASCENDING
        viewModel.orderBy()

        val result = viewModel.listFighters.getOrAwaitValue()
        assert(result.first().name == "Bayonetta")
    }

    @Test
    fun `when changed sort option to Descending should reorder list`() {
        viewModel.sortedBy = SortedBy.DESCENDING
        viewModel.orderBy()

        val result = viewModel.listFighters.getOrAwaitValue()
        assert(result.first().name == "Zelda")

    }

    @Test
    fun `when changed sort option to Rate should reorder list`() {
        viewModel.sortedBy = SortedBy.RATE
        viewModel.orderBy()

        val result = viewModel.listFighters.getOrAwaitValue()
        assert(result.first().name == "Peach")
    }

    @Test
    fun `when changed sort option to Downloads should reorder list`() {
        viewModel.sortedBy = SortedBy.DOWNLOADS
        viewModel.orderBy()

        val result = viewModel.listFighters.getOrAwaitValue()
        assert(result.first().name == "Samus")

    }

    @Test
    fun `when changed Price Range option should filter list`() {
        viewModel.priceRange = listOf(0.0f, 300.0f)
        viewModel.filterPriceRange()

        val result = viewModel.listFighters.getOrAwaitValue()
        assert(result.first().name == "Peach")

    }

    @Test
    fun `when changed Rating Stars option should filter list`() {
        viewModel.stars = 5
        viewModel.filterRatingStars()

        val result = viewModel.listFighters.getOrAwaitValue()
        assert(result.first().name == "Samus")
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    private fun getFightersList(): List<Fighter> {
        return listOf(
            Fighter(
                "a", "Peach", "Mario", "253", 2, "999", "a", ""
            ),
            Fighter(
                "b", "Zelda", "The Legend of Zelda", "550", 3, "9", "b", ""
            ),
            Fighter(
                "c", "Bayonetta", "Bayonetta", "550", 3, "43643", "c", ""
            ),
            Fighter(
                "d", "Samus", "Metroid", "550", 5, "1", "d", ""
            )
        )
    }
}