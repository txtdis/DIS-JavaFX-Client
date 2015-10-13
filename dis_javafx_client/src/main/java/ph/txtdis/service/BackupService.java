package ph.txtdis.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Backup;
import ph.txtdis.exception.FailedFileWriteAccessException;
import ph.txtdis.util.Binary;
import ph.txtdis.util.Temporal;

@Service("backupService")
public class BackupService {

	@Autowired
	private ReadOnlyService<Backup> readOnlyService;

	private String pathname;

	public byte[] getBackup() throws Exception {
		return readOnlyService.module("backup").getOne("").getFile();
	}

	public String writeBackup(File folder) throws Exception {
		pathname = folder + "\\\n" + Temporal.toFilename(ZonedDateTime.now()) + ".backup";
		String filename = pathname.replace("\n", "");
		writeBackup(Binary.toPath(filename));
		return pathname;
	}

	private void writeBackup(Path path) throws Exception {
		try {
			Files.write(path, getBackup());
		} catch (IOException e) {
			throw new FailedFileWriteAccessException("backup", pathname);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
