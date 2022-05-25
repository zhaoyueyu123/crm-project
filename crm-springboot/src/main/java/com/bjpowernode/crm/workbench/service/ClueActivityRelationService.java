package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationList(List<ClueActivityRelation> activityRelations);

    int removeClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);
}
