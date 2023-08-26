package el.sft.bw.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import el.sft.bw.R
import el.sft.bw.databinding.ActivitySearchBinding
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity
import el.sft.bw.utils.finishWithEmptyString

class SearchActivity : SwipeBackAppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var currentKeyword: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
            currentKeyword = it.getStringExtra("bvId") ?: finishWithEmptyString()
        }

    }
}