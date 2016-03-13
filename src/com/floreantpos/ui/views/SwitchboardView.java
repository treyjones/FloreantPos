/*
 * SwitchboardView.java
 *
 * Created on August 14, 2006, 11:45 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.reflections.Reflections;

import com.floreantpos.ITicketList;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.actions.NewBarTabAction;
import com.floreantpos.actions.RefundAction;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.OrderTypeProperties;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.services.TicketService;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TicketSelectionDialog;
import com.floreantpos.ui.dialog.VoidTicketDialog;
import com.floreantpos.ui.util.TicketUtils;
import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.order.ViewPanel;
import com.floreantpos.ui.views.payment.SettleTicketDialog;
import com.floreantpos.util.OrderUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

/**
 * 
 * @author MShahriar
 */
public class SwitchboardView extends ViewPanel implements ActionListener, ITicketList {
	private final AutoLogoffHandler logoffHandler = new AutoLogoffHandler();

	public final static String VIEW_NAME = com.floreantpos.POSConstants.SWITCHBOARD;

	private OrderServiceExtension orderServiceExtension;

	private static SwitchboardView instance;

	private Timer autoLogoffTimer = new Timer(1000, logoffHandler);

	// private Timer ticketListUpdateTimer = new Timer(10 * 1000, new
	// TicketListUpdaterTask());

	/** Creates new form SwitchboardView */
	public SwitchboardView() {
		initComponents();

		btnDineIn.addActionListener(this);
		btnTakeout.addActionListener(this);
		btnPickup.addActionListener(this);
		btnHomeDelivery.addActionListener(this);
		btnDriveThrough.addActionListener(this);
		btnBarTab.addActionListener(this);

		btnEditTicket.addActionListener(this);
		btnGroupSettle.addActionListener(this);
		btnOrderInfo.addActionListener(this);
		btnReopenTicket.addActionListener(this);
		btnSettleTicket.addActionListener(this);
		btnSplitTicket.addActionListener(this);
		btnVoidTicket.addActionListener(this);

		orderServiceExtension = Application.getPluginManager().getPlugin(OrderServiceExtension.class);

		if (orderServiceExtension == null) {
			btnHomeDelivery.setEnabled(false);
			btnPickup.setEnabled(false);
			btnDriveThrough.setEnabled(false);
			btnAssignDriver.setEnabled(false);

			orderServiceExtension = new DefaultOrderServiceExtension();
		} else {
			btnAssignDriver.setEnabled(true);
			btnHomeDelivery.setEnabled(true);

		}

		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

		instance = this;
	}

