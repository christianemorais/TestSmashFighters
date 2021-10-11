package com.chrismorais.smashultimatefighters.features.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.chrismorais.smashultimatefighters.R
import com.google.android.material.button.MaterialButton

class OnboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val btNavigate = view.findViewById<MaterialButton>(R.id.bt_navigate)

        btNavigate.setOnClickListener {
            goToPageTwo()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        setFullscreen()
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    private fun setFullscreen() {
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    private fun goToPageTwo() {
        view?.let {
            it.findViewById<ImageView>(R.id.iv_onboard)
                .setImageResource(R.drawable.image_onboard_02)

            it.findViewById<TextView>(R.id.tv_message).text = getString(R.string.onboard_message_universes)

            it.findViewById<MaterialButton>(R.id.bt_navigate).setOnClickListener {
                goToLastPage()
            }
        }
    }

    private fun goToLastPage() {
        view?.let {
            it.findViewById<ImageView>(R.id.iv_onboard)
                .setImageResource(R.drawable.image_onboard_03)

            it.findViewById<TextView>(R.id.tv_message).text = getString(R.string.onboard_message_more)

            it.findViewById<MaterialButton>(R.id.bt_navigate).apply {
                text = getString(R.string.button_get_stared)
                setOnClickListener {
                    goToList()
                }
            }
        }
    }

    private fun goToList() {
        saveOnboardShowed()

        findNavController().popBackStack()
        findNavController().navigate(R.id.action_global_fightersListFragment)
    }

    private fun saveOnboardShowed() {
        activity?.getPreferences(Context.MODE_PRIVATE)?.edit(true) {
            putBoolean("SHOWED_ONBOARD", true)
        }
    }
}