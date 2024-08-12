package travelmate.backend.dto;

import java.util.List;

public class SaveVisitedRequest {

    private List<String> contentIds;

    public List<String> getContentIds() {
        return contentIds;
    }

    public void setContentIds(List<String> contentIds) {
        this.contentIds = contentIds;
    }
}