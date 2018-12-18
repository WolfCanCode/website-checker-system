package com.fpt.capstone.wcs.service.system.trietree;

import com.fpt.capstone.wcs.model.entity.website.EnglishDictionary;
import com.fpt.capstone.wcs.model.pojo.TrieNodePOJO;
import com.fpt.capstone.wcs.repository.website.EnglishDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrieService {
    private TrieNodePOJO root;
    private List<String> suggestList;
    final EnglishDictionaryRepository dictionaryRepository;

    @Autowired
    public TrieService(EnglishDictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
        this.root = new TrieNodePOJO();
        this.suggestList = new ArrayList<>();
        buildTrieTree();
    }

    public TrieNodePOJO getRoot() {
        return root;
    }

    public List<String> getSuggestList() {
        return suggestList;
    }

    public EnglishDictionaryRepository getDictionaryRepository() {
        return dictionaryRepository;
    }

    public List<String> getSuggestList(String word) {
        this.suggestList.clear();
        inspectWordApplyTrieTree(word, "", 0, 1, this.root);
        return this.suggestList;
    }

    public void buildTrieTree() {
        List<EnglishDictionary> enDict = dictionaryRepository.findAll();

        if (enDict != null) {
            for (EnglishDictionary word : enDict) {
                String w = word.getWord();
                String type = word.getType();
                String definition = word.getDefinition();
                System.out.println("************" + w);
                addWordToTrie(w, type, definition);
            }
        }

    }

    public void addWordToTrie(String word, String type, String definition) {
        TrieNodePOJO curr = this.root;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.toLowerCase().charAt(i);
            int pos = (int) letter - 'a';
            if (curr.getRefNode().get(pos) == null) {
                TrieNodePOJO newNode = new TrieNodePOJO(letter);
                curr.getRefNode().set(pos, newNode);
            }
            curr = curr.getRefNode().get(pos);
        }
        curr.setLeaf(true);
//        curr.setWType(type);
//        curr.setWDefinition(definition);
    }

    public void calculateTotalLeafNode(TrieNodePOJO node) {
        int total = 0;
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
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

    public void inspectWordApplyTrieTree(String word, String suggestWord, int position, int limit, TrieNodePOJO node) {

        if (position == word.length()) {
            if (node.isLeaf()) {
                if (this.suggestList.size() < 3)
                    this.suggestList.add(word);
            }
            return;
        }
        if (limit == 0) {
            String nWord = suggestWord + word.charAt(position);
            int newRefPosition = word.charAt(position) - 'a';
            if (node.getRefNode().get(newRefPosition) != null) {
                inspectWordApplyTrieTree(word, nWord, position + 1, limit, node.getRefNode().get(newRefPosition));
            }
        } else {
            //sai
            for (int i = 0; i < 26; i++)
                if (i != word.charAt(position) - 'a') {
                    String nWord = suggestWord + (char) (i + 'a');
                    int newPos = i;
                    if (node.getRefNode().get(newPos) != null) {
                        inspectWordApplyTrieTree(word, nWord, position + 1, limit - 1, node.getRefNode().get(newPos));
                    }

                }
            //du
            String nWord2 = suggestWord;
            inspectWordApplyTrieTree(word, nWord2, position + 1, limit - 1, node);

            //thieu
            for (int i = 0; i < 26; i++) {
                String nWord = suggestWord + (char) (i + 'a');
                int newPos = i;
                if (node.getRefNode().get(newPos) != null) {
                    inspectWordApplyTrieTree(word, nWord, position, limit - 1, node.getRefNode().get(newPos));
                }
            }
            //ko lam gi
            String nWord3 = suggestWord + word.charAt(position);
            int newRefPosition = word.charAt(position) - 'a';
            if (node.getRefNode().get(newRefPosition) != null) {
                inspectWordApplyTrieTree(word, nWord3, position + 1, limit, node.getRefNode().get(newRefPosition));
            }

        }
    }

    public boolean isExistInDictionary(String word) {

        TrieNodePOJO curr = this.root;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.toLowerCase().charAt(i);
            int pos = (int) letter - 'a';

            if (pos < 0 || pos > 26) {
                return false;
            }
            if (curr.getRefNode().get(pos) == null) {
                return false;
            }

            curr = curr.getRefNode().get(pos);
        }
        return curr.isLeaf();
    }

    public void printTree(TrieNodePOJO node, String buffer) {

        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            System.out.println(buffer);
        }

        for (int i = 0; i < 26; i++) {
            Character ch = new Character((char) ('a' + i));
            printTree(node.getRefNode().get(i), buffer + ch);
        }

    }
}
