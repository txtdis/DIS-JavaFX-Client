package ph.txtdis.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Booking;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.PickList;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.Truck;
import ph.txtdis.dto.User;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.type.UserType;
import ph.txtdis.util.Temporal;

@Service("pickListService")
public class PickListService implements AlternateNamed, Reset, Serviced<PickList, Long>, SpunById<Long> {

	private class DateAfterTomorrowWhichIsNotASundayException extends Exception {

		private static final long serialVersionUID = 6963002563936579349L;

		public DateAfterTomorrowWhichIsNotASundayException() {
			super("Date cannot be after tomorrow\nunless it is a Sunday");
		}
	}

	private class NothingToPickException extends Exception {

		private static final long serialVersionUID = 7000764332504494319L;

		public NothingToPickException(LocalDate date) {
			super("There's nothing to pick on\n" + Temporal.format(date));
		}
	}

	@Autowired
	private BookingService bookingService;

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

	private List<Truck> allTrucks;

	private List<User> assignedHelpers, allHelpers, allDrivers;

	private PickList pickList;

	public PickListService() {
		reset();
	}

	@Override
	public PickList find(String id) throws Exception {
		PickList p = readOnlyService.module(getModule()).getOne("/" + id);
		if (p == null)
			throw new NotFoundException(getAlternateName() + "No. " + id);
		return p;
	}

	@Override
	public PickList get() {
		return pickList;
	}

	@Override
	public String getAlternateName() {
		return "Pick List";
	}

	public PickList getByBooking(Long id) throws Exception {
		return readOnlyService.module(getModule()).getOne("/booking?id=" + id);
	}

	@Override
	public String getCreatedBy() {
		return pickList.getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return pickList.getCreatedOn();
	}

	@Override
	public Long getId() {
		return pickList.getId();
	}

	@Override
	public String getModule() {
		return "pickList";
	}

	@Override
	public SavingService<PickList> getSavingService() {
		return savingService;
	}

	@Override
	public SpunService<PickList, Long> getSpunService() {
		return spunService;
	}

	public List<Booking> listBookings(Route r) {
		List<Booking> picked = unpickedBookings.stream().filter(b -> b.getRoute().equals(r))
				.collect(Collectors.toList());
		picked.forEach(p -> unpickedBookings.remove(p));
		return picked;
	}

	public List<User> listDrivers() throws Exception {
		return allDrivers != null ? allDrivers : userService.listByRole(UserType.DRIVER);
	}

	public List<User> listHelpers() throws Exception {
		return allHelpers != null ? allHelpers : userService.listByRole(UserType.HELPER);
	}

	public List<Route> listRoutes() throws Exception {
		return unpickedBookings.stream().map(b -> b.getRoute()).distinct()
				.sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
	}

	public List<Truck> listTrucks() throws Exception {
		return allTrucks != null ? allTrucks : truckService.list();
	}

	@Override
	public void next() throws Exception {
		set(spunService.module(getModule()).next(getSpunId()));
	}

	@Override
	public void previous() throws Exception {
		set(spunService.module(getModule()).previous(getSpunId()));
	}

	public void print() throws Exception {
		set(readOnlyService.module(getModule()).getOne("/print?id=" + getId()));
	}

	public void renew() {
		reset();
		nullifyLists();
	}

	@Override
	public void reset() {
		set(new PickList());
	}

	@Override
	public void set(Keyed<Long> entity) {
		pickList = (PickList) entity;
		instatiateLists();
	}

	public void setAsstHelperUponValidation(User t) throws Exception {
		if (t == null)
			return;
		if (assignedHelpers().contains(t) || isALeadHelper(t))
			throw new Exception(t + " has been assigned");
		get().setAsstHelper(t);
	}

	public void setDriverUponValidation(User t) throws Exception {
		if (t == null)
			return;
		if (assignedDrivers().contains(t))
			throw new Exception(t + " has been assigned");
		get().setDriver(t);
	}

	public void setLeadHelperUponValidation(User t) throws Exception {
		if (t == null)
			return;
		if (assignedHelpers().contains(t) || isAnAsstHelper(t))
			throw new Exception(t + " has been assigned");
		get().setLeadHelper(t);
	}

