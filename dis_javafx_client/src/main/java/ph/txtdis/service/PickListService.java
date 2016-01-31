package ph.txtdis.service;

import static java.time.DayOfWeek.MONDAY;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static ph.txtdis.type.UserType.DRIVER;
import static ph.txtdis.type.UserType.HELPER;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;

import ph.txtdis.dto.Booking;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.PickList;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Service("pickListService")
public class PickListService implements Reset, Serviced<Long>, SpunById<Long> {

	private class AlreadyAssignedPersonException extends Exception {

		private static final long serialVersionUID = -2596273848835338308L;

		public AlreadyAssignedPersonException(String u) {
			super(u + " has been assigned");
		}
	}

	private class DateAfterTomorrowWhichIsNotAMondayException extends Exception {

		private static final long serialVersionUID = 6963002563936579349L;

		public DateAfterTomorrowWhichIsNotAMondayException() {
			super("Date cannot be after tomorrow\nunless that is a Monday");
		}
	}

	private class NothingToPickException extends Exception {

		private static final long serialVersionUID = 7000764332504494319L;

		public NothingToPickException(LocalDate date) {
			super("There's nothing to pick on\n" + toDateDisplay(date));
		}
	}

	private static final String PICK_UP = "PICK-UP";

	@Autowired
	private BillableService bookingService;

	@Autowired
	private ReadOnlyService<PickList> readOnlyService;

	@Autowired
	private SavingService<PickList> savingService;

	@Autowired
	private SpunService<PickList, Long> spunService;

	@Autowired
	private TruckService truckService;

	@Autowired
	private UserService userService;

	private List<Booking> unpickedBookings;

	private List<PickList> pickLists;

	private PickList pickList;

	public PickListService() {
		reset();
	}

	@Override
	@SuppressWarnings("unchecked")
	public PickList get() {
		if (pickList == null)
			reset();
		return pickList;
	}

	@Override
	public String getAlternateName() {
		return "Pick List";
	}

	@Override
	public String getCreatedBy() {
		return get().getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return get().getCreatedOn();
	}

	@Override
	public Long getId() {
		return get().getId();
	}

