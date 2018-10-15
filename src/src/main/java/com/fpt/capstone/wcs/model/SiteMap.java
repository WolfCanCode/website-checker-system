package com.fpt.capstone.wcs.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class SiteMap {
    private HashMap<String, Integer> urlMap = new HashMap<String, Integer>();
    private String rootDomain = "";
    private List<List<SiteLink>> graph = new ArrayList<List<SiteLink>>();
    private List<List<SiteLink>> invGraph = null;
    private int verticesNum;
    private List<Integer> typeNode = new ArrayList<Integer>(); //Type link: 1 is internal, 2 is external and 3 is error internal link
    private List<String> links = new ArrayList<String>();

    // init site-map with 'root Domain' link
    public SiteMap(String rootDomain) {
        this.rootDomain = rootDomain;
        urlMap.clear();
        invGraph.clear();
        verticesNum = 0;
    }

    public List<String> crawlHtmlSource(String url) {
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
        List<SiteLink> list = graph.get(mapId);
        for (SiteLink siteLink : list) {
            System.out.println(siteLink.getDesUrl() + " " + siteLink.getDesType());
        }
    }


    public void buildSiteMap() {
        Queue<String> queue = new LinkedList<String>();

        queue.add(rootDomain);
        urlMap.put(rootDomain, verticesNum);
        typeNode.add(1);
        verticesNum++;
        graph.add(new ArrayList<SiteLink>());
        links.add(rootDomain);

        while (!queue.isEmpty()) {
            String curUrl = queue.peek();
            int mapId = urlMap.get(curUrl);
            queue.remove();
            List<String> newUrl = crawlHtmlSource(curUrl);
            for (String url : newUrl) {
                url = url.trim();
                if (!urlMap.containsKey(url)) {
                    // add to map & graph
                    urlMap.put(url, verticesNum);
                    verticesNum++;
                    graph.add(new ArrayList<SiteLink>());
                    if (url.contains(rootDomain)) {
                        // internal
                        SiteLink sl = new SiteLink(curUrl, url, 1);
                        graph.get(mapId).add(sl);
                        queue.add(url);
                        typeNode.add(1);
                        links.add(url);
                    } else {
                        // external
                        SiteLink sl = new SiteLink(curUrl, url, 2);
                        graph.get(mapId).add(sl);
                        typeNode.add(2);
                        links.add(url);
                    }
                } else {
                    // existed node
                    int mapCurUrl = urlMap.get(url);
                    int typeCurUrl = typeNode.get(mapCurUrl);
                    SiteLink sl = new SiteLink(curUrl, url, typeCurUrl);
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
                    SiteLink siteLink = graph.get(node).get(i);
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
        invGraph = new ArrayList<List<SiteLink>>();
        int sz = graph.size();
        for (int i = 0; i < sz; i++) invGraph.add(new ArrayList<SiteLink>());
        for (int i = 0; i < graph.size(); i++) {
            List<SiteLink> listSite = graph.get(i);
            for (SiteLink siteLink : listSite) {
                String src = siteLink.getSrcUrl();
                String des = siteLink.getDesUrl();
                int desId = urlMap.get(des);
                int srcId = urlMap.get(src);
                int srcType = typeNode.get(srcId);
                SiteLink invSiteLink = new SiteLink(des, src, srcType);
                invGraph.get(desId).add(invSiteLink);
            }
        }

    }


    /**
     * Get all links which are referencing to this url
     *
     * @param url
     * @return List<SiteLink>
     */
    public List<SiteLink> getReferenceLinks(String url) {
        int id = urlMap.get(url);
        return invGraph.get(id);
    }


    public void printReferenceLinks(String url) {
        int id = urlMap.get(url);
        for (SiteLink siteLink : invGraph.get(id)) {
            System.out.println("link: " + siteLink.getDesUrl() + " type: " + siteLink.getDesType());
        }
    }

}
