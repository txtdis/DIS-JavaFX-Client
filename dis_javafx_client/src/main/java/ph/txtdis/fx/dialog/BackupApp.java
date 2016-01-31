package ph.txtdis.fx.dialog;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ph.txtdis.service.BackupService;

@Scope("prototype")
@Component("backupApp")
public class BackupApp {

	@Autowired
	private BackupService service;

	@Autowired
	private MessageDialog dialog;

	public void chooseFolder(Stage stage) {
		try {
			File folder = new DirectoryChooser().showDialog(stage);
			if (folder != null)
				writeBackup(stage, folder);
		} catch (Exception e) {
			dialog.showError("Backup unsuccessful;\n" + e.getMessage()).addParent(stage).start();
		}
	}

	private void writeBackup(Stage stage, File folder) throws Exception {
		String path = service.writeBackup(folder);
		dialog.showInfo("Backup saved as\n" + path).addParent(stage).start();
	}
}
