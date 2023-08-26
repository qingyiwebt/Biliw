package el.sft.bw.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import el.sft.bw.databinding.ActivityAddFavoriteBinding
import el.sft.bw.utils.finishWithEmptyString

class AddFavoriteActivity : AppCompatActivity() {
    private lateinit var currentBvId: String
    private lateinit var binding: ActivityAddFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
            currentBvId = it.getStringExtra("bvId") ?: finishWithEmptyString()
        }


    }


}