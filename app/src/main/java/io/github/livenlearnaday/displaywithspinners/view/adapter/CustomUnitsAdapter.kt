package io.github.livenlearnaday.displaywithspinners.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.databinding.CustomUnitItemBinding
import io.github.livenlearnaday.displaywithspinners.utils.UnitDataUtil
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class CustomUnitAdapter @Inject constructor(
    private var mUnitDataList: MutableList<UnitData>,
    private val listener: CustomUnitItemListener
) : RecyclerView.Adapter<CustomUnitViewHolder>() {


    var customUnitList = UnitDataUtil.getAllCustomUnits(mUnitDataList).toMutableList()


    override fun getItemCount(): Int = customUnitList.size



    interface CustomUnitItemListener {
        fun onClickedDelete(unit: UnitData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomUnitViewHolder {
        val binding =
            CustomUnitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomUnitViewHolder(binding, listener, mUnitDataList)

    }

    override fun onBindViewHolder(holder: CustomUnitViewHolder, position: Int) {
        holder.bind(customUnitList[position])
    }


}

class CustomUnitViewHolder(
    private val itemBinding: CustomUnitItemBinding,
    private val listener: CustomUnitAdapter.CustomUnitItemListener,
    private var mUnitDataList: MutableList<UnitData>,
) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    private lateinit var unit: UnitData

    init {
        itemBinding.imageViewBtnDeleteUnit.setOnClickListener(this)
    }

    fun bind(item: UnitData) {
        this.unit = item
        itemBinding.apply {
            tvUnitSystem.text = item.measurementSystem
            tvUnitName.text = item.unitName
            tvUnitCategory.text = item.unitCategory


        }


        val baseUnit = UnitDataUtil.getBaseUnitObjectForGivenCategory(
            mUnitDataList.toList(),
            unit.unitCategory
        )

        Timber.v("baseUnit :: %s", baseUnit.unitName)

        try {
            itemBinding.tvUnitBase.text =
                "1 ${item.unitName} = ${(1/(item.unitConversionFactor))} ${baseUnit.unitName}"

        } catch (e: NoSuchElementException) {
        }
    }

    override fun onClick(v: View?) {

        unit.let { listener.onClickedDelete(unit) }

    }


}

