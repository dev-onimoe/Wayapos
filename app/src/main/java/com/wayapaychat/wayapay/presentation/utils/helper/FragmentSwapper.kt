package com.wayapaychat.wayapay.presentation.utils.helper

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 *  The class helps in adding or replacing a fragment in its parent activity.
 */
const val TAG = "FRAGMENTSWAPPER"

object FragmentSwapper {
    fun addFragmentToActivity(fm: FragmentManager, fragment: Fragment, frgId: Int) {
        val transaction = fm.beginTransaction()
        if (fragment.isAdded.not()) {
            transaction.add(frgId, fragment)
            transaction.commit()
        } else {
        }
    }

    fun replaceFragmentInActivity(fm: FragmentManager, fragment: Fragment, frgId: Int) {
        Log.d(TAG, "replaceFragmentInActivity ${fragment::class.simpleName}")
        val transaction = fm.beginTransaction()
/*
        if (fragment.isAdded.not()) {
            val frg = fm.findFragmentByTag(fragment::class.simpleName)
            if (frg != null) {
                transaction.remove(frg)
            }
            transaction.add(frgId, fragment, fragment::class.simpleName)
            transaction.commitNow()
        } else {
            transaction.replace(frgId, fragment, fragment::class.simpleName)
            transaction.commit()
        }
*/

        transaction.replace(frgId, fragment, null)
        transaction.commit()
    }

    fun addFragmentToParentFragment(
        transaction: FragmentTransaction,
        @IdRes container: Int,
        fragment: Fragment
    ) {
        transaction.replace(container, fragment).commit()
    }
}
