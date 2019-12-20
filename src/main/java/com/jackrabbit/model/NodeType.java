package com.jackrabbit.model;

public enum NodeType {
	FILE("file_node"), FOLDER("folder_node");

    private String nodeValue;

    private NodeType(String nodeType) {
        this.nodeValue = nodeType;
    }

    public String getValue() {
        return nodeValue;
    }

}
