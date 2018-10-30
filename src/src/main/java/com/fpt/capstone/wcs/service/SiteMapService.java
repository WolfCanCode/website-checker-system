package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.SiteLinkPOJO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class SiteMapService {
    private HashMap<String, Integer> urlMap = new HashMap<String, Integer>();
    private String rootDomain = "";
    private List<List<SiteLinkPOJO>> graph = new ArrayList<List<SiteLinkPOJO>>();
    private List<List<SiteLinkPOJO>> invGraph = new ArrayList<List<SiteLinkPOJO>>();
    private int verticesNum;
    private List<Integer> typeNode = new ArrayList<Integer>(); //Type link: 1 is internal, 2 is external and 3 is error internal link
    private List<String> links = new ArrayList<String>();

    // init site-map with 'root Domain' link
    public SiteMapService(String rootDomain) {
        this.rootDomain = rootDomain;
        urlMap.clear();
        invGraph.clear();
        verticesNum = 0;
    }

    public List<String> crawlHtmlSource(String url) throws MalformedURLException {
        int nodeId = urlMap.get(url);
        List<String> newURL = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements aElements = doc.getElementsByTag("a");
            for (Element e : aElements) {
                String candidateUrl = e.attr("href");
                if (candidateUrl.length() > 0 && candidateUrl.charAt(0) == '/') {
                    candidateUrl = rootDomain + candidateUrl;
                }
                if (candidateUrl.length() > 0)
                    newURL.add(candidateUrl);
            }
        } catch (IOException ex) {
            typeNode.set(nodeId, 3);
            System.out.println("Error: occur when crawling site:  " + url);
            ex.printStackTrace();
            System.out.println("=======");
        }
        return newURL;
    }

    // list all page that url direct to
    public void getLinks(String url) {
        int mapId = urlMap.get(url);
        List<SiteLinkPOJO> list = graph.get(mapId);
        for (SiteLinkPOJO siteLink : list) {
            System.out.println(siteLink.getDesUrl() + " " + siteLink.getDesType());
        }
    }


    public void buildSiteMap() throws MalformedURLException {
        Queue<String> queue = new LinkedList<String>();

        queue.add(rootDomain);
        urlMap.put(rootDomain, verticesNum);
        typeNode.add(1);
        verticesNum++;
        graph.add(new ArrayList<SiteLinkPOJO>());
        links.add(rootDomain);

        while (!queue.isEmpty()) {
            String curUrl = queue.peek();
            int mapId = urlMap.get(curUrl);
            queue.remove();
            List<String> newUrl = crawlHtmlSource(curUrl);
            int deepLimit = 0;

            for (String url : newUrl) {
                deepLimit++;
                if (deepLimit > 30) break;
                url = url.trim();
                if (!urlMap.containsKey(url)) {
                    // add to map & graph
                    urlMap.put(url, verticesNum);
                    verticesNum++;
                    graph.add(new ArrayList<SiteLinkPOJO>());
                    if (url.contains(rootDomain)) {
                        // internal
                        SiteLinkPOJO sl = new SiteLinkPOJO(curUrl, url, 1);
                        graph.get(mapId).add(sl);
                        queue.add(url);
                        typeNode.add(1);
                        links.add(url);
                    } else {
                        // external
                        SiteLinkPOJO sl = new SiteLinkPOJO(curUrl, url, 2);
                        graph.get(mapId).add(sl);
                        typeNode.add(2);
                        links.add(url);
                    }
                    if (verticesNum >= 200) {
                        return;
                    }
                } else {
                    // existed node
                    int mapCurUrl = urlMap.get(url);
                    int typeCurUrl = typeNode.get(mapCurUrl);
                    SiteLinkPOJO sl = new SiteLinkPOJO(curUrl, url, typeCurUrl);
                    graph.get(mapId).add(sl);
                }
            }
        }
    }

    public List<String> getDecodeGraph() {
        int root = 0;
        List<String> rs = new ArrayList<String>();
        HashSet<Integer> hashSet = new HashSet<Integer>();
        ArrayList<Integer> degList = new ArrayList<Integer>();
        degList.add(root);
        hashSet.add(root);
        // build a 'codeMap'
        List<List<Integer>> codeMap = new ArrayList<List<Integer>>();
        // build a 'idMap'
        List<List<Integer>> idMap = new ArrayList<List<Integer>>();
        // initial
        codeMap.add(new ArrayList<Integer>(Arrays.asList(0)));
        idMap.add(new ArrayList<Integer>(Arrays.asList(0)));

        while (true) {
            ArrayList<Integer> newList = new ArrayList<Integer>();
            ArrayList<Integer> subIdMap = new ArrayList<Integer>();
            int counting = 1;
            List<Integer> addition = new ArrayList<Integer>();
            for (int j = 0; j < degList.size(); j++) {
                int node = degList.get(j);
                for (int i = 0; i < graph.get(node).size(); i++) {
                    SiteLinkPOJO siteLink = graph.get(node).get(i);
                    String toUrl = siteLink.getDesUrl();
                    int toUrlId = urlMap.get(toUrl);
                    if (!hashSet.contains(toUrlId)) {
                        newList.add(toUrlId);
                        addition.add(counting);
                        hashSet.add(toUrlId);
                        // addition: subIdList
                        subIdMap.add(toUrlId);
                    }
                }
                counting++;
            }
            if (newList.size() == 0) break;
            degList.clear();
            for (int j = 0; j < newList.size(); j++)
                degList.add(newList.get(j));
            newList.clear();
            codeMap.add(addition);
            idMap.add(subIdMap);
        }
        // Initial 'typeMap', 'urlMap' (future)
        List<List<Integer>> typeMap = new ArrayList<List<Integer>>();
        List<List<String>> urlMap = new ArrayList<List<String>>();
        // Convert: 'codeMap' to 'genMap' by adding '0'
        List<List<Integer>> genMap = new ArrayList<List<Integer>>();
        genMap.add(new ArrayList<Integer>(Arrays.asList(1)));
        typeMap.add(new ArrayList<Integer>(Arrays.asList(typeNode.get(0))));
        urlMap.add(new ArrayList<String>(Arrays.asList(rootDomain)));

        for (int i = 1; i < codeMap.size(); i++) {
            List<Integer> subGenMap = new ArrayList<Integer>();
            List<Integer> subTypeMap = new ArrayList<Integer>();
            List<String> subUrlMap = new ArrayList<String>();

            int count = 0;
            int pos = 0;

            for (int j = 0; j < genMap.get(i - 1).size(); j++) {
                if (genMap.get(i - 1).get(j) > 0) count++;
                boolean isHave = false;
                while (pos < codeMap.get(i).size() && codeMap.get(i).get(pos) == count) {
                    subGenMap.add(codeMap.get(i).get(pos));
                    subTypeMap.add(typeNode.get(idMap.get(i).get(pos)));
                    subUrlMap.add(links.get(idMap.get(i).get(pos)));
                    pos++;
                    isHave = true;
                }
                if (!isHave) {
                    subGenMap.add(0);
                    subTypeMap.add(0);
                    subUrlMap.add("");
                }
            }
            genMap.add(subGenMap);
            typeMap.add(subTypeMap);
            urlMap.add(subUrlMap);
        }

        // Generate structure code
        String codeGraph = "";

        System.out.println("Finishing:");
        for (int i = 0; i < genMap.size(); i++) {
            String subCode = "[";
            for (int j = 0; j < genMap.get(i).size(); j++) {
                subCode = subCode + genMap.get(i).get(j);
                if (j < genMap.get(i).size() - 1) subCode = subCode + ", ";
            }
            subCode = subCode + "]";
            if (i < genMap.size() - 1) subCode = subCode + ", ";
            codeGraph = codeGraph + subCode;
        }

        // code of graph
        System.out.println(codeGraph);
        rs.add(codeGraph);
        String typeCodeGraph = "";

        // Generate structure of 'type'
        for (int i = 0; i < typeMap.size(); i++) {
            String subCode = "[";
            for (int j = 0; j < typeMap.get(i).size(); j++) {
                subCode = subCode + typeMap.get(i).get(j);
                if (j < typeMap.get(i).size() - 1) subCode = subCode + ", ";
            }
            subCode = subCode + "]";
            if (i < typeMap.size() - 1) subCode = subCode + ", ";
            typeCodeGraph = typeCodeGraph + subCode;
        }
        System.out.println(typeCodeGraph);
        rs.add(typeCodeGraph);

        // Generate structure of 'url'
        String urlCodeGraph = "";
        // Generate structure of 'type'
        for (int i = 0; i < urlMap.size(); i++) {
            String subCode = "[";
            for (int j = 0; j < urlMap.get(i).size(); j++) {
                subCode = subCode + "\'" + urlMap.get(i).get(j) + "\'";
                if (j < urlMap.get(i).size() - 1) subCode = subCode + ", ";
            }
            subCode = subCode + "]";
            if (i < urlMap.size() - 1) subCode = subCode + ", ";
            urlCodeGraph = urlCodeGraph + subCode;
        }
        System.out.println(urlCodeGraph);
        rs.add(urlCodeGraph);
        return rs;
    }

    public void buildInverseGraph() {
        invGraph = new ArrayList<List<SiteLinkPOJO>>();
        int sz = graph.size();
        for (int i = 0; i < sz; i++) invGraph.add(new ArrayList<SiteLinkPOJO>());
        for (int i = 0; i < graph.size(); i++) {
            List<SiteLinkPOJO> listSite = graph.get(i);
            for (SiteLinkPOJO siteLink : listSite) {
                String src = siteLink.getSrcUrl();
                String des = siteLink.getDesUrl();
                int desId = urlMap.get(des);
                int srcId = urlMap.get(src);
                int srcType = typeNode.get(srcId);
                SiteLinkPOJO invSiteLink = new SiteLinkPOJO(des, src, srcType);
                invGraph.get(desId).add(invSiteLink);
            }
        }

    }


    /**
     * Get all links which are referencing to this url
     *
     * @param url
     * @return List<SiteLinkPOJO>
     */
    public List<SiteLinkPOJO> getReferenceLinks(String url) {
        int id = urlMap.get(url);
        return invGraph.get(id);
    }


    public void printReferenceLinks(String url) {
        int id = urlMap.get(url);
        for (SiteLinkPOJO siteLink : invGraph.get(id)) {
            System.out.println("link: " + siteLink.getDesUrl() + " type: " + siteLink.getDesType());
        }
    }


    /**
     * @return internal link (type: 1 vs 3)
     */
    public List<String> getInternalLink() {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : urlMap.entrySet()) {
            String url = entry.getKey();
            int idNode = entry.getValue();

            int type = typeNode.get(idNode);
            if (type != 2) {
                result.add(url);
            }
        }
        return result;
    }

    /**
     * @return all  link (type: 1, 2 & 3)
     */
    public List<String> getAllLinks() {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : urlMap.entrySet()) {
            String url = entry.getKey();
            result.add(url);
        }
        return result;
    }

    public List<Page> getAllPage(Website web, Version ver){
        List<Page> pages = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : urlMap.entrySet()) {
            String url = entry.getKey();
            int idNode = entry.getValue();
            int type = typeNode.get(idNode);
            Page tmpPage = new Page();
            tmpPage.setType(type);
            tmpPage.setUrl(url);
            tmpPage.setWebsite(web);
            tmpPage.setVersion(ver);
            pages.add(tmpPage);
        }
        return pages;
    }


    public boolean isChildUrlOf(String parentUrl, String childUrl) {
        return childUrl.contains(parentUrl);
    }

    // process
    List<List<Integer>> subChild;

    public void printTree(int nodeId, int space) {
        for (int i = 1; i <= space; i++) {
            System.out.print("---");
        }
        System.out.println("URL: " + links.get(nodeId));

        for (int i = 0; i < subChild.get(nodeId).size(); i++) {
            int desNodeId = subChild.get(nodeId).get(i);
            printTree(desNodeId, space + 1);
        }
    }


    public void makeUI() {
        // initial
        int parent[] = new int[verticesNum];
        int diff[] = new int[verticesNum];

        Arrays.fill(parent, -1);
        subChild = new ArrayList<List<Integer>>();
        for (int i = 0; i < verticesNum; i++) subChild.add(new ArrayList<Integer>());
        // process
        for (int i = 0; i < verticesNum; i++)
            for (int j = 0; j < verticesNum; j++)
                if (i != j) {
                    if (isChildUrlOf(links.get(i), links.get(j))) {
                        int curDiff = links.get(j).length() - links.get(i).length();
                        if (parent[j] == -1) {
                            parent[j] = i;
                            diff[j] = curDiff;
                        } else // minimize the difference
                            if (diff[j] > curDiff) {
                                diff[j] = curDiff;
                                parent[j] = i;
                            }

                    }
                }

        for (int i = 0; i < verticesNum; i++) {
            int parentId = parent[i];
            if (parentId != -1) {
                subChild.get(parentId).add(i);
            }
            //System.out.println("node: " + i + " parent: " + parentId);
        }
        // print tree
        for (int i = 0; i < verticesNum; i++)
            if (parent[i] == -1)
                printTree(i, 0);
    }

}
