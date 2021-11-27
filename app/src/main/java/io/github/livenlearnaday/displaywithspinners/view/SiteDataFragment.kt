package io.github.livenlearnaday.displaywithspinners.view


import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.displaywithspinners.R
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteData
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.databinding.SitesFragmentBinding
import io.github.livenlearnaday.displaywithspinners.utils.Status
import io.github.livenlearnaday.displaywithspinners.utils.UnitDataUtil
import io.github.livenlearnaday.displaywithspinners.utils.autoCleared
import io.github.livenlearnaday.displaywithspinners.view.adapter.CustomSpinnerAdapter
import io.github.livenlearnaday.displaywithspinners.view.adapter.SiteDataAdapter
import io.github.livenlearnaday.displaywithspinners.viewmodel.SiteDataViewModel
import io.github.livenlearnaday.displaywithspinners.viewmodel.UnitDataViewModel
import timber.log.Timber


@AndroidEntryPoint
class SiteDataFragment : Fragment() {

    //use autocleared class, so that binding is nullified in onDestroyView
    private var binding: SitesFragmentBinding by autoCleared()
    private val mSiteDataViewModel: SiteDataViewModel by viewModels()
    private val mUnitDataViewModel: UnitDataViewModel by viewModels()
    private lateinit var mSiteDataAdapter: SiteDataAdapter

    private var mSitesLoaded: Boolean = false

    private var mUnitsLoaded: Boolean = false

    private var mEditModeEnabled = false

    private var mSiteDataList = ArrayList<SiteData>()

    private var mUnitDataList = mutableListOf<UnitData>()

    private var mSelectedUnitDatalist = mutableListOf<UnitData>()

    private lateinit var mSystemSpinnerAdapter: CustomSpinnerAdapter

    private lateinit var mAreaSpinnerAdapter: CustomSpinnerAdapter

    private lateinit var mDensitySpinnerAdapter: CustomSpinnerAdapter

    private lateinit var mRateSpinnerAdapter: CustomSpinnerAdapter

    private lateinit var mLengthSpinnerAdapter: CustomSpinnerAdapter

    private var mAreaUnitData = UnitData()

    private var mDensityUnitData = UnitData()

    private var mRateUnitData = UnitData()

    private var mLengthUnitData = UnitData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SitesFragmentBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolBarTitle(getString(R.string.title_home))


        mEditModeEnabled = false
        mUnitsLoaded = mUnitDataViewModel.isUnitsLoaded()
        Timber.v("value mUnitsLoaded %s ", mUnitsLoaded)

        mSitesLoaded = mSiteDataViewModel.isSitesLoaded()
                Timber.v("value mSitesLoaded %s ", mSitesLoaded)


        if (!mSitesLoaded) {
            mSiteDataViewModel.clearSiteDataTable()
        }

