package com.appster.dentamatch.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class ActivityUtils {

    companion object {
        fun replaceFragment(fragmentManager: FragmentManager,
                            container: Int,
                            fragment: Fragment,
                            tag: String,
                            addToBackStack: Boolean): Any =
                if (addToBackStack) {
                    fragmentManager.beginTransaction()
                            .replace(container, fragment, tag)
                            .addToBackStack(null)
                            .commit()
                } else {
                    fragmentManager.beginTransaction()
                            .replace(container, fragment, tag)
                            .commit()
                }
    }
}
