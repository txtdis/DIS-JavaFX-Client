package ph.txtdis.app;

import static org.apache.log4j.Logger.getLogger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.startsWith;

import static ph.txtdis.type.ModuleType.DOWNLOAD;

import javafx.concurrent.Task;
import javafx.stage.Stage;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.fx.dialog.ProgressDialog;
import ph.txtdis.service.ReadOnlyService;
import ph.txtdis.service.ScriptService;
import ph.txtdis.type.ModuleType;
import ph.txtdis.util.ServerUtil;

@Scope("prototype")
@Component("syncApp")
public class SyncApp extends Stage implements MultiTyped, Startable {

	private static Logger logger = getLogger(SyncApp.class);

	@Autowired
	private ReadOnlyService<String> readOnlyService;

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private MessageDialog messageDialog;

	@Autowired
	private ProgressDialog progressDialog;

	private ModuleType type;

	@Autowired
	private ServerUtil serverUtil;

	@Override
	public Startable addParent(Stage stage) {
		return null;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void start() {
		if (serverUtil.isOffSite() //
				&& scriptService.unpostedTransactionsExist()//
				&& type == DOWNLOAD)
			showPostOrReplicateDialog();
		else
			startTask(type());
	}

	@Override
	public String type() {
		logger.info("Type @type() = " + type);
		return type.toString().toLowerCase();
	}

	@Override
	public Startable type(ModuleType type) {
		this.type = type;
		logger.info("Type @type(type) = " + this.type);
		return this;
	}

	private void onFailed(Task<String> t) {
		progressDialog.close();
		String msg = t.getException().getMessage();
		messageDialog.showError(msg).addParent(this).start();
	}

	private void onSucceeded(Task<String> t) {
		try {
			progressDialog.close();
			String msg = t.get();
			if (startsWith(msg, "No"))
				messageDialog.showError(msg).addParent(this).start();
			else
				messageDialog.showInfo(msg).addParent(this).start();
		} catch (Exception e) {
			e.printStackTrace();
			messageDialog.show(e).addParent(this).start();
		}
	}

	private void postUnpostedTransaction() {
		messageDialog.close();
		startTask("upload");
	}

	private void replicate() {
		messageDialog.close();
		startTask(type());
	}

	private void showPostOrReplicateDialog() {
		messageDialog.showOption("Unposted transactions exist;\nproceed, how?", "Post", "Replicate");
		messageDialog.setOnOptionSelection(e -> postUnpostedTransaction());
		messageDialog.setOnDefaultSelection(e -> replicate());
		messageDialog.addParent(this).start();
	}

	private void start(Task<String> task) {
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

	private void startTask(String type) {
		Task<String> task = new Task<String>() {
			@Override
			protected String call() throws Exception {
				logger.info("Endpoint = /" + type);
				return readOnlyService.module("sync").getOne("/" + type);
			}
		};
		task.setOnSucceeded(e -> onSucceeded(task));
		task.setOnFailed(e -> onFailed(task));
		progressDialog.addParent(this).start();
		start(task);
	}
}