	@Override
	public String getModule() {
		return "pickList";
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadOnlyService<PickList> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	@SuppressWarnings("unchecked")
	public SavingService<PickList> getSavingService() {
		return savingService;
	}

	@Override
	public SpunService<PickList, Long> getSpunService() {
		return spunService;
	}

	public List<String> listAsstHelpers() {
		return get().getLeadHelper() == null ? allHelpers() : asstHelpers();
	}

	public List<Booking> listBookings(String route) {
		List<Booking> picked = unpickedBookings.stream().filter(b -> b.getRoute().equals(route)).collect(toList());
		picked.forEach(p -> unpickedBookings.remove(p));
		return picked;
	}

	public List<String> listDrivers() {
		return get().getTruck() == null ? null : drivers();
	}

	public List<String> listLeadHelpers() {
		return get().getDriver() == null ? allHelpers() : leadHelpers();
	}

	public List<String> listRoutes() {
		List<String> unpickedRoutes = unpickedBookings.stream().map(b -> b.getRoute()).distinct().sorted()
				.collect(toList());
		List<Booking> bookings = get().getBookings();
		if (unpickedRoutes.isEmpty() || bookings == null)
			return unpickedRoutes;

		ArrayList<String> unpickedRouteList = new ArrayList<>(unpickedRoutes);
		List<String> pickedRoutes = bookings.stream().map(b -> b.getRoute()).distinct().collect(toList());
		if (pickedRoutes != null)
			unpickedRouteList.removeAll(pickedRoutes);
		return unpickedRouteList;
	}

	public List<String> listTrucks() {
		return isNew() ? allTrucks() : truck();
	}

	public List<Booking> listUnpickedBookings(String route) {
		return unpickedBookings.stream().filter(b -> route.equals(b.getRoute())).collect(toList());
	}

	@Override
	public void next() throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException, RestException {
		set(spunService.module(getModule()).next(getSpunId()));
	}

	@Override
	public void previous() throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException, RestException {
		set(spunService.module(getModule()).previous(getSpunId()));
	}

	public void print() throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException, RestException {
		set(readOnlyService.module(getModule()).getOne("/print?id=" + getId()));
	}

	@Override
	public void reset() {
		set(new PickList());
		instantiateLists();
	}

	@Override
	public <T extends Keyed<Long>> void set(T t) {
		pickList = (PickList) t;
	}

	public void setAsstHelperUponValidation(String h) throws AlreadyAssignedPersonException {
		if (h == null)
			return;
		if (hasPersonBeenAssigned(h))
			throw new AlreadyAssignedPersonException(h);
		get().setAsstHelper(h);
	}

	public void setDriverUponValidation(String d) throws AlreadyAssignedPersonException {
		if (d == null)
			return;
		if (hasPersonBeenAssigned(d))
			throw new AlreadyAssignedPersonException(d);
		get().setDriver(d);
	}

	public void setLeadHelperUponValidation(String h) throws AlreadyAssignedPersonException {
		if (h == null)
			return;
		if (hasPersonBeenAssigned(h))
			throw new AlreadyAssignedPersonException(h);
		get().setLeadHelper(h);
	}

	public void setPickDateUponValidation(LocalDate d)
			throws DateInThePastException, DateAfterTomorrowWhichIsNotAMondayException, NothingToPickException,
			NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException {
		verifyDateIsTodayOrTheNextWorkDay(d);
		get().setPickDate(d);
	}

	public void setTruckUponValidation(String t) throws NothingToPickException, NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException {
		if (t == null || t.isEmpty())
			return;
		if (isNew() && get().getPickDate() != null)
			verifyThereAreBookingsToBePickedOnPickDate(t);
		assignPersonsBasedOnTruckStatus(t);
	}

	public void unpick(Booking b) {
		unpickedBookings.add(b);
	}

	private List<String> allDrivers() {
		return userService.listNamesByRole(DRIVER);
	}

	private List<String> allHelpers() {
		return userService.listNamesByRole(HELPER);
	}

	private List<String> allTrucks() {
		List<String> l = new ArrayList<>();
		l.add(PICK_UP);
		l.addAll(truckService.listNames());
		return l;
	}

	private void assignPersonsBasedOnTruckStatus(String t) {
		if (!t.equals(PICK_UP)) {
			Optional<PickList> o = pickLists.stream().filter(p -> p.getTruck().equals(t)).findFirst();
			if (o.isPresent()) {
				get().setLeadHelper(o.get().getLeadHelper());
				get().setAsstHelper(o.get().getAsstHelper());
			}
		}
		get().setTruck(t.equals(PICK_UP) ? null : t);
	}

	private List<String> asstHelpers() {
		String s = get().getAsstHelper();
		return s == null ? null : asList(s);
	}

	private boolean dateIsAfterTomorrowWhichIsNotMonday(LocalDate d) {
		if (now().until(d, DAYS) > 2)
			return true;
		if (d.isEqual(LocalDate.now()) || d.isEqual(LocalDate.now().plusDays(1L)))
			return false;
		return d.getDayOfWeek() != MONDAY;
	}

	private List<String> drivers() {
		String d = get().getDriver();
		return d == null ? allDrivers() : asList(d);
	}

	private boolean hasPersonBeenAssigned(String t) {
		return hasPersonBeenAssignedInOtherTrucks(t) || hasPersonBeenAssignedInCurrentTruck(t);
	}

	private boolean hasPersonBeenAssignedInCurrentTruck(String t) {
		return t.equals(get().getDriver()) || t.equals(get().getLeadHelper()) || t.equals(get().getAsstHelper());
	}

	private boolean hasPersonBeenAssignedInOtherTrucks(String t) {
		return pickLists.stream()
				.anyMatch(p -> t.equals(p.getDriver()) || t.equals(p.getLeadHelper()) || t.equals(p.getAsstHelper()));
	}

	private void instantiateLists() {
		unpickedBookings = new ArrayList<>();
		pickLists = new ArrayList<>();
	}

	private List<String> leadHelpers() {
		String h = get().getLeadHelper();
		return h == null ? allHelpers() : asList(h);
	}

	private LocalDate pickDate() {
		return get().getPickDate();
	}

	private List<String> truck() {
		String s = get().getTruck();
		return asList(s == null ? PICK_UP : s);
	}

	private void verifyDateIsTodayOrTheNextWorkDay(LocalDate date)
			throws DateInThePastException, DateAfterTomorrowWhichIsNotAMondayException {
		if (date.isBefore(now()))
			throw new DateInThePastException();
		if (dateIsAfterTomorrowWhichIsNotMonday(date))
			throw new DateAfterTomorrowWhichIsNotAMondayException();
	}

	private void verifyThereAreBookingsToBePickedOnPickDate(String t) throws NothingToPickException,
			NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException {
		unpickedBookings = bookingService.listUnpicked(pickDate(), t);
		if (unpickedBookings.isEmpty())
			throw new NothingToPickException(pickDate());
	}
}
