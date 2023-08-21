package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import el.sft.bw.R
import el.sft.bw.activities.FavoriteVideosActivity
import el.sft.bw.components.VideoCardData
import el.sft.bw.databinding.FragmentFavListBinding
import el.sft.bw.network.ApiClient
import el.sft.bw.network.model.FavListModel
import el.sft.bw.network.model.VideoModel
import el.sft.bw.utils.LocalBroadcastUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavListFragment : Fragment() {
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var binding: FragmentFavListBinding
    private var favListModelList: MutableList<FavListModel> = mutableListOf()
    private lateinit var favListArrayAdapter: FavListArrayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavListBinding.inflate(inflater, container, false)

        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        binding.titleBar.setOnClickListener {
            localBroadcastManager.sendBroadcast(
                Intent(
                    LocalBroadcastUtils.ACTION_OPEN_DRAWER
                )
            )
        }

        favListArrayAdapter = FavListArrayAdapter(requireContext(), favListModelList)
        binding.favList.adapter = favListArrayAdapter
        binding.favList.setOnItemClickListener { _, _, position, _ ->
            val item = favListArrayAdapter.getItem(position)

            Intent(requireContext(), FavoriteVideosActivity::class.java).also {
                it.putExtra("mlid", item!!.id)
                startActivity(it)
            }
        }

        binding.refreshLayout.setOnRefreshListener { requestReloadFavList() }

        requestReloadFavList()
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestReloadFavList() {
        binding.refreshLayout.isRefreshing = true

        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie()
                val userInfo = ApiClient.getUserInfoFromNav()
                val favList = ApiClient.getFavLists(userInfo.data?.mid ?: 0)

                withContext(Dispatchers.Main) {
                    val list = favList.data?.list!!

                    favListModelList.clear()
                    favListModelList.addAll(list)
                    favListArrayAdapter.notifyDataSetChanged()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    val ctx = requireContext()
                    Toast
                        .makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG)
                        .show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.refreshLayout.isRefreshing = false
                }
            }
        }
    }

    inner class FavListArrayAdapter(context: Context, list: List<FavListModel>) :
        ArrayAdapter<FavListModel>(context, 0, list) {
        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val item = getItem(position)
            val textView = (convertView ?: LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)) as TextView

            textView.text = item?.title ?: "...?"

            return textView
        }
    }
}