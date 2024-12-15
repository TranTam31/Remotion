package com.example.hope.dass21.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hope.dass21.data.model.FormData
import com.example.hope.dass21.data.model.ResultResponse
import com.example.hope.dass21.network.RetrofitClient

import kotlinx.coroutines.launch

class SurveyViewModel : ViewModel() {
    val resultResponse = MutableLiveData<ResultResponse?>()
    val errorMessage = MutableLiveData<String?>()

    // Gửi dữ liệu form tới API
    fun submitForm(formData: FormData) {
        Log.d("SurveyViewModel", "Form data being sent: $formData")

        viewModelScope.launch {
            try {
                // Gửi yêu cầu API sử dụng Coroutines
                val response = RetrofitClient.apiService.submitForm(formData)

                // Kiểm tra kết quả trả về
                if (response.isSuccessful) {
                    resultResponse.value = response.body()
                    Log.d("SurveyViewModel", "API Response: ${response.body()}")
                } else {
                    errorMessage.value = "Error: ${response.message()}"
                    Log.e("SurveyViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Xử lý lỗi mạng hoặc lỗi bất thường
                errorMessage.value = "Network error: ${e.message}"
                Log.e("SurveyViewModel", "Network error: ${e.message}")
            }
        }
    }
}