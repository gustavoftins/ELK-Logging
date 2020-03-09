package com.hbsis.logging.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LogService {

    private final RestHighLevelClient client;
    private final Scroll scroll = new Scroll(TimeValue.timeValueSeconds(100l));

    private ObjectMapper objectMapper;

    public LogService(RestHighLevelClient client) {
        this.client = client;
    }

    public void getLog(String queryTag, String queryMatch) throws IOException {
        SearchRequest searchRequest = new SearchRequest("logapp");
        searchRequest.scroll(scroll);//setting timeout for 100 seconds
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(queryTag, queryMatch));//matches results using the specified tag and value
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        String scrollId = searchResponse.getScrollId();
        SearchHit[] hits = searchResponse.getHits().getHits();
        getResult(hits);

        do {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits().getHits();
            getResult(hits);

        } while (hits != null && hits.length > 0);

    }

    private void getResult(SearchHit[] hits) {
        List<String> logs = new ArrayList<>(10);//initial capacity is 10 beacause the search will return only 10 results by default

        if (hits.length > 0) {
            System.out.println(hits.length);
            Arrays.stream(hits).forEach(hit -> {
                LogStack.stack.push(hit.getSourceAsMap().get("logmessage").toString());
            });
        }
    }

    public Long count(String queryTag, String queryMatch) throws IOException {
        CountRequest countRequest = new CountRequest("logapp");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(4000);
        searchSourceBuilder.query(QueryBuilders.matchQuery(queryTag, queryMatch));
        countRequest.source(searchSourceBuilder);

        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

        return countResponse.getCount();
    }

}
