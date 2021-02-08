package com.dimfcompany.aknewsclient.logic.utils.builders

import android.content.Context
import android.content.DialogInterface
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.extensions.getColorMy
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import java.util.*

class BuilderDateDialog()
{
    var date_min: Date? = null
        private set

    var date_max: Date? = null
        private set

    var date_current: Date? = null
        private set

    var action_success: ((Date) -> Unit)? = null
        private set

    fun setDateMin(date: Date?) = apply(
        {
            this.date_min = date
        })

    fun setDateMax(date: Date?) = apply(
        {
            this.date_max = date
        })

    fun setDateCurrent(date: Date?) = apply(
        {
            this.date_current = date
        })

    fun setActionSuccess(action: (Date) -> Unit) = apply(
        {
            this.action_success = action
        })

    fun show(context: Context)
    {
        var builder = SpinnerDatePickerDialogBuilder()
                .context(context)
                .callback(
                    { view, year, monthOfYear, dayOfMonth ->
                        val calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
                        val date = calendar.time
                        action_success?.invoke(date)
                    })
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .showDaySpinner(true)

        if (date_min != null)
        {
            val calendar_min = Calendar.getInstance().apply({ this.time = date_min!! })
            builder = builder.minDate(calendar_min.get(Calendar.YEAR), calendar_min.get(Calendar.MONTH), calendar_min.get(Calendar.DAY_OF_MONTH))
        }

        if (date_current != null)
        {
            val calendar_current = Calendar.getInstance().apply({ this.time = date_current!! })
            builder = builder.defaultDate(calendar_current.get(Calendar.YEAR), calendar_current.get(Calendar.MONTH), calendar_current.get(Calendar.DAY_OF_MONTH))
        }

        if (date_max != null)
        {
            val calendar_max = Calendar.getInstance().apply({ this.time = date_max!! })
            builder = builder.maxDate(calendar_max.get(Calendar.YEAR), calendar_max.get(Calendar.MONTH), calendar_max.get(Calendar.DAY_OF_MONTH))
        }

        val dialog = builder.build()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColorMy(R.color.red))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColorMy(R.color.red))
    }

    fun sendInVm(base_vm: BaseVm)
    {
        base_vm.ps_to_show_date_dialog.onNext(this)
    }
}