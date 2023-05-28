package EMC.Web.emc.shared;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix  = "file")
public class FileStorageProperties {
	
	private String uploadImgChequeDir;

	public String getUploadImgChequeDir() {
		return uploadImgChequeDir;
	}

	public void setUploadImgChequeDir(String uploadImgChequeDir) {
		this.uploadImgChequeDir = uploadImgChequeDir;
	}

}
