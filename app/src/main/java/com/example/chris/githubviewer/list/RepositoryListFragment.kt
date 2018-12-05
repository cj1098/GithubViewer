package com.example.chris.githubviewer.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import com.example.chris.githubviewer.R
import com.example.chris.githubviewer.model.GithubRepository
import com.example.chris.githubviewer.view.VerticalSpaceItemDecoration
import java.lang.ClassCastException

class RepositoryListFragment: Fragment() {

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

    override fun onResume() {
        super.onResume()
        listener.restoreSearch()
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
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = RepositoryListAdapter(context, listener)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            val dividerItemDecoration = VerticalSpaceItemDecoration(16)
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search, menu)
        val myActionMenuItem = menu?.findItem(R.id.action_search)
        val searchView = myActionMenuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupObservableViewModels() {
        repositoryListViewModel.githubResult.observe(this, Observer { it?.let {
            if (it.items.isEmpty()) {
                emptyListResultsView.visibility = View.VISIBLE
            } else {
                emptyListResultsView.visibility = View.GONE
            }
            adapter.setItems(it.items)
        }})

        // This will never happen unless we get some kind of exception from github because we hit
        // the limit on api calls during testing.
        repositoryListViewModel.githubError.observe(this, Observer { it?.let {
            emptyListResultsView.visibility = View.VISIBLE
            emptyListResultsView.text = it
        }})
    }

    interface OnRepositorySelected {
        fun onRepositoryItemSelected(githubRepository: GithubRepository, transitionName: String, sharedView: View)
        fun restoreSearch()
    }

    companion object {
        @JvmField
        val TAG: String = RepositoryListFragment::class.java.simpleName

        fun newInstance(): RepositoryListFragment = RepositoryListFragment()
    }
}