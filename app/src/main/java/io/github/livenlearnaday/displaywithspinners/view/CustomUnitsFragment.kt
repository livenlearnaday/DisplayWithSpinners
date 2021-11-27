package io.github.livenlearnaday.displaywithspinners.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.displaywithspinners.R
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitData
import io.github.livenlearnaday.displaywithspinners.databinding.CustomUnitsFragmentBinding
import io.github.livenlearnaday.displaywithspinners.utils.Status
import io.github.livenlearnaday.displaywithspinners.utils.autoCleared
import io.github.livenlearnaday.displaywithspinners.view.adapter.CustomUnitAdapter
import io.github.livenlearnaday.displaywithspinners.viewmodel.UnitDataViewModel
import timber.log.Timber

@AndroidEntryPoint
class CustomUnitsFragment : Fragment(), CustomUnitAdapter.CustomUnitItemListener {

    private var binding: CustomUnitsFragmentBinding by autoCleared()

    private val mUnitDataViewModel: UnitDataViewModel by viewModels()

    private var mUnitDatalist = mutableListOf<UnitData>()

    private lateinit var mAdapter: CustomUnitAdapter

    private var mUnitsLoaded: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = CustomUnitsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolBarTitle(getString(R.string.custom_unit_title))

        mUnitsLoaded = mUnitDataViewModel.isUnitsLoaded()
        Timber.v("value mUnitsLoaded %s ", mUnitsLoaded)



        setupObservers()
        setupListeners()



    }

    private fun setupObservers(){
        setupObserverAllUnitDataFromDb()

    }


    private fun setupListeners() {
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_fragment_add_custom_unit)
        }

        binding.resetUnitsButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.warning_message_reset_units))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                    mUnitDataViewModel.clearUnitDataTable()
                    setupObserverAllUnitDataFromApi()
                }
                .create()
                .show()
        }

    }


    private fun initCustomUnitListUI() {

        binding.rvCustomUnits.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = CustomUnitAdapter(mUnitDatalist,this)
        binding.rvCustomUnits.adapter = mAdapter


    }


    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_fragment_custom_units_to_fragment_site_data)

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


    override fun onClickedDelete(unit: UnitData) {
        AlertDialog.Builder(requireContext())
            .setMessage("Do you want to delete ${unit.unitName} ?")
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->

                mUnitDataViewModel.deleteUnitDataBySystemCategoryName(unit.measurementSystem, unit.unitCategory, unit.unitName)
                mUnitDatalist.remove(unit)
                initCustomUnitListUI()

            }
            .create()
            .show()
    }


    private fun setupObserverAllUnitDataFromDb() {
        mUnitDataViewModel.getAllUnitDataFromDb().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {
                            //reset uni data in list
                            mUnitDatalist.clear()
                            mUnitDatalist.addAll(resource.data!!)
                            initCustomUnitListUI()
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




    private fun setupObserverAllUnitDataFromApi() {
        mUnitDataViewModel.getAllUnitDataFromApi().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        if (!it.data.isNullOrEmpty()) {
                            mUnitDatalist.clear()
                            mUnitDatalist.addAll(resource.data!!)
                            initCustomUnitListUI()
                            mUnitDataViewModel.insertAllUnitData(resource.data)
                            mUnitsLoaded = true
                            mUnitDataViewModel.setUnitsLoaded(mUnitsLoaded)


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
