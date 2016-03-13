/*
 * PayoutDialog.java
 *
 * Created on August 25, 2006, 8:44 PM
 */

package com.floreantpos.ui.dialog;

import java.util.Date;

import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.PayOutTransaction;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PayoutReason;
import com.floreantpos.model.PayoutRecepient;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.PayOutTransactionDAO;
import com.floreantpos.util.NumberUtil;

/**
 *
 * @author  MShahriar
 */
public class PayoutDialog extends POSDialog {

	/** Creates new form PayoutDialog */
	public PayoutDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();

		setTitle(Application.getTitle() + ": PAY OUT");

		payOutView.initialize();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
        payOutView = new com.floreantpos.ui.views.PayOutView();
        transparentPanel2 = new com.floreantpos.swing.TransparentPanel();
        btnFinish = new com.floreantpos.swing.PosButton();
        btnCancel = new com.floreantpos.swing.PosButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        transparentPanel1.setLayout(new java.awt.BorderLayout());

        transparentPanel1.setOpaque(true);
        transparentPanel1.add(payOutView, java.awt.BorderLayout.CENTER);

        transparentPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        btnFinish.setText(com.floreantpos.POSConstants.FINISH);
        btnFinish.setPreferredSize(new java.awt.Dimension(140, 50));
        btnFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doFinishPayout(evt);
            }
        });

        transparentPanel2.add(btnFinish);

        btnCancel.setText(com.floreantpos.POSConstants.CANCEL);
        btnCancel.setPreferredSize(new java.awt.Dimension(140, 50));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        transparentPanel2.add(btnCancel);

        transparentPanel1.add(transparentPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(transparentPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void doFinishPayout(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doFinishPayout
		Application application = Application.getInstance();

		Terminal terminal = application.getTerminal();

		double payoutAmount = payOutView.getPayoutAmount();
		PayoutReason reason = payOutView.getReason();
		PayoutRecepient recepient = payOutView.getRecepient();
		String note = payOutView.getNote();
		
		terminal.setCurrentBalance(terminal.getCurrentBalance() - payoutAmount);

		PayOutTransaction payOutTransaction = new PayOutTransaction();
		payOutTransaction.setPaymentType(PaymentType.CASH.name());
		payOutTransaction.setTransactionType(TransactionType.DEBIT.name());
		
		payOutTransaction.setReason(reason);
		payOutTransaction.setRecepient(recepient);
		payOutTransaction.setNote(note);
		payOutTransaction.setAmount(Double.valueOf(payoutAmount));
		
		payOutTransaction.setUser(Application.getCurrentUser());
		payOutTransaction.setTransactionTime(new Date());
		payOutTransaction.setTerminal(terminal);

		try {
			PayOutTransactionDAO dao = new PayOutTransactionDAO();
			dao.saveTransaction(payOutTransaction, terminal);
			setCanceled(false);
			
//			PAYOUT ACTION
			String actionMessage = "";
			actionMessage += "TOTAL" + ":" + NumberUtil.formatNumber(payoutAmount);
			ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.PAY_OUT, actionMessage);
			
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}//GEN-LAST:event_doFinishPayout

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		canceled = true;
		dispose();
	}//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.floreantpos.swing.PosButton btnCancel;
    private com.floreantpos.swing.PosButton btnFinish;
    private com.floreantpos.ui.views.PayOutView payOutView;
    private com.floreantpos.swing.TransparentPanel transparentPanel1;
    private com.floreantpos.swing.TransparentPanel transparentPanel2;
    // End of variables declaration//GEN-END:variables

}
