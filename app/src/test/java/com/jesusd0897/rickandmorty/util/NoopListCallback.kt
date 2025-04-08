package com.jesusd0897.rickandmorty.util

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

internal class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

internal class CharacterDiffCallback : DiffUtil.ItemCallback<CharacterEntity>() {
    override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharacterEntity,
        newItem: CharacterEntity
    ): Boolean {
        return oldItem == newItem
    }
}

suspend fun PagingData<CharacterEntity>.collectData(): List<CharacterEntity> {
    val differ = AsyncPagingDataDiffer(
        diffCallback = CharacterDiffCallback(),
        updateCallback = NoopListCallback(),
        mainDispatcher = Dispatchers.Main,
        workerDispatcher = Dispatchers.Main
    )

    differ.submitData(this)
    delay(100)
    return List(differ.itemCount) { index ->
        differ.getItem(index)!!
    }
}