package el.sft.bw.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import el.sft.bw.R
import el.sft.bw.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}