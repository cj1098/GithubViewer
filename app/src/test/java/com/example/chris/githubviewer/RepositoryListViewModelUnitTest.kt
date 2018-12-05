package com.example.chris.githubviewer

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chris.githubviewer.model.GithubResult
import com.example.chris.githubviewer.service.repository.ProjectRepository
import com.example.chris.githubviewer.viewmodel.RepositoryListViewModel
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock

class RepositoryListViewModelUnitTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers: RxImmediateSchedulerRule = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var projectRepository: ProjectRepository

    @Mock
    internal lateinit var repositoryListViewModel: RepositoryListViewModel

    @Before
    fun setUp() {
        projectRepository = mock(ProjectRepository::class.java)
        repositoryListViewModel = RepositoryListViewModel(projectRepository)
    }

    @Test
    fun testGetRepositoriesSuccessIsNotEmpty() {
        val githubResult = mockGithubResult()
        whenever(projectRepository.getRepositories(anyString())).thenReturn(Observable.just(githubResult))

        repositoryListViewModel.loadGithubResults(anyString())
        verify(projectRepository).getRepositories(anyString())
        Assert.assertTrue(repositoryListViewModel.githubResult.value?.total_count == 10)
    }

    @Test
    fun testGetRepositoriesFails() {
        whenever(projectRepository.getRepositories(anyString())).thenReturn(Observable.error(Throwable("FAILED")))

        repositoryListViewModel.loadGithubResults(anyString())
        verify(projectRepository).getRepositories(anyString())
        Assert.assertTrue(repositoryListViewModel.githubError.value?.equals("FAILED") ?: false)
    }

    private fun mockGithubResult(): GithubResult = GithubResult(10, true, emptyList())
}