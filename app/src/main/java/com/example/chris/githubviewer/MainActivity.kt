package com.example.chris.githubviewer

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModelFactory
import javax.inject.Inject
import android.support.v7.widget.SearchView
import android.app.SearchManager
import android.content.Context
import android.view.Menu

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repositoryListViewModelFactory: RepositoryListViewModelFactory

    @Inject
    lateinit var repositoryListViewModel: RepositoryListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GithubViewerApplication.graph.inject(this)

        if (savedInstanceState == null) {
            addFragments()
        }

        repositoryListViewModel = ViewModelProviders.of(this, repositoryListViewModelFactory).get(
                repositoryListViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print
                repositoryListViewModel.loadGithubResults(query)
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                //myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false
            }
        })
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        repositoryListViewModel.dispose()
        super.onDestroy()
    }

    private fun addFragments() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.list_content, ListFragment(), ListFragment.TAG)
                .commit()
    }

}
