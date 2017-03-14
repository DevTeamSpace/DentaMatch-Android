package com.appster.dentamatch.interfaces;

import com.appster.dentamatch.model.SubSkillModel;

import java.util.ArrayList;

/**
 * Created by bawenderyandra on 14/03/17.
 */

public interface OnSkillClick {
    void onItemSelected(ArrayList<SubSkillModel> subSkillList, int position);
}
