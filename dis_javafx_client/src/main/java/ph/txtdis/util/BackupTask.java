package ph.txtdis.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.concurrent.Task;
import ph.txtdis.service.BackupService;

@Component("backupTask")
public class BackupTask extends Task<Void> {

	@Autowired
	private BackupService backup;

	private File folder;

	public BackupTask() {
		// TODO Auto-generated constructor stub
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	@Override
	protected Void call() throws Exception {
		backup.writeBackup(folder);
		return null;
	}

}
