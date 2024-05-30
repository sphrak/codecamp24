package org.kortapp.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State by mutableStateOf(State.Loading)
        private set

    init {
        viewModelScope
            .launch {
                delay(1.seconds)
                state = State.DisplayCards(cardList)
            }
    }
}
sealed interface State {
    data object Loading : State
    data class DisplayCards(val debitCards: List<DebitCard>) : State
}

val cardList = listOf(
    DebitCard(
        id = "1",
        cardNumber = "1234 5678 0192 4237",
        validity = "01/23",
        imageUrl = "https://www.sparbankenskane.se/content/dam/savings-banks/savings-bank-8313/images/products/Betalkort-Foretag-500x300.png"
    ),
    DebitCard(
        id = "2",
        cardNumber = "8681 9128 7491 7212",
        validity = "07/25",
        imageUrl = "https://www.sparbankenskane.se/content/dam/savings-banks/savings-bank-8313/images/products/Platinum-Credit-(MC)-500x300.png"
    )
)

data class DebitCard(
    val id: String,
    val cardNumber: String,
    val validity: String,
    val imageUrl: String
)