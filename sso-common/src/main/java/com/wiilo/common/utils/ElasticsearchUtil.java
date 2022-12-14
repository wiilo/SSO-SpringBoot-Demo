package com.wiilo.common.utils;

import com.alibaba.fastjson.JSON;
import com.wiilo.common.config.ElasticsearchConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * elasticsearch ?????????
 *
 * @author Whitlock Wang
 */
@Component
@Slf4j
public class ElasticsearchUtil {

    @Resource
    private RestHighLevelClient client;
    
    @Resource
    private RestClient restClient;
    
    @Resource
    private ElasticsearchConfig instance;

    /*??????????????????*/
    private static final String TIME_OUT = "20s";
    private static final String ADD = "ADD";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    /*??????????????????ID*/
    private static final String ES_ID = "ES_ID";
    private static final String ENDPOINT = "/_sql?format=json";
    /*??????dsl??????*/
    private static final String ENDPOINT_TRANSLATE = "/_sql/translate";

    /**
     * ????????????????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @return true or false
     */
    public Boolean isIndexExists(String indexName, String document) {
        GetRequest getRequest = new GetRequest(indexName, document);
        // ???????????????
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        // ????????????????????????
        getRequest.storedFields("_none_");
        boolean exists = false;
        try {
            exists = client.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * ????????????????????????
     *
     * @param indexName ????????????
     * @return true or false
     */
    public Boolean isIndexExists(String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = false;
        try {
            exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * ???es?????????????????????, ???es???????????????index?????????????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @param jsonStr   json???????????? ??????????????????
     */
    public Boolean insertIndexWithJsonStr(String indexName, String document, String jsonStr) {
        Boolean indexExists = isIndexExists(indexName, document);
        boolean result = false;
        if (!indexExists) {
            IndexRequest indexRequest = new IndexRequest(indexName).id(document).source(jsonStr, XContentType.JSON);
            IndexResponse indexResponse;
            try {
                indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    result = true;
                }
                log.info("{} -- {} -- ????????????", indexResponse.getIndex(), indexResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param indexName ????????????
     * @param list      ????????????
     * @param type      ?????? ADD-??????, UPDATE-??????, DELETE-??????
     */
    public Boolean bulkIndex(String indexName, List<Map<String, Object>> list, String type) {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TIME_OUT);
        boolean result = false;
        for (Map<String, Object> stringObjectMap : list) {
            //??????????????????
            // TODO id????????????????????????????????????ES_ID
            if (ADD.equals(type)) {
                IndexRequest source = new IndexRequest(indexName).id(stringObjectMap.get(ES_ID).toString()).source(stringObjectMap);
                bulkRequest.add(source);
            } else if (UPDATE.equals(type)) {
                UpdateRequest updateRequest = new UpdateRequest(indexName, stringObjectMap.get(ES_ID).toString()).doc(stringObjectMap);
                bulkRequest.add(updateRequest);
            } else if (DELETE.equals(type)) {
                DeleteRequest deleteRequest = new DeleteRequest(indexName, stringObjectMap.get(ES_ID).toString());
                bulkRequest.add(deleteRequest);
            }
        }
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                /*????????????????????????false?????????*/
                log.error("???????????????????????????: {}", bulkResponse.buildFailureMessage());
                return false;
            } else {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ????????????id???????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @return ????????????????????????, ????????????null
     * {"age":18,"id":1,"name":"xuchenglei"}
     */
    public String queryIndex(String indexName, String document) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (indexExists) {
            GetResponse getResponse = null;
            GetRequest getRequest = new GetRequest(indexName, document);
            try {
                getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return getResponse == null ? null : getResponse.getSourceAsString();
        } else {
            return null;
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param indexName ????????????
     * @return ????????????List<Map < String, Object>>??? ????????????null
     * [{"name":"xuchenglei","id":1,"age":18},{"name":"xuchenglei","id":2,"age":18}]
     */
    public List<Map<String, Object>> queryIndex(String indexName, Integer startNum, Integer pageSize) {
        Boolean indexExists = isIndexExists(indexName);
        if (!indexExists) {
            return null;
        }
        SearchResponse searchResponse = null;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        searchSourceBuilder.from(startNum);
        searchSourceBuilder.size(pageSize);
        searchRequest.source(searchSourceBuilder);
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == searchResponse) {
            return null;
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<Map<String, Object>> arrayListMap = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            arrayListMap.add(sourceAsMap);
        }
        return arrayListMap;
    }

    /**
     * ?????????????????????id????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @return ????????????????????? ?????????????????????false
     */
    public Boolean deleteIndex(String indexName, String document) {
        Boolean indexExists = isIndexExists(indexName, document);
        boolean result = false;
        if (indexExists) {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, document);
            DeleteResponse deleteResponse;
            try {
                deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
                if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
                    result = true;
                }
                log.info("{} -- {} :?????????", deleteResponse.getIndex(), deleteResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ??????indexName????????????????????????
     *
     * @param indexName ????????????
     * @return ?????????????????????
     */
    public Boolean deleteIndex(String indexName) {
        Boolean indexExists = isIndexExists(indexName);
        boolean result = false;
        if (indexExists) {
            DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse acknowledgedResponse;
            try {
                acknowledgedResponse = client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
                if (acknowledgedResponse.isAcknowledged()) {
                    result = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ??????????????????index
     *
     * @return ???????????????????????? Set<String>
     */
    public Set<String> queryAllIndex() {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
        Set<String> indexNameKeySet = new HashSet<>();
        try {
            GetAliasesResponse getAliasesResponse = client.indices().getAlias(getAliasesRequest, RequestOptions.DEFAULT);
            Set<String> keySet = getAliasesResponse.getAliases().keySet();
            for (String s : keySet) {
                if (!s.startsWith(".")) {
                    indexNameKeySet.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexNameKeySet;
    }

    /**
     * ?????? index ??? id ?????? ????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @return ??????????????????
     */
    public Boolean updateIndex(String indexName, String document, String jsonStr) {
        Boolean indexExists = isIndexExists(indexName, document);
        boolean result = false;
        if (indexExists) {
            UpdateRequest updateRequest = new UpdateRequest(indexName, document).doc(jsonStr, XContentType.JSON);
            UpdateResponse updateResponse;
            try {
                updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
                if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    result = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param field     ??????
     * @param text      ???
     * @param indexName ????????????
     * @param startNum  ???????????????
     * @param pageSize  ???????????????
     * @return ????????????????????????
     */
    public String search(String field, String text, String indexName, Integer startNum, Integer pageSize) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.query(QueryBuilders.termQuery(field, text));
        searchSourceBuilder.from(startNum);
        searchSourceBuilder.size(pageSize);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                resultMapList.add(sourceAsMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMapList);
    }

    /**
     * ????????????, ???????????????
     *
     * @param field     ????????????????????????
     * @param text      value ??????????????????
     * @param indexName ????????????
     * @return json
     */
    public String searchFuzzy(String field, String text, String indexName, Integer startNum, Integer pageSize) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        /*???????????????*/
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*???????????? wildcardQuery*/
        /*wildcardQuery ??? term ????????? query???????????????????????????text??????????????????????????????????????????????????????????????? ??????text?????????????????????????????????????????????????????? field.keyword??????????????????*/
        WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery(field, "*" + text + "*");
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(startNum);
        searchSourceBuilder.size(pageSize);
        searchRequest.source(searchSourceBuilder);
        // ????????????
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(field);
        //unified:??????Luncene?????????highlighter
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        /*?????????????????????*/
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        //?????????0???????????????????????? ???????????? TODO
        highlightBuilder.numOfFragments(0);
        searchSourceBuilder.highlighter(highlightBuilder);
        SearchResponse searchResponse;
        try {
            //????????????
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            /*es???????????? ??????hits???*/
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                //??????????????????
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlightField = highlightFields.get(field);
                Text[] fragments = highlightField.fragments();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                sourceAsMap.put(field, fragments[0].string());
                resultMapList.add(sourceAsMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMapList);
    }

    /**
     * ?????????????????????????????????
     *
     * @param indexName ????????????
     * @return ???????????????
     */
    public Long searchTotalHitsNum(String indexName) {
        Boolean indexExists = isIndexExists(indexName);
        long result = -1L;
        if (indexExists) {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchResponse searchResponse;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                result = searchResponse.getHits().getTotalHits().value;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ???es?????????????????????, ???es???????????????index?????????????????????????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @param jsonStr   json???????????? ??????????????????
     */
    public void insertIndexWithJsonStrAsync(String indexName, String document, String jsonStr) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (!indexExists) {
            IndexRequest indexRequest = new IndexRequest(indexName).id(document).source(jsonStr, XContentType.JSON);
            ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    log.info("?????? {} -- {} ????????????", indexResponse.getIndex(), indexResponse.getId());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("????????????, ?????? {} -- {} ?????????", indexName, document);
        }
    }

    /**
     * ?????????????????????id???????????????????????????
     *
     * @param indexName ????????????
     * @param document  id
     * ????????????????????? ?????????????????????false
     */
    public void deleteIndexAsync(String indexName, String document) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (indexExists) {
            final DeleteRequest deleteRequest = new DeleteRequest(indexName, document);
            ActionListener<DeleteResponse> listener = new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse deleteResponse) {
                    log.info("{} -- {} ?????????", deleteResponse.getIndex() , deleteResponse.getId());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("?????? {} -- {} ?????????", indexName, document);
        }
    }

    /**
     * ??????indexName???????????????????????????????????????
     *
     * @param indexName ????????????
     * ?????????????????????
     */
    public void deleteIndexAsync(String indexName) {
        Boolean indexExists = isIndexExists(indexName);
        if (indexExists) {
            final DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
            ActionListener<AcknowledgedResponse> listener = new ActionListener<AcknowledgedResponse>() {
                @Override
                public void onResponse(AcknowledgedResponse AcknowledgedResponse) {
                    log.info(AcknowledgedResponse.toString());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.indices().deleteAsync(deleteRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("?????? {} ?????????", indexName);
        }
    }

    /**
     * ?????? index ??? id ?????? ???????????? ????????????
     *
     * @param indexName ????????????
     * @param document  id
     * @param jsonStr   ????????????json
     * ??????????????????
     */
    public void updateIndexAsync(String indexName, String document, String jsonStr) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (indexExists) {
            final UpdateRequest updateRequest = new UpdateRequest(indexName, document).doc(jsonStr, XContentType.JSON);
            ActionListener<UpdateResponse> listener = new ActionListener<UpdateResponse>() {
                @Override
                public void onResponse(UpdateResponse updateResponse) {
                    log.info("?????? {} -- {} ????????????", updateResponse.getIndex(),updateResponse.getId() );
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.updateAsync(updateRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("?????? {} -- {} ?????????", indexName, document);
        }
    }

    /**
     * ????????????(??????)??? ??????????????????????????????????????????
     *
     * @param fromIndex ????????????????????????
     * @param destIndex ???????????????????????????
     * @return ?????????????????????
     */
    public Long reIndex(String fromIndex, String destIndex) {
        Boolean fromIndexExists = isIndexExists(fromIndex);
        Boolean destIndexExists = isIndexExists(destIndex);
        long result = 0L;
        if (fromIndexExists && destIndexExists) {
            ReindexRequest reindexRequest = new ReindexRequest();
            reindexRequest.setSourceIndices(fromIndex);
            reindexRequest.setDestIndex(destIndex);
            BulkByScrollResponse reindexResponse = null;
            try {
                reindexResponse = client.reindex(reindexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reindexResponse != null) {
                result = reindexResponse.getCreated();
            }
        } else {
            if (!fromIndexExists) {
                log.info("fromIndex?????????");
            }
            if (!destIndexExists) {
                log.info("destIndex?????????");
            }
        }
        return result;
    }

    /**
     * ????????????(??????)??? ??????????????????????????????????????????
     *
     * @param fromIndex ????????????????????????
     * @param destIndex ???????????????????????????
     */
    public void reIndexAsync(String fromIndex, String destIndex) {
        Boolean fromIndexExists = isIndexExists(fromIndex);
        Boolean destIndexExists = isIndexExists(destIndex);
        if (fromIndexExists && destIndexExists) {
            ReindexRequest reindexRequest = new ReindexRequest();
            reindexRequest.setSourceIndices(fromIndex);
            reindexRequest.setDestIndex(destIndex);
            ActionListener<BulkByScrollResponse> actionListener = new ActionListener<BulkByScrollResponse>() {
                @Override
                public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
                    log.info("?????????????????????: {}" ,bulkByScrollResponse.getCreated());
                    log.info("??????????????????: {}" ,bulkByScrollResponse.getUpdated());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.reindexAsync(reindexRequest, RequestOptions.DEFAULT, actionListener);
        } else {
            if (!fromIndexExists) {
                log.info("fromIndex?????????");
            }
            if (!destIndexExists) {
                log.info("destIndex?????????");
            }
        }
    }

    /**
     * ????????????
     */
    public void close() {
        instance.close();
    }

    /**
     * sql??????
     *
     * @param sql    sql??????
     * @param size   ????????????
     * @param method ????????????
     * @return ???????????? json?????????
     */
    public String sqlQuery(String sql, Integer size, String method) throws IOException {
        Map<String, Object> postData = new HashMap<>();
        postData.put("query", sql);
        if (null != size) {
            postData.put("fetch_size", size);
        }
        HttpEntity entity = new NStringEntity(JSON.toJSONString(postData), ContentType.APPLICATION_JSON);
        //??????request??????
        Request request = new Request(method, ENDPOINT);
        request.setEntity(entity);
        Response response = restClient.performRequest(request);
        HttpEntity httpEntity = response.getEntity();
        return EntityUtils.toString(httpEntity);
    }

    /**
     * sql?????????DSL??????
     *
     * @param sql    sql??????
     * @param size   ????????????
     * @param method ????????????
     * @return DSL?????? json?????????
     */
    public String sqlTranslate(String sql, Integer size, String method) throws IOException {
        HttpEntity entity = new NStringEntity("{\"query\":\"" + sql + "\"}", ContentType.APPLICATION_JSON);
        //??????request??????
        Request request = new Request(method, ENDPOINT_TRANSLATE);
        request.setEntity(entity);
        Response response = restClient.performRequest(request);
        HttpEntity httpEntity = response.getEntity();
        return EntityUtils.toString(httpEntity);
    }

    /**
     * Dsl????????????
     *
     * @param Dsl       ??????
     * @param indexName ?????????
     * TODO ????????????Dsl?????? ??????????????????????????????????????????
     */
    public String DslExecute(String Dsl, String indexName) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        Request request = new Request("POST", Dsl.trim());
        try {
            Response response = restClient.performRequest(request);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            System.out.println(result);
            //            SearchHit[] hits = search.getHits().getHits();
            //            for (SearchHit hit : hits) {
            //                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //                resultMapList.add(sourceAsMap);
            //                System.out.println(sourceAsMap);
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return JSON.toJSONString(resultMapList);
    }

}
