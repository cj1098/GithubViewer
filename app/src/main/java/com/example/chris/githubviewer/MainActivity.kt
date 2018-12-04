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
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.chris.githubviewer.model.GithubRepository
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.opengl.ETC1.getHeight
import android.view.animation.TranslateAnimation
import android.view.animation.AlphaAnimation
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.opengl.ETC1.getWidth
import android.view.ViewAnimationUtils
import android.os.Build
import android.support.v4.content.ContextCompat



class MainActivity : AppCompatActivity(), ListFragment.OnRepositorySelected {

    @Inject
    lateinit var repositoryListViewModelFactory: RepositoryListViewModelFactory

    @Inject
    lateinit var repositoryListViewModel: RepositoryListViewModel

    private lateinit var searchView: SearchView

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

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
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
        addFragment(R.id.content, ListFragment.newInstance(), ListFragment.TAG)
    }

    override fun restoreToolbar() {
        supportActionBar?.show()
    }

    override fun onRepositoryItemSelected(githubRepository: GithubRepository) {
//        searchView.isIconified = true
//        searchView.onActionViewCollapsed()
//        searchView.clearFocus()
        supportActionBar?.hide()
        replaceFragment(R.id.content, RepositoryDetailFragment.newInstance(githubRepository), RepositoryDetailFragment.TAG)
    }

    // extension functions
    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.inTransaction({ add(frameId, fragment, tag) }, "")
    }

    fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.inTransaction({ replace(frameId, fragment, tag) }, tag)
    }

    inline fun FragmentManager.inTransaction(function: FragmentTransaction.() -> FragmentTransaction, tag: String) {
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, -toolbar.height as Float, 0.0f)
                translateAnimation.duration = 220
                toolbar.clearAnimation()
                toolbar.startAnimation(translateAnimation)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
            } else {
                val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
                val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, -toolbar.height as Float)
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(alphaAnimation)
                animationSet.addAnimation(translateAnimation)
                animationSet.duration = 220
                animationSet.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        toolbar.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                toolbar.startAnimation(animationSet)
            }
            window.statusBarColor = (ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }
    }


}
