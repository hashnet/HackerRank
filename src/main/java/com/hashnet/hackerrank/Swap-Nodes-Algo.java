package com.hashnet.hackerrank;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Complete the 'swapNodes' function below.
     *
     * The function is expected to return a 2D_INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY indexes
     *  2. INTEGER_ARRAY queries
     */
    private static class Node {
        int val;
        Node left;
        Node right;
        
        public Node(int val) {
            this.val = val;
            left = null;
            right = null;
        }
        
        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }

    public static List<List<Integer>> swapNodes(List<List<Integer>> indexes, List<Integer> queries) {
        Node root = new Node(1);
        List<List<Node>> levels = new ArrayList<>();
        levels.add(new ArrayList<>());
        levels.get(0).add(root);
        
        for(int next=0,level=0; level< levels.size(); level++) {
            for(Node node: levels.get(level)) {
                Node left = indexes.get(next).get(0) == -1 ? null : new Node(indexes.get(next).get(0));
                Node right = indexes.get(next).get(1) == -1 ? null : new Node(indexes.get(next).get(1));
                ++next;
                
                node.left = left;
                node.right = right;
                
                if(left != null || right != null) {
                    int nextLevel = level+1;
                    if(levels.size() <= nextLevel) {
                        levels.add(new ArrayList<>());
                    }

                    if(left != null) levels.get(nextLevel).add(left);
                    if(right != null) levels.get(nextLevel).add(right);
                }
            }
        }

        List<List<Integer>> results = new ArrayList<>();

        queries.forEach(query -> {
            for(int q=query; q< levels.size(); q += query) {
                levels.get(q - 1).forEach(Result::swap);
            }

            List<Integer> result = new ArrayList<>();
            inorderVisit(root, result);
            results.add(result);
        });


        return results;
    }

    private static void swap(Node node) {
        Node temp = node.left;
        node.left = node.right;
        node.right = temp;
    }

    private static void inorderVisit(Node node, List<Integer> inorderResult) {
        if(node == null) return;

        inorderVisit(node.left, inorderResult);
        inorderResult.add(node.val);
        inorderVisit(node.right, inorderResult);
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(System.out));


        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> indexes = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                indexes.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int queriesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = IntStream.range(0, queriesCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

        List<List<Integer>> result = Result.swapNodes(indexes, queries);

        result.stream()
            .map(
                r -> r.stream()
                    .map(Object::toString)
                    .collect(joining(" "))
            )
            .map(r -> r + "\n")
            .collect(toList())
            .forEach(e -> {
                try {
                    bufferedWriter.write(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