        getUnits()
        getSites()
        fabSettingState()
        setupListener()
        initRecyclerView()

    }


    private fun setupListener() {
        binding.fabSetting.setOnClickListener {
            mEditModeEnabled = true
            findNavController().navigate(R.id.action_fragment_site_data_to_fragment_custom_unit)
        }
    }


    private fun initRecyclerView() {

        binding.rvSites.layoutManager = LinearLayoutManager(requireContext())

        mSiteDataAdapter = SiteDataAdapter(mSiteDataList, mSelectedUnitDatalist)
        binding.rvSites.adapter = mSiteDataAdapter
        mSiteDataAdapter.notifyDataSetChanged()

    }

    private fun setupObserverAllSiteDataFromDb() {
        Timber.v("setupObserverAllSiteDataFromDb loaded: %s", mSitesLoaded)
        mSiteDataViewModel.getAllSiteDataFromDb().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.rvSites.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        mSiteDataList.clear()
                        mSiteDataList = ArrayList(it.data!!.toMutableList())

                    }
                    Status.ERROR -> {

                        binding.rvSites.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvSites.visibility = View.GONE
                    }
                }
            }
        })
    }


    private fun setupObserverSiteDataFromApi() {
        Timber.v("setupObserverSiteDataFromApi loaded: %s", mSitesLoaded)
        mSiteDataViewModel.getAllSiteDataFromApi().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.rvSites.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {

                            mSiteDataList = ArrayList(it.data)
                            mSiteDataViewModel.insertAllSiteData(resource.data!!)

                            Timber.v(
                                "setupObserverSiteDataFromApi before loaded is set: %s",
                                mSitesLoaded
                            )

                            mSitesLoaded = true

                            mSiteDataViewModel.setSitesLoaded(mSitesLoaded)

                            Timber.v(
                                "setupObserverSiteDataFromApi after loaded is set: %s",
                                mSitesLoaded
                            )


                        }

                    }
                    Status.ERROR -> {

                        binding.rvSites.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvSites.visibility = View.GONE
                    }
                }
            }
        })
    }


    private fun setupObserverUnitDataFromApi() {
        mUnitDataViewModel.getAllUnitDataFromApi().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {
                            mUnitDataList.clear()
                            mUnitDataList.addAll(resource.data!!)
                            setupSpinners()
                            mUnitDataViewModel.insertAllUnitData(resource.data)
                            mUnitsLoaded = true
                            mUnitDataViewModel.setUnitsLoaded(mUnitsLoaded)

                            Timber.v(
                                "setupObserverDataUnitDataFromApi after units loaded is set: %s",
                                mUnitsLoaded
                            )


                        }

                    }
                    Status.ERROR -> {

                        binding.rvSites.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvSites.visibility = View.GONE
                    }
                }
            }
        })
    }


    private fun getSites() {
        if (mSitesLoaded) {
            setupObserverAllSiteDataFromDb()
        } else {
            setupObserverSiteDataFromApi()
        }


    }


    private fun getUnits() {

        if (mUnitsLoaded) {
            setupObserverAllUnitDataFromDb()
        } else {
            setupObserverUnitDataFromApi()
        }
    }


    private fun fabSettingState() {

        if (mEditModeEnabled) {
            binding.fabSetting.hide()
        } else {
            binding.fabSetting.show()
        }

    }


    private fun setupSpinners() {


        val measurementSystemOptions = mUnitDataViewModel.getMeasurementSystemList(mUnitDataList)
        Timber.v("measurementSystemOptions %s", measurementSystemOptions.toString())


        mSystemSpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_unit_category,
            measurementSystemOptions
        )
        binding.spinnerMeasurementSystem.adapter = mSystemSpinnerAdapter
        mSystemSpinnerAdapter.notifyDataSetChanged()

        binding.spinnerMeasurementSystem.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                // On selecting a spinner item
                val systemText = parent.getItemAtPosition(position).toString()

                setupCategorySpinners(systemText)

                if (mSelectedUnitDatalist.isNullOrEmpty()) {
                    addBaseUnitsToInitialSelectedUnitsBeforeSpinnerSelection()
                }

                initRecyclerView()
                mSystemSpinnerAdapter.notifyDataSetChanged()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }


    }


    private fun setupCategorySpinners(systemText: String) {


        val areaStringList = mUnitDataViewModel.getCategoryUnitListBySystem(
            mUnitDataList,
            systemText,
            getString(R.string.area_category)
        )
        mAreaSpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_custom_unit,
            areaStringList
        )
        binding.spinnerUnitArea.adapter = mAreaSpinnerAdapter
        mAreaSpinnerAdapter.notifyDataSetChanged()


        val densityStringList = mUnitDataViewModel.getCategoryUnitListBySystem(
            mUnitDataList,
            systemText,
            getString(R.string.rate_category)
        )
        mDensitySpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_custom_unit,
            densityStringList
        )
        binding.spinnerUnitDensity.adapter = mDensitySpinnerAdapter
        mDensitySpinnerAdapter.notifyDataSetChanged()


        val rateStringList = mUnitDataViewModel.getCategoryUnitListBySystem(
            mUnitDataList,
            systemText,
            getString(R.string.density_category)
        )
        mRateSpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_custom_unit,
            rateStringList
        )
        binding.spinnerUnitRate.adapter = mRateSpinnerAdapter
        mRateSpinnerAdapter.notifyDataSetChanged()


        val lengthStringList = mUnitDataViewModel.getCategoryUnitListBySystem(
            mUnitDataList,
            systemText,
            getString(R.string.length_category)
        )
        mLengthSpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_custom_unit,
            lengthStringList
        )
        binding.spinnerUnitLength.adapter = mLengthSpinnerAdapter
        mLengthSpinnerAdapter.notifyDataSetChanged()


