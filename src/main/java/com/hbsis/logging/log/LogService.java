package com.hbsis.logging.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LogService {

    private final RestHighLevelClient client;

    private ObjectMapper objectMapper;

    public LogService(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Log> getLog() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        return getResult(client.search(searchRequest, RequestOptions.DEFAULT));
    }

    private List<Log> getResult(SearchResponse searchResponse) {
        SearchHit[] hits = searchResponse.getHits().getHits();

        List<Log> logs = new ArrayList<>();

        if (hits.length > 0) {
            System.out.println(hits.length);
            Arrays.stream(hits).forEach(hit -> {
                Map<String, Object> map = hit.getSourceAsMap();
                map.forEach((key, value) -> System.out.println("key:"+key+"  ---  valor:"+value));
            });
        }
        return logs;
    }

}
