package com.wayapaychat.wayapos.listeners

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface FragmentListener {

    fun switchFragments(fragment : Fragment, fragManager : FragmentManager? = null)
}