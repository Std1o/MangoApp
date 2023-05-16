package com.stdio.mangoapp.common

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.stdio.mangoapp.R
import com.stdio.mangoapp.domain.DataState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun Fragment.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    requireActivity().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun Fragment.showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    requireActivity().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun DialogFragment.showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    requireDialog().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

fun DialogFragment.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    requireDialog().window?.let {
        Snackbar.make(it.decorView, message, duration).show()
    }
}

/** Fragment binding delegate, may be used since onViewCreated up to onDestroyView (inclusive) */
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: factory(requireView()).also {
                // if binding is accessed after Lifecycle is DESTROYED, create new instance, but don't cache it
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }

fun <T> Flow<T>.launchWhenStartedCollect(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launch {
        this@launchWhenStartedCollect.collect()
    }
}

fun <T> StateFlow<DataState<T>>.subscribeInUI(
    fragment: Fragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeInUI.onEach {
        progressBar.isVisible = it is DataState.Loading
        if (it is DataState.Success) {
            listener.invoke(it.data)
        } else if (it is DataState.ValidationError) {
            fragment.showSnackbar(it.messageResId)
        } else if (it is DataState.Error) {
            fragment.showSnackbar(it.exception)
        }
    }.launchWhenStartedCollect(fragment.lifecycleScope)
}

fun <T> StateFlow<DataState<T>>.subscribeInUI(
    dialogFragment: DialogFragment,
    progressBar: ProgressBar,
    listener: (T) -> Unit
) {
    this@subscribeInUI.onEach {
        progressBar.isVisible = it is DataState.Loading
        if (it is DataState.Success) {
            listener.invoke(it.data)
        } else if (it is DataState.ValidationError) {
            dialogFragment.showSnackbar(it.messageResId)
        } else if (it is DataState.Error) {
            dialogFragment.showSnackbar(it.exception)
        }
    }.launchWhenStartedCollect(dialogFragment.lifecycleScope)
}

fun String.handlePhone() = this.replace(" ", "").replace("-", "")

fun TextInputLayout.isValidUsername(): Boolean {
    val inputLayout = this@isValidUsername
    val context = inputLayout.context
    inputLayout.editText?.doOnTextChanged { _, _, _, _ ->
        inputLayout.error = null
    }
    editText?.let {
        return if (!USERNAME_PATTERN.matcher(it.text).matches()) {
            inputLayout.error = context.getString(R.string.invalid_sms_code)
            false
        } else {
            inputLayout.error = null
            true
        }
    }
    return false
}

val USERNAME_PATTERN: Pattern = Pattern.compile(
("[a-zA-Z0-9][a-zA-Z0-9\\-\\_]{1,25}")
)

fun TextInputLayout.isNotEmpty(): Boolean {
    val inputLayout = this@isNotEmpty
    val context = inputLayout.context
    inputLayout.editText?.doOnTextChanged { _, _, _, _ ->
        inputLayout.error = null
    }
    editText?.let {
        return if (it.text.isEmpty()) {
            inputLayout.error = context.getString(R.string.error_empty_field)
            false
        } else {
            inputLayout.error = null
            true
        }
    }
    return false
}