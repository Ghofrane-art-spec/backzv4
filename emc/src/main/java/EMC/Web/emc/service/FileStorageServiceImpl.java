package EMC.Web.emc.service;

import org.springframework.stereotype.Service;

import EMC.Web.emc.repo.FilesStorageService;
import EMC.Web.emc.shared.FileStorageProperties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


@Service
public class FileStorageServiceImpl implements FilesStorageService {

	private final Path imglocation ;
	   
	   
	public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
		 this.imglocation = Paths.get(fileStorageProperties.getUploadImgChequeDir())
	                .toAbsolutePath().normalize();

	        try {
	            Files.createDirectories(this.imglocation);
	        } catch (Exception ex) {
	            //throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
	        }
	}

	@Override
	public String store(MultipartFile file) {
		 // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                //throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)

            Files.copy(file.getInputStream(), this.imglocation.resolve(fileName) , StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("FAIL");
        }
        return file.getOriginalFilename();
	}

	@Override
	public Resource loadResource(String filename) {
		try {
            Path file = imglocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else
            {
                throw new RuntimeException("FAIL");
            }
        }catch (MalformedURLException e) {
            throw new RuntimeException("FAIL");
        }
	}

	@Override
	public void deleeAll() {
		   FileSystemUtils.deleteRecursively(imglocation.toFile());
		
	}

	@Override
	public void init() {
		try {
            Files.createDirectory(imglocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage");
        }
		
	}

	@Override
	public Stream<Path> loadFiles() {
		 try {
	            return Files.walk(this.imglocation, 1)
	                    .filter(path -> !path.equals(this.imglocation))
	                    .map(this.imglocation::relativize);
	        } catch (IOException e) {
	            throw new RuntimeException("\" failed to read stored file");
	        }
	}

	@Override
	public ResponseEntity<Resource> downloadImage(String imageName, HttpServletRequest request) {
		Resource resource = this.loadResource(imageName);
        String contentType = null;
        try {
            if (resource!=null){
                contentType=request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        assert contentType != null;
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+resource.getFilename()+"\"")
                .body(resource);
	}
}
