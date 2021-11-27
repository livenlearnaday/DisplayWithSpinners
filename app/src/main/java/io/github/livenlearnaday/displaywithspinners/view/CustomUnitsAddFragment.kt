package io.github.livenlearnaday.displaywithspinners.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.displaywithspinners.R
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.databinding.CustomUnitsAddFragmentBinding
import io.github.livenlearnaday.displaywithspinners.utils.MessageUtils
import io.github.livenlearnaday.displaywithspinners.utils.Status
import io.github.livenlearnaday.displaywithspinners.utils.autoCleared
import io.github.livenlearnaday.displaywithspinners.view.adapter.CustomSpinnerAdapter
import io.github.livenlearnaday.displaywithspinners.viewmodel.UnitDataViewModel
import timber.log.Timber


@AndroidEntryPoint
class CustomUnitsAddFragment : Fragment() {
    private var binding: CustomUnitsAddFragmentBinding by autoCleared()

    private val mUnitDataViewModel: UnitDataViewModel by viewModels()

    private var mUnitDataList = mutableListOf<UnitData>()

    private val mTextWatcher = UnitTextWatcher()

    private var mSystemList = mutableListOf<String>()

    private var mCategoryList = mutableListOf<String>()

    private lateinit var mSpinnerSystem: CustomSpinnerAdapter

    private lateinit var mSpinnerCategory: CustomSpinnerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = CustomUnitsAddFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as MainActivity).setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        )

        (activity as MainActivity).setToolBarTitle(getString(R.string.add_new_unit_title))

        setupObserverAllUnitDataFromDb()

        setupListener()
        setupTextWatchers()


    }


    private fun setupTextWatchers() {
        binding.editTextSystem.addTextChangedListener(mTextWatcher)
        binding.editTextUnitName.addTextChangedListener(mTextWatcher)
        binding.editTextConversionFactor.addTextChangedListener(mTextWatcher)

    }


    private fun setupSpinners(unitDataList: List<UnitData>) {
        mSystemList = mUnitDataViewModel.getMeasurementSystemList(unitDataList)
            .toMutableList()
        mSystemList.forEachIndexed { index, s ->
            Timber.v("value in systemList index %s : %s ", index, s)
        }
        setupSystemSpinner(mSystemList)

        mCategoryList =
            mUnitDataViewModel.getAllCategoryStringList(unitDataList)
                .toMutableList()
        mCategoryList.forEachIndexed { index, c ->
            Timber.v("value in categoryList index %s : %s ", index, c)
        }

        val baseUnitObjectsList =
            mUnitDataViewModel.getAllBaseUnitObjects(unitDataList)
        baseUnitObjectsList.forEachIndexed { index, b ->
            Timber.v("value in baseUnitObjectList index %s : %s ", index, b)
        }

        setupCategorySpinner(mCategoryList, baseUnitObjectsList)


    }


    private fun setupSystemSpinner(systemList: List<String>) {
        mSpinnerSystem = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_unit_category,
            systemList
        )
        binding.spinnerUnitSystem.adapter = mSpinnerSystem
        mSpinnerSystem.notifyDataSetChanged()

        binding.spinnerUnitSystem.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {

                updatePreview()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


    }


    private fun setupCategorySpinner(
        categoryList: List<String>,
        baseUnitObjectList: List<UnitData>
    ) {


        //Category Spinner
        mSpinnerCategory = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_custom_textview_unit_category,
            categoryList
        )
        binding.spinnerUnitCategory.adapter = mSpinnerCategory
        mSpinnerCategory.notifyDataSetChanged()

        binding.spinnerUnitCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {


                val categoryTextInput = parent.getItemAtPosition(position).toString()

                val baseUnit = mUnitDataViewModel.getBaseUnitObjectForGivenCategory(
                    baseUnitObjectList,
                    categoryTextInput
                )


                binding.tvUnitBaseSystem.text = baseUnit.measurementSystem
                binding.tvUnitBase.text = baseUnit.unitName

                updatePreview()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


    }

    private fun setupListener() {
        binding.addButton.setOnClickListener {
            addNewCustomUnit()
        }

        binding.addSystemButton.setOnClickListener {
            addNewSystem()
        }
    }


    fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_fragment_add_custom_unit_to_fragment_custom_units)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }


    private fun updatePreview() {
        if (binding.spinnerUnitSystem.selectedItemPosition == -1 || binding.spinnerUnitCategory.selectedItemPosition == -1) {
            return
        }
        val factor = binding.editTextConversionFactor.text.toString().toDoubleOrNull()
        val baseUnit = binding.tvUnitBase.text.toString()

        if (binding.editTextUnitName.text.isNullOrEmpty() || factor == null || factor == 0.0) {
            binding.unitPreviewLabel.visibility = View.GONE
            binding.tvUnitPreview.visibility = View.GONE
            return
        }
        binding.unitPreviewLabel.visibility = View.VISIBLE
        binding.tvUnitPreview.visibility = View.VISIBLE
        binding.tvUnitPreview.text =
            " 1 ${binding.editTextUnitName.text} = ${1 / factor} ${baseUnit}"

    }


    private fun addNewCustomUnit() {

        val currentSystem = binding.spinnerUnitSystem.selectedItem.toString()
        val currentCategory = binding.spinnerUnitCategory.selectedItem.toString()
        val factor = binding.editTextConversionFactor.text.toString().toDoubleOrNull()
        if (factor == null || factor == 0.0) {
            binding.editTextConversionFactor.error =
                getString(R.string.conversion_factor_input_error)
            return
        }
        if (binding.editTextUnitName.text.isNullOrEmpty()) {
            binding.editTextUnitName.error = getString(R.string.unit_name_input_error)
            return
        }

        val unitDataObject = UnitData(
            false,
            currentSystem,
            currentCategory,
            binding.editTextUnitName.text.toString(),
            factor,
            false
        )


        setupObserverUnitDataFromDb(unitDataObject)



    }


    private fun setupObserverUnitDataFromDb(unitDataObject: UnitData) {
        mUnitDataViewModel.getAllUnitDataFromDb().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        mUnitDataViewModel.insertUnitData(unitDataObject)
                        findNavController().navigate(R.id.action_fragment_add_custom_unit_to_fragment_custom_units)

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

    private inner class UnitTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updatePreview()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


    private fun addNewSystem() {
        if (binding.editTextSystem.text.isNullOrEmpty()) {
            MessageUtils.showAlertDialog(
                requireContext(),
                "",
                getString(R.string.add_new_system_error)
            )
            return
        }

        val systemInput = binding.editTextSystem.text.toString().trim()

        if (mUnitDataViewModel.unitSystemExistedInDb(mUnitDataList, systemInput)) {
            MessageUtils.showAlertDialog(
                requireContext(),
                "",
                getString(R.string.add_new_system_duplicate_error)
            )
            return
        }
        mSystemList.add(systemInput)
        binding.editTextSystem.text?.clear()
    }


    private fun setupObserverAllUnitDataFromDb() {
        mUnitDataViewModel.getAllUnitDataFromDb().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {
                            mUnitDataList.clear()
                            mUnitDataList.addAll(resource.data!!)

                            setupSpinners(mUnitDataList)

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
