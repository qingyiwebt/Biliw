package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import el.sft.bw.activities.SearchActivity
import el.sft.bw.components.StringItemLayout
import el.sft.bw.databinding.FragmentSearchBinding
import el.sft.bw.framework.components.RecyclerItemClickListener
import el.sft.bw.framework.components.ScrollableFragment
import el.sft.bw.framework.viewbinding.ListBindingAdapter
import el.sft.bw.utils.LocalBroadcastUtils
import el.sft.bw.utils.PrefsUtils

class SearchFragment : ScrollableFragment() {
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ListBindingAdapter<String, StringItemLayout>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        binding.titleBar.setOnClickListener {
            localBroadcastManager.sendBroadcast(
                Intent(
                    LocalBroadcastUtils.ACTION_OPEN_DRAWER
                )
            )
        }

        adapter = ListBindingAdapter(PrefsUtils.searchHistory) { StringItemLayout() }
        binding.searchHistory.adapter = adapter
        binding.searchHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchHistory.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.searchHistory,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val data = PrefsUtils.searchHistory[position]
                        openSearch(data)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        binding.searchButton.setOnClickListener {
            val keyword = binding.searchKeyword.text.toString()
            if (keyword.isEmpty()) return@setOnClickListener

            PrefsUtils.putSearchHistory(keyword)
            openSearch(keyword)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openSearch(keyword: String) {
        adapter.notifyDataSetChanged()

        Intent(requireContext(), SearchActivity::class.java).let {
            it.putExtra("keyword", keyword)
            startActivity(it)
        }
    }
}