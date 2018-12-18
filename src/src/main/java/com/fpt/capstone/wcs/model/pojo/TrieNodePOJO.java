package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor

public class TrieNodePOJO {

    private char letter;
    private boolean isLeaf = false;
    private int totalLeafNode; // include itself
    private int eWeight; // error times
    private int maxEWeight; // of entire branch
    private List<TrieNodePOJO> refNode;

    public TrieNodePOJO() {
        this.refNode = new ArrayList<TrieNodePOJO>();
        for (int i = 0; i < 26; i++) {
            this.refNode.add(null);
        }
    }

    public TrieNodePOJO(char letter, boolean isLeaf, List<TrieNodePOJO> refNode) {
        this.letter = letter;
        this.isLeaf = isLeaf;
        this.refNode = refNode;
    }

    public TrieNodePOJO(char letter) {
        this.letter = letter;
        this.refNode = new ArrayList<TrieNodePOJO>();
        for (int i = 0; i < 26; i++) {
            this.refNode.add(null);
        }
    }

    public TrieNodePOJO(char letter, int totalLeafNode, int eWeight, List<TrieNodePOJO> refNode) {
        this.letter = letter;
        this.totalLeafNode = totalLeafNode;
        this.eWeight = eWeight;
        this.refNode = refNode;
    }
}
