package com.eoshopping.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eoshopping.User
import com.eoshopping.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _users = MutableLiveData<User>()

    val users:LiveData<User> get() = _users

    fun fetchUsers(id: String){
        viewModelScope.launch {
            _users.value = repository.getUser(id)
        }
    }
}