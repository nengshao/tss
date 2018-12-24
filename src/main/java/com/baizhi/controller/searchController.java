package com.baizhi.controller;

import com.baizhi.entity.Poet;
import com.baizhi.entity.Poetry;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/search")
@Controller
public class searchController {

    @RequestMapping("/search")
    public String search(String text, HttpServletRequest request,Integer pageIndex) throws IOException, ParseException,InvalidTokenOffsetsException {
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\笔记\\lucene资料\\代码\\05"));
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 查询解析器对象  作用 解析查询表达式  域名:条件
        // 参数一: 域名(默认域)
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = null;
        query = queryParser.parse("author:"+text);

        // 分页数据
        TopDocs topDocs = null;
        int pageSize = 5;//每页显示条数
        if(pageIndex == null||pageIndex==0){
            pageIndex = 1;
        }
        if (pageIndex == 1 || pageIndex < 1){
            // 假如说: 查第一页 每页5条
            topDocs = indexSearcher.search(query, pageSize);
        } else if( pageIndex > 1){
            // 假如说: 不是第一页 必须先获取上一页的最后一条记录的ScoreDoc
            topDocs = indexSearcher.search(query, (pageIndex-1) * pageSize);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            ScoreDoc sd = scoreDocs[scoreDocs.length-1];
            // 参数一: 当前页的上一页的最后的文档的ScoreDoc对象
            topDocs = indexSearcher.searchAfter(sd,query,pageSize);
        }
        // 总记录数
        int count = topDocs.totalHits;
        //页面总页数为
        int pageCount = 0;
        if(count % pageSize == 0){
            pageCount = count / pageSize;
        }else {
            pageCount = count / pageSize + 1;
        }
        // 创建高亮器对象
        Scorer scorer = new QueryScorer(query);
        // 默认高亮样式 加粗
        // 使用自定义的高亮样式
        Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:red\">","</span>");
        Highlighter highlighter = new Highlighter(formatter,scorer);
        List<Poetry> list = new ArrayList<Poetry>();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docID = scoreDoc.doc;
            //根据docid获取对象
            Document document = indexReader.document(docID);
            //获取高亮
            String highlighterBestFragment = highlighter.getBestFragment(new IKAnalyzer(), "author", document.get("author"));
            Poetry poetry = new Poetry();
            Poet poet = new Poet();
            //将获取的对象存到对象中
            String title = document.get("title");
            String content = document.get("content");
            poetry.setTitle(title);
            poetry.setContent(content);
            poet.setName(highlighterBestFragment);
            poetry.setPoet(poet);
            list.add(poetry);
        }
        request.setAttribute("list",list);
        request.setAttribute("count",count);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("text",text);
        indexReader.close();
        return "findAll";
    }

    public void t(){

    }
}
