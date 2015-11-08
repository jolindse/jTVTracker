package se.jolind.jtvtracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import se.jolind.jtvtracker.application.Application;
import se.jolind.jtvtracker.data.tvmaze.TvmShortShow;
import se.jolind.jtvtracker.gui.interfaces.ISearchRequest;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class SearchPanel extends JPanel {

	private JPanel topPanel, resultsPanel;
	private JTextField fieldSearch;
	private JButton btnSearch;
	private JLabel lblResultsInfo;
	private GridBagConstraints gc;
	private IShowChange showListener;
	private ISearchRequest searchListener;

	public SearchPanel() {
		setLayout(new BorderLayout());
		makeStartFields();
		noSearchDone();
		add(topPanel,BorderLayout.NORTH);
		add(resultsPanel,BorderLayout.SOUTH);
	}

	public SearchPanel(List<TvmShortShow> currResults) {
		setLayout(new BorderLayout());
		makeStartFields();
		showSearchResults(currResults);
		add(topPanel,BorderLayout.NORTH);
		add(resultsPanel,BorderLayout.CENTER);
	}
	
	private void noSearchDone(){
		resultsPanel = new JPanel();
		lblResultsInfo = new JLabel("No search conducted...");
		lblResultsInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		resultsPanel.add(lblResultsInfo);
	}

	private void makeStartFields() {
		topPanel = new JPanel();

		String defaultTextfieldText = "Enter search terms";

		fieldSearch = new JTextField(defaultTextfieldText, 25);
		fieldSearch.setForeground(Color.GRAY);
		lblResultsInfo = new JLabel("No search conducted...");
		btnSearch = new JButton("Search");

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchListener == null) {
					searchListener = Application.getListener();
				}

				String currString = fieldSearch.getText();
				if (currString.equals(defaultTextfieldText) || currString.equals(null)) {

				} else {
					searchListener.searchRequest(fieldSearch.getText());
				}
			}
		});
		fieldSearch.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (fieldSearch.getText().equals("")) {
					fieldSearch.setText(defaultTextfieldText);
					fieldSearch.setForeground(Color.GRAY);
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				fieldSearch.setText("");
				fieldSearch.setForeground(Color.BLACK);

			}
		});

		topPanel.add(fieldSearch);

		topPanel.add(btnSearch);

	}

	private void showSearchResults(List<TvmShortShow> searchList) {

		resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridBagLayout());
		gc = new GridBagConstraints();


		lblResultsInfo.setText(" ");
		JLabel lblFiller = new JLabel(" ");

		if (showListener == null) {
			showListener = Application.getListener();
		}

		JLabel[] searchName = new JLabel[searchList.size()];
		JLabel[] searchPic = new JLabel[searchList.size()];

		int gridPos = 0;

		for (int i = 0; i < searchList.size(); i++) {

			TvmShortShow currResult = searchList.get(i);

			int id = currResult.getId();
			searchName[i] = new JLabel(currResult.getInfo());
			searchPic[i] = new JLabel(currResult.getIcon());

			searchName[i].setOpaque(true);
			searchPic[i].setOpaque(true);

			Color normal = searchName[i].getBackground();
			JLabel currLabel = searchName[i];
			JLabel currPicLabel = searchPic[i];

			searchName[i].addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					currLabel.setBackground(normal);
					currPicLabel.setBackground(normal);

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					currLabel.setBackground(Color.WHITE);
					currPicLabel.setBackground(Color.WHITE);
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					showListener.showChangedEvent(id);

				}
			});

			gc.gridx = 0;
			gc.gridy = gridPos;
			gc.weightx = 0.1;
			gc.weighty = 0.1;
			gc.gridwidth = 1;
			gc.insets = new Insets(0, 0, 0, 0);
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.LINE_START;
			resultsPanel.add(searchName[i], gc);

			gc.gridx = 1;
			gc.weightx = 0.1;
			gc.weighty = 0.1;
			gc.insets = new Insets(0, 0, 0, 0);
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.LINE_END;
			resultsPanel.add(searchPic[i], gc);

			gridPos++;
		}

		gc.gridx = 0;
		gc.gridy = gridPos;
		gc.weightx = 0.1;
		gc.weighty = 1;
		gc.gridwidth = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.LINE_START;
		resultsPanel.add(lblFiller, gc);

	}
}
