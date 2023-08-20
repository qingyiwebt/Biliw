package el.sft.bw.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import el.sft.bw.R
import el.sft.bw.activities.PlayerActivity
import el.sft.bw.databinding.FragmentCustomSourceBinding
import el.sft.bw.framework.components.ScrollableFragment

class CustomSourceFragment : ScrollableFragment() {
    private lateinit var binding: FragmentCustomSourceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomSourceBinding.inflate(inflater, container, false)

        binding.playButton.setOnClickListener {
            Intent(requireContext(), PlayerActivity::class.java).also {
                it.putExtra("source", PlayerActivity.SOURCE_URL)
                it.putExtra("url", binding.urlTextInput.text.toString())
                startActivity(it)
            }
        }

        return binding.root
    }
}