package com.haier.openplatform.ueditor.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.haier.openplatform.ueditor.ActionConfig;
import com.haier.openplatform.ueditor.PathFormat;
import com.haier.openplatform.ueditor.define.AppInfo;
import com.haier.openplatform.ueditor.define.BaseState;
import com.haier.openplatform.ueditor.define.FileType;
import com.haier.openplatform.ueditor.define.State;
import com.haier.openplatform.ueditor.manager.IUeditorFileManager;
import com.haier.openplatform.commons.utils.IOUtils;

public class BinaryUploader {

	public static final State save(IUeditorFileManager fileManager, HttpServletRequest request,
			ActionConfig conf) {
		if (!(request instanceof MultipartHttpServletRequest)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		InputStream is = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String fieldName = conf.getFieldName();
			MultipartFile file = multipartRequest.getFile(fieldName);

			String savePath = conf.getSavePath();
			String originFileName = file.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = conf.getMaxSize();

			if (!validType(suffix, conf.getAllowFiles())) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);
			String rootPath = conf.getRootPath();

			is = file.getInputStream();
			State storageState = fileManager.saveFile(is, rootPath, savePath, maxSize);

			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}
			return storageState;
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		} finally {
			IOUtils.closeQuietly(is);
		}

	}

	private static boolean validType(String type, List<String> allowTypes) {
		return allowTypes.contains(type);
	}
}
