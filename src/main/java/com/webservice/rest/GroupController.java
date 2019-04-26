package com.webservice.rest;

import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ch.qos.logback.core.net.SyslogOutputStream;

import org.w3c.dom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GroupController {

	private static final String SUCCESS_STATUS = "success";
	private static final String ERROR_STATUS = "error";
	private static final int CODE_SUCCESS = 100;
	private static final int AUTH_FAILURE = 102;
	private final String sharedKey = "SHARED_KEY";
	// private final String sharedKey = "SHARED_KEY";
	private String e1;
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
	@Autowired
	private FileStorageService fileStorageService;
	@PostMapping("/test")
	public Response test(@RequestParam(value = "id") String id, String fileName) {
	String clientRegion = "US_EAST_1";
    String bucketName = "demo1bd";
    Response response = new Response();
    try {
    	response.setStatus(SUCCESS_STATUS);
		response.setCode(CODE_SUCCESS);
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials("userid", "secretid");
    	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
    	                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    	                        .withRegion(Regions.US_EAST_1)
    	                        .build();
    	InputStream s3stream = s3Client.getObject("elasticbeanstalk-us-east-1-234604196508", "CSTest.xml").getObjectContent();
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		dBuilder.setEntityResolver(new EntityResolver() {

			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				System.out.println("Ignoring " + publicId + ", " + systemId);
				return new InputSource(new StringReader(""));
			}
		});
		Document doc = dBuilder.parse(s3stream);
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		Node thisNode = doc.getDocumentElement();
		System.out.println(((Node) thisNode.getChildNodes()).getFirstChild().getNextSibling());

		NodeList nList = doc.getElementsByTagName("CLASSIFICATION_GROUP");

		System.out.println("----------------------------");
		System.out.println(nList.getLength());

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			System.out.println(nNode.getFirstChild().getTextContent());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				System.out.println("Group: "
						+ eElement.getElementsByTagName("CLASSIFICATION_GROUP_ID").item(0).getTextContent());
				if (eElement.getElementsByTagName("CLASSIFICATION_GROUP_ID").item(0).getTextContent().equals(id)) {
					e1 = eElement.getElementsByTagName("CLASSIFICATION_GROUP_NAME").item(0).getTextContent();
					response.setId(id);
					response.setName(e1);

				}
			}
		}

	} catch (Exception e) {
		e.printStackTrace();

	}
	return (response);
}
}

