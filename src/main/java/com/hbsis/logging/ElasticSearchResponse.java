package com.hbsis.logging;

import java.util.List;

public class ElasticSearchResponse {

    private List<Log> hits;

    public ElasticSearchResponse(List<Log> hits) {
        this.hits = hits;
    }

    public ElasticSearchResponse() {
    }

    public List<Log> getHits() {
        return hits;
    }

    public void setHits(List<Log> hits) {
        this.hits = hits;
    }
}
