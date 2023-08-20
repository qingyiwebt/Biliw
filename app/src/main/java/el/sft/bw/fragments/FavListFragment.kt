package el.sft.bw.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import el.sft.bw.databinding.FragmentFavListBinding

class FavListFragment : Fragment() {
    private lateinit var binding: FragmentFavListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavListBinding.inflate(inflater, container, false)
        return binding.root
    }
}