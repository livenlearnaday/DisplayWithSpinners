package io.github.livenlearnaday.displaywithspinners.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import io.github.livenlearnaday.displaywithspinners.R
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteData
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.databinding.SiteItemBinding
import io.github.livenlearnaday.displaywithspinners.utils.UnitDataUtil
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class SiteDataAdapter @Inject constructor(
    private var siteDataList: MutableList<SiteData>,
    private var selectedUnitDataObjectsList: MutableList<UnitData>
) :
    RecyclerView.Adapter<SiteDataViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteDataViewHolder {
        val binding =
            SiteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SiteDataViewHolder(binding, selectedUnitDataObjectsList)
    }


    override fun getItemCount(): Int = siteDataList.size


    override fun onBindViewHolder(holder: SiteDataViewHolder, position: Int) =
        holder.bind(siteDataList[position])

}


class SiteDataViewHolder(
    private val itemBinding: SiteItemBinding,
    private val selectedUnitDataObjectsList: MutableList<UnitData>
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var siteData: SiteData


    fun bind(item: SiteData) {
        this.siteData = item

        val areaUnitObject = UnitDataUtil.getUnitDataObjectForGivenCategory(
            selectedUnitDataObjectsList,
            getCategory(itemBinding.tvArea)
        )

        Timber.v("value areaUnitObject for conversion: %s ", areaUnitObject.toString())


        val areaConvertedValue =
            UnitDataUtil.convert(item.surfaceArea, areaUnitObject.unitConversionFactor)
        Timber.v("value areaConvertedValue %s ", areaConvertedValue.toString())
        val areaValueText = UnitDataUtil.resultDisplayText3DecimalOrWholeNumber(areaConvertedValue)


        val densityUnitObject = UnitDataUtil.getUnitDataObjectForGivenCategory(
            selectedUnitDataObjectsList,
            getCategory(itemBinding.tvDensity)
        )

        Timber.v("value densityUnitObject for conversion: %s ", densityUnitObject.toString())

        val densityConvertedValue =
            UnitDataUtil.convert(item.averageSoilDensity, densityUnitObject.unitConversionFactor)
        //density value is very small so display data with 2 decimal places
        val densityValueText = UnitDataUtil.resultDisplayText3DecimalOrWholeNumber(densityConvertedValue)

        val rateUnitObject = UnitDataUtil.getUnitDataObjectForGivenCategory(
            selectedUnitDataObjectsList,
            getCategory(itemBinding.tvRate)
        )

        Timber.v("value rateUnitObject for conversion: %s ", rateUnitObject.toString())


        val rateConvertedValue =
            UnitDataUtil.convert(item.averageInfiltrationRate, rateUnitObject.unitConversionFactor)
        val rateValueText =
            UnitDataUtil.resultDisplayText3DecimalOrWholeNumber(rateConvertedValue)


        val lengthUnitObject = UnitDataUtil.getUnitDataObjectForGivenCategory(
            selectedUnitDataObjectsList,
            getCategory(itemBinding.tvLength)
        )

        Timber.v("value lengthUnitObject for conversion: %s ", lengthUnitObject.toString())


        val lengthConvertedValue =
            UnitDataUtil.convert(item.averageMonthlyRainfall, lengthUnitObject.unitConversionFactor)
        val lengthValueText =
            UnitDataUtil.resultDisplayText3DecimalOrWholeNumber(lengthConvertedValue)


        itemBinding.tvName.text = item.name
        itemBinding.tvArea.text = "${areaValueText} ${areaUnitObject.unitName}"
        itemBinding.tvDensity.text = "${densityValueText} ${densityUnitObject.unitName}"
        itemBinding.tvRate.text = "${rateValueText} ${rateUnitObject.unitName}"
        itemBinding.tvLength.text = "${lengthValueText} ${lengthUnitObject.unitName}"

    }

    private fun getCategory(v: View): String {
        val category: String
        if (v == itemBinding.tvArea) {
            category = v.context.getString(R.string.area_category)
        } else if (v == itemBinding.tvDensity) {
            category = v.context.getString(R.string.density_category)
        } else if (v == itemBinding.tvRate) {
            category = v.context.getString(R.string.rate_category)
        } else {
            category = v.context.getString(R.string.length_category)
        }

        return category
    }


}






