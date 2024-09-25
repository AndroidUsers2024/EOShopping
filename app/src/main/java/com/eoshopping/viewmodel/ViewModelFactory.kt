import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.repositories.UserRepository
import com.eoshopping.viewmodel.UserViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

