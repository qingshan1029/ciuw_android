package com.ciuwapp.helpers

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView

import java.text.DecimalFormat
import java.text.SimpleDateFormat

import com.ciuwapp.data.UserProfile

class AppHelper {
    companion object {
        var userProfile: UserProfile? = null
//        fun Get2DPCurrency(number: Double): String {
//            if(number < 0)
//                Log.e("GetDecimal", "Minus");
//            // val formatter = DecimalFormat("#,##0.00")
//            val formatter = DecimalFormat("#,###0.0;-#,###0.0")
//            val formattedNumber = formatter.format(number)
//            return formattedNumber
//        }
//        fun ConvertDate(date: String): String {
//            val sdf = SimpleDateFormat("yyyy-MM-dd")
//            val sdf1 = SimpleDateFormat("dd MMM yyyy")
//            val date = sdf.parse(date)
//            return sdf1.format(date)
//        }
//        fun SetLinearLayoutManagerAndHairlineDividerFor(
//            recyclerView: RecyclerView,
//            context: Context,
//            reversed: Boolean
//        ) {
//            if (recyclerView.itemDecorationCount > 0) {
//                recyclerView.removeItemDecoration(recyclerView.getItemDecorationAt(0))
//            }
//
//            var linearLayoutManager = LinearLayoutManager(context)
//            if(reversed)
//                linearLayoutManager.reverseLayout = true
//            recyclerView.layoutManager = linearLayoutManager
//
//            var dividerItemDecoration = DividerItemDecoration(
//                context,
//                linearLayoutManager.getOrientation()
//            )
//            ContextCompat.getDrawable(
//                context,
//                R.drawable.cell_hairline_divider
//            )?.let {
//                dividerItemDecoration.setDrawable(it)
//            }
//            recyclerView.addItemDecoration(dividerItemDecoration)
//        }
//        fun SetLinearLayoutManagerAndRemoveDividerFor(
//            recyclerView: RecyclerView,
//            context: Context
//        ) {
//            if (recyclerView.itemDecorationCount > 0) {
//                recyclerView.removeItemDecoration(recyclerView.getItemDecorationAt(0))
//            }
//
//            var linearLayoutManager = LinearLayoutManager(context)
//            recyclerView.layoutManager = linearLayoutManager
//
//            var dividerItemDecoration = DividerItemDecoration(
//                context,
//                linearLayoutManager.getOrientation()
//            )
//            ContextCompat.getDrawable(
//                context,
//                R.drawable.cell_hairline_divider
//            )?.let {
//                dividerItemDecoration.setDrawable(it)
//            }
////            recyclerView.addItemDecoration(DividerItemDecoration(
////                context,
////                LinearLayoutManager.HORIZONTAL
////            ))
////            recyclerView.addItemDecoration(dividerItemDecoration)
//        }

        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}