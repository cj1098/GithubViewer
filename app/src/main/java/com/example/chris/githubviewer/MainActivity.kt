package com.example.chris.githubviewer

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModelFactory
import javax.inject.Inject
import android.support.v7.widget.SearchView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import com.example.chris.githubviewer.model.GithubRepository
import kotlinx.android.synthetic.main.activity_main.*
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewAnimationUtils
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import com.example.chris.githubviewer.detail.RepositoryDetailFragment
import com.example.chris.githubviewer.list.RepositoryListFragment


class MainActivity : AppCompatActivity(), RepositoryListFragment.OnRepositorySelected {

    @Inject
    lateinit var repositoryListViewModelFactory: RepositoryListViewModelFactory

    @Inject
    lateinit var repositoryListViewModel: RepositoryListViewModel

    private lateinit var searchItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GithubViewerApplication.graph.inject(this)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            addFragments()
        }

        repositoryListViewModel = ViewModelProviders.of(this, repositoryListViewModelFactory).get(
                repositoryListViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search, menu)

        searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(searchView: MenuItem?): Boolean {
                animateSearchToolbar(1, true, true)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                if (searchItem.isActionViewExpanded) {
                    animateSearchToolbar(1, false, false)
                }
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                repositoryListViewModel.loadGithubResults(query)

                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        repositoryListViewModel.dispose()
        super.onDestroy()
    }

    private fun addFragments() {
        addFragment(R.id.content, RepositoryListFragment.newInstance(), RepositoryListFragment.TAG)
    }

    override fun restoreSearch() {
        if (::searchItem.isInitialized && !searchItem.isVisible) {
            searchItem.isVisible = true
        }
    }

    override fun onRepositoryItemSelected(githubRepository: GithubRepository, transitionName: String, sharedView: View) {
        searchItem.isVisible = false
        closeAndClearSearchView()
        ViewCompat.getTransitionName(sharedView)?.let { replaceFragment(R.id.content,
                RepositoryDetailFragment.newInstance(githubRepository, it), RepositoryDetailFragment.TAG, true, sharedView) }
    }

    // This is gross :'(
    private fun closeAndClearSearchView() {
        val searchView = (searchItem.actionView as SearchView)
        searchView.clearFocus()
        searchView.isIconified = true
        searchItem.collapseActionView()
    }

    // extension functions
    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.transaction({ add(frameId, fragment, tag) }, "")
    }

    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment, tag: String, isSharedElement: Boolean, sharedView: View) {
        if (isSharedElement) {
            supportFragmentManager.transactionWithSharedElements({replace(frameId, fragment, tag)}, tag, sharedView)
        } else {
            supportFragmentManager.transaction({ replace(frameId, fragment, tag) }, tag)
        }
    }

    inline fun FragmentManager.transactionWithSharedElements(function: FragmentTransaction.() -> FragmentTransaction, tag: String, sharedView: View) {
        if (!tag.isEmpty()) {
            ViewCompat.getTransitionName(sharedView)?.let { beginTransaction().function().addSharedElement(sharedView, it).addToBackStack(tag).commit() }
        } else {
            ViewCompat.getTransitionName(sharedView)?.let { beginTransaction().function().addSharedElement(sharedView, it).commit() }
        }
    }

    inline fun FragmentManager.transaction(function: FragmentTransaction.() -> FragmentTransaction, tag: String) {
        if (!tag.isEmpty()) {
            beginTransaction().function().addToBackStack(tag).commit()
        } else {
            beginTransaction().function().commit()
        }
    }

    fun animateSearchToolbar(numberOfMenuIcon: Int, containsOverflow: Boolean, show: Boolean) {

        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        window.statusBarColor = (ContextCompat.getColor(this, R.color.colorPrimaryDark))

        if (show) {
            val width = toolbar.width -
                    (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                    resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
            val createCircularReveal = ViewAnimationUtils.createCircularReveal(
                toolbar,
                width,
                toolbar.height / 2,
                0.0f,
                width.toFloat()
            )
            createCircularReveal.duration = 250
            createCircularReveal.start()
        } else {
            val width = toolbar.width -
                    (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                    resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
            val createCircularReveal = ViewAnimationUtils.createCircularReveal(
                toolbar,
                width,
                toolbar.height / 2,
                width.toFloat(),
                0.0f
            )
            createCircularReveal.duration = 250
            createCircularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    toolbar.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    window.statusBarColor = (ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark))
                }
            })
            createCircularReveal.start()
            window.statusBarColor = (ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }
    }
}
