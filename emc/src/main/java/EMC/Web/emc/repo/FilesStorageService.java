package EMC.Web.emc.repo;


import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.stream.Stream;
public interface FilesStorageService {
	String store(MultipartFile file);
    Resource loadResource(String filename );

    void deleeAll();
    void init();
    Stream<Path> loadFiles();

    ResponseEntity<Resource> downloadImage(String imageName, HttpServletRequest request);

}
