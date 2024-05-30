package org.kortapp.carddetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import org.kortapp.card.DebitCard
import org.kortapp.card.cardList
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val id = savedStateHandle.get<String>("id")

    var state: State by mutableStateOf(
        cardList.find { it.id == id }!!.let {
            State.DisplayDetails(it)
        }
    )
        private set

    init {

        //cardList.find { it.id == id }!!.let {
         //   state = State.DisplayDetails(it)
        //}

    }
}

sealed interface State {
    data object Loading : State
    data class DisplayDetails(val debitCard: DebitCard) : State
}