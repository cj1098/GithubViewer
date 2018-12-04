package com.example.chris.githubviewer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chris.githubviewer.model.GithubRepository

class RepositoryDetailFragment: Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false).apply {
        }
    }

    companion object {
        @JvmField val TAG: String = RepositoryDetailFragment::class.java.simpleName
        private const val REPOSITORY = "REPOSITORY"

        fun newInstance(githubRepository: GithubRepository): RepositoryDetailFragment {
            val args = Bundle()
            val fragment = RepositoryDetailFragment()
            args.putParcelable(REPOSITORY, githubRepository)
            fragment.arguments = args
            return fragment
        }
    }
}