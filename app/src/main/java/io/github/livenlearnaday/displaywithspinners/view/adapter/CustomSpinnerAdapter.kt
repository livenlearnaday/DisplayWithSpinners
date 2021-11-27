package io.github.livenlearnaday.displaywithspinners.view.adapter

import android.content.Context
import android.view.View
import android.widget.TextView

import android.view.ViewGroup

import android.widget.ArrayAdapter
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class CustomSpinnerAdapter @Inject constructor(
    context: Context,
    textViewResourceId: Int,
    val list: List<String>
) : ArrayAdapter<String>(
    context,
    textViewResourceId,
    list
) {
    override fun getCount() = list.size

    override fun getItem(position: Int) = list[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            text = list[position]
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            text = list[position]
        }
    }
}