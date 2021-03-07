package com.canonicalexamples.mypantry.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.canonicalexamples.mypantry.model.Job
import com.canonicalexamples.mypantry.model.JobDatabase
import com.canonicalexamples.mypantry.model.JobFactsService
import com.canonicalexamples.mypantry.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class JobListViewModel(private val database: JobDatabase, private val webservice: JobFactsService): ViewModel() {
    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigate: LiveData<Event<Boolean>> = _navigate
    private var jobList = listOf<Job>()
    data class Item(val name: String, val quantity: Int)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            jobList = database.jobDao.fetchJob()
        }
    }

    val numberOfItems: Int
        get() = jobList.count()

    fun addButtonClicked() {
        _navigate.value = Event(true)
    }

    fun getItem(n: Int) = Item(name = jobList[n].name, quantity = jobList[n].quantity)

    fun onClickItem(n: Int) {
        println("Item $n clicked")
        //viewModelScope.launch(Dispatchers.IO) {
        //    val todo = webservice.getTodo(n).await()
        //    println("todo: ${todo.title}")
        // }
    }
}

class JobListViewModelFactory(private val database: JobDatabase, private val webservice: JobFactsService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobListViewModel(database, webservice) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}