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
 * elasticsearch 工具类
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

    /*请求处理时间*/
    private static final String TIME_OUT = "20s";
    private static final String ADD = "ADD";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    /*数据插入主键ID*/
    private static final String ES_ID = "ES_ID";
    private static final String ENDPOINT = "/_sql?format=json";
    /*获取dsl语句*/
    private static final String ENDPOINT_TRANSLATE = "/_sql/translate";

    /**
     * 判断索引是否存在
     *
     * @param indexName 索引名称
     * @param document  id
     * @return true or false
     */
    public Boolean isIndexExists(String indexName, String document) {
        GetRequest getRequest = new GetRequest(indexName, document);
        // 禁用提取源
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        // 禁用提取存储字段
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
     * 判断索引是否存在
     *
     * @param indexName 索引名称
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
     * 向es中创建文档索引, 若es中已存在改index，则插入失败。
     *
     * @param indexName 索引名称
     * @param document  id
     * @param jsonStr   json字符串， 要存入的数据
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
                log.info("{} -- {} -- 插入成功", indexResponse.getIndex(), indexResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 批量处理数据
     *
     * @param indexName 索引名称
     * @param list      数据集合
     * @param type      类型 ADD-添加, UPDATE-更新, DELETE-删除
     */
    public Boolean bulkIndex(String indexName, List<Map<String, Object>> list, String type) {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TIME_OUT);
        boolean result = false;
        for (Map<String, Object> stringObjectMap : list) {
            //批量添加数据
            // TODO id可以自己设置，这里默认为ES_ID
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
                /*是否失败，如果是false则成功*/
                log.error("批量插入失败，原因: {}", bulkResponse.buildFailureMessage());
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
     * 查询指定id下索引数据
     *
     * @param indexName 索引名称
     * @param document  id
     * @return 返回得到的字符串, 没有返回null
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
     * 查询索引下的所有数据
     *
     * @param indexName 索引名称
     * @return 数组形式List<Map < String, Object>>； 没有返回null
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
     * 根据索引名称和id删除数据
     *
     * @param indexName 索引名称
     * @param document  id
     * @return 是否删除成功； 索引不存在返货false
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
                log.info("{} -- {} :已删除", deleteResponse.getIndex(), deleteResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 删除indexName下的所有索引数据
     *
     * @param indexName 索引名称
     * @return 删除是否成功。
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
     * 查询出所有的index
     *
     * @return 查询出所有的索引 Set<String>
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
     * 根据 index 和 id 更新 索引数据
     *
     * @param indexName 索引名称
     * @param document  id
     * @return 是否更新成功
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
     * 全量匹配查询
     *
     * @param field     属性
     * @param text      值
     * @param indexName 索引名称
     * @param startNum  开始的位置
     * @param pageSize  分页的大小
     * @return 返回符合条件的值
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
     * 模糊查询, 并实现高亮
     *
     * @param field     属性名，字段名称
     * @param text      value 希望高亮的值
     * @param indexName 索引名称
     * @return json
     */
    public String searchFuzzy(String field, String text, String indexName, Integer startNum, Integer pageSize) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        /*查询构造器*/
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*模糊查询 wildcardQuery*/
        /*wildcardQuery 是 term 级别的 query，经过分词器以后，text的值会被分开，根据倒排索引内容中就不会存在 完整text值，导致正常情况下查询不到数据，所以 field.keyword可以精准匹配*/
        WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery(field, "*" + text + "*");
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(startNum);
        searchSourceBuilder.size(pageSize);
        searchRequest.source(searchSourceBuilder);
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(field);
        //unified:使用Luncene同一的highlighter
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        /*自定义高亮标签*/
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        //设置为0即可返回完整内容 而非片段 TODO
        highlightBuilder.numOfFragments(0);
        searchSourceBuilder.highlighter(highlightBuilder);
        SearchResponse searchResponse;
        try {
            //获取结果
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            /*es返回内容 都在hits中*/
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                //处理高亮内容
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
     * 获取当前索引下的数据量
     *
     * @param indexName 索引名称
     * @return 数据量条数
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
     * 向es中创建文档索引, 若es中已存在改index，则插入失败。（异步）
     *
     * @param indexName 索引名称
     * @param document  id
     * @param jsonStr   json字符串， 要存入的数据
     */
    public void insertIndexWithJsonStrAsync(String indexName, String document, String jsonStr) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (!indexExists) {
            IndexRequest indexRequest = new IndexRequest(indexName).id(document).source(jsonStr, XContentType.JSON);
            ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    log.info("索引 {} -- {} 插入成功", indexResponse.getIndex(), indexResponse.getId());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("插入失败, 索引 {} -- {} 已存在", indexName, document);
        }
    }

    /**
     * 根据索引名称和id删除数据，（异步）
     *
     * @param indexName 索引名称
     * @param document  id
     * 是否删除成功； 索引不存在返货false
     */
    public void deleteIndexAsync(String indexName, String document) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (indexExists) {
            final DeleteRequest deleteRequest = new DeleteRequest(indexName, document);
            ActionListener<DeleteResponse> listener = new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse deleteResponse) {
                    log.info("{} -- {} 已删除", deleteResponse.getIndex() , deleteResponse.getId());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("索引 {} -- {} 不存在", indexName, document);
        }
    }

    /**
     * 删除indexName下的所有索引数据，（异步）
     *
     * @param indexName 索引名称
     * 删除是否成功。
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
            log.info("索引 {} 不存在", indexName);
        }
    }

    /**
     * 根据 index 和 id 更新 索引数据 （异步）
     *
     * @param indexName 索引名称
     * @param document  id
     * @param jsonStr   要更新的json
     * 是否更新成功
     */
    public void updateIndexAsync(String indexName, String document, String jsonStr) {
        Boolean indexExists = isIndexExists(indexName, document);
        if (indexExists) {
            final UpdateRequest updateRequest = new UpdateRequest(indexName, document).doc(jsonStr, XContentType.JSON);
            ActionListener<UpdateResponse> listener = new ActionListener<UpdateResponse>() {
                @Override
                public void onResponse(UpdateResponse updateResponse) {
                    log.info("索引 {} -- {} 更新成功", updateResponse.getIndex(),updateResponse.getId() );
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.updateAsync(updateRequest, RequestOptions.DEFAULT, listener);
        } else {
            log.info("索引 {} -- {} 不存在", indexName, document);
        }
    }

    /**
     * 索引重建(同步)。 注意：目标索引需要提前创建好
     *
     * @param fromIndex 重新索引的索引名
     * @param destIndex 重新索引后的索引名
     * @return 新创建的文档数
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
                log.info("fromIndex不存在");
            }
            if (!destIndexExists) {
                log.info("destIndex不存在");
            }
        }
        return result;
    }

    /**
     * 索引重建(异步)。 注意：目标索引需要提前创建好
     *
     * @param fromIndex 重新索引的索引名
     * @param destIndex 重新索引后的索引名
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
                    log.info("新创建的索引数: {}" ,bulkByScrollResponse.getCreated());
                    log.info("更新的索引数: {}" ,bulkByScrollResponse.getUpdated());
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            };
            client.reindexAsync(reindexRequest, RequestOptions.DEFAULT, actionListener);
        } else {
            if (!fromIndexExists) {
                log.info("fromIndex不存在");
            }
            if (!destIndexExists) {
                log.info("destIndex不存在");
            }
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        instance.close();
    }

    /**
     * sql查询
     *
     * @param sql    sql语句
     * @param size   查询数量
     * @param method 查询方式
     * @return 查询结果 json字符串
     */
    public String sqlQuery(String sql, Integer size, String method) throws IOException {
        Map<String, Object> postData = new HashMap<>();
        postData.put("query", sql);
        if (null != size) {
            postData.put("fetch_size", size);
        }
        HttpEntity entity = new NStringEntity(JSON.toJSONString(postData), ContentType.APPLICATION_JSON);
        //获取request对象
        Request request = new Request(method, ENDPOINT);
        request.setEntity(entity);
        Response response = restClient.performRequest(request);
        HttpEntity httpEntity = response.getEntity();
        return EntityUtils.toString(httpEntity);
    }

    /**
     * sql转化成DSL语句
     *
     * @param sql    sql语句
     * @param size   查询数量
     * @param method 查询方式
     * @return DSL语句 json字符串
     */
    public String sqlTranslate(String sql, Integer size, String method) throws IOException {
        HttpEntity entity = new NStringEntity("{\"query\":\"" + sql + "\"}", ContentType.APPLICATION_JSON);
        //获取request对象
        Request request = new Request(method, ENDPOINT_TRANSLATE);
        request.setEntity(entity);
        Response response = restClient.performRequest(request);
        HttpEntity httpEntity = response.getEntity();
        return EntityUtils.toString(httpEntity);
    }

    /**
     * Dsl语句执行
     *
     * @param Dsl       语句
     * @param indexName 索引名
     * TODO 直接执行Dsl语句 当前执行逻辑有问题，需要改进
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
