package Lakshmi.core.querybuilder;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Session;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component(service = Servlet.class, immediate = true, enabled = true)
@SlingServletResourceTypes(resourceTypes = "demo/querybuilder/Lakshmidhar")
public class DemoQueryBuilderServlet extends SlingAllMethodsServlet {


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Resource resource = request.getResource();
        ResourceResolver resolver=request.getResourceResolver();
        Session session = resolver.adaptTo(Session.class);
        try{
            JsonArray resultArray = new JsonArray();
            Map<String,String> map = new HashMap<>();
            map.put("type","cq:Component");
            map.put("path","/content/Lakshmidhar");
            QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
            Query query = queryBuilder.createQuery(new PredicateGroup().create(map),session);
            SearchResult searchResult = query.getResult();
            List<Hit> hits =searchResult.getHits();

            for (Hit hit : hits) {
                JsonObject objPage = new JsonObject();
                objPage.addProperty("path",hit.getPath());
                resultArray.add(objPage);

            }
            response.setContentType("application/json");
            response.getWriter().write(resultArray.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}