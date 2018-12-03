package com.fpt.capstone.wcs.service.lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    /**
     * @author vintt
     */
    public class TrieTree {
        private String dictionaryPath;
        private TrieNode root;
        NormalSearch normalSearch;

        public TrieTree() {
        }

        public TrieTree(String dictionaryPath) {
            this.dictionaryPath = dictionaryPath;
            this.root = new TrieNode();
            normalSearch = new NormalSearch(dictionaryPath);
            normalSearch.init();
        }

        public TrieNode getRoot() {
            return root;
        }

        public void testParagraphInCaseHaveOneDifferentCharacter(String paragraph) {
            String[] words = paragraph.split(" ");
            for (String w : words) {
                w = w.trim();

                if (isExistInDictionary(w)) continue;

                List<String> rs = suggestListForWrongOneCharacter(w);
                if (rs.size() > 0) {
                    System.out.println("Diff: " + w);
                    System.out.println("Recommend: ");
                    for (String rw : rs) {
                        System.out.println("-- " + rw);
                    }
                }

                List<String> rs2 = suggestListForRedundantOneCharacter(w);
                if (rs2.size() > 0) {
                    System.out.println("Redundant: " + w);
                    System.out.println("Recommend: ");
                    for (String rw : rs2) {
                        System.out.println("-- " + rw);
                    }
                }
                List<String> rs3 = suggestListForMissingOneCharacter(w);
                if (rs3.size() > 0) {
                    System.out.println("Missing: " + w);
                    System.out.println("Recommend: ");
                    for (String rw : rs3) {
                        System.out.println("-- " + rw);
                    }
                }
            }
        }

        // sai 1 character
        public List<String> suggestListForWrongOneCharacter(String word) {
            List<String> wList = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                // replace at char [i]
                List<String> rsReplaceList = getSuggestReplaceList(word, i);
                for (String s : rsReplaceList) {
                    wList.add(s);
                }
            }

            return wList;
        }

        // thieu 1 chu
        public List<String> suggestListForMissingOneCharacter(String word) {
            List<String> wList = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                // add one at i
                List<String> rsReplaceList = getSuggestInsertList(word, i);
                for (String s : rsReplaceList) {
                    wList.add(s);
                }
            }

            return wList;
        }

        // du 1 ki tu
        public List<String> suggestListForRedundantOneCharacter(String word) {
            List<String> wList = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                String nWord = word.substring(0, i);
                if (i < word.length() - 1)
                    nWord = nWord + word.substring(i + 1);
                boolean isFound = isExistInDictionary(nWord);
                if (isFound) {
                    wList.add(nWord);
                }
            }
            return wList;
        }

        public List<String> getSuggestInsertList(String word, int pos) {
            List<String> rs = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                char c = (char) ('a' + i);
                String nWord = word.substring(0, pos + 1) + c + word.substring(pos + 1);

                boolean isFound = isExistInDictionary(nWord);
                if (isFound) {
                    rs.add(nWord);
                }
            }
            return rs;
        }

        public List<String> getSuggestReplaceList(String word, int pos) {
            List<String> rs = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                char c = (char) ('a' + i);
                String nWord = word.substring(0, pos) + c + word.substring(pos + 1);
                boolean isFound = isExistInDictionary(nWord);
                if (isFound) {
                    rs.add(nWord);
                }
            }
            return rs;
        }

        public boolean isExistInDictionary(String word) {

            return normalSearch.isExistInDictionary(word);
/*
        TrieNode curr = this.root;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
//            System.out.println(letter);
            int pos = (int) letter - 'a';

            if (pos < 0 || pos > 26) {
                return false;
            }
//            System.out.println(pos);
            if (curr.getRefNode().get(pos) == null) {
                return false;
            }

            curr = curr.getRefNode().get(pos);
        }
        return curr.isIsLeaf();

/**/
        }

        public void addWordToTrie(String word) {
            TrieNode curr = this.root;

            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
//            System.out.println(letter);
                int pos = (int) letter - 'a';
//            System.out.println(pos);
                if (curr.getRefNode().get(pos) == null) {
                    TrieNode newNode = new TrieNode(letter);
                    curr.getRefNode().set(pos, newNode);
                }

                curr = curr.getRefNode().get(pos);
            }
            curr.setIsLeaf(true);
        }

        public void printTree(TrieNode node, String buffer) {
            if (node == null) {
                return;
            }
            if (node.isIsLeaf()) {
                System.out.println(buffer);
            }

            for (int i = 0; i < 26; i++) {
                Character ch = new Character((char) ('a' + i));
                printTree(node.getRefNode().get(i), buffer + ch);
            }
            //////

//        System.out.println("prefix: " + buffer);
//        System.out.println("total leaf node: " + node.getTotalLeafNode());
        }

        public void calculateTotalLeafNode(TrieNode node) {
            int total = 0;
            if (node == null) {
                return;
            }
            if (node.isIsLeaf()) {
                total++;

            }

            for (int i = 0; i < 26; i++) {
                calculateTotalLeafNode(node.getRefNode().get(i));

            }

            for (int i = 0; i < 26; i++) {
                if (node.getRefNode().get(i) != null) {
                    total += node.getRefNode().get(i).getTotalLeafNode();
                }
            }

            node.setTotalLeafNode(total);
        }


        // relation
        // rala
        public void inspectOneWrongCharacterCase(String word, String suggestWord, int position, boolean isContinue, String mess) {

            if (position == word.length()) {
                System.out.println(mess + ": " + suggestWord);
                return;
            }

            if (!isContinue) {
                String nWord = suggestWord + word.charAt(position);
                inspectOneWrongCharacterCase(word, nWord, position + 1, isContinue, mess);
            } else {
                //sai
                for (int i = 0; i < 26; i++)
                    if (i != word.charAt(position) - 'a'){
                        String nWord = suggestWord + (char) (i + 'a');
                        inspectOneWrongCharacterCase(word, nWord, position + 1, false, "Replace suggest");
                    }
                //du
                String nWord2 = suggestWord;
                inspectOneWrongCharacterCase(word, nWord2, position + 1, false, "Omit suggest");

                //thieu
                for (int i = 0; i < 26; i++) {
                    String nWord = suggestWord + (char) (i + 'a');
                    inspectOneWrongCharacterCase(word, nWord, position, false, "Add suggest");
                }
                //ko lam gi
                String nWord3 = suggestWord + word.charAt(position);
                inspectOneWrongCharacterCase(word, nWord3, position + 1, true, mess);
            }
        }

        public void inspectOneWrongCharacterCaseApplyTrieTree(String word, String suggestWord, int position, boolean isContinue, TrieNode node) {

            if (position == word.length()) {

                if(node.isLeaf){
                    System.out.println("Suggest word: " + suggestWord);
                }
                return;
            }

            if (!isContinue) {

                String nWord = suggestWord + word.charAt(position);
                int newRefPosition = word.charAt(position) - 'a';
                if(node.getRefNode().get(newRefPosition) != null){
                    inspectOneWrongCharacterCaseApplyTrieTree(word, nWord, position + 1, isContinue, node.getRefNode().get(newRefPosition) );
                }


            } else {
                //sai
                for (int i = 0; i < 26; i++)
                    if (i != word.charAt(position) - 'a'){
                        String nWord = suggestWord + (char) (i + 'a');
                        int newPos = i;
                        if(node.getRefNode().get(newPos)!= null){
                            inspectOneWrongCharacterCaseApplyTrieTree(word, nWord, position + 1, false, node.getRefNode().get(newPos));
                        }

                    }
                //du
                String nWord2 = suggestWord;
                inspectOneWrongCharacterCaseApplyTrieTree(word, nWord2, position + 1, false, node);

                //thieu
                for (int i = 0; i < 26; i++) {
                    String nWord = suggestWord + (char) (i + 'a');
                    int newPos = i;
                    if(node.getRefNode().get(newPos)!= null) {
                        inspectOneWrongCharacterCaseApplyTrieTree(word, nWord, position, false, node.getRefNode().get(newPos));
                    }
                }
                //ko lam gi
                String nWord3 = suggestWord + word.charAt(position);
                int newRefPosition = word.charAt(position) - 'a';
                if(node.getRefNode().get(newRefPosition) != null){
                    inspectOneWrongCharacterCaseApplyTrieTree(word, nWord3, position + 1, true, node.getRefNode().get(newRefPosition));
                }

            }
        }

        public void inspectWordApplyTrieTree(String word, String suggestWord, int position, int limit, TrieNode node) {

            if (position == word.length()) {

                if(node.isLeaf){
                    System.out.println("Suggest word: " + suggestWord);
                }
                return;
            }

            if (limit==0) {

                String nWord = suggestWord + word.charAt(position);
                int newRefPosition = word.charAt(position) - 'a';
                if(node.getRefNode().get(newRefPosition) != null){
                    inspectWordApplyTrieTree(word, nWord, position + 1, limit, node.getRefNode().get(newRefPosition) );
                }


            } else {
                //sai
                for (int i = 0; i < 26; i++)
                    if (i != word.charAt(position) - 'a'){
                        String nWord = suggestWord + (char) (i + 'a');
                        int newPos = i;
                        if(node.getRefNode().get(newPos)!= null){

                            inspectWordApplyTrieTree(word, nWord, position + 1, limit -1, node.getRefNode().get(newPos));
                        }

                    }
                //du
                String nWord2 = suggestWord;
                inspectWordApplyTrieTree(word, nWord2, position + 1, limit -1 , node);

                //thieu
                for (int i = 0; i < 26; i++) {
                    String nWord = suggestWord + (char) (i + 'a');
                    int newPos = i;
                    if(node.getRefNode().get(newPos)!= null) {
                        inspectWordApplyTrieTree(word, nWord, position, limit -1 , node.getRefNode().get(newPos));
                    }
                }
                //ko lam gi
                String nWord3 = suggestWord + word.charAt(position);
                int newRefPosition = word.charAt(position) - 'a';
                if(node.getRefNode().get(newRefPosition) != null){
                    inspectWordApplyTrieTree(word, nWord3, position + 1, limit, node.getRefNode().get(newRefPosition));
                }

            }
        }

        public void buildTree() {
            BufferedReader br = null;
            FileReader fr = null;
            String content = "";
            try {

                //br = new BufferedReader(new FileReader(FILENAME));
                fr = new FileReader(dictionaryPath);
                br = new BufferedReader(fr);
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
//                                content += sCurrentLine;
                    addWordToTrie(sCurrentLine.trim());
                }

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (br != null) {
                        br.close();
                    }

                    if (fr != null) {
                        fr.close();
                    }

                } catch (IOException ex) {

                    ex.printStackTrace();

                }

            }
        }

        public class TrieNode {

            private char letter;
            private boolean isLeaf = false;
            private int totalLeafNode; // include itselve
            private int eWeight; // error times user had met
            private int maxEWeight; // of entrie branch
            private List<TrieNode> refNode;

            public TrieNode() {
                this.refNode = new ArrayList<TrieNode>();
                for (int i = 0; i < 26; i++) {
                    this.refNode.add(null);
                }
            }

            public TrieNode(char letter, boolean isLeaf, List<TrieNode> refNode) {
                this.letter = letter;
                this.isLeaf = isLeaf;
                this.refNode = refNode;
            }

            public TrieNode(char letter) {
                this.letter = letter;
                this.refNode = new ArrayList<TrieNode>();
                for (int i = 0; i < 26; i++) {
                    this.refNode.add(null);
                }
            }

            public TrieNode(char letter, int totalLeafNode, int eWeight, List<TrieNode> refNode) {
                this.letter = letter;
                this.totalLeafNode = totalLeafNode;
                this.eWeight = eWeight;
                this.refNode = refNode;
            }

            public char getLetter() {
                return letter;
            }

            public void setLetter(char letter) {
                this.letter = letter;
            }

            public List<TrieNode> getRefNode() {
                return refNode;
            }

            public boolean isIsLeaf() {
                return isLeaf;
            }

            public void setRefNode(List<TrieNode> refNode) {
                this.refNode = refNode;
            }

            public void setIsLeaf(boolean isLeaf) {
                this.isLeaf = isLeaf;
            }

            public int getTotalLeafNode() {
                return totalLeafNode;
            }

            public int geteWeight() {
                return eWeight;
            }

            public int getMaxEWeight() {
                return maxEWeight;
            }

            public void setTotalLeafNode(int totalLeafNode) {
                this.totalLeafNode = totalLeafNode;
            }

            public void seteWeight(int eWeight) {
                this.eWeight = eWeight;
            }

            public void setMaxEWeight(int maxEWeight) {
                this.maxEWeight = maxEWeight;
            }

        }

        public class NormalSearch {
            public NormalSearch(String path) {
                this.path = path;
            }

            private String path;
            private List<String> lstWord = new ArrayList<>();

            public void init() {
                BufferedReader br = null;
                FileReader fr = null;
                String content = "";
                try {

                    //br = new BufferedReader(new FileReader(FILENAME));
                    fr = new FileReader(path);
                    br = new BufferedReader(fr);
                    String sCurrentLine;
                    while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
//                                content += sCurrentLine;
                        sCurrentLine = sCurrentLine.trim();
                        lstWord.add(sCurrentLine);
                    }

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    try {

                        if (br != null) {
                            br.close();
                        }

                        if (fr != null) {
                            fr.close();
                        }

                    } catch (IOException ex) {

                        ex.printStackTrace();

                    }

                }
            }

            public boolean isExistInDictionary(String w) {

                for (int i = 0; i < lstWord.size(); i++) {
                    if (lstWord.get(i).equals(w)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }



