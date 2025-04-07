package com.jesusd0897.rickandmorty.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity

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