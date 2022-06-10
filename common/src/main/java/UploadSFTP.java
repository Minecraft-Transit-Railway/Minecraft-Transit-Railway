import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * This is a class for uploading build files to the SFTP server and is not directly used for the mod.
 **/

public class UploadSFTP {

	private static final File RELEASE_FOLDER = new File("build/release");
	private static final File LIBS_FOLDER = new File("build/libs");

	public static void main(String[] args) throws IOException {
		if (args.length < 4) {
			return;
		}

		final String host = args[0];
		final String username = args[1];
		final String password = args[2];
		final String version = args[3];
		final String sftpPath = args[4];

		final SSHClient sshClient = new SSHClient();
		sshClient.addHostKeyVerifier(new PromiscuousVerifier());
		sshClient.connect(host);
		sshClient.authPassword(username, password);
		final SFTPClient sftpClient = sshClient.newSFTPClient();

		uploadFiles(RELEASE_FOLDER.listFiles(), sftpClient, sftpPath, version, "");
		uploadFiles(LIBS_FOLDER.listFiles(), sftpClient, sftpPath, version, "-dev");
		uploadFiles(LIBS_FOLDER.listFiles(), sftpClient, sftpPath, version, "-dev-shadow");

		sftpClient.close();
		sshClient.disconnect();
	}

	private static void uploadFiles(File[] files, SFTPClient sftpClient, String sftpPath, String version, String suffix) throws IOException {
		if (files != null) {
			for (final File file : files) {
				if (file.getName().startsWith("MTR-" + version) && file.getName().endsWith(suffix + ".jar")) {
					final String fileName = "MTR-" + version + "-latest" + suffix + ".jar";
					FileUtils.copyFile(file, new File(fileName));
					sftpClient.put(fileName, sftpPath);
				}
			}
		}
	}
}
