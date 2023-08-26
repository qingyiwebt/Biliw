package el.sft.bw.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import el.sft.bw.R
import el.sft.bw.databinding.FragmentUserInfoBinding
import el.sft.bw.framework.components.ScrollableFragment
import el.sft.bw.network.ApiClient
import el.sft.bw.utils.runWithFragment
import el.sft.bw.utils.setClipboardPlainText
import el.sft.bw.utils.toHumanReadable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.StringJoiner

class UserInfoFragment : ScrollableFragment() {
    private lateinit var binding: FragmentUserInfoBinding
    private var currentUserId: Long = -1
    private var descriptionExpanded = false

    private var loaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        requireArguments().let {
            currentUserId = it.getLong("userId", 1)
        }

        binding.description.setOnClickListener {
            descriptionExpanded = !descriptionExpanded
            binding.description.maxLines = if (descriptionExpanded) 1024 else 3
        }

        binding.followButton.setOnClickListener {

        }

        binding.userId.text = currentUserId.toString()
        binding.userId.setOnClickListener {
            setClipboardPlainText(requireContext(), currentUserId.toString())

            Toast
                .makeText(requireContext(), R.string.copied, Toast.LENGTH_LONG)
                .show()
        }

        requestLoadUserInfo()

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoadUserInfo() = GlobalScope.launch(Dispatchers.IO) {
        try {
            ApiClient.reloadCookie();

            val spaceRes = ApiClient.getUserSpaceInfo(currentUserId)
            val spaceData = spaceRes.data!!
            val cardRes = ApiClient.getUserCardInfo(currentUserId)
            val cardData = cardRes.data!!.card!!

            runWithFragment(this@UserInfoFragment) {
                binding.uploaderName.text = cardData.name ?: "-"
                binding.followerCount.text = (cardData.followerCount ?: 0L).toHumanReadable()
                binding.followCount.text = (cardData.followCount ?: 0L).toHumanReadable()
                binding.description.text = cardData.description ?: "-"
                val detailStringJoiner = StringJoiner(" ")
                detailStringJoiner.add("LV${cardData.levelInfo?.level ?: 0}")

                detailStringJoiner.add(if (spaceData.isSeniorMember == 1) "硬核会员" else "")

                detailStringJoiner.add(
                    when (cardData.vip?.vipType ?: 0) {
                        0 -> "不是大会员"
                        1 -> "月度大会员"
                        2 -> "年度大会员"
                        else -> "年度及以上大会员"
                    }
                )

                binding.detail.text = detailStringJoiner.toString()

                binding.followButton.text =
                    getText(if (!spaceData.isFollowed) R.string.follow else R.string.unfollow)
                binding.avatarImage.load(cardData.face)
            }

            loaded = true
        } catch (ex: Exception) {
            ex.printStackTrace()
            runWithFragment(this@UserInfoFragment) {
                val ctx = requireContext()
                Toast
                    .makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}