	public void setPickDateUponValidation(LocalDate date) throws Exception {
		verifyDateIsTodayOrTheNextWorkDay(date);
		verifyThereAreBookingsToBePickedOnSaidDate(date);
		get().setPickDate(date);
	}

	public void setTruckUponValidation(Truck t) throws Exception {
		if (t == null)
			return;
		if (loadedTrucks().contains(t))
			throw new Exception(t + " has been loaded");
		get().setTruck(t);
	}

	public void unpick(Booking b) {
		unpickedBookings.add(b);
	}

	private void addAssignedAsstHelpersInOtherTrucks() throws Exception {
		List<User> c = pickLists.stream().map(p -> p.getAsstHelper()).collect(Collectors.toList());
		if (!c.isEmpty())
			assignedHelpers.addAll(c);
	}

	private void addAssignedLeadHelpersInOtherTrucks() throws Exception {
		List<User> c = pickLists.stream().map(p -> p.getLeadHelper()).collect(Collectors.toList());
		if (!c.isEmpty())
			assignedHelpers.addAll(c);
	}

	private List<User> assignedDrivers() throws Exception {
		return pickLists.stream().map(p -> p.getDriver()).collect(Collectors.toList());
	}

	private List<User> assignedHelpers() throws Exception {
		if (assignedHelpers.isEmpty()) {
			addAssignedLeadHelpersInOtherTrucks();
			addAssignedAsstHelpersInOtherTrucks();
		}
		return assignedHelpers;
	}

	private boolean dateIsAfterTomorrowWhichIsNotASunday(LocalDate date) {
		return LocalDate.now().until(date, ChronoUnit.DAYS) > 1 && date.getDayOfWeek() != DayOfWeek.SUNDAY;
	}

	private void instatiateLists() {
		unpickedBookings = new ArrayList<>();
		pickLists = new ArrayList<>();
		assignedHelpers = new ArrayList<>();
	}

	private boolean isALeadHelper(User t) {
		User u = get().getLeadHelper();
		return u != null && u.equals(t);
	}

	private boolean isAnAsstHelper(User t) {
		User u = get().getAsstHelper();
		return u != null && u.equals(t);
	}

	private List<Truck> loadedTrucks() throws Exception {
		return pickLists.stream().map(p -> p.getTruck()).collect(Collectors.toList());
	}

	private void nullifyLists() {
		allTrucks = null;
		allDrivers = null;
		allHelpers = null;
	}

	private List<Booking> pickedBookings(LocalDate d) throws Exception {
		List<Booking> b = new ArrayList<>();
		pickLists(d).forEach(p -> b.addAll(p.getBookings()));
		return b;
	}

	private List<PickList> pickLists(LocalDate date) throws Exception {
		pickLists = readOnlyService.module(getModule()).getList("/date?on=" + date);
		return pickLists == null ? Collections.emptyList() : pickLists;
	}

	private <T> List<T> removeDuplicates(List<T> all, List<T> sub) {
		if (sub.isEmpty())
			return all;
		return all.stream().filter(t -> !sub.contains(t)).collect(Collectors.toList());
	}

	private List<Booking> unpickedBookings(LocalDate d) throws Exception {
		List<Booking> b = bookingService.listByPickDate(d);
		if (b == null)
			throw new NothingToPickException(d);
		return b;
	}

	private void verifyDateIsTodayOrTheNextWorkDay(LocalDate date) throws Exception {
		if (date.isBefore(LocalDate.now()))
			throw new DateInThePastException();
		if (dateIsAfterTomorrowWhichIsNotASunday(date))
			throw new DateAfterTomorrowWhichIsNotASundayException();
	}

	private void verifyThereAreBookingsToBePickedOnSaidDate(LocalDate date) throws Exception {
		List<Booking> l = removeDuplicates(unpickedBookings(date), pickedBookings(date));
		if (l.isEmpty())
			throw new NothingToPickException(date);
		unpickedBookings = new ArrayList<>(l);
	}
}
