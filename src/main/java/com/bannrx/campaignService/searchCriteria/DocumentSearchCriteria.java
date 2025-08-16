package com.bannrx.campaignService.searchCriteria;

import com.bannrx.common.enums.DocumentStatus;
import com.bannrx.common.enums.DocumentType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import rklab.utility.models.searchCriteria.PageableSearchCriteria;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DocumentSearchCriteria extends PageableSearchCriteria {

    private String mimeType;
    private String filename;
    private Set<DocumentType> type;
    private Set<DocumentStatus> statuses;

    @Builder(builderMethodName = "documentSearchCriteriaBuilder")
    public DocumentSearchCriteria(
            final String mimeType,
            final String filename,
            final Set<DocumentType> type,
            final Set<DocumentStatus> statuses,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Sort.Direction sortDirection
    ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.mimeType = mimeType;
        this.filename = filename;
        this.type = type;
        this.statuses = statuses;
    }

}
