package com.bannrx.campaignService.repository.specifications;

import com.bannrx.campaignService.searchCriteria.DocumentSearchCriteria;
import com.bannrx.common.enums.DocumentStatus;
import com.bannrx.common.enums.DocumentType;
import com.bannrx.common.persistence.entities.Document;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

import static rklab.utility.constants.GlobalConstants.Symbols.PERCENTAGE;

public class DocumentSpecification {

    private static final String MIME_TYPE = "mimeType";
    private static final String TYPE = "type";
    private static final String STATUS = "status";
    private static final String FILENAME = "fileName";

    public static Specification<Document> buildSearchCriteria(final DocumentSearchCriteria searchCriteria){
        var retVal = Specification.<Document>where(null);
        if (StringUtils.isNotBlank(searchCriteria.getMimeType())){
            retVal = retVal.and(filterByMimeTypeLike(searchCriteria.getMimeType()));
        }
        if (CollectionUtils.isNotEmpty(searchCriteria.getType())){
            retVal = retVal.and(filterByTypeIn(searchCriteria.getType()));
        }
        if (CollectionUtils.isNotEmpty(searchCriteria.getStatuses())){
            retVal = retVal.and(filterByStatusIn(searchCriteria.getStatuses()));
        }
        if (StringUtils.isNotBlank(searchCriteria.getFilename())){
            retVal = retVal.and(filterByFilenameLike(searchCriteria.getFilename()));
        }
        return retVal;
    }

    private static Specification<Document> filterByMimeTypeLike(final String mimeType){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(MIME_TYPE), withLikePattern(mimeType))
        );
    }

    private static Specification<Document> filterByTypeIn(final Set<DocumentType> types){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(TYPE)).value(types)
        );
    }

    private static Specification<Document> filterByStatusIn(final Set<DocumentStatus> status){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(STATUS)).value(status)
        );
    }

    private static Specification<Document> filterByFilenameLike(final String fileName){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(FILENAME), withLikePattern(fileName))
        );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }
}
