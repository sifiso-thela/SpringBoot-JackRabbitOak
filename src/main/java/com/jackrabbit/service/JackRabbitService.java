package com.jackrabbit.service;

import java.io.IOException;
import java.util.Date;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jackrabbit.model.FileDetail;
import com.jackrabbit.model.NodeType;

@Service
public class JackRabbitService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JackRabbitService.class);
	
	public void addFileNode(Session session, FileDetail fileDetail) throws RepositoryException, IOException{
		Node parentNode = session.getRootNode();
		
		Node createdNode = parentNode.addNode(fileDetail.getFileName());
		
		createdNode.addMixin("mix:versionable");
		createdNode.setProperty("jcr:createdBy", fileDetail.getCreatedBy());
		createdNode.setProperty("jcr:nodeType", NodeType.FILE.getValue());
		createdNode.setProperty("size", fileDetail.getSize());
		
		Node file1 = createdNode.addNode("theFile", "nt:file"); // create node of type file.

        Date now = new Date();
        now.toInstant().toString();
        
        Node content = file1.addNode("jcr:content", "nt:resource");
        content.setProperty("jcr:mimeType", fileDetail.getContentType());
        
        Binary binary = session.getValueFactory().createBinary(fileDetail.getFileData());
        
        content.setProperty("jcr:data", binary);
        content.setProperty("jcr:lastModified", now.toInstant().toString());
        session.save();
        VersionManager vm = session.getWorkspace().getVersionManager();
        vm.checkin(createdNode.getPath());
        System.out.println("File Saved...");
	}
}
