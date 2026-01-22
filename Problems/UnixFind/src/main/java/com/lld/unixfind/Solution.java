package com.lld.unixfind;

import java.util.ArrayList;
import java.util.List;

// --- File System Entity ---
class File {
    String name;
    long size;
    boolean isDirectory;
    List<File> children;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
        this.children = new ArrayList<>();
        this.isDirectory = false;
    }

    public void addChild(File f) {
        children.add(f);
        this.isDirectory = true;
    }
}

// --- Specification Interface ---
interface Filter {
    boolean apply(File file);
}

// --- Concrete Specs ---
class MinSizeFilter implements Filter {
    private final long minSize;

    public MinSizeFilter(long minSize) {
        this.minSize = minSize;
    }

    @Override
    public boolean apply(File file) {
        return file.size >= minSize;
    }
}

class ExtensionFilter implements Filter {
    private final String ext;

    public ExtensionFilter(String ext) {
        this.ext = ext;
    }

    @Override
    public boolean apply(File file) {
        return file.name.endsWith("." + ext);
    }
}

class NameContainsFilter implements Filter {
    private final String substring;

    public NameContainsFilter(String substring) {
        this.substring = substring;
    }

    @Override
    public boolean apply(File file) {
        return file.name.contains(substring);
    }
}

// --- Combinators (AND / OR) ---
class AndFilter implements Filter {
    private final Filter f1;
    private final Filter f2;

    public AndFilter(Filter f1, Filter f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public boolean apply(File file) {
        return f1.apply(file) && f2.apply(file);
    }
}

class OrFilter implements Filter {
    private final Filter f1;
    private final Filter f2;

    public OrFilter(Filter f1, Filter f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public boolean apply(File file) {
        return f1.apply(file) || f2.apply(file);
    }
}

// --- Searcher ---
class FindCommand {
    public List<File> find(File root, Filter filter) {
        List<File> result = new ArrayList<>();
        searchRecursive(root, filter, result);
        return result;
    }

    private void searchRecursive(File current, Filter filter, List<File> result) {
        if (!current.isDirectory) {
            if (filter.apply(current)) {
                result.add(current);
            }
            return;
        }

        // Directory logic: match current dir? Maybe. But find usually matches children.
        // Let's assume we scan contents.
        for (File child : current.children) {
            searchRecursive(child, filter, result);
        }
    }
}

// --- Demo ---
public class Solution {
    public static void main(String[] args) {
        System.out.println("--- Unix Find Filter Demo ---");

        // Specification: Find (Size >= 5MB) AND (Extension == "xml")

        // Setup File System
        File root = new File("root", 0);
        File docs = new File("src", 0);
        File bigData = new File("data.xml", 100); // 100MB
        File smallConfig = new File("config.xml", 1); // 1MB
        File log = new File("error.log", 50); // 50MB

        root.addChild(docs);
        docs.addChild(bigData);
        docs.addChild(smallConfig);
        docs.addChild(log);

        // Build Spec
        Filter sizeFilter = new MinSizeFilter(5);
        Filter extFilter = new ExtensionFilter("xml");
        Filter complexQuery = new AndFilter(sizeFilter, extFilter);

        // Execute
        FindCommand cmd = new FindCommand();
        List<File> results = cmd.find(root, complexQuery);

        System.out.println("Results for (>5MB AND .xml):");
        for (File f : results) {
            System.out.println("- " + f.name + " (" + f.size + "MB)");
        }
        // Expected: data.xml (100MB) only. config is small, log is wrong ext.
    }
}
