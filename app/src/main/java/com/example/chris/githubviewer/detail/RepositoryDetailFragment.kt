package com.example.chris.githubviewer.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.chris.githubviewer.R
import com.example.chris.githubviewer.model.GithubRepository
import com.squareup.picasso.Picasso

class RepositoryDetailFragment: Fragment() {

    private lateinit var userImage: ImageView
    private lateinit var repositoryName: TextView
    private lateinit var userName: TextView
    private lateinit var repositoryDescription: TextView
    private lateinit var repositorySize: TextView
    private lateinit var repositoryForkCount: TextView
    private lateinit var repositoryOpenIssues: TextView
    private lateinit var repositoryLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false).apply {
            userImage = findViewById(R.id.user_image)
            repositoryName = findViewById(R.id.user_repository_name)
            userName = findViewById(R.id.user_name)
            repositoryDescription = findViewById(R.id.user_repository_description)
            repositorySize = findViewById(R.id.user_repository_size)
            repositoryForkCount = findViewById(R.id.user_repository_forks)
            repositoryOpenIssues = findViewById(R.id.user_repository_open_issues)
            repositoryLink = findViewById(R.id.user_repository_link)

            repositoryLink.setOnClickListener {

            }
            if (arguments?.getString(TRANSITION_NAME) != null) {
                userImage.transitionName = arguments?.getString(TRANSITION_NAME)
            }
            val repository = arguments?.getParcelable<GithubRepository>(REPOSITORY)
            Picasso.get().load(repository?.owner?.avatar_url).placeholder(R.drawable.placeholder).into(userImage)

            repository?.let { apply {
                repositoryName.text = getString(R.string.repository_name_detail, repository.name)
                repositoryDescription.text = getString(R.string.repository_description_detail, repository.description)
                userName.text = getString(R.string.user_name_detail, repository.owner.login)
                repositorySize.text = getString(R.string.repository_size_detail, repository.size)
                repositoryForkCount.text = getString(R.string.repository_forks_detail, repository.forks_count)
                repositoryOpenIssues.text = getString(R.string.repository_open_issues_detail, repository.open_issues_count)
                repositoryLink.text = getString(R.string.repository_link_detail, repository.html_url)
            }}
        }
    }

    companion object {
        @JvmField val TAG: String = RepositoryDetailFragment::class.java.simpleName
        private const val REPOSITORY = "REPOSITORY"
        private const val TRANSITION_NAME = "TRANSITION_NAME"

        fun newInstance(githubRepository: GithubRepository, transitionName: String): RepositoryDetailFragment {
            val args = Bundle()
            args.putString(TRANSITION_NAME, transitionName)
            val fragment = RepositoryDetailFragment()
            args.putParcelable(REPOSITORY, githubRepository)
            fragment.arguments = args
            return fragment
        }
    }
}