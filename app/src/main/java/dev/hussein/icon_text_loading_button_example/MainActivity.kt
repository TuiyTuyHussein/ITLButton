package dev.hussein.icon_text_loading_button_example

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dev.hussein.icon_text_loading_button_example.databinding.ActivityMainBinding
import dev.hussein.itl_button.ITLButton

class MainActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)



        initActions()
    }

    private fun initActions() {

        mBinding.likeAction.setOnActionClickListener(object : ITLButton.OnActionClickListener {
            override fun onClick(v: ITLButton) {
                if (v.isLoading) {
                    v.postDelayed({
                        v.stopLoading()
                        v.changeIconState(!v.isChecked)
                    }, 3000)
                }
            }
        })
        mBinding.shareAction.setOnActionClickListener(object : ITLButton.OnActionClickListener {
            override fun onClick(v: ITLButton) {
                if (v.isLoading)
                    mBinding.shareAction.postDelayed({ mBinding.shareAction.stopLoading() }, 3000)
            }
        })
    }


}
