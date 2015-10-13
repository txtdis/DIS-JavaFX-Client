package ph.txtdis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Keyed;

@Service("spunService")
public class SpunService<T extends Keyed<PK>, PK> {

	@Autowired
	private ReadOnlyService<T> readOnlyService;

	private String module;

	public SpunService<T, PK> module(String module) {
		this.module = module;
		return this;
	}

	public T next(PK id) throws Exception {
		return getFirstForNewOrLastElseNext(id);
	}

	public T previous(PK id) throws Exception {
		return getLastForNewOrFirstElsePrevious(id);
	}

	private T first() throws Exception {
		return readOnlyService.module(module).getOne("/first");
	}

	private T getFirstForNewOrLastElseNext(PK id) throws Exception {
		return id == null || isLast(id) ? first() : getNext(id);
	}

	private T getLastForNewOrFirstElsePrevious(PK id) throws Exception {
		return id == null || isFirst(id) ? last() : getPrevious(id);
	}

	private T getNext(PK id) throws Exception {
		return readOnlyService.module(module).getOne("/next?id=" + id);
	}

	private T getPrevious(PK id) throws Exception {
		return readOnlyService.module(module).getOne("/previous?id=" + id);
	}

	private boolean isFirst(PK id) throws Exception {
		return first().getId().equals(id);
	}

	private boolean isLast(PK id) throws Exception {
		return last().getId().equals(id);
	}

	private T last() throws Exception {
		return readOnlyService.module(module).getOne("/last");
	}
}
