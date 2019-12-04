package fr.gtm.backoffice.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

///**
// * Servlet Filter implementation class ImageFilter
// */
//@Order(1)
//public class ImageFilter implements Filter {
//	private String folder;
//	private final static Logger LOGGER = Logger.getLogger(ImageFilter.class.getCanonicalName());
//
//	@Override
//	public void init(FilterConfig fConfig) throws ServletException {
//		folder = fConfig.getServletContext().getInitParameter("upload-folder");
//		LOGGER.info(">>> folder : "+folder);
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		String url = ((HttpServletRequest) request).getRequestURL().toString();
//		LOGGER.info(">>> url : " + url);
//		String[] urlSplit = url.split("/");
//		String image = urlSplit[urlSplit.length - 1];
//		LOGGER.info(">>> image : " + image);
//
//		// Positionnement du type MIME
//		FileNameMap fileNameMap = URLConnection.getFileNameMap();
//		String mimeType = fileNameMap.getContentTypeFor(image);
//		((HttpServletResponse) response).setContentType(mimeType);
//		OutputStream out = response.getOutputStream();
//
//		// Depuis Java 7
//		Path path = FileSystems.getDefault().getPath(folder, image);
//		Files.copy(path, out);
//
//		out.flush();
//		out.close();
//	}
//
//	@Override
//	public void destroy() {
//
//	}

//}
