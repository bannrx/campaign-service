package com.bannrx.campaignService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteDocumentResponse {

    private Set<String> success;
    private Set<DeleteResponse> failed;
    private int failedCount;

    @Data
    @Builder
    private static class DeleteResponse{
        private String id;
        private String reason;
    }


    @JsonIgnore
    public void appendSuccess(final String id){
        var existing = Optional.ofNullable(this.success)
                .orElse(new LinkedHashSet<>());
        existing.add(id);
        setSuccess(existing);
    }

    @JsonIgnore
    public void appendFailed(final String id, final String reason){
        var existing = Optional.ofNullable(this.failed)
                .orElse(new LinkedHashSet<>());
        existing.add(DeleteResponse.builder()
                        .id(id)
                        .reason(reason)
                .build());
        setFailed(existing);
        this.failedCount = this.failedCount + 1;
    }
}
