package com.wayapaychat.wayapos.views.fragments.transaction

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.presentation.utils.cache.CacheImpl
import com.wayapaychat.wayapos.helperClasses.NumberFormats
import com.wayapaychat.wayapos.models.*
import com.wayapaychat.wayapos.viewmodels.TerminalTransactionViewModel
import com.wayapaychat.wayapos.views.adapters.AgencyTransactionAdapter
import kotlinx.android.synthetic.main.fragment_agency_account_transactions.*
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Agency_account_transactions(var account: WalletAccount) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var pageNumber: Int = 0
    var totalPages: Int = 0
    var loading = false
    var items: MutableList<AgencyTransactions>? = null
    var filterList: MutableList<AgencyTransactions>? = null
    var vlist: MutableList<AgencyTransactions>? = null
    private var filterOn = false

    lateinit var recycler: RecyclerView
    lateinit var yhnty: TextView
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var adapter: AgencyTransactionAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var act: AppCompatActivity
    val viewModel by lazy {
        ViewModelProvider(activity as AppCompatActivity).get(TerminalTransactionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_agency_account_transactions, container, false)
        initializeViews(view)
        initViewModel()
        return view
    }

    private fun initializeViews(view: View) {

        filterList = mutableListOf<AgencyTransactions>()
        vlist = mutableListOf<AgencyTransactions>()

        val name_number = view.findViewById<TextView>(R.id.number_name_text)
        val balance = view.findViewById<TextView>(R.id.agency_balance_text)
        val filterIcon = view.findViewById<ImageView>(R.id.filter_icon)
        val backIcon = view.findViewById<ImageView>(R.id.filter_icon)
        val ref_search = view.findViewById<TextView>(R.id.search_bar)
        yhnty = view.findViewById<TextView>(R.id.yhnty_text)
        val saveTransactions = view.findViewById<ImageView>(R.id.download_ic)
        val agc_ll = view.findViewById<FrameLayout>(R.id.agc_ll)
        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        recycler = view.findViewById(R.id.agency_recycler)
        recycler.visibility = View.GONE
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        //progressBar.visibility = View.GONE
        val filterBack = LayoutInflater.from(activity as Context).inflate(R.layout.filter_inapp_transactions_layout, agc_ll,false)

        val watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                println(p0!!)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList!!.clear()
                if (p0!!.length == 0) {
                    yhnty.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                    populateRecyclerView(items!!)
                }else {
                    val ref = ref_search.text.toString()
                    for (item in items!!) {
                        if (ref == item.paymentReference) {
                            filterList!!.add(item)
                            yhnty.visibility = View.GONE
                            recycler.visibility = View.VISIBLE
                            populateRecyclerView(filterList!!)
                        }else {
                            recycler.visibility = View.GONE
                            yhnty.text = "There is no record of a transaction with the reference you provided, please load more transactions or check the reference number"
                            yhnty.visibility = View.VISIBLE
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println(p0!!)
            }
        }

        ref_search.addTextChangedListener(watcher)

        mLayoutManager =
            LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = mLayoutManager.getChildCount()
                    val totalItemCount = mLayoutManager.getItemCount()
                    val pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            if (pageNumber < totalPages) {
                                progressBar.visibility = View.VISIBLE
                                getTransactions(pageNumber + 1)
                            }
                            //Log.v("...", "Last Item Wow !")
                            // Do pagination.. i.e. fetch new data

                        }
                    }
                }
            }
        })

        saveTransactions.setOnClickListener{
            exportCSV()
        }

        backIcon.setOnClickListener{
            items!!.clear()
            adapter.notifyDataSetChanged()
            (activity as AppCompatActivity).onBackPressed()
        }


        filterIcon.setOnClickListener{
            if (filterOn) {
                populateRecyclerView(items!!)
            }
            if (filterList != null) {
                filterList!!.clear()
            }

            if (filterBack.visibility == View.GONE) {
                filterBack.visibility = View.VISIBLE
            }else {
                agc_ll.addView(filterBack)
                var calendarDate = ""

                val removeFilterBack = filterBack.findViewById<ImageView>(R.id.close_view)
                val categoryText = filterBack.findViewById<TextView>(R.id.category_filter_text)
                val categorySpinner = filterBack.findViewById<Spinner>(R.id.category_filter_spinner)
                val dateText = filterBack.findViewById<TextView>(R.id.date_filter_text)
                val dateSpinner = filterBack.findViewById<Spinner>(R.id.date_filter_spinner)
                val filterBtn = filterBack.findViewById<Button>(R.id.filter_transaction_btn)
                val filterClear = filterBack.findViewById<TextView>(R.id.filter_clear_btn)

                val listener =
                    OnDateSetListener { datePicker, i, i1, i2 ->
                        val current = LocalDate.of(i,i1+1,i2)
                        //val d = Date(i, i1, i2)
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = current.format(formatter)
                        dateText.text = formatted
                        println(formatted)
                        if (!(dateText.text as String?).equals("Date Period", true)) {

                            if (filterList!!.isEmpty() && filterOn == false) {
                                for (item in items!!) {

                                    if (formatted.equals(item.tranDate, true)) {
                                        filterList!!.add(item)
                                        vlist!!.add(item)

                                    }
                                }
                            }else if (filterList!!.isEmpty() && filterOn == true){
                                val category = categoryText.text as String
                                for (item in items!!) {

                                    if (formatted.equals(item.tranDate, true) && category.equals(item.tranType, true)) {
                                        filterList!!.add(item)

                                    }
                                }

                            }else if (!filterList!!.isEmpty() && filterOn == true) {

                                for (item in filterList!!) {

                                    if (!formatted.equals(item.tranDate, true)) {

                                        vlist!!.remove(item)

                                    }
                                }
                                filterList!!.clear()
                                filterList!!.addAll(vlist!!)
                            }
                            filterOn = true
                        }else {
                            if (!filterOn) {
                                filterList!!.clear()
                                filterOn = false
                            }
                        }
                    }

                dateText.setOnClickListener{
                    val dialog = DatePickerDialog(context!!)
                    dialog.setOnDateSetListener(listener)
                    dialog.show()
                }

                filterBack.setOnClickListener{
                    agc_ll.removeView(filterBack)
                    yhnty.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                    populateRecyclerView(items!!)
                    Toast.makeText(context!!, "Filter Options not applied", 4)
                }

                removeFilterBack.setOnClickListener{
                    //agc_ll.removeView(filterBack)
                    filterBack.performClick()
                }

                val ad: ArrayAdapter<*> = ArrayAdapter.createFromResource(
                    activity as Context,
                    R.array.agency_filter_category,
                    android.R.layout.simple_spinner_item
                )
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = ad

                categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val cat_text = resources.getStringArray(R.array.agency_filter_category)[p2]
                        categoryText.text = cat_text
                        if (!cat_text.equals("-Select Category-", true)) {

                            if (filterList!!.isEmpty() && filterOn == false) {
                                for (item in items!!) {
                                    if (cat_text.equals(item.tranType, true)) {
                                        filterList!!.add(item)
                                        vlist!!.add(item)

                                    }
                                }
                            }else if (filterList!!.isEmpty() && filterOn == true){
                                val formatted = dateText.text as String
                                for (item in items!!) {

                                    if (formatted.equals(item.tranDate, true) && cat_text.equals(item.tranType, true)) {
                                        filterList!!.add(item)

                                    }
                                }

                            }else if (!filterList!!.isEmpty() && filterOn == true) {

                                for (item in filterList!!) {
                                    if (!cat_text.equals(item.tranType, true)) {
                                        vlist!!.remove(item)
                                    }
                                }
                                filterList!!.clear()
                                filterList!!.addAll(vlist!!)
                            }
                            filterOn = true
                        }else {
                            if (!filterOn) {
                                filterList!!.clear()
                                filterOn = false
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

                categoryText.setOnClickListener{
                    categorySpinner.performClick()
                }

                filterBtn.setOnClickListener{

                    val default = resources.getStringArray(R.array.agency_filter_category)[0]
                    if (!categoryText.text.equals(default) || !dateText.text.equals("Date Period")) {
                        filterOn = true
                    }else {
                        filterOn = false
                    }
                    categoryText.text = default
                    dateText.text = "Date Period"
                    filterBack.visibility = View.GONE
                    if(filterOn) {
                        if (filterList!!.isEmpty()) {
                            recycler.visibility = View.GONE
                            yhnty.text = "There are no results for your filter options, please load more transactions by scrolling downwards or check the options again"
                            yhnty.visibility = View.VISIBLE
                        }else {
                            yhnty.visibility = View.GONE
                            recycler.visibility = View.VISIBLE
                            populateRecyclerView(filterList!!)
                        }
                    }else {
                        yhnty.visibility = View.GONE
                        recycler.visibility = View.VISIBLE
                        populateRecyclerView(items!!)
                    }
                    filterList!!.clear()

                }
            }

        }

        name_number.text = account.accountNo + " - " + account.accountName
        balance.text = NumberFormats.formatToNaira(account.clr_bal_amt)

        val cache = CacheImpl(context!!, Gson())
        cache.putString("account", account.accountNo)

        swipeRefresh.setOnRefreshListener {
            getTransactions(1)
        }

    }

    private fun exportCSV() {

        val csv = csvOf(
            listOf("Transaction Type", "Transaction Date",
                "Payment Reference", "Amount"),
            Collections.unmodifiableList(items)
        ) {
            listOf(it.tranType!!, it.tranDate!!, it.paymentReference!!, NumberFormats.formatToNaira(it.tranAmount!!))
        }
        println(csv)

        try {

            val path = context!!.getExternalFilesDir(null)!!.absolutePath
            val file = path + "/Transactions.csv"
            val filePath = File(file)

            filePath.writeBytes(csv.toByteArray())
            //file.close()
            val pathString = "File saved to"+file
            Toast.makeText(context!!, pathString, Toast.LENGTH_LONG)


        }catch(e : Exception) {
            Toast.makeText(context!!, e.message, Toast.LENGTH_LONG)
            println(e.message)
        }
    }

    fun <T> csvOf(
        headers: List<String>,
        data: List<T>,
        itemBuilder: (T) -> List<String>
    ) = buildString {
        append(headers.joinToString(",") { "\"$it\"" })
        append("\n")
        data.forEach { item ->
            append(itemBuilder(item).joinToString(",") { "\"$it\"" })
            append("\n")
        }
    }

    private fun initViewModel() {
        viewModel.context = activity as Context
        viewModel.cache = CacheImpl(activity as Context, Gson())

        items = ArrayList<AgencyTransactions>()
        items!!.clear()
        getTransactions(1)
    }

    private fun getTransactions(page: Int) {
        loading = true
        viewModel.getInApptransactionDataListObserver()
            .observe(activity as AppCompatActivity, Observer<Response> {

                when (it) {
                    is Response.Success -> {
                        loading = false
                        val responseBody = ResponseBody(it.body)
                        val map = responseBody.data as LinkedTreeMap<String, Any>
                        val pageable = map.get("pageable") as LinkedTreeMap<String, Any>
                        //println(pageNumber)

                        pageNumber = (pageable.get("pageNumber") as Double).toInt()
                        totalPages = (map.get("totalPages") as Double).toInt()

                        var content = map.get("content") as List<Any>
                        if (page == 1) {
                            val ct: List<AgencyTransactions> =
                                content.map { AgencyTransactions(it as LinkedTreeMap<String, Any>) }
                            items!!.clear()
                            items!!.addAll(ct)
                            if (activity != null) {
                                act = activity as AppCompatActivity
                            }
                            if (items!!.isEmpty()) {
                                recycler.visibility = View.GONE
                                yhnty.text = "You have no transactions yet"
                                yhnty.visibility = View.VISIBLE
                            }else {
                                recycler.visibility = View.VISIBLE
                                populateRecyclerView(items!!)
                            }
                            swipeRefresh.isRefreshing = false
                            if (progressBar != null) {
                                progressBar.visibility = View.GONE
                            }
                        } else {
                            val ct: List<AgencyTransactions> =
                                content.map { AgencyTransactions(it as LinkedTreeMap<String, Any>) }
                            if (!(items!!.containsAll(ct))) {
                                items!!.addAll(ct)
                                println(items!!.size)
                                println(page)

                                recycler.post(Runnable {
                                    adapter.notifyDataSetChanged()
                                })
                            }
                            loading = false
                            Handler().postDelayed({ progressBar.visibility = View.GONE }, 3000)


                            //adapter.notifyDataSetChanged()
                        }


                    }

                    is Response.Failure -> {
                        loading = false
                        Toast.makeText(
                            activity as Context,
                            it.exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
        viewModel.getInAppTransactions(account.accountNo, page)
    }

    private fun populateRecyclerView(items : MutableList<AgencyTransactions>) {
        adapter = AgencyTransactionAdapter(act as Context, items)
        recycler.adapter = adapter
        //val mLayoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = mLayoutManager
        adapter.notifyDataSetChanged()


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, acct: WalletAccount) =
            Agency_account_transactions(acct).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}