	public static SwitchboardView getInstance() {
		return instance;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		setLayout(new java.awt.BorderLayout(10, 10));

		javax.swing.JPanel centerPanel = new javax.swing.JPanel(new java.awt.BorderLayout(5, 5));
		javax.swing.JPanel ticketsAndActivityPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));

		TitledBorder titledBorder = BorderFactory.createTitledBorder(null, POSConstants.OPEN_TICKETS_AND_ACTIVITY, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		ticketsAndActivityPanel.setBorder(new CompoundBorder(titledBorder, new EmptyBorder(2, 2, 2, 2)));

		orderFiltersPanel = createOrderFilters();
		ticketsAndActivityPanel.add(orderFiltersPanel, BorderLayout.NORTH);
		ticketsAndActivityPanel.add(openTicketList, java.awt.BorderLayout.CENTER);

		JPanel activityPanel = createActivityPanel();

		ticketsAndActivityPanel.add(activityPanel, java.awt.BorderLayout.SOUTH);

		btnAssignDriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAssignDriver();
			}
		});

		btnCloseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCloseOrder();
			}
		});

		centerPanel.add(ticketsAndActivityPanel, java.awt.BorderLayout.CENTER);

		JPanel rightPanel = new JPanel(new BorderLayout(20, 20));
		TitledBorder titledBorder2 = BorderFactory.createTitledBorder(null, "-", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		rightPanel.setBorder(new CompoundBorder(titledBorder2, new EmptyBorder(2, 2, 6, 2)));

		JPanel orderPanel = new JPanel(new MigLayout("ins 2 2 0 2, fill, hidemode 3, flowy", "fill, grow", ""));
		orderPanel.add(btnDineIn, "grow");
		orderPanel.add(btnTakeout, "grow");
		orderPanel.add(btnPickup, "grow");
		orderPanel.add(btnHomeDelivery, "grow");
		orderPanel.add(btnDriveThrough, "grow");
		orderPanel.add(btnBarTab, "grow");

		setupOrderTypes();

		rightPanel.add(orderPanel);

		PosButton btnOthers = new PosButton("OTHER FUNCTIONS");
		btnOthers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwitchboardOtherFunctionsDialog dialog = new SwitchboardOtherFunctionsDialog(SwitchboardView.this);
				dialog.open();
			}
		});

		PosButton btnBackOffice = new PosButton(POSConstants.BACK_OFFICE_BUTTON_TEXT);
		btnBackOffice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BackOfficeWindow window = BackOfficeWindow.getInstance();
				if (window == null) {
					window = new BackOfficeWindow();
					Application.getInstance().setBackOfficeWindow(window);
				}
				window.setVisible(true);
				window.toFront();
			}
		});

		rightPanel.add(btnBackOffice, BorderLayout.SOUTH);

		centerPanel.add(rightPanel, java.awt.BorderLayout.EAST);

		add(centerPanel, java.awt.BorderLayout.CENTER);
	}

	private JPanel createActivityPanel() {
		JPanel activityPanel = new JPanel(new BorderLayout(5, 5));
		JPanel innerActivityPanel = new JPanel(new MigLayout("hidemode 3, fill, ins 0", "fill, grow", ""));

		JPanel panel1 = new JPanel(new GridLayout(1, 0, 5, 5));

		// POSToggleButton btnOrderFilters = new
		// POSToggleButton("ORDER FILTERS");
		// btnOrderFilters.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// orderFiltersPanel.setCollapsed(!orderFiltersPanel.isCollapsed());
		// }
		// });
		//
		// panel1.add(btnOrderFilters);
		panel1.add(btnOrderInfo);
		panel1.add(btnEditTicket);
		panel1.add(btnSettleTicket);
		// panel1.add(btnReopenTicket);
		panel1.add(btnVoidTicket);
		panel1.add(btnRefundTicket);
		panel1.add(btnAssignDriver);
		panel1.add(btnCloseOrder);

		innerActivityPanel.add(panel1);

		// final JXCollapsiblePane collapsiblePane = new JXCollapsiblePane();
		// collapsiblePane.setAnimated(false);
		// collapsiblePane.getContentPane().setLayout(new GridLayout(1, 0, 5,
		// 5));
		// collapsiblePane.getContentPane().add(btnGroupSettle);
		// collapsiblePane.getContentPane().add(btnSplitTicket);
		// collapsiblePane.getContentPane().add(btnReopenTicket);
		// collapsiblePane.getContentPane().add(btnVoidTicket);
		//
		// collapsiblePane.getContentPane().add(btnRefundTicket);
		// collapsiblePane.getContentPane().add(btnAssignDriver);
		//
		// collapsiblePane.setCollapsed(true);
		// innerActivityPanel.add(collapsiblePane, "newline");
		//
		// final PosButton btnMore = new
		// PosButton(POSConstants.MORE_ACTIVITY_BUTTON_TEXT);
		// final Border border1 =
		// BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,
		// 0, 5, 0), btnMore.getBorder());
		// final Border border2 =
		// BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,
		// 0, 0, 0), btnMore.getBorder());
		// btnMore.setBorder(border1);
		//
		// btnMore.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// boolean collapsed = collapsiblePane.isCollapsed();
		// collapsiblePane.setCollapsed(!collapsed);
		// if (collapsed) {
		// btnMore.setText(POSConstants.LESS_ACTIVITY_BUTTON_TEXT);
		// btnMore.setBorder(border2);
		// } else {
		// btnMore.setText(POSConstants.MORE_ACTIVITY_BUTTON_TEXT);
		// btnMore.setBorder(border1);
		// }
		// }
		// });

		activityPanel.add(innerActivityPanel);
		// activityPanel.add(btnMore, BorderLayout.EAST);

		return activityPanel;

		// panel1.add(btnPayout);

		// cardPanel.add(panel1);
		//
		// JPanel panel2 = new JPanel(new GridLayout(1, 0, 5, 5));
		// panel2.setBorder(border);
		// //panel2.setBackground(Color.blue);
		//
		// cardPanel.add(panel2);
		//
		// JPanel panel3 = new JPanel(new GridLayout(1, 0, 5, 5));
		// panel3.setBorder(border);
		// //panel3.setBackground(Color.red);
		// panel3.add(btnAuthorize);
		// panel3.add(btnManager);
		// panel3.add(btnKitchenDisplay);
		// panel3.add(btnBackOffice);
		// cardPanel.add(panel3);
		//
		// activityPanel.add(cardPanel);
		//
		// PosButton btnPrev = new PosButton();
		//		btnPrev.setIcon(IconFactory.getIcon("previous.png")); //$NON-NLS-1$
		// btnPrev.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// cardPanel.showPreviousCard();
		// }
		// });
		// activityPanel.add(btnPrev, BorderLayout.WEST);
		//
		// PosButton btnNext = new PosButton();
		//		btnNext.setIcon(IconFactory.getIcon("next.png")); //$NON-NLS-1$
		// btnNext.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// cardPanel.showNextCard();
		// }
		// });
		// activityPanel.add(btnNext, BorderLayout.EAST);
		//
		// final FloorLayoutPlugin floorLayoutPlugin =
		// Application.getPluginManager().getPlugin(FloorLayoutPlugin.class);
		// if (floorLayoutPlugin != null) {
		// btnTableManage = new
		// PosButton(POSConstants.TABLE_MANAGE_BUTTON_TEXT);
		// btnTableManage.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// floorLayoutPlugin.openTicketsAndTablesDisplay();
		// }
		// });
		//
		// panel3.add(btnTableManage);
		// }
		//
		// TicketImportPlugin ticketImportPlugin =
		// Application.getPluginManager().getPlugin(TicketImportPlugin.class);
		// if (ticketImportPlugin != null) {
		// btnOnlineTickets = new
		// PosButton(POSConstants.ONLINE_TICKET_BUTTON_TEXT, new
		// TicketImportAction());
		// panel3.add(btnOnlineTickets);
		// }
	}

	private JXCollapsiblePane createOrderFilters() {
		JXCollapsiblePane filterPanel = new JXCollapsiblePane();
		filterPanel.setCollapsed(true);
		filterPanel.getContentPane().setLayout(new MigLayout("fill", "fill, grow", ""));

		POSToggleButton btnNoPaymentFilter = new POSToggleButton("ALL");
		POSToggleButton btnFilterByPaid = new POSToggleButton("PAID");
		POSToggleButton btnFilterByUnPaid = new POSToggleButton("UNPAID");

		ButtonGroup paymentGroup = new ButtonGroup();
		paymentGroup.add(btnNoPaymentFilter);
		paymentGroup.add(btnFilterByPaid);
		paymentGroup.add(btnFilterByUnPaid);

		JPanel filterByPaymentStatusPanel = new JPanel(new MigLayout("", "fill, grow", ""));
		filterByPaymentStatusPanel.setBorder(new TitledBorder("FILTER BY PAYMENT STATUS"));
		filterByPaymentStatusPanel.add(btnNoPaymentFilter);
		filterByPaymentStatusPanel.add(btnFilterByPaid);
		filterByPaymentStatusPanel.add(btnFilterByUnPaid);

		filterPanel.getContentPane().add(filterByPaymentStatusPanel);

		POSToggleButton btnFilterByOrderTypeALL = new POSToggleButton("ALL");
		POSToggleButton btnFilterByDineIn = new POSToggleButton("DINE IN");
		POSToggleButton btnFilterByTakeOut = new POSToggleButton("TAKE OUT");
		POSToggleButton btnFilterByPickup = new POSToggleButton("PICKUP");
		POSToggleButton btnFilterByHomeDeli = new POSToggleButton("HOME DELIVERY");
		POSToggleButton btnFilterByDriveThru = new POSToggleButton("DRIVE THRU");
		POSToggleButton btnFilterByBarTab = new POSToggleButton("BAR TAB");

		ButtonGroup orderTypeGroup = new ButtonGroup();
		orderTypeGroup.add(btnFilterByOrderTypeALL);
		orderTypeGroup.add(btnFilterByDineIn);
		orderTypeGroup.add(btnFilterByTakeOut);
		orderTypeGroup.add(btnFilterByPickup);
		orderTypeGroup.add(btnFilterByHomeDeli);
		orderTypeGroup.add(btnFilterByDriveThru);
		orderTypeGroup.add(btnFilterByBarTab);

		JPanel filterByOrderPanel = new JPanel(new MigLayout("", "fill, grow", ""));
		filterByOrderPanel.setBorder(new TitledBorder("FILTER BY ORDER TYPE"));
		filterByOrderPanel.add(btnFilterByOrderTypeALL);
		filterByOrderPanel.add(btnFilterByDineIn);
		filterByOrderPanel.add(btnFilterByTakeOut);
		filterByOrderPanel.add(btnFilterByPickup);
		filterByOrderPanel.add(btnFilterByHomeDeli);
		filterByOrderPanel.add(btnFilterByDriveThru);
		filterByOrderPanel.add(btnFilterByBarTab);

		filterPanel.getContentPane().add(filterByOrderPanel);

		return filterPanel;
	}

	private void setupOrderTypes() {
		setupOrderType(OrderType.DINE_IN, btnDineIn, OrderType.DINE_IN.name());
		setupOrderType(OrderType.TAKE_OUT, btnTakeout, OrderType.TAKE_OUT.name());
		setupOrderType(OrderType.PICKUP, btnPickup, OrderType.PICKUP.name());
		setupOrderType(OrderType.HOME_DELIVERY, btnHomeDelivery, OrderType.HOME_DELIVERY.name());
		setupOrderType(OrderType.DRIVE_THRU, btnDriveThrough, OrderType.DRIVE_THRU.name());
		setupOrderType(OrderType.BAR_TAB, btnBarTab, OrderType.BAR_TAB.name());
	}

	private void setupOrderType(OrderType orderType, JButton button, String textKey) {
		button.setText(orderType.toString());
		OrderTypeProperties properties = orderType.getProperties();

		if (properties == null) {
			button.setVisible(true);
		} else {
			button.setVisible(properties.isVisible());
		}
	}

	protected void doCloseOrder() {
		Ticket ticket = getFirstSelectedTicket();
		if (ticket != null) {
			int due = (int) POSUtil.getDouble(ticket.getDueAmount());
			if (due != 0) {
				POSMessageDialog.showError("Ticket is not fully paid");
				return;
			}

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Ticket# " + ticket.getId() + " will be closed.", "Confirm", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);

			if (option != JOptionPane.OK_OPTION) {
				return;
			}

			OrderController.closeOrder(ticket);

			updateTicketList();
		}
	}

	protected void doAssignDriver() {
		try {

			Ticket ticket = getFirstSelectedTicket();

			if (ticket == null) {
				return;
			}

			if (ticket.getType() != OrderType.HOME_DELIVERY) {
				POSMessageDialog.showError("Driver can be assigned only for Home Delivery");
				return;
			}

			User assignedDriver = ticket.getAssignedDriver();
			if (assignedDriver != null) {
				int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Driver already assigned. Do you want to reassign?", "Confirm", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			orderServiceExtension.assignDriver(ticket.getId());
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
			LogFactory.getLog(SwitchboardView.class).error(e);
		}
		updateTicketList();

	}

	private void doReopenTicket() {
		try {

			int ticketId = NumberSelectionDialog2.takeIntInput("Enter ticket id");

			if (ticketId == -1) {
				return;
			}

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

			if (ticket == null) {
				throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + ticketId + " " + POSConstants.FOUND);
			}

			if (!ticket.isClosed()) {
				throw new PosException(POSConstants.TICKET_IS_NOT_CLOSED);
			}

			if (ticket.isVoided()) {
				throw new PosException("Void ticket cannot be reopened");
			}

			ticket.setClosed(false);
			ticket.setClosingDate(null);
			ticket.setReOpened(true);

			TicketDAO.getInstance().saveOrUpdate(ticket, true);

			OrderInfoView view = new OrderInfoView(Arrays.asList(ticket));
			OrderInfoDialog dialog = new OrderInfoDialog(view);
			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

			updateTicketList();

		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getLocalizedMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doSettleTicket() {
		try {
			Ticket ticket = null;

			List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

			if (selectedTickets.size() > 0) {
				ticket = selectedTickets.get(0);
			} else {
				// int ticketId =
				// NumberSelectionDialog2.takeIntInput("Enter ticket id");
				// if (ticketId == -1) {
				// return;
				// }
				String ticketId = TicketSelectionDialog.takeTicketInput("Enter ticket id", TicketUtils.getTicketPrefix());
				if (ticketId == null) {
					return;
				}
				ticket = TicketService.getTicket(ticketId);
			}

			new SettleTicketAction(ticket.getId()).execute();

			updateTicketList();

		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doShowOrderInfo() {
		doShowOrderInfo(openTicketList.getSelectedTickets());
		updateTicketList();
	}

	private void doShowOrderInfo(List<Ticket> tickets) {
		try {

			if (tickets.size() == 0) {
				POSMessageDialog.showMessage("Please select at atleast 1 ticket");
				return;
			}

			List<Ticket> ticketsToShow = new ArrayList<Ticket>();

			for (int i = 0; i < tickets.size(); i++) {
				Ticket ticket = tickets.get(i);
				ticketsToShow.add(TicketDAO.getInstance().loadFullTicket(ticket.getId()));
			}

			OrderInfoView view = new OrderInfoView(ticketsToShow);
			OrderInfoDialog dialog = new OrderInfoDialog(view);
			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
		updateTicketList();
	}

	private void doVoidTicket() {
		try {
			Ticket ticket = null;

			List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

			if (selectedTickets.size() > 0) {
				ticket = selectedTickets.get(0);
			} else {
				// int ticketId =
				// NumberSelectionDialog2.takeIntInput("Enter ticket id");
				// if (ticketId == -1) {
				// return;
				// }
				String ticketId = TicketSelectionDialog.takeTicketInput("Enter ticket id", TicketUtils.getTicketPrefix());
				if (ticketId == null) {
					return;
				}
				ticket = TicketService.getTicket(ticketId);
			}

			Ticket ticketToVoid = TicketDAO.getInstance().loadFullTicket(ticket.getId());

			VoidTicketDialog voidTicketDialog = new VoidTicketDialog(Application.getPosWindow(), true);
			voidTicketDialog.setTicket(ticketToVoid);
			voidTicketDialog.open();

			if (!voidTicketDialog.isCanceled()) {
				updateView();
			}
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doSplitTicket() {
		try {
			Ticket selectedTicket = getFirstSelectedTicket();

			if (selectedTicket == null) {
				return;
			}

			// initialize the ticket.
			Ticket ticket = TicketDAO.getInstance().loadFullTicket(selectedTicket.getId());

			SplitTicketDialog dialog = new SplitTicketDialog();
			dialog.setTicket(ticket);
			dialog.open();

			updateView();
		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doEditTicket() {
		try {
			Ticket ticket = null;

			List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

			if (selectedTickets.size() > 0) {
				ticket = selectedTickets.get(0);
			} else {
				// int ticketId =
				// NumberSelectionDialog2.takeIntInput("Enter or scan ticket id");
				// if (ticketId == -1) {
				// return;
				// }
				String ticketId = TicketSelectionDialog.takeTicketInput("Enter ticket id", TicketUtils.getTicketPrefix());
				if (ticketId == null) {
					return;
				}

				ticket = TicketService.getTicket(ticketId);
			}

			editTicket(ticket);
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(this, e.getMessage(), e);
		}
	}

	private void editTicket(Ticket ticket) {
		// if (ticket.isPaid()) {
		// POSMessageDialog.showMessage("Paid ticket cannot be edited");
		// return;
		// }

		Ticket ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticket.getId());
		OrderView.getInstance().setCurrentTicket(ticketToEdit);

		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

	private void doCreateNewTicket(final OrderType ticketType) {
		try {
			OrderServiceExtension orderService = new DefaultOrderServiceExtension();
			orderService.createNewTicket(ticketType);

		} catch (TicketAlreadyExistsException e) {

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
	}

	protected void doHomeDelivery(OrderType ticketType) {
		try {

			orderServiceExtension.createNewTicket(ticketType);

		} catch (TicketAlreadyExistsException e) {

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
	}

	private void doGroupSettle() {
		List<Ticket> selectedTickets = openTicketList.getSelectedTickets();
		if (selectedTickets == null) {
			return;
		}

		for (int i = 0; i < selectedTickets.size(); i++) {
			Ticket ticket = selectedTickets.get(i);

			Ticket fullTicket = TicketDAO.getInstance().loadFullTicket(ticket.getId());

			SettleTicketDialog posDialog = new SettleTicketDialog();
			posDialog.setTicket(fullTicket);
			posDialog.setSize(800, 700);
			posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			posDialog.open();
		}

		updateTicketList();
	}

	public void updateView() {
		setupOrderTypes();

		User user = Application.getCurrentUser();
		UserType userType = user.getType();
		if (userType != null) {
			Set<UserPermission> permissions = userType.getPermissions();
			if (permissions != null) {
				btnDineIn.setEnabled(false);
				btnEditTicket.setEnabled(false);
				btnGroupSettle.setEnabled(false);
				btnReopenTicket.setEnabled(false);
				btnSettleTicket.setEnabled(false);
				btnSplitTicket.setEnabled(false);
				btnTakeout.setEnabled(false);
				btnVoidTicket.setEnabled(false);

				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.VOID_TICKET)) {
						btnVoidTicket.setEnabled(true);
					} else if (permission.equals(UserPermission.SETTLE_TICKET)) {
						btnSettleTicket.setEnabled(true);
						// btnGroupSettle.setEnabled(true);
					} else if (permission.equals(UserPermission.REOPEN_TICKET)) {
						btnReopenTicket.setEnabled(true);
					}
					// else if (permission.equals(UserPermission.SPLIT_TICKET))
					// {
					// btnSplitTicket.setEnabled(true);
					// }
					else if (permission.equals(UserPermission.TAKE_OUT)) {
						btnTakeout.setEnabled(true);
						btnTakeout.setForeground(Color.red);
					} else if (permission.equals(UserPermission.EDIT_TICKET)) {
						btnEditTicket.setEnabled(true);
					} else if (permission.equals(UserPermission.CREATE_TICKET)) {
						btnDineIn.setEnabled(true);
					}
					btnSplitTicket.setVisible(false);
					btnGroupSettle.setVisible(false);
				}
			}
		}

		updateTicketList();
	}

	public synchronized void updateTicketList() {
		try {

			List<Ticket> totalTickets = new ArrayList<Ticket>();

			// ticketListUpdateTimer.stop();
			Application.getPosWindow().setGlassPaneVisible(true);

			User user = Application.getCurrentUser();

			TicketDAO dao = TicketDAO.getInstance();

			List<Ticket> openTickets = null;

			if (user.canViewAllOpenTickets()) {
				openTickets = dao.findOpenTickets();
			} else {
				openTickets = dao.findOpenTicketsForUser(user);
			}
			openTicketList.setTickets(openTickets);

			// For now show last N (ticketHistory) tickets.
			List<Ticket> lastTickets = null;

			RestaurantDAO resDao = RestaurantDAO.getInstance();
			Restaurant res = resDao.findAll().get(0);
			lastTickets = dao.findLastNTickets(res.getTicketHistory());

			totalTickets.addAll(lastTickets);
			totalTickets.addAll(openTickets);
			openTicketList.setTickets(totalTickets);
		} catch (Exception e) {
			POSMessageDialog.showError(this, "Error getting open ticket list", e);
		} finally {
			Application.getPosWindow().setGlassPaneVisible(false);
			// ticketListUpdateTimer.restart();
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables

	private PosButton btnDineIn = new PosButton(POSConstants.DINE_IN_BUTTON_TEXT);
	private PosButton btnTakeout = new PosButton(POSConstants.TAKE_OUT_BUTTON_TEXT);
	private PosButton btnPickup = new PosButton(POSConstants.PICKUP_BUTTON_TEXT);
	private PosButton btnHomeDelivery = new PosButton(POSConstants.HOME_DELIVERY_BUTTON_TEXT);
	private PosButton btnDriveThrough = new PosButton(POSConstants.DRIVE_THRU_BUTTON_TEXT);
	private PosButton btnBarTab = new PosButton(POSConstants.BAR_TAB_BUTTON_TEXT);

	private PosButton btnEditTicket = new PosButton(POSConstants.EDIT_TICKET_BUTTON_TEXT);
	private PosButton btnGroupSettle = new PosButton(POSConstants.GROUP_SETTLE_BUTTON_TEXT);

	private PosButton btnOrderInfo = new PosButton(POSConstants.ORDER_INFO_BUTTON_TEXT);
	private PosButton btnReopenTicket = new PosButton(POSConstants.REOPEN_TICKET_BUTTON_TEXT);
	private PosButton btnSettleTicket = new PosButton(POSConstants.SETTLE_TICKET_BUTTON_TEXT);
	private PosButton btnSplitTicket = new PosButton(POSConstants.SPLIT_TICKET_BUTTON_TEXT);

	private PosButton btnVoidTicket = new PosButton(POSConstants.VOID_TICKET_BUTTON_TEXT);
	private PosButton btnRefundTicket = new PosButton(POSConstants.REFUND_BUTTON_TEXT, new RefundAction(this));

	private PosButton btnAssignDriver = new PosButton(POSConstants.ASSIGN_DRIVER_BUTTON_TEXT);
	private PosButton btnCloseOrder = new PosButton(POSConstants.CLOSE_ORDER_BUTTON_TEXT);

	private com.floreantpos.ui.TicketListView openTicketList = new com.floreantpos.ui.TicketListView();

	private JLabel timerLabel = new JLabel();

	private JXCollapsiblePane orderFiltersPanel;

	// End of variables declaration//GEN-END:variables

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			updateView();

			logoffHandler.reset();
			if (TerminalConfig.isAutoLogoffEnable()) {
				autoLogoffTimer.start();
			}
		} else {
			// ticketListUpdateTimer.stop();
			autoLogoffTimer.stop();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == btnDineIn) {
			doCreateNewTicket(OrderType.DINE_IN);
		} else if (source == btnTakeout) {
			OrderUtil.createNewTakeOutOrder(OrderType.TAKE_OUT);
		} else if (source == btnPickup) {
			doHomeDelivery(OrderType.PICKUP);
		} else if (source == btnHomeDelivery) {
			doHomeDelivery(OrderType.HOME_DELIVERY);
		} else if (source == btnDriveThrough) {
			OrderUtil.createNewTakeOutOrder(OrderType.DRIVE_THRU);
		} else if (source == btnBarTab) {
			new NewBarTabAction(this).actionPerformed(e);
		} else if (source == btnEditTicket) {
			doEditTicket();
		} else if (source == btnGroupSettle) {
			doGroupSettle();
		} else if (source == btnOrderInfo) {
			doShowOrderInfo();
		} else if (source == btnReopenTicket) {
			doReopenTicket();
		} else if (source == btnSettleTicket) {
			doSettleTicket();
		} else if (source == btnSplitTicket) {
			doSplitTicket();
		} else if (source == btnVoidTicket) {
			doVoidTicket();
		}
	}

	public Ticket getFirstSelectedTicket() {
		List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			POSMessageDialog.showMessage("Please select a ticket");
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	public Ticket getSelectedTicket() {
		List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	private class TicketListUpdaterTask implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateTicketList();
		}
	}

	private class AutoLogoffHandler implements ActionListener {
		int countDown = TerminalConfig.getAutoLogoffTime();

		@Override
		public void actionPerformed(ActionEvent e) {
			if (PosGuiUtil.isModalDialogShowing()) {
				reset();
				return;
			}

			--countDown;
			int min = countDown / 60;
			int sec = countDown % 60;

			timerLabel.setText("Aoto logoff in " + min + ":" + sec);

			if (countDown == 0) {
				// doLogout();
			}
		}

		public void reset() {
			timerLabel.setText("");
			countDown = TerminalConfig.getAutoLogoffTime();
		}

	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}
