package com.example.weatherapp.ui.home.favourite

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.constants.*
import com.example.weatherapp.databinding.FavouriteFragmentBinding
import com.example.weatherapp.databinding.HomeFragmentBinding
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.ui.home.DaysAdapter
import com.example.weatherapp.ui.home.HomeViewModel
import com.example.weatherapp.ui.home.favourite.favouritedetails.FavouriteDetailsFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.orhanobut.hawk.Hawk

class FavouriteFragment : Fragment() {
    private lateinit var binding: FavouriteFragmentBinding
    private var transaction : FragmentTransaction? = null
    private lateinit var citisListAdapter : FavoriteAdapter
    private lateinit var citiesList : ArrayList<WeatherResponse>

    companion object {
        fun newInstance() = FavouriteFragment()
    }
    private val vm: FavouriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouriteFragmentBinding.inflate(inflater, container, false)
        initUI()
        binding.addFavCityFab.setOnClickListener({
            showSearchContainer()
        }
        )
        return binding.root

        vm.refreshFavCitiesList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipToDelete()
        vm.refreshFavCitiesList()
        vm.fetchFavCities()
        observeViewModel(vm)

    }

    private fun observeViewModel(vm: FavouriteViewModel) {
        vm.citisListLiveData.observe(requireActivity(),  {
            citiesList = it as ArrayList<WeatherResponse>
            updateUI(it)
        })

    }
    private fun updateUI(it: List<WeatherResponse>) {
        citisListAdapter.updateHours(it)
    }
    private fun showSearchContainer() {
        binding.searchFragmentContainer.visibility= View.VISIBLE
        val autocompleteFragment = PlaceAutocompleteFragment.newInstance(MAPBOX_API_KEY)
        transaction = requireActivity().supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.searchFragmentContainer, autocompleteFragment, AUTOCOMPLETE_FRAGMENT_TAG)
        transaction?.commit()

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(carmenFeature: CarmenFeature) {
                // TODO: Use the longitude and latitude
                Toast.makeText(requireContext(),"latitude ${carmenFeature.center()?.latitude()} \n longitude ${carmenFeature.center()?.longitude()}"
                    , Toast.LENGTH_LONG).show()
               requireActivity(). supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)?.commit()
                binding.searchFragmentContainer.visibility= View.GONE
              saveCurrentLocationToSharedPref(carmenFeature.center()?.latitude(), carmenFeature.center()?.longitude())
               vm.fetchData()
            }

            override fun onCancel() {
                requireActivity().supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)?.commit()
                binding.searchFragmentContainer.visibility= View.GONE
            }
        })

    }
    fun saveCurrentLocationToSharedPref(latitude: Double?,longitude: Double?){
        Log.i("test", "Fav save in shPref lat => ${latitude} and lon => ${longitude}")
        val sharedPref =requireContext() .getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(FAV_LATITUDE,latitude.toString()).apply()
        editor.putString(FAV_LONGITUDE,longitude.toString()).apply()
    }
    private fun initUI() {
        binding.citiesRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            citisListAdapter = FavoriteAdapter(arrayListOf()){item ->
                Hawk.put("LAT", item.lat)
                Hawk.put("LON", item.lon)
                requireActivity(). supportFragmentManager?.beginTransaction()?.replace(R.id.homeContainer,FavouriteDetailsFragment())?.commit()
            }
            adapter = citisListAdapter
        }
    }

    private fun swipToDelete() {
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    Log.i("test", "Fav swip to dlt recycler view onMove()")
                    return false // true if moved, false otherwise
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //showConfirmationDialog()
                    MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_Delete)
                        .setTitle(resources.getString(R.string.deleteDialogtitle))
                        .setMessage(resources.getString(R.string.deleteDialogSupportingText))
                        .setPositiveButton(resources.getString(R.string.deleteDialogDelete)){ dialog, which ->
                            // remove from adapter
                            Log.i("test", "onSwiped PstvBtn Before adptr => ${citisListAdapter.cities} \n listyyy => ${citiesList}")
                            vm.deleteCity(citiesList[viewHolder.adapterPosition])
                            Log.i("test", "onSwiped PstvBtn middle adptr => ${citisListAdapter.cities} \n listyyy => ${citiesList}")
                            vm.fetchFavCities()                                                  // remove these two from here
//                            Log.i("test", "Fav swip to dlt recycler view onSwiped() DeleteBtn")
                            Log.i("test", "onSwiped PstvBtn after adptr => ${citisListAdapter.cities} \n listyyy => ${citiesList}")
                        }
                        .setNegativeButton(resources.getString(R.string.deleteDialogCancel)) { dialog, which ->
//                            Log.i("test", "Fav swip to dlt recycler view onSwiped() CancelBtn")
                            Log.i("test", "onSwiped NgtvBtn adptr => ${citisListAdapter.cities} \n listyyy => ${citiesList}")
                        }
                        .setOnDismissListener {
                            citisListAdapter.notifyDataSetChanged()                                  // to here    &&    u can remove this
                            Log.i("test", "onSwiped Dismiss adptr => ${citisListAdapter.cities} \n listyyy => ${citiesList}")
//                            Log.i("test", "Fav swip to dlt recycler view onSwiped() DismissBtn Listener")
                        }  //mlhash lazma

                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setCancelable(false)
                        .show()
                }
                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                         actionState: Int, isCurrentlyActive: Boolean) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    val background =  ColorDrawable(Color.RED)
                    val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_forever_24)
                    val itemView = viewHolder.itemView
                    val backgroundCornerOffset = 20
                    val iconMargin: Int = (itemView.height - icon!!.intrinsicHeight) / 2
                    val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                    val iconBottom: Int = iconTop + icon.intrinsicHeight

                    //if (dX < 0) { // Swiping to the left
                    val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight: Int = itemView.right - iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(itemView.right + (dX.toInt()) - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom)
                    // }
                    background.draw(c)
                    icon.draw(c)
                }

                override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                    super.onSelectedChanged(viewHolder, actionState)
                }
                // DELETE  onSelectedChanged() if useless
            })
        mIth.attachToRecyclerView(binding.citiesRecyclerView)
    }
}