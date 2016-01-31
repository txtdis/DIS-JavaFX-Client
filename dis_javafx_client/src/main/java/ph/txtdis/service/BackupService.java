package ph.txtdis.service;

import static java.nio.file.Files.write;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toTimestampFilename;

import ph.txtdis.dto.Backup;
import ph.txtdis.exception.FailedFileWriteAccessException;
import ph.txtdis.util.Binary;

@Service("backupService")
public class BackupService {

	@Autowired
	private ReadOnlyService<Backup> readOnlyService;

	private String pathname;

	public byte[] getBackup() throws Exception {
		return readOnlyService.module("backup").getOne("/inBytes").getFile();
	}

	public String writeBackup(File folder) throws Exception {
		pathname = folder + "\\\n" + toTimestampFilename(ZonedDateTime.now()) + ".backup";
		String filename = pathname.replace("\n", "");
		writeBackup(Binary.toPath(filename));
		return pathname;
	}

	private void writeBackup(Path path) throws Exception {
		try {
			write(path, getBackup());
		} catch (IOException e) {
			throw new FailedFileWriteAccessException("backup", pathname);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
