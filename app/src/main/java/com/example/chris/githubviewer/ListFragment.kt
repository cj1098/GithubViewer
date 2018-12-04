package com.example.chris.githubviewer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import com.example.chris.githubviewer.model.GithubRepository
import java.lang.ClassCastException

class ListFragment: Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: RepositoryListAdapter
    private lateinit var repositoryListViewModel: RepositoryListViewModel
    private lateinit var emptyListResultsView: TextView
    private lateinit var listener: OnRepositorySelected

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnRepositorySelected) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + getString(R.string.no_implementation, TAG))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        listener.restoreToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { repositoryListViewModel = ViewModelProviders.of(it).get(RepositoryListViewModel::class.java) }
        setupObservableViewModels()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_list, container, false)
        return v.apply { val recyclerView: RecyclerView = findViewById(R.id.github_recycler)
            emptyListResultsView = findViewById(R.id.empty_results_view)
            layoutManager = LinearLayoutManager(context)
            adapter = RepositoryListAdapter(context, listener)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun setupObservableViewModels() {
        repositoryListViewModel.githubResult.observe(this, Observer { it?.let {
            if (it.items.isEmpty()) {
                emptyListResultsView.visibility = View.VISIBLE
            } else {
                emptyListResultsView.visibility = View.GONE
            }
            adapter.setItems(it.items)
        } })

        // This will never happen unless we get some kind of exception from github because we hit
        // the limit on api calls during testing.
        repositoryListViewModel.githubError.observe(this, Observer { it?.let {
            emptyListResultsView.visibility = View.VISIBLE
            emptyListResultsView.text = it
        } })
    }

    interface OnRepositorySelected {
        fun onRepositoryItemSelected(githubRepository: GithubRepository)
        fun restoreToolbar()
    }

    companion object {
        @JvmField val TAG: String = ListFragment::class.java.simpleName

        fun newInstance(): ListFragment = ListFragment()
    }
}