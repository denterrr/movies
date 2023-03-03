package ter.den.feature_profile.ui

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ter.den.core.domain.MovieDB
import ter.den.core.domain.extensions.hideSoftKeyboard
import ter.den.core.domain.model.CustomThrowable
import ter.den.feature_profile.R
import ter.den.feature_profile.databinding.FragmentProfileBinding
import ter.den.feature_profile.di.ProfileComponentViewModel
import ter.den.feature_profile.domain.model.AccountDetails
import ter.den.feature_profile.presentation.ProfileViewModel
import ter.den.feature_profile.presentation.ViewModelFactory
import javax.inject.Inject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding ?: throw CustomThrowable.BindingNull


    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ProfileViewModel
            by viewModels(factoryProducer = { viewModelFactory })


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<ProfileComponentViewModel>()
            .profileComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.tvSignUp.movementMethod = LinkMovementMethod.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initObservers()
    }


    private fun initObservers() {
        viewModel.accountDetails.observe(viewLifecycleOwner) {
            binding.progress.isVisible = false
            if (it != null) {
                showAccountDetails(it)
            } else {
                showLoginUi()
            }
        }

        viewModel.throwableMessage.observe(viewLifecycleOwner) {
            binding.btnLogin.isEnabled = true
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showLoginUi() {
        binding.clProfile.isVisible = false
        binding.clLogin.isVisible = true
    }

    private fun showAccountDetails(details: AccountDetails) {
        binding.clProfile.isVisible = true
        binding.clLogin.isVisible = false
        val avatarUrl =
            if (details.avatar.tmdb.avatar_path.isNotEmpty())
                MovieDB.IMAGE_BASE_URL + details.avatar.tmdb.avatar_path
            else MovieDB.GRAVATAR_BASE_URL + details.avatar.gravatar.hash
        Glide.with(binding.root)
            .load(avatarUrl)
            .circleCrop()
            .into(binding.ivAvatar)
        binding.tvName.text = details.name.ifEmpty { getString(R.string.no_name) }
        binding.tvUsername.text = getString(R.string.username_format).format(details.username)
    }


    private fun initClickListeners() {
        binding.cvFavorites.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_moviesFragment, bundleOf(
                    MoviesFragment.MOVIE_TYPE to MoviesFragment.FAVORITES
                )
            )
        }
        binding.cvWatchList.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_moviesFragment, bundleOf(
                    MoviesFragment.MOVIE_TYPE to MoviesFragment.WATCH_LIST
                )
            )
        }

        binding.btnLogin.setOnClickListener {
            viewModel.tryLogin(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
            binding.btnLogin.isEnabled = false
            requireActivity().hideSoftKeyboard()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}