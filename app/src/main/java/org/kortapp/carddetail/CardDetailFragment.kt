package org.kortapp.carddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.codecamp24.databinding.CardDetailFragmentBinding

class CardDetailFragment : Fragment() {

    private var _binding: CardDetailFragmentBinding? = null
    private val binding: CardDetailFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CardDetailFragmentBinding.inflate(inflater, container, false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CardDetailScreen()
            }
        }
        return binding.root
    }
}