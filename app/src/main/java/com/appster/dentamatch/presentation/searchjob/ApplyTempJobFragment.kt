package com.appster.dentamatch.presentation.searchjob


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.appster.dentamatch.R
import com.appster.dentamatch.model.JobDetailModel
import kotlinx.android.synthetic.main.fragment_apply_temp_job.*
import kotlinx.android.synthetic.main.toolbar_general.*
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApplyTempJobFragment : Fragment() {

    companion object {
        const val TAG = "ApplyTempJobFragment"
        private const val ARG_JOB_DETAIL_MODEL = "JOB_DETAIL_MODEL"
        private const val ARG_NOTIFICATION_ID = "NOTIFICATION_ID"

        fun newInstance(notificationId: Int, jobDetailModel: JobDetailModel): ApplyTempJobFragment =
                ApplyTempJobFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_JOB_DETAIL_MODEL, jobDetailModel)
                        putInt(ARG_NOTIFICATION_ID, notificationId)
                    }
                }
    }

    private val adapter = ApplyTempJobAdapter(ApplyTempJobItemCallback())

    private var callback: ApplyTempJobCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? ApplyTempJobCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_apply_temp_job, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTempJobRecycler.adapter = adapter
        arguments?.getParcelable<JobDetailModel>(ARG_JOB_DETAIL_MODEL)?.also {
            adapter.submitList(createDaysList(it))
        }
        applyTempJobButton.setOnClickListener {
            callback?.onDatesSelected(arguments?.getInt(ARG_NOTIFICATION_ID) ?: 0,
                    adapter.getSelectedDates())
            activity?.onBackPressed()
        }
        ivToolBarLeft.setOnClickListener { activity?.onBackPressed() }
        txvToolbarGeneralCenter.text = getString(R.string.header_job_detail)
    }

    private fun createDaysList(jobDetailModel: JobDetailModel) =
            ArrayList<ApplyTempJobModel>().apply {
                val dates = jobDetailModel.jobTypeDates ?: ArrayList()
                for (date in dates) {
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                    val dateFormat = SimpleDateFormat("(MMMM d, yyyy)", Locale.getDefault())
                    val d1 = format.parse(date)
                    add(ApplyTempJobModel(dayFormat.format(d1), dateFormat.format(d1), date))
                }
            }
}
