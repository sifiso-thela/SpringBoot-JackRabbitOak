package com.jackrabbit.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jackrabbit.model.FileDetail;
import com.jackrabbit.service.JackRabbitService;

@WebServlet(
	urlPatterns = {"/upload"},
	description = "Servlet to upload files",
	name = "Upload Servlet"
	)
public class UploadServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServlet.class);
	
	@Autowired Repository repository;
	@Autowired JackRabbitService service;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		out.print("Servlet working!!");
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// check if its a file upload
		
				if(!ServletFileUpload.isMultipartContent(req))
					throw new ServletException("Content type is not multipart/form-data");
				
				try {
					LOGGER.info("Begin file upload->>>>>");
					
//					LOGGER.info("parameter --> " +  req.getParameter("name"));
					
					ServletFileUpload upload = new ServletFileUpload();
					
					// Parse the request
					FileItemIterator iter = upload.getItemIterator(req);
					while (iter.hasNext()) {
					    FileItemStream item = iter.next();
					    String name = item.getFieldName();
					    InputStream stream = item.openStream();
					    
					    FileDetail file = new FileDetail();
					    file.setFileData(stream);
					    file.setFileName(item.getName());
					    file.setContentType(item.getContentType());
					    file.setCreatedBy("sifiso");
					    LOGGER.info("File name: " + name + " \nFile contentType: " + file.getContentType() + "\nStream: " + file.getFileData().toString());
					   
					    
					    Session session = repository.login(
					            new SimpleCredentials("admin", "admin".toCharArray()));
					    
					    service.addFileNode(session, file);
					    
					    PrintWriter out = resp.getWriter();
					    out.println("Done Saving file: ");
						
					}
					
				}catch(Exception e) {
					e.printStackTrace();
					
				}
	}

}
