package com.hbsis.logging.log;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LogService {

    private static String scrollId;
    private final Logger LOGGER = LoggerFactory.getLogger(LogService.class);
    private final RestHighLevelClient client;
    private final Scroll scroll = new Scroll(TimeValue.timeValueSeconds(100l));

    public LogService(RestHighLevelClient client) {
        this.client = client;
    }

    public void getLog() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        es.submit(() -> {
            try {
                searchLogs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void searchLogs() throws InterruptedException, IOException {
        SearchRequest searchRequest = new SearchRequest(new String[]{"producao-logstash-05.03.2020", "producao-logstash-06.03.2020"});
        searchRequest.scroll(scroll);//setting timeout for 100 seconds
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10000);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        scrollId = searchResponse.getScrollId();
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

    private void getResult(SearchHit[] hits) throws InterruptedException {
        while (LogStack.stack.size() >= 50000) {
            Thread.sleep(30);
            continue;
        }

        LOGGER.info("Recebendo dados do banco. ScrollId: {}", scrollId);

        if (hits.length > 0) {
            Arrays.stream(hits).forEach(hit -> {
                LogStack.stack.push(hit.getSourceAsMap().get("log").toString());
            });
        }
    }

    public void getLogFromScrollId(String scrollId) {
        ExecutorService es = Executors.newFixedThreadPool(2);
        es.submit(() -> {
            try {
                searchFromScrollId(scrollId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void searchFromScrollId(String scrollId) throws IOException, InterruptedException {
        SearchHit[] hits = null;
        do {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            SearchResponse searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits().getHits();
            getResult(hits);

        } while (hits != null && hits.length > 0);
    }

}
