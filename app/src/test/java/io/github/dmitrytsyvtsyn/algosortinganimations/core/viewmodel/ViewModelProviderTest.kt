package io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

class ViewModelProviderTest {

    @Test
    fun test() {
        // rootProvider is simply a container for all ViewModelProviders
        val rootProvider = ViewModelProvider()

        // create first-level ViewModelProvider
        val sortingMainProvider = rootProvider.provideSubProvider("sortingMainProvider")

        // try to get SortingMainViewModel when it does not exist
        assertThrows(IllegalStateException::class.java) {
            sortingMainProvider.provide(SortingMainViewModel::class.java)
        }

        // create SortingMainViewModel
        val sortingMainViewModel = sortingMainProvider.provide(SortingMainViewModel::class.java) { SortingMainViewModel() }
        // make some operations
        sortingMainViewModel.step = 13

        // create child ViewModelProvider
        val sortingListProvider = sortingMainProvider.provideSubProvider("sortingListProvider")

        // get parent ViewModel because child ViewModelProviders has access to it
        val parentViewModel = sortingListProvider.provide(SortingMainViewModel::class.java)
        assertEquals(13, parentViewModel.step)

        // create ViewModel for child ViewModelProvider
        val sortingListViewModel = sortingListProvider.provide(SortingListViewModel::class.java) { SortingListViewModel() }
        // make some operations
        sortingListViewModel.count = 4

        // try to get SortingListViewModel from parent ViewModelProvider
        assertThrows(IllegalStateException::class.java) {
            sortingMainProvider.provide(SortingListViewModel::class.java)
        }

        // final check
        assertEquals(4, sortingListProvider.provide(SortingListViewModel::class.java).count)
    }

    private class SortingMainViewModel : CoreViewModel {
        var step = 0
    }

    private class SortingListViewModel : CoreViewModel {
        var count = 0
    }

}