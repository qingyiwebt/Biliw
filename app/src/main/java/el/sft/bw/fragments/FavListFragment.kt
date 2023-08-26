package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import el.sft.bw.R
import el.sft.bw.activities.FavoriteVideosActivity
import el.sft.bw.components.FavListItemLayout
import el.sft.bw.databinding.FragmentFavListBinding
import el.sft.bw.framework.SpacingDecoration
import el.sft.bw.framework.components.RecyclerItemClickListener
import el.sft.bw.framework.viewbinding.ListBindingAdapter
import el.sft.bw.network.ApiClient
import el.sft.bw.network.model.FavListModel
import el.sft.bw.utils.LocalBroadcastUtils
import el.sft.bw.utils.runWithFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavListFragment : Fragment() {
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var binding: FragmentFavListBinding

    private val favListModelList: ArrayList<FavListModel> = ArrayList()
    private var favListArrayAdapter = ListBindingAdapter(favListModelList) { FavListItemLayout() }

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

        binding.favList.adapter = favListArrayAdapter

        binding.favList.adapter = favListArrayAdapter
        binding.favList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.favList.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.favList,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val item = favListModelList[position]

                        Intent(requireContext(), FavoriteVideosActivity::class.java).also {
                            it.putExtra("mlid", item!!.id)
                            startActivity(it)
                        }
                    }
                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        binding.refreshLayout.setOnRefreshListener { requestReloadFavList() }

        requestReloadFavList()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    private fun requestReloadFavList() {
        binding.refreshLayout.isRefreshing = true

        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie()
                val userInfo = ApiClient.getUserInfoFromNav()
                val favList = ApiClient.getFavLists(userInfo.data?.mid ?: 0)

                runWithFragment(this@FavListFragment) {
                    val list = favList.data?.list!!

                    favListModelList.clear()
                    favListModelList.addAll(list)
                    favListArrayAdapter.notifyDataSetChanged()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                runWithFragment(this@FavListFragment) {
                    val ctx = requireContext()
                    Toast
                        .makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG)
                        .show()
                }
            } finally {
                runWithFragment(this@FavListFragment) {
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