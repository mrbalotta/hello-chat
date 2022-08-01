package br.com.bhavantis.chatroom.registration.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.chatroom.registration.domain.UserRegistrationRepository
import br.com.bhavantis.jinko.di.inject
import kotlinx.coroutines.launch

class RegistrationViewModel: ViewModel() {
    private val userRepository: UserRegistrationRepository by inject()
    private val liveData = MutableLiveData<User>()

    fun registrate(nickname: String) {
        viewModelScope.launch {
            val user = userRepository.save(User(nickname = nickname))
            userRepository.setCurrentUser(user)
            liveData.postValue(user)
        }
    }

    fun getLiveData(): LiveData<User> = liveData
}