package mk.grabit.gpay.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import mk.grabit.gpay.R
import mk.grabit.gpay.databinding.FragmentLoginBinding

class LoginFragment: Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private var binding: FragmentLoginBinding? = null
    private lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)
        setHasOptionsMenu(false)

        binding?.nextButton?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }
}