//===================================Spinners Selection ===============//

        binding.spinnerUnitArea.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                // On selecting a spinner item
                val areaUnitTxtSelected = parent.getItemAtPosition(position).toString()

                mSelectedUnitDatalist.remove(mAreaUnitData)

                mAreaUnitData = UnitDataUtil.getUnitDataFromNameCategorySystem(
                    mUnitDataList,
                    systemText,
                    getString(R.string.area_category),
                    areaUnitTxtSelected
                )

                mSelectedUnitDatalist.add(mAreaUnitData)

                initRecyclerView()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}


        }




        binding.spinnerUnitDensity.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {

                // On selecting a spinner item
                val distanceUnitTxtSelected = parent.getItemAtPosition(position).toString()

                mSelectedUnitDatalist.remove(mDensityUnitData)


                mDensityUnitData = UnitDataUtil.getUnitDataFromNameCategorySystem(
                    mUnitDataList,
                    systemText,
                    getString(R.string.rate_category),
                    distanceUnitTxtSelected
                )

                mSelectedUnitDatalist.add(mDensityUnitData)

                initRecyclerView()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.spinnerUnitRate.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {

                // On selecting a spinner item
                val volumeUnitTxtSelected = parent.getItemAtPosition(position).toString()

                mSelectedUnitDatalist.remove(mRateUnitData)

                val mVolumeUnitData = UnitDataUtil.getUnitDataFromNameCategorySystem(
                    mUnitDataList,
                    systemText,
                    getString(R.string.density_category),
                    volumeUnitTxtSelected
                )

                mSelectedUnitDatalist.add(mVolumeUnitData)

                initRecyclerView()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        binding.spinnerUnitLength.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                // On selecting a spinner item
                val speedUnitTxtSelected = parent.getItemAtPosition(position).toString()

                mSelectedUnitDatalist.remove(mLengthUnitData)

                mLengthUnitData = UnitDataUtil.getUnitDataFromNameCategorySystem(
                    mUnitDataList,
                    systemText,
                    getString(R.string.length_category),
                    speedUnitTxtSelected
                )

                mSelectedUnitDatalist.add(mLengthUnitData)

                initRecyclerView()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


    }


    private fun addBaseUnitsToInitialSelectedUnitsBeforeSpinnerSelection() {

        mAreaUnitData = mUnitDataViewModel.getBaseUnitObjectForGivenCategory(
            mUnitDataList,
            getString(R.string.area_category)
        )
        mSelectedUnitDatalist.add(mAreaUnitData)
        mDensityUnitData = mUnitDataViewModel.getBaseUnitObjectForGivenCategory(
            mUnitDataList,
            getString(R.string.rate_category)
        )
        mSelectedUnitDatalist.add(mDensityUnitData)
        mRateUnitData = mUnitDataViewModel.getBaseUnitObjectForGivenCategory(
            mUnitDataList,
            getString(R.string.density_category)
        )
        mSelectedUnitDatalist.add(mRateUnitData)
        mLengthUnitData = mUnitDataViewModel.getBaseUnitObjectForGivenCategory(
            mUnitDataList,
            getString(R.string.length_category)
        )
        mSelectedUnitDatalist.add(mLengthUnitData)

    }


    private fun setupObserverAllUnitDataFromDb() {
        mUnitDataViewModel.getAllUnitDataFromDb().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {
                            //reset uni data in list
                            mUnitDataList.clear()
                            mUnitDataList.addAll(resource.data!!)
                            setupSpinners()

                        }

                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }
                }
            }
        })
    }

}


