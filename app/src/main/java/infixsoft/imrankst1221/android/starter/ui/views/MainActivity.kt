package infixsoft.imrankst1221.android.starter.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import infixsoft.imrankst1221.android.starter.R
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.ui.viewmodels.UsersViewModel
import infixsoft.imrankst1221.android.starter.utilities.Coroutines

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
    }

    private fun bindUI() = Coroutines.main {
        viewModel.users.await().observe(this, Observer {
            Log.d(TAG, ""+it.size.toString())
        })
    }
}