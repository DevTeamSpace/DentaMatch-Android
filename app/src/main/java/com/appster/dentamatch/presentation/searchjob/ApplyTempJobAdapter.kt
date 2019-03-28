package com.appster.dentamatch.presentation.searchjob

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appster.dentamatch.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_apply_temp_job.*

class ApplyTempJobAdapter(callback: DiffUtil.ItemCallback<ApplyTempJobModel>)
    : ListAdapter<ApplyTempJobModel, ApplyTempJobAdapter.ApplyTempJobViewHolder>(callback) {

    private var items: MutableList<ApplyTempJobModel>? = null

    fun getSelectedDates() = ArrayList<String>().apply {
        items?.forEach {
            if (it.checked) {
                add(it.rawDateString)
            }
        }
    }

    override fun submitList(list: MutableList<ApplyTempJobModel>?) {
        super.submitList(list)
        items = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
            ApplyTempJobViewHolder(
                    LayoutInflater.from(p0.context)
                            .inflate(R.layout.item_apply_temp_job, p0, false)
            )

    override fun onBindViewHolder(p0: ApplyTempJobViewHolder, p1: Int) = p0.bind(getItem(p1))

    class ApplyTempJobViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(applyTempJobModel: ApplyTempJobModel) {
            itemApplyTempJobCheckbox.setOnCheckedChangeListener(null)
            itemApplyTempJobCheckbox.isChecked = applyTempJobModel.checked
            itemApplyTempJobCheckbox.setOnCheckedChangeListener { _, isChecked ->
                applyTempJobModel.checked = isChecked
            }
            itemApplyTempJobDay.text = applyTempJobModel.day
            itemApplyTempJobDate.text = applyTempJobModel.date
        }
    }
}