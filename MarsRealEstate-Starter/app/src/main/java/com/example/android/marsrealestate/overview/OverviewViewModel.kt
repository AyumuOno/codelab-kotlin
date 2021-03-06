/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.security.auth.callback.Callback

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

  // The internal MutableLiveData String that stores the most recent response
  private val _response = MutableLiveData<String>()

  // The external immutable LiveData for the response String
  val response: LiveData<String>
    get() = _response

  private val _property = MutableLiveData<List<MarsProperty>>()

  val property: LiveData<List<MarsProperty>>
    get() = _property

  private val _status = MutableLiveData<MarsApiStatus>()

  val status: LiveData<MarsApiStatus>
    get() = _status

  private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()

  val navigateToSelectedProperty: LiveData<MarsProperty>
    get() = _navigateToSelectedProperty

  private var viewModelJob = Job()
  private val coroutineScope = CoroutineScope(
      viewModelJob + Dispatchers.Main
  )

  /**
   * Call getMarsRealEstateProperties() on init so we can display status immediately.
   */
  init {
    getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
  }

  /**
   * Sets the value of the status LiveData to the Mars API status.
   */
  private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
    coroutineScope.launch {
      var getPropertiesDeferred = MarsApi.retrofitService.getProperties(filter.value)
      try {
        _status.value = MarsApiStatus.LOADING
        var listResult = getPropertiesDeferred.await()
        _status.value = MarsApiStatus.DONE
//        _response.value = "Success: ${listResult.size} Mars Properties retrieved"

        if (listResult.size > 0) {
          _property.value = listResult
        }
      } catch (e: Exception) {
//        _response.value = "Failure: ${e.message}"
        _status.value = MarsApiStatus.ERROR
        _property.value = ArrayList()
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }

  fun updateFilter(filter: MarsApiFilter) {
    getMarsRealEstateProperties(filter)
  }

  fun displayPropertyDetails(marsProperty: MarsProperty) {
    _navigateToSelectedProperty.value = marsProperty
  }

  fun displayPropertyDetailsComplete() {
    _navigateToSelectedProperty.value = null
  }